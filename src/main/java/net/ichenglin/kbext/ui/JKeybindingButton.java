package net.ichenglin.kbext.ui;

import net.ichenglin.kbext.util.ValueRange;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.function.Consumer;

public class JKeybindingButton extends JToggleButton {

    private       int       keycode_selected;
    private final Integer[] keycode_filter;

    public JKeybindingButton(int keycode_selected, Integer[] keycode_filter) {
        this.keycode_filter = keycode_filter;
        this.keybinding_update(keycode_selected);
        super.addItemListener((item_event) -> {
            switch (item_event.getStateChange()) {
                case ItemEvent.SELECTED:
                    super.addKeyListener(new LocalKeyListener((key_event) -> {
                        int keycode_new = key_event.getExtendedKeyCode();
                        if (!this.keybinding_valid(keycode_new)) return;
                        this.keybinding_update(keycode_new);
                        this.keylistener_clear();
                    }));
                    break;
                case ItemEvent.DESELECTED:
                    this.keylistener_clear();
                    break;
                default:
                    break;
            }
        });
    }

    public void addKeybindingListener(KeybindingListener keybinding_listener) {
        super.listenerList.add(KeybindingListener.class, keybinding_listener);
    }

    protected void keybinding_notify(KeybindingEvent keybinding_event) {
        Object[] event_listeners = super.listenerList.getListenerList();
        for (int listener_index = (event_listeners.length - 2); listener_index >= 0; listener_index -= 2) {
            if (event_listeners[listener_index] != KeybindingListener.class) continue;
            ((KeybindingListener) event_listeners[listener_index + 1]).keybindingUpdated(keybinding_event);
        }
    }

    private boolean keybinding_valid(int keycode_selected) {
        boolean keycode_number    = new ValueRange(KeyEvent.VK_0, KeyEvent.VK_9).check_within(keycode_selected);
        boolean keycode_character = new ValueRange(KeyEvent.VK_A, KeyEvent.VK_Z).check_within(keycode_selected);
        boolean keycode_blacklist = Arrays.asList(this.keycode_filter).contains(keycode_selected);
        return (keycode_number || keycode_character) && (!keycode_blacklist);
    }

    private void keybinding_update(int keycode_selected) {
        int keycode_old       = this.keycode_selected;
        this.keycode_selected = keycode_selected;
        this.setText(Character.toString((char) keycode_selected));
        this.setSelected(false);
        this.keybinding_notify(new KeybindingEvent(keycode_old, keycode_selected));
    }

    private void keylistener_clear() {
        for (KeyListener key_listener : super.getKeyListeners()) super.removeKeyListener(key_listener);
    }
}

class LocalKeyListener implements KeyListener {

    private final Consumer<KeyEvent> key_event;

    public LocalKeyListener(Consumer<KeyEvent> key_event) {
        this.key_event = key_event;
    }

    @Override
    public void keyTyped(KeyEvent key_event) {}

    @Override
    public void keyPressed(KeyEvent key_event) {
        this.key_event.accept(key_event);
    }

    @Override
    public void keyReleased(KeyEvent key_event) {}
}
