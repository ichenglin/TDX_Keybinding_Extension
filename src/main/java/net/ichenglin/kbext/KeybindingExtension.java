package net.ichenglin.kbext;

import com.sun.jna.platform.win32.User32;
import net.ichenglin.kbext.extension.*;
import net.ichenglin.kbext.object.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.time.Instant;

public class KeybindingExtension {
    public static void main(String[] args) {
        KeybindingReference Extension        = new KeybindingReference();
        Extension.extension_registry         = new ProgramRegistry();
        Extension.extension_hotkey           = new KeybindingHotkey();
        Extension.extension_interface        = new KeybindingInterface(Extension.extension_hotkey, Extension.extension_registry);
        Extension.extension_robot            = new KeybindingRobot(Extension.extension_registry);
        Extension.extension_recognition      = new KeybindingRecognition();
        Extension.extension_gamestate        = new GameState(0, 0, 0);
        Extension.extension_gamestate_filter = new GameStateFilter(15);
        Extension.extension_hotkey.hotkey_register(1, User32.MOD_NOREPEAT, KeyEvent.VK_F, true, () -> {
            KeybindingWindow window_focused = KeybindingWindow.window_focused();
            if (window_focused == null)                      return;
            if (!window_focused.get_name().equals("Roblox")) return;
            // upgrade top path
            Extension.extension_robot.upgrade_max(KeyEvent.VK_E, KeyEvent.VK_R);
        });
        Extension.extension_hotkey.hotkey_register(2, User32.MOD_NOREPEAT, KeyEvent.VK_T, true, () -> {
            KeybindingWindow window_focused = KeybindingWindow.window_focused();
            if (window_focused == null)                      return;
            if (!window_focused.get_name().equals("Roblox")) return;
            // upgrade bottom path
            Extension.extension_robot.upgrade_max(KeyEvent.VK_R, KeyEvent.VK_E);
        });
        Extension.extension_hotkey.hotkey_register(3, User32.MOD_NOREPEAT, KeyEvent.VK_Z, true, () -> {
            KeybindingWindow window_focused = KeybindingWindow.window_focused();
            if (window_focused == null)                      return;
            if (!window_focused.get_name().equals("Roblox")) return;
            // skip wave
            try {
                Point skipwave_location = Extension.extension_recognition.locate_skipwave(window_focused.get_bounds().getLocation())[1];
                Extension.extension_robot.click_once(skipwave_location);
                Extension.extension_registry.set_data("ext_vsb", "success");
            } catch (RecognitionException exception) {
                Extension.extension_registry.set_data("ext_vsb", exception.getMessage());
            }
        });
        Extension.extension_hotkey.hotkey_toggle(true);
        Extension.extension_registry.set_data("ext_rdy", true);
        new ScheduledTask(1000, 300, () -> KeybindingExtension.task_recognition(Extension));
    }

    private static void task_recognition(KeybindingReference Extension) {
        KeybindingWindow window_roblox = KeybindingWindow.window_named_fallback("Roblox");
        if (window_roblox == null) return;
        BufferedImage window_screenshot = Extension.extension_robot.screenshot_capture(window_roblox.get_bounds());
        Extension.extension_registry   .set_data ("ext_wnd", window_roblox);
        Extension.extension_recognition.set_image(window_screenshot);
        // scoreboard
        try {
            Extension.extension_gamestate = Extension.extension_recognition.recognize_state();
            Extension.extension_gamestate_filter.state_add(Extension.extension_gamestate);
            GameState gamestate_filtered = Extension.extension_gamestate_filter.state_forecast(Instant.now());
            Extension.extension_registry.set_data("rnd_hp",   gamestate_filtered.get_health());
            Extension.extension_registry.set_data("rnd_wave", gamestate_filtered.get_wave());
            Extension.extension_registry.set_data("rnd_cdn",  (gamestate_filtered.get_timer() / 60) + "m" + (gamestate_filtered.get_timer() % 60) + "s");
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
    public GameStateFilter       extension_gamestate_filter;
}