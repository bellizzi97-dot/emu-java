package com.emu.java.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.emu.java.core.EmulatorEngine;
import com.emu.java.core.JarLoader;
import com.emu.java.core.KeyMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends Activity {
    private J2CanvasView canvasView;
    private GamePadView gamePadView;
    private EmulatorEngine engine;
    private static final int PICK_JAR_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        canvasView = new J2CanvasView(this, null);
        gamePadView = new GamePadView(this, null);

        LinearLayout.LayoutParams canvasParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 2.0f);
        LinearLayout.LayoutParams padParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);

        layout.addView(canvasView, canvasParams);
        layout.addView(gamePadView, padParams);
        setContentView(layout);

        engine = new EmulatorEngine();
        engine.setFrameUpdateListener(new EmulatorEngine.FrameUpdateListener() {
            @Override
            public void onFrameUpdate() {
                canvasView.postInvalidate();
            }
        });

        gamePadView.setOnKeyListener(new GamePadView.OnKeyListener() {
            @Override
            public void onKeyPair(KeyMapper.GbButton button, boolean isPressed) {
                int j2meKey = KeyMapper.toJ2meKey(button);
                engine.sendKeyEvent(j2meKey, isPressed);
            }
        });

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"application/java-archive", "application/x-java-archive", "application/octet-stream"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Selecciona tu juego Java"), PICK_JAR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_JAR_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    File tempFile = copyUriToTempFile(uri);
                    JarLoader loader = new JarLoader();
                    loader.loadJar(tempFile.getAbsolutePath());

                    String appName = loader.getAppName();
                    if (appName == null || appName.isEmpty()) {
                        appName = "Juego Java";
                    }
                    Toast.makeText(this, "Cargando: " + appName, Toast.LENGTH_LONG).show();
                    engine.start();
                } catch (Exception e) {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private File copyUriToTempFile(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = new File(getCacheDir(), "loaded_game.jar");
        FileOutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
        return tempFile;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (engine != null) {
            engine.stop();
        }
    }
}
