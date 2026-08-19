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
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private OnKeyListener listener;

    public interface OnKeyListener {
        void onKeyPair(KeyMapper.GbButton button, boolean isPressed);
    }

    public GamePadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnKeyListener(OnKeyListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        boolean isPressed = (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN);
        
        if (listener != null && isPressed) {
            float x = event.getX();
            float y = event.getY();
            if (x < getWidth() / 2f) {
                if (y < getHeight() / 2f) listener.onKeyPair(KeyMapper.GbButton.UP, true);
                else listener.onKeyPair(KeyMapper.GbButton.DOWN, true);
            } else {
                listener.onKeyPair(KeyMapper.GbButton.BUTTON_A, true);
            }
        }
        return true;
    }
}
