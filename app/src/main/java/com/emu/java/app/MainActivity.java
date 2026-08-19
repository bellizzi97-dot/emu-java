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
        intent.setType("application/java-archive");
        startActivityForResult(Intent.createChooser(intent, "Selecciona tu juego Java"), PICK_JAR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_JAR_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    JarLoader loader = new JarLoader();
                    loader.loadJar(uri.getPath());
                    Toast.makeText(this, "Cargando: " + loader.getAppName(), Toast.LENGTH_LONG).show();
                    engine.start();
                } catch (Exception e) {
                    Toast.makeText(this, "Error cargando archivo", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (engine != null) {
            engine.stop();
        }
    }
}
