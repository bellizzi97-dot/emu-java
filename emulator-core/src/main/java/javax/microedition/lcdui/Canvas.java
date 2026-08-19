package javax.microedition.lcdui;

public abstract class Canvas extends Displayable {
    public static final int UP = -1;
    public static final int DOWN = -2;
    public static final int LEFT = -3;
    public static final int RIGHT = -4;
    public static final int FIRE = -5;

    protected abstract void paint(Graphics g);

    public void repaint() {}

    protected void keyPressed(int keyCode) {}
    protected void keyReleased(int keyCode) {}
}
