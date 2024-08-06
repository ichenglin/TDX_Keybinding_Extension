package net.ichenglin.kbext.ui;

public class KeybindingEvent {

    private final int keycode_original;
    private final int keycode_new;

    public KeybindingEvent(int keycode_original, int keycode_new) {
        this.keycode_original = keycode_original;
        this.keycode_new      = keycode_new;
    }

    public int get_original() {
        return this.keycode_original;
    }

    public int get_new() {
        return this.keycode_new;
    }
}
