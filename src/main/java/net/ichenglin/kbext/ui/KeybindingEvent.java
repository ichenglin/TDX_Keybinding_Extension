package net.ichenglin.kbext.ui;

public class KeybindingEvent {

    private final char keybinding_original;
    private final char keybinding_new;

    public KeybindingEvent(char keybinding_original, char keybinding_new) {
        this.keybinding_original = keybinding_original;
        this.keybinding_new      = keybinding_new;
    }

    public char get_original() {
        return this.keybinding_original;
    }

    public char get_new() {
        return this.keybinding_new;
    }
}
