package javax.microedition.lcdui;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Graphics {
    private Canvas canvas;
    private final Paint paint = new Paint();

    public Graphics(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setColor(int red, int green, int blue) {
        paint.setARGB(255, red, green, blue);
    }

    public void fillRect(int x, int y, int width, int height) {
        if (canvas != null) {
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
    }

    public void drawRect(int x, int y, int width, int height) {
        if (canvas != null) {
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
    }
}
