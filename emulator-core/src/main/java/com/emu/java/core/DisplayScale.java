package com.emu.java.core;

public class DisplayScale {
    public static int[] calculateScaledBounds(int origW, int origH, int screenW, int screenH) {
        float scale = Math.min((float) screenW / origW, (float) screenH / origH);
        int scaledW = Math.round(origW * scale);
        int scaledH = Math.round(origH * scale);
        int offsetX = (screenW - scaledW) / 2;
        int offsetY = (screenH - scaledH) / 2;
        return new int[]{scaledW, scaledH, offsetX, offsetY};
    }
}
