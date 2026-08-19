package com.emu.java.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.emu.java.core.EmulatorEngine;

public class MainActivity extends Activity {
    private J2CanvasView canvasView;
    private GamePadView gamePadView;
    private EmulatorEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.BLACK);

        canvasView = new J2CanvasView(this, null);
        gamePadView = new GamePadView(this, null);

        LinearLayout.LayoutParams canvasParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.1f);
        LinearLayout.LayoutParams padParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.4f);

        rootLayout.addView(canvasView, canvasParams);
        rootLayout.addView(gamePadView, padParams);
        setContentView(rootLayout);

        engine = new EmulatorEngine();
        engine.setFrameUpdateListener(frame -> canvasView.post(() -> canvasView.updateFrame(frame)));

        gamePadView.setOnKeyListener((keyCode, isPressed) -> engine.sendKeyEvent(keyCode, isPressed));
        engine.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (engine != null) engine.stop();
    }
}
