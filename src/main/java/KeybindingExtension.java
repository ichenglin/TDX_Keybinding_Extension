import com.sun.jna.platform.win32.User32;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeybindingExtension {
    public static void main(String[] args) throws AWTException {
        KeybindingInterface extension_interface = new KeybindingInterface();
        KeybindingRobot     extension_robot     = new KeybindingRobot();
        KeybindingHotkey    extension_hotkey    = new KeybindingHotkey();
        extension_hotkey.hotkey_register(1, User32.MOD_NOREPEAT, KeyEvent.VK_F, () -> {
            boolean roblox_focused = KeybindingWindow.window_focused().get_name().equals("Roblox");
            if (roblox_focused) extension_robot.upgrade_max(KeyEvent.VK_E, KeyEvent.VK_R);
        });
        extension_hotkey.hotkey_register(2, User32.MOD_NOREPEAT, KeyEvent.VK_T, () -> {
            boolean roblox_focused = KeybindingWindow.window_focused().get_name().equals("Roblox");
            if (roblox_focused) extension_robot.upgrade_max(KeyEvent.VK_R, KeyEvent.VK_E);
        });
        extension_hotkey.start();
    }
}