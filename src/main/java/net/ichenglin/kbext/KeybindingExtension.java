package net.ichenglin.kbext;

import com.sun.jna.platform.win32.User32;
import net.ichenglin.kbext.extension.*;
import net.ichenglin.kbext.object.GameState;
import net.ichenglin.kbext.object.ProgramRegistry;
import net.ichenglin.kbext.object.RecognitionException;
import net.ichenglin.kbext.object.ScheduledTask;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class KeybindingExtension {
    public static void main(String[] args) {
        KeybindingReference Extension   = new KeybindingReference();
        Extension.extension_registry    = new ProgramRegistry();
        Extension.extension_hotkey      = new KeybindingHotkey();
        Extension.extension_interface   = new KeybindingInterface(Extension.extension_hotkey, Extension.extension_registry);
        Extension.extension_robot       = new KeybindingRobot(Extension.extension_registry);
        Extension.extension_recognition = new KeybindingRecognition();
        Extension.extension_gamestate   = new GameState(0, 0, 0);
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
        Extension.extension_hotkey.hotkey_toggle(true);
        Extension.extension_registry.set_data("ext_rdy", true);
        new ScheduledTask(500, 200, () -> KeybindingExtension.task_recognition(Extension));
    }

    private static void task_recognition(KeybindingReference Extension) {
        try {
            KeybindingWindow window_roblox = KeybindingWindow.window_named_fallback("Roblox");
            if (window_roblox == null) throw new RecognitionException("err_wnf");
            BufferedImage window_screenshot = Extension.extension_robot.screenshot_capture(window_roblox.get_bounds());
            Extension.extension_registry   .set_data ("ext_wnd", window_roblox);
            Extension.extension_recognition.set_image(window_screenshot);
            Extension.extension_gamestate = Extension.extension_recognition.recognize_state();
            // registry update
            Extension.extension_registry.set_data("rnd_hp",   Extension.extension_gamestate.get_health());
            Extension.extension_registry.set_data("rnd_wave", Extension.extension_gamestate.get_wave());
            Extension.extension_registry.set_data("rnd_cdn",  Extension.extension_gamestate.get_timer() % 60);
            Extension.extension_registry.set_data("ext_ocr",  "success");
        } catch (RecognitionException exception) {
            Extension.extension_registry.set_data("ext_ocr", exception.getMessage());
        }
    }
}

class KeybindingReference {
    public KeybindingHotkey      extension_hotkey;
    public KeybindingRobot       extension_robot;
    public KeybindingInterface   extension_interface;
    public KeybindingRecognition extension_recognition;
    public ProgramRegistry       extension_registry;
    public GameState             extension_gamestate;
}