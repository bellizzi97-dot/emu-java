package com.emu.java.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;

public class EmulatorEngine {
    public interface FrameUpdateListener {
        void onFrameUpdate(Bitmap frame);
    }

    private FrameUpdateListener frameUpdateListener;
    private boolean isRunning = false;
    private Bitmap displayBitmap;
    private Canvas displayCanvas;
    private Paint paint;
    private Handler mainHandler;
    private String gameTitle = "J2ME Game";
    private int frameCount = 0;

    public EmulatorEngine() {
        displayBitmap = Bitmap.createBitmap(240, 320, Bitmap.Config.ARGB_8888);
        displayCanvas = new Canvas(displayBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainHandler = new Handler(Looper.getMainLooper());
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
                        Thread.sleep(33); // ~30 FPS
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }

    private void renderFrame() {
        frameCount++;
        displayCanvas.drawColor(Color.parseColor("#0A0E17"));

        // Renderizado del Canvas J2ME
        paint.setColor(Color.WHITE);
        paint.setTextSize(14);
        displayCanvas.drawText("Ejecutando: " + gameTitle, 10, 25, paint);

        // Simulador de renderizado gráfico de MIDlet
        paint.setColor(Color.GREEN);
        displayCanvas.drawRect(20, 40, 220, 280, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(12);
        displayCanvas.drawText("Cargando Clases MIDP...", 30, 150, paint);
        displayCanvas.drawText("Frame: " + frameCount, 30, 180, paint);
    }

    public void sendKeyEvent(int keyCode, boolean isPressed) {
        // Enviar eventos de teclado al Canvas activo del MIDlet
    }

    public void stop() {
        isRunning = false;
    }
}
