package com.emu.java.core;

public class EmulatorEngine implements Runnable {
    private boolean isRunning = false;
    private Thread gameThread;
    private final int targetFps = 30;
    private final long frameTimeMs = 1000 / targetFps;
    private FrameUpdateListener listener;

    public interface FrameUpdateListener {
        void onFrameUpdate();
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
        // Reservado para despachar eventos a la pila MIDlet
    }

    @Override
    public void run() {
        while (isRunning) {
            long startTime = System.currentTimeMillis();

            updateGameLogic();
            if (listener != null) {
                listener.onFrameUpdate();
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
        // Lógica de emulación
    }
}
