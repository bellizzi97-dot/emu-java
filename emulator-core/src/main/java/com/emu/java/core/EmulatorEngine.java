package com.emu.java.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import dalvik.system.DexClassLoader;

import java.io.File;
import javax.microedition.midlet.MIDlet;

public class EmulatorEngine {
    public interface FrameUpdateListener {
        void onFrameUpdate(Bitmap frame);
    }

    private FrameUpdateListener frameUpdateListener;
    private boolean isRunning = false;
    private final Bitmap displayBitmap;
    private final Canvas displayCanvas;
    private final Paint paint;
    private MIDlet activeMidlet;

    public EmulatorEngine() {
        displayBitmap = Bitmap.createBitmap(240, 320, Bitmap.Config.ARGB_8888);
        displayCanvas = new Canvas(displayBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setFrameUpdateListener(FrameUpdateListener listener) {
        this.frameUpdateListener = listener;
    }

    public void loadAndRunMidlet(Context context, String jarPath, String mainClassName) {
        try {
            File dexOptDir = context.getDir("dex", Context.MODE_PRIVATE);
            DexClassLoader classLoader = new DexClassLoader(
                    jarPath,
                    dexOptDir.getAbsolutePath(),
                    null,
                    context.getClassLoader()
            );

            Class<?> midletClass = classLoader.loadClass(mainClassName);
            activeMidlet = (MIDlet) midletClass.newInstance();
            
            // Iniciar ciclo de vida del MIDlet
            activeMidlet.startApp();
            startRenderLoop();
        } catch (Exception e) {
            e.printStackTrace();
            renderError(e.getMessage());
        }
    }

    private void startRenderLoop() {
        isRunning = true;
        new Thread(() -> {
            while (isRunning) {
                if (frameUpdateListener != null) {
                    frameUpdateListener.onFrameUpdate(displayBitmap);
                }
                try {
                    Thread.sleep(33);
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void renderError(String message) {
        displayCanvas.drawColor(Color.BLACK);
        paint.setColor(Color.RED);
        paint.setTextSize(14);
        displayCanvas.drawText("Error al ejecutar MIDlet:", 10, 30, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(11);
        displayCanvas.drawText(message != null ? message : "Clase no encontrada", 10, 60, paint);
        if (frameUpdateListener != null) {
            frameUpdateListener.onFrameUpdate(displayBitmap);
        }
    }

    public void sendKeyEvent(int keyCode, boolean isPressed) {
        // Enviar evento de tecla al Canvas del MIDlet activo
    }

    public void stop() {
        isRunning = false;
        if (activeMidlet != null) {
            activeMidlet.destroyApp(true);
        }
    }
}
