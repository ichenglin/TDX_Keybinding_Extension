package net.ichenglin.kbext.extension;

import com.sun.jna.platform.win32.WinUser.MSG;
import net.ichenglin.kbext.util.User32;

import java.util.HashMap;
import java.util.Map;

public class KeybindingHotkey {

    private final HashMap<Integer, HotkeyRegistry> hotkey_registry;
    private       KeybindingHotkeyThread           hotkey_thread;

    public KeybindingHotkey() {
        this.hotkey_registry = new HashMap<Integer, HotkeyRegistry>();
    }

    public void hotkey_register(int id, int modifiers, int keycode, Runnable handler) {
        try {
            if (this.hotkey_thread != null) this.hotkey_thread.hotkey_stop();
            this.hotkey_registry.put(id, new HotkeyRegistry(id, modifiers, keycode, handler));
            this.hotkey_thread = new KeybindingHotkeyThread(this.hotkey_registry);
            this.hotkey_thread.hotkey_start();
        } catch (InterruptedException ignored) {}
    }

    public void hotkey_update(int id, Integer modifiers, Integer keycode, Runnable handler) {
        HotkeyRegistry registry_data = this.hotkey_registry.get(id);
        if (registry_data == null) return;
        int      hotkey_modifiers = (modifiers == null) ? registry_data.hotkey_modifiers : modifiers;
        int      hotkey_keycode   = (keycode   == null) ? registry_data.hotkey_keycode   : keycode;
        Runnable hotkey_handler   = (handler   == null) ? registry_data.hotkey_handler   : handler;
        this.hotkey_register(id, hotkey_modifiers, hotkey_keycode, hotkey_handler);
    }
}

class KeybindingHotkeyThread extends Thread {

    private final HashMap<Integer, HotkeyRegistry> hotkey_registry;

    public KeybindingHotkeyThread(HashMap<Integer, HotkeyRegistry> hotkey_registry) {
        this.hotkey_registry = hotkey_registry;
    }

    public void hotkey_start() {
        if (this.isAlive()) throw new IllegalStateException("Hotkey thread is already running");
        this.start();
    }

    public void hotkey_stop() throws InterruptedException {
        if (!this.isAlive()) throw new IllegalStateException("Hotkey thread is not running");
        this.interrupt();
        this.join();
    }

    @Override
    public void run() {
        // register hotkeys
        for (Map.Entry<Integer, HotkeyRegistry> registry_entry : this.hotkey_registry.entrySet()) {
            HotkeyRegistry registry_data = registry_entry.getValue();
            User32.INSTANCE.RegisterHotKey(null, registry_data.hotkey_id, registry_data.hotkey_modifiers, registry_data.hotkey_keycode);
        }
        // handle hotkeys
        try {
            MSG hotkey_message = new MSG();
            while (!Thread.currentThread().isInterrupted()) {
                while (User32.INSTANCE.PeekMessage(hotkey_message, null, 0, 0, User32.PM_REMOVE)) {
                    if (hotkey_message.message != User32.WM_HOTKEY) continue;
                    this.hotkey_registry.get(hotkey_message.wParam.intValue()).hotkey_handler.run();
                }
                Thread.sleep(50);
            }
        } catch (InterruptedException ignored) {}
        // unregister hotkeys
        for (Map.Entry<Integer, HotkeyRegistry> registry_entry : this.hotkey_registry.entrySet()) {
            HotkeyRegistry registry_data = registry_entry.getValue();
            User32.INSTANCE.UnregisterHotKey(null, registry_data.hotkey_id);
        }
    }
}

// workaround for java records introduced in Java 14
class HotkeyRegistry {
    public int      hotkey_id;
    public int      hotkey_modifiers;
    public int      hotkey_keycode;
    public Runnable hotkey_handler;

    public HotkeyRegistry(int id, int modifiers, int keycode, Runnable handler) {
        this.hotkey_id        = id;
        this.hotkey_modifiers = modifiers;
        this.hotkey_keycode   = keycode;
        this.hotkey_handler   = handler;
    }
}