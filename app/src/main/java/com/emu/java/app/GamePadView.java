package com.emu.java.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.emu.java.core.KeyMapper;

public class GamePadView extends View {
    private final Paint bgPaint = new Paint();
    private final Paint btnPaint = new Paint();
    private final Paint textPaint = new Paint();
    private OnKeyListener listener;

    public interface OnKeyListener {
        void onKeyPair(KeyMapper.GbButton button, boolean isPressed);
    }

    public GamePadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bgPaint.setColor(Color.parseColor("#1A1A24"));
        btnPaint.setColor(Color.parseColor("#2E2E3E"));
        btnPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(32);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }

    public void setOnKeyListener(OnKeyListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        canvas.drawRect(0, 0, w, h, bgPaint);

        float cx = w * 0.25f;
        float cy = h * 0.5f;
        float btnSize = Math.min(w, h) * 0.2f;

        // D-Pad Cruceta
        canvas.drawRoundRect(cx - btnSize/2, cy - btnSize * 1.3f, cx + btnSize/2, cy - btnSize*0.3f, 12, 12, btnPaint);
        canvas.drawRoundRect(cx - btnSize/2, cy + btnSize*0.3f, cx + btnSize/2, cy + btnSize * 1.3f, 12, 12, btnPaint);
        canvas.drawRoundRect(cx - btnSize * 1.3f, cy - btnSize/2, cx - btnSize*0.3f, cy + btnSize/2, 12, 12, btnPaint);
        canvas.drawRoundRect(cx + btnSize*0.3f, cy - btnSize/2, cx + btnSize * 1.3f, cy + btnSize/2, 12, 12, btnPaint);

        canvas.drawText("▲", cx, cy - btnSize*0.6f + 10, textPaint);
        canvas.drawText("▼", cx, cy + btnSize*0.8f + 10, textPaint);
        canvas.drawText("◄", cx - btnSize*0.8f, cy + 10, textPaint);
        canvas.drawText("►", cx + btnSize*0.8f, cy + 10, textPaint);

        // Botones de acción
        float rx = w * 0.75f;
        float radius = Math.min(w, h) * 0.18f;
        canvas.drawCircle(rx - radius * 1.2f, cy, radius, btnPaint);
        canvas.drawCircle(rx + radius * 1.2f, cy, radius, btnPaint);

        canvas.drawText("OK", rx - radius * 1.2f, cy + 10, textPaint);
        canvas.drawText("LS", rx + radius * 1.2f, cy + 10, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (listener == null) return true;
        int action = event.getAction();
        boolean isPressed = (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE);

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            isPressed = false;
        }

        float x = event.getX();
        float y = event.getY();
        float w = getWidth();
        float h = getHeight();
        float cx = w * 0.25f;
        float cy = h * 0.5f;
        float btnSize = Math.min(w, h) * 0.2f;

        if (Math.abs(x - cx) < btnSize && Math.abs(y - (cy - btnSize)) < btnSize) {
            listener.onKeyPair(KeyMapper.GbButton.UP, isPressed);
        } else if (Math.abs(x - cx) < btnSize && Math.abs(y - (cy + btnSize)) < btnSize) {
            listener.onKeyPair(KeyMapper.GbButton.DOWN, isPressed);
        } else if (Math.abs(x - (cx - btnSize)) < btnSize && Math.abs(y - cy) < btnSize) {
            listener.onKeyPair(KeyMapper.GbButton.LEFT, isPressed);
        } else if (Math.abs(x - (cx + btnSize)) < btnSize && Math.abs(y - cy) < btnSize) {
            listener.onKeyPair(KeyMapper.GbButton.RIGHT, isPressed);
        }

        return true;
    }
}
