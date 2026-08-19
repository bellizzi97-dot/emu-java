package com.emu.java.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private LinearLayout menuLayout;
    private LinearLayout gameLayout;
    private TextView statusText;

    private static final int PICK_JAR_REQUEST = 1;
    private static final int PICK_FOLDER_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        menuLayout = new LinearLayout(this);
        menuLayout.setOrientation(LinearLayout.VERTICAL);
        menuLayout.setPadding(32, 32, 32, 32);

        TextView title = new TextView(this);
        title.setText("EmuJava - Biblioteca de Juegos");
        title.setTextSize(20);
        title.setPadding(0, 0, 0, 24);
        menuLayout.addView(title);

        Button btnSelectJar = new Button(this);
        btnSelectJar.setText("Cargar Archivo .JAR");
        btnSelectJar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
        menuLayout.addView(btnSelectJar);

        Button btnSelectFolder = new Button(this);
        btnSelectFolder.setText("Seleccionar Carpeta de Juegos");
        btnSelectFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFolderPicker();
            }
        });
        menuLayout.addView(btnSelectFolder);

        statusText = new TextView(this);
        statusText.setPadding(0, 16, 0, 0);
        menuLayout.addView(statusText);

        gameLayout = new LinearLayout(this);
        gameLayout.setOrientation(LinearLayout.VERTICAL);
        gameLayout.setVisibility(View.GONE);

        canvasView = new J2CanvasView(this, null);
        gamePadView = new GamePadView(this, null);

        LinearLayout.LayoutParams canvasParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 2.0f);
        LinearLayout.LayoutParams padParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);

        gameLayout.addView(canvasView, canvasParams);
        gameLayout.addView(gamePadView, padParams);

        rootLayout.addView(menuLayout);
        rootLayout.addView(gameLayout);
        setContentView(rootLayout);

        engine = new EmulatorEngine();
        engine.setFrameUpdateListener(new EmulatorEngine.FrameUpdateListener() {
            @Override
            public void onFrameUpdate(final android.graphics.Bitmap frame) {
                canvasView.post(new Runnable() {
                    @Override
                    public void run() {
                        canvasView.updateFrame(frame);
                    }
                });
            }
        });

        gamePadView.setOnKeyListener(new GamePadView.OnKeyListener() {
            @Override
            public void onKeyPair(KeyMapper.GbButton button, boolean isPressed) {
                int j2meKey = KeyMapper.toJ2meKey(button);
                engine.sendKeyEvent(j2meKey, isPressed);
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"application/java-archive", "application/x-java-archive", "application/octet-stream"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Selecciona tu juego Java"), PICK_JAR_REQUEST);
    }

    private void openFolderPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, PICK_FOLDER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                if (requestCode == PICK_JAR_REQUEST) {
                    loadGameFromUri(uri);
                } else if (requestCode == PICK_FOLDER_REQUEST) {
                    statusText.setText("Carpeta seleccionada: " + uri.getPath());
                    Toast.makeText(this, "Carpeta asignada correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void loadGameFromUri(Uri uri) {
        try {
            File tempFile = copyUriToTempFile(uri);
            JarLoader loader = new JarLoader();
            loader.loadJar(tempFile.getAbsolutePath());

            String appName = loader.getAppName();
            if (appName == null || appName.isEmpty()) {
                appName = "Juego Java";
            }

            engine.setGameTitle(appName);
            menuLayout.setVisibility(View.GONE);
            gameLayout.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Ejecutando: " + appName, Toast.LENGTH_SHORT).show();
            engine.start();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
