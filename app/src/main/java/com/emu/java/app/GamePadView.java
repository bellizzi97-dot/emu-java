package com.emu.java.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.emu.java.core.KeyMapper;

public class GamePadView extends View {
    private final Paint bgPaint = new Paint();
    private final Paint btnPaint = new Paint();
    private final Paint borderPaint = new Paint();
    private final Paint textPaint = new Paint();
    private final Paint subTextPaint = new Paint();
    private OnKeyListener listener;

    public interface OnKeyListener {
        void onKey(int keyCode, boolean isPressed);
    }

    public GamePadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bgPaint.setColor(Color.parseColor("#0D0D0D"));
        
        btnPaint.setColor(Color.parseColor("#1C1C1E"));
        btnPaint.setStyle(Paint.Style.FILL);

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(3f);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(34);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        subTextPaint.setColor(Color.parseColor("#AAAAAA"));
        subTextPaint.setTextSize(18);
        subTextPaint.setTextAlign(Paint.Align.CENTER);
        subTextPaint.setAntiAlias(true);
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

        // --- Panel de Navegación Superior ---
        float navH = h * 0.32f;
        float btnW = w * 0.28f;
        float btnH = navH * 0.42f;

        // Soft Key Izquierda (Verde)
        borderPaint.setColor(Color.parseColor("#2ECC71"));
        drawButton(canvas, 16, 12, btnW, btnH, borderPaint);
        canvas.drawText("─", 16 + btnW/2, 12 + btnH/2 + 10, textPaint);

        // Soft Key Derecha (Roja)
        borderPaint.setColor(Color.parseColor("#E74C3C"));
        drawButton(canvas, w - btnW - 16, 12, btnW, btnH, borderPaint);
        canvas.drawText("─", w - btnW/2 - 16, 12 + btnH/2 + 10, textPaint);

        // D-Pad Central
        float dpadX = w * 0.33f;
        float dpadW = w * 0.34f;
        borderPaint.setColor(Color.parseColor("#8E8E93"));
        drawButton(canvas, dpadX, 8, dpadW, navH - 16, borderPaint);
        canvas.drawText("OK", dpadX + dpadW/2, navH/2 + 10, textPaint);

        // --- Teclado Numérico T9 ---
        float keyStartY = navH + 8;
        float keyGridH = h - keyStartY - 12;
        float cellW = (w - 32) / 3f;
        float cellH = (keyGridH - 24) / 4f;

        String[][] keys = {
            {"1", "2", "3"},
            {"4", "5", "6"},
            {"7", "8", "9"},
            {"*", "0", "#"}
        };

        String[][] subKeys = {
            {"@.", "abc", "def"},
            {"ghi", "jkl", "mno"},
            {"pqrs", "tuv", "wxyz"},
            {"", "+", "⇧"}
        };

        borderPaint.setColor(Color.parseColor("#3A3A3C"));
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 3; c++) {
                float x = 16 + c * cellW;
                float y = keyStartY + r * cellH;
                drawButton(canvas, x + 2, y + 2, cellW - 4, cellH - 4, borderPaint);

                canvas.drawText(keys[r][c], x + cellW/2, y + cellH/2, textPaint);
                if (!subKeys[r][c].isEmpty()) {
                    canvas.drawText(subKeys[r][c], x + cellW/2, y + cellH/2 + 22, subTextPaint);
                }
            }
        }
    }

    private void drawButton(Canvas canvas, float x, float y, float w, float h, Paint bPaint) {
        RectF rect = new RectF(x, y, x + w, y + h);
        canvas.drawRoundRect(rect, 10, 10, btnPaint);
        canvas.drawRoundRect(rect, 10, 10, bPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (listener == null) return true;
        int action = event.getAction();
        boolean isPressed = (action == MotionEvent.ACTION_DOWN);

        if (action == MotionEvent.ACTION_UP) {
            isPressed = false;
        }

        // Mapeo básico de toques del teclado
        float x = event.getX();
        float y = event.getY();
        int w = getWidth();
        int h = getHeight();

        float navH = h * 0.32f;
        if (y < navH) {
            if (x < w * 0.3f) listener.onKey(-6, isPressed); // Left SoftKey
            else if (x > w * 0.7f) listener.onKey(-7, isPressed); // Right SoftKey
            else listener.onKey(-5, isPressed); // Select/OK
        }

        return true;
    }
}
