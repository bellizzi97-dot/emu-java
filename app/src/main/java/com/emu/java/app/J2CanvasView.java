package com.emu.java.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.emu.java.core.DisplayScale;

public class J2CanvasView extends View {
    private Bitmap frameBuffer;
    private final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private int gameWidth = 240;
    private int gameHeight = 320;

    public J2CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        frameBuffer = Bitmap.createBitmap(gameWidth, gameHeight, Bitmap.Config.ARGB_8888);
    }

    public void setGameResolution(int width, int height) {
        this.gameWidth = width;
        this.gameHeight = height;
        this.frameBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        invalidate();
    }

    public void updateFrame(Bitmap newFrame) {
        this.frameBuffer = newFrame;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK); // Fondos y márgenes negros

        if (frameBuffer != null) {
            int[] bounds = DisplayScale.calculateScaledBounds(gameWidth, gameHeight, getWidth(), getHeight());
            Rect src = new Rect(0, 0, gameWidth, gameHeight);
            Rect dest = new Rect(bounds[2], bounds[3], bounds[2] + bounds[0], bounds[3] + bounds[1]);
            canvas.drawBitmap(frameBuffer, src, dest, paint);
        }
    }
}
