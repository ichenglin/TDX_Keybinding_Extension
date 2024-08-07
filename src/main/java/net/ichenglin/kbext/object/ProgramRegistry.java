package net.ichenglin.kbext.object;

import net.ichenglin.kbext.ui.RegistryEvent;
import net.ichenglin.kbext.ui.RegistryListener;

import java.util.*;

public class ProgramRegistry {

    private final HashMap<String, ProgramRegistryData> registry_data;
    private final ArrayList<RegistryListener>          registry_listeners;

    public ProgramRegistry() {
        this.registry_data      = new HashMap<String, ProgramRegistryData>();
        this.registry_listeners = new ArrayList<RegistryListener>();
    }

    public void set_data(String data_key, Object data_value) {
        ProgramRegistryData data_old = this.registry_data.put(data_key, new ProgramRegistryData(data_key, data_value));
        if (data_old != null && data_value.equals(data_old.get_value())) return;
        this.notify_listeners(new RegistryEvent(data_key));
        if (!data_key.equals("int_upd")) this.set_data("int_upd", (int) this.get_data_default("int_upd", 0) + 1);
    }

    public Object get_data(String data_key) {
        return this.registry_data.get(data_key).get_value();
    }

    public Object get_data_default(String data_key, Object data_default) {
        if (!this.registry_data.containsKey(data_key)) return data_default;
        return this.registry_data.get(data_key).get_value();
    }

    public ProgramRegistryData[] get_all() {
        ProgramRegistryData[] entry_pairs = this.registry_data.values().toArray(new ProgramRegistryData[0]);
        Arrays.sort(entry_pairs);
        return entry_pairs;
    }

    public void add_listener(RegistryListener registry_listener) {
        this.registry_listeners.add(registry_listener);
    }

    private void notify_listeners(RegistryEvent registry_event) {
        this.registry_listeners.forEach((registry_listener) -> registry_listener.registryUpdated(registry_event));
    }

}

