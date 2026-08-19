package com.emu.java.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class EmulatorEngine implements Runnable {
    private boolean isRunning = false;
    private Thread gameThread;
    private final int targetFps = 30;
    private final long frameTimeMs = 1000 / targetFps;
    private FrameUpdateListener listener;
    private Bitmap frameBuffer;
    private Canvas canvasBuffer;
    private Paint paint;
    private String currentGameName = "Sin juego";
    private int frameCounter = 0;

    public interface FrameUpdateListener {
        void onFrameUpdate(Bitmap frame);
    }

    public EmulatorEngine() {
        frameBuffer = Bitmap.createBitmap(240, 320, Bitmap.Config.ARGB_8888);
        canvasBuffer = new Canvas(frameBuffer);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setGameTitle(String title) {
        this.currentGameName = title;
    }

    public void setFrameUpdateListener(FrameUpdateListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (isRunning) return;
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stop() {
        isRunning = false;
        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void sendKeyEvent(int keyCode, boolean isPressed) {
        // Reservado para entrada de teclado
    }

    @Override
    public void run() {
        while (isRunning) {
            long startTime = System.currentTimeMillis();

            updateGameLogic();
            if (listener != null) {
                listener.onFrameUpdate(frameBuffer);
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = frameTimeMs - elapsedTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void updateGameLogic() {
        frameCounter++;
        canvasBuffer.drawColor(Color.rgb(10, 25, 47));

        paint.setColor(Color.WHITE);
        paint.setTextSize(14);
        canvasBuffer.drawText("J2ME Canvas Active", 10, 30, paint);

        paint.setColor(Color.GREEN);
        paint.setTextSize(12);
        canvasBuffer.drawText("Juego: " + currentGameName, 10, 60, paint);

        paint.setColor(Color.YELLOW);
        int xPos = (frameCounter * 4) % 200 + 20;
        canvasBuffer.drawRect(xPos, 100, xPos + 20, 120, paint);

        paint.setColor(Color.CYAN);
        canvasBuffer.drawText("FPS: 30 | Frame: " + frameCounter, 10, 290, paint);
    }
}
