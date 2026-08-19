package com.emu.java.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.emu.java.core.EmulatorEngine;

public class MainActivity extends Activity {
    private static final int REQUEST_PICK_JAR = 1001;
    private J2CanvasView canvasView;
    private GamePadView gamePadView;
    private EmulatorEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.BLACK);

        // Botón superior para seleccionar juegos JAR
        Button btnSelectGame = new Button(this);
        btnSelectGame.setText("📂 BUSCAR JUEGO (.JAR)");
        btnSelectGame.setBackgroundColor(Color.parseColor("#1F1F28"));
        btnSelectGame.setTextColor(Color.WHITE);
        btnSelectGame.setOnClickListener(v -> openFilePicker());

        canvasView = new J2CanvasView(this, null);
        gamePadView = new GamePadView(this, null);

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btnParams.setMargins(16, 16, 16, 8);

        LinearLayout.LayoutParams canvasParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
        LinearLayout.LayoutParams padParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.2f);

        rootLayout.addView(btnSelectGame, btnParams);
        rootLayout.addView(canvasView, canvasParams);
        rootLayout.addView(gamePadView, padParams);
        setContentView(rootLayout);

        engine = new EmulatorEngine();
        engine.setFrameUpdateListener(frame -> canvasView.post(() -> canvasView.updateFrame(frame)));

        gamePadView.setOnKeyListener((keyCode, isPressed) -> engine.sendKeyEvent(keyCode, isPressed));
        engine.start();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_PICK_JAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_JAR && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String fileName = uri.getLastPathSegment();
                Toast.makeText(this, "Cargando: " + fileName, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (engine != null) engine.stop();
    }
}
