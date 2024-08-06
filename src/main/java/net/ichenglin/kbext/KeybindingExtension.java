package net.ichenglin.kbext;

import com.sun.jna.platform.win32.User32;
import net.ichenglin.kbext.extension.*;
import net.ichenglin.kbext.object.GameState;
import net.ichenglin.kbext.object.RecognitionException;
import net.ichenglin.kbext.object.ScheduledTask;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class KeybindingExtension {

    private static final KeybindingHotkey      extension_hotkey      = new KeybindingHotkey();
    private static final KeybindingInterface   extension_interface   = new KeybindingInterface(extension_hotkey);
    private static final KeybindingRecognition extension_recognition = new KeybindingRecognition("D:\\Github\\TDX_Keybinding_Extension\\asset");
    private static final KeybindingRobot       extension_robot       = new KeybindingRobot();
    private static       GameState             extension_gamestate   = new GameState(0, 0, 0);

    public static void main(String[] args) {
        // hotkeys
        KeybindingExtension.extension_hotkey.hotkey_register(1, User32.MOD_NOREPEAT, KeyEvent.VK_F, () -> {
            if (!KeybindingWindow.window_focused().get_name().equals("Roblox")) return;
            KeybindingExtension.extension_robot.upgrade_max(KeyEvent.VK_E, KeyEvent.VK_R);
        });
        KeybindingExtension.extension_hotkey.hotkey_register(2, User32.MOD_NOREPEAT, KeyEvent.VK_T, () -> {
            if (!KeybindingWindow.window_focused().get_name().equals("Roblox")) return;
            KeybindingExtension.extension_robot.upgrade_max(KeyEvent.VK_R, KeyEvent.VK_E);
        });
        KeybindingExtension.extension_hotkey.hotkey_register(3, User32.MOD_NOREPEAT, KeyEvent.VK_Z, () -> {
            if (!KeybindingWindow.window_focused().get_name().equals("Roblox")) return;
            System.out.println("Nothing Now :(");
        });
        // recognition
        new ScheduledTask(500, KeybindingExtension::task_recognition);
    }

    private static void task_recognition() {
        try {
            KeybindingWindow window_focused    = KeybindingWindow.window_focused();
            BufferedImage    window_screenshot = KeybindingExtension.extension_robot.screenshot_capture(window_focused.get_bounds());
            KeybindingExtension.extension_recognition.set_image(window_screenshot);
            KeybindingExtension.extension_gamestate = KeybindingExtension.extension_recognition.recognize_state();
            System.out.println(extension_gamestate.get_timer() % 60);
        } catch (RecognitionException exception) {
            System.out.println("Recognition Error: " + exception.getMessage());
        }
    }
}