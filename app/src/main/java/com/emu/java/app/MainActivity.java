package com.emu.java.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.emu.java.core.KeyMapper;

public class MainActivity extends Activity {
    private J2CanvasView canvasView;
    private GamePadView gamePadView;

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

        gamePadView.setOnKeyListener(new GamePadView.OnKeyListener() {
            @Override
            public void onKeyPair(KeyMapper.GbButton button, boolean isPressed) {
                int j2meKey = KeyMapper.toJ2meKey(button);
                // Aquí se enviará la tecla al motor de emulación
            }
        });
    }
}
