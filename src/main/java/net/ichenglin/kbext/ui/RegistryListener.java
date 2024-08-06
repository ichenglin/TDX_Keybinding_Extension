package net.ichenglin.kbext.ui;

import java.util.EventListener;

public interface RegistryListener extends EventListener {
    void registryUpdated(RegistryEvent event);
}
