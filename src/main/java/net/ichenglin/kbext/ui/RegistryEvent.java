package net.ichenglin.kbext.ui;

public class RegistryEvent {

    private final String registry_key;

    public RegistryEvent(String registry_key) {
        this.registry_key = registry_key;
    }

    public String get_key() {
        return this.registry_key;
    }
}
