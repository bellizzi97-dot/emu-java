package com.emu.java.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class EmulatorEngine {
    public interface FrameUpdateListener {
        void onFrameUpdate(Bitmap frame);
    }

    private FrameUpdateListener frameUpdateListener;
    private boolean isRunning = false;
    private final Bitmap displayBitmap;
    private final Canvas displayCanvas;
    private final Paint paint;

    public EmulatorEngine() {
        displayBitmap = Bitmap.createBitmap(240, 320, Bitmap.Config.ARGB_8888);
        displayCanvas = new Canvas(displayBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setFrameUpdateListener(FrameUpdateListener listener) {
        this.frameUpdateListener = listener;
    }

    public void start() {
        isRunning = true;
        new Thread(() -> {
            while (isRunning) {
                renderFrame();
                if (frameUpdateListener != null) {
                    frameUpdateListener.onFrameUpdate(displayBitmap);
                }
                try {
                    Thread.sleep(33);
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void renderFrame() {
        displayCanvas.drawColor(Color.parseColor("#000000"));
        paint.setColor(Color.parseColor("#2ECC71"));
        paint.setTextSize(16);
        displayCanvas.drawText("J2ME Canvas Active", 20, 50, paint);
    }

    public void sendKeyEvent(int keyCode, boolean isPressed) {
    }

    public void stop() {
        isRunning = false;
    }
}
