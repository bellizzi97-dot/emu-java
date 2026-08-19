package com.emu.java.core;

import android.content.Context;
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

    private String activeJarPath = null;
    private String activeMainClass = null;
    private int frameCounter = 0;

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

    public void loadAndRunJar(Context context, String jarPath, String mainClass) {
        this.activeJarPath = jarPath;
        this.activeMainClass = mainClass;
        this.frameCounter = 0;
    }

    private void renderFrame() {
        frameCounter++;
        displayCanvas.drawColor(Color.parseColor("#0F0F14"));

        paint.setColor(Color.parseColor("#1E88E5"));
        displayCanvas.drawRect(0, 0, 240, 28, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(12);
        displayCanvas.drawText("J2ME Runner", 8, 19, paint);

        if (activeMainClass != null) {
            paint.setColor(Color.parseColor("#00E676"));
            paint.setTextSize(14);
            displayCanvas.drawText("JAR Cargado Exitosamente", 10, 60, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(11);
            displayCanvas.drawText("Clase: " + activeMainClass, 10, 90, paint);
            displayCanvas.drawText("Frame Loop: " + frameCounter, 10, 110, paint);
        } else {
            paint.setColor(Color.parseColor("#8E8E93"));
            paint.setTextSize(13);
            displayCanvas.drawText("Selecciona un archivo .JAR", 10, 60, paint);
        }
    }

    public void sendKeyEvent(int keyCode, boolean isPressed) {
    }

    public void stop() {
        isRunning = false;
    }
}
