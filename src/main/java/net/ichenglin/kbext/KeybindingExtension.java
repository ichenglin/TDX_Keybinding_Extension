package net.ichenglin.kbext;

import com.sun.jna.platform.win32.User32;
import net.ichenglin.kbext.extension.KeybindingHotkey;
import net.ichenglin.kbext.extension.KeybindingInterface;
import net.ichenglin.kbext.extension.KeybindingRobot;
import net.ichenglin.kbext.extension.KeybindingWindow;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeybindingExtension {
    public static void main(String[] args) throws AWTException {
        KeybindingHotkey    extension_hotkey    = new KeybindingHotkey();
        KeybindingInterface extension_interface = new KeybindingInterface(extension_hotkey);
        KeybindingRobot     extension_robot     = new KeybindingRobot();
        extension_hotkey.hotkey_register(1, User32.MOD_NOREPEAT, KeyEvent.VK_F, () -> {
            boolean roblox_focused = KeybindingWindow.window_focused().get_name().equals("Roblox");
            if (roblox_focused) extension_robot.upgrade_max(KeyEvent.VK_E, KeyEvent.VK_R);
            System.out.println("test");
        });
        extension_hotkey.hotkey_register(2, User32.MOD_NOREPEAT, KeyEvent.VK_T, () -> {
            boolean roblox_focused = KeybindingWindow.window_focused().get_name().equals("Roblox");
            if (roblox_focused) extension_robot.upgrade_max(KeyEvent.VK_R, KeyEvent.VK_E);
        });
    }
}