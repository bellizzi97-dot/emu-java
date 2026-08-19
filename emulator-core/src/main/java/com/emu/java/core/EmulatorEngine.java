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
    private String gameTitle = "J2ME Game";
    private int tick = 0;

    public EmulatorEngine() {
        displayBitmap = Bitmap.createBitmap(240, 320, Bitmap.Config.ARGB_8888);
        displayCanvas = new Canvas(displayBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setFrameUpdateListener(FrameUpdateListener listener) {
        this.frameUpdateListener = listener;
    }

    public void setGameTitle(String title) {
        this.gameTitle = title;
    }

    public void start() {
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    renderFrame();
                    if (frameUpdateListener != null) {
                        frameUpdateListener.onFrameUpdate(displayBitmap);
                    }
                    try {
                        Thread.sleep(33);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }

    private void renderFrame() {
        tick++;
        displayCanvas.drawColor(Color.parseColor("#0F0F14"));

        paint.setColor(Color.parseColor("#1E88E5"));
        displayCanvas.drawRect(0, 0, 240, 28, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(13);
        displayCanvas.drawText(gameTitle, 8, 19, paint);

        paint.setColor(Color.parseColor("#00E676"));
        paint.setTextSize(15);
        displayCanvas.drawText("MIDlet Cargado", 15, 60, paint);

        paint.setColor(Color.LTGRAY);
        paint.setTextSize(11);
        displayCanvas.drawText("Iniciando Canvas J2ME...", 15, 85, paint);
        displayCanvas.drawText("Resolución: 240x320", 15, 105, paint);
        displayCanvas.drawText("Ticks activos: " + tick, 15, 125, paint);
    }

    public void sendKeyEvent(int keyCode, boolean isPressed) {
    }

    public void stop() {
        isRunning = false;
    }
}
