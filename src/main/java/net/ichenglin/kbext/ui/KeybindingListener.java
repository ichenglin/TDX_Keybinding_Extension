package net.ichenglin.kbext.ui;

import java.util.EventListener;

public interface KeybindingListener extends EventListener {
    void keybindingUpdated(KeybindingEvent event);
}
