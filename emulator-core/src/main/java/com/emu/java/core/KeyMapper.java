package com.emu.java.core;

public class KeyMapper {
    public static final int KEY_UP = -1;
    public static final int KEY_DOWN = -2;
    public static final int KEY_LEFT = -3;
    public static final int KEY_RIGHT = -4;
    public static final int KEY_FIRE = -5;
    public static final int KEY_NUM0 = 48;

    public enum GbButton { UP, DOWN, LEFT, RIGHT, BUTTON_A, BUTTON_B }

    public static int toJ2meKey(GbButton button) {
        switch (button) {
            case UP: return KEY_UP;
            case DOWN: return KEY_DOWN;
            case LEFT: return KEY_LEFT;
            case RIGHT: return KEY_RIGHT;
            case BUTTON_A: return KEY_FIRE;
            case BUTTON_B: return KEY_NUM0;
            default: return 0;
        }
    }
}
