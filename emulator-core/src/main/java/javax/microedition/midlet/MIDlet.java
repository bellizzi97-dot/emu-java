package javax.microedition.midlet;

public abstract class MIDlet {
    protected MIDlet() {}
    public abstract void startApp() throws Exception;
    public abstract void pauseApp();
    public abstract void destroyApp(boolean unconditional);
}
