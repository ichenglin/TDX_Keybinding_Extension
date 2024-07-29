import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser.MSG;

import java.util.HashMap;
import java.util.Map;

public class KeybindingHotkey extends Thread {

    private final HashMap<Integer, HotkeyRegistry> hotkey_registry;

    public KeybindingHotkey() {
        this.hotkey_registry = new HashMap<Integer, HotkeyRegistry>();
    }

    public boolean hotkey_register(int id, int modifiers, int keycode, Runnable handler) {
        if (this.isAlive()) return false;
        this.hotkey_registry.put(id, new HotkeyRegistry(id, modifiers, keycode, handler));
        return true;
    }

    @Override
    public void run() {
        // register hotkeys
        for (Map.Entry<Integer, HotkeyRegistry> registry_entry : this.hotkey_registry.entrySet()) {
            HotkeyRegistry registry_data = registry_entry.getValue();
            User32.INSTANCE.RegisterHotKey(null, registry_data.hotkey_id, registry_data.hotkey_modifiers, registry_data.hotkey_keycode);
        }
        // handle hotkeys
        MSG hotkey_message = new MSG();
        while (User32.INSTANCE.GetMessage(hotkey_message, null, 0, 0) != 0) {
            if (hotkey_message.message != User32.WM_HOTKEY) continue;
            this.hotkey_registry.get(hotkey_message.wParam.intValue()).hotkey_handler.run();
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