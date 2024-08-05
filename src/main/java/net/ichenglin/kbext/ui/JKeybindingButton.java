package net.ichenglin.kbext.ui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

public class JKeybindingButton extends JToggleButton {

    private char keybinding_selected;

    public JKeybindingButton(char keybinding_selected) {
        super.addActionListener((action_event) -> {
            super.addKeyListener(new LocalKeyListener((key_event) -> {
                this.keybinding_update(key_event.getKeyChar());
                super.removeKeyListener(this.getKeyListeners()[0]);
            }));
        });
        this.keybinding_update(keybinding_selected);
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

    private void keybinding_update(char keybinding_selected) {
        char keybinding_old      = this.keybinding_selected;
        this.keybinding_selected = Character.toLowerCase(keybinding_selected);
        this.setText(Character.toString(Character.toUpperCase(keybinding_selected)));
        this.setSelected(false);
        this.keybinding_notify(new KeybindingEvent(keybinding_old, this.keybinding_selected));
    }
}

class LocalKeyListener implements KeyListener {

    private final Consumer<KeyEvent> key_event;

    public LocalKeyListener(Consumer<KeyEvent> key_event) {
        this.key_event = key_event;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        this.key_event.accept(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
