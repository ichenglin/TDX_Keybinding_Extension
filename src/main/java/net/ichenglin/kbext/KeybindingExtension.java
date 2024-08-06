package net.ichenglin.kbext;

import com.sun.jna.platform.win32.User32;
import net.ichenglin.kbext.extension.*;
import net.ichenglin.kbext.object.GameState;
import net.ichenglin.kbext.object.RecognitionException;
import net.ichenglin.kbext.object.ScheduledTask;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class KeybindingExtension {
    public static void main(String[] args) {
        KeybindingReference Extension = new KeybindingReference();
        Extension.extension_hotkey    = new KeybindingHotkey();
        Extension.extension_robot     = new KeybindingRobot();
        Extension.extension_hotkey.hotkey_register(1, User32.MOD_NOREPEAT, KeyEvent.VK_F, () -> {
            if (!KeybindingWindow.window_focused().get_name().equals("Roblox")) return;
            Extension.extension_robot.upgrade_max(KeyEvent.VK_E, KeyEvent.VK_R);
        });
        Extension.extension_hotkey.hotkey_register(2, User32.MOD_NOREPEAT, KeyEvent.VK_T, () -> {
            if (!KeybindingWindow.window_focused().get_name().equals("Roblox")) return;
            Extension.extension_robot.upgrade_max(KeyEvent.VK_R, KeyEvent.VK_E);
        });
        Extension.extension_hotkey.hotkey_register(3, User32.MOD_NOREPEAT, KeyEvent.VK_Z, () -> {
            if (!KeybindingWindow.window_focused().get_name().equals("Roblox")) return;
            System.out.println("Nothing Now :(");
        });
        Extension.extension_interface   = new KeybindingInterface(Extension.extension_hotkey);
        Extension.extension_recognition = new KeybindingRecognition("D:\\Github\\TDX_Keybinding_Extension\\asset");
        Extension.extension_gamestate   = new GameState(0, 0, 0);
        new ScheduledTask(500, () -> KeybindingExtension.task_recognition(Extension));
    }

    private static void task_recognition(KeybindingReference Extension) {
        try {
            KeybindingWindow window_focused    = KeybindingWindow.window_focused();
            BufferedImage    window_screenshot = Extension.extension_robot.screenshot_capture(window_focused.get_bounds());
            Extension.extension_recognition.set_image(window_screenshot);
            Extension.extension_gamestate = Extension.extension_recognition.recognize_state();
            System.out.println(Extension.extension_gamestate.get_timer() % 60);
        } catch (RecognitionException exception) {
            System.out.println("Recognition Error: " + exception.getMessage());
        }
    }
}

class KeybindingReference {
    public KeybindingHotkey           extension_hotkey;
    public KeybindingRobot            extension_robot;
    public KeybindingInterface        extension_interface;
    public KeybindingRecognition      extension_recognition;
    public GameState                  extension_gamestate;
}