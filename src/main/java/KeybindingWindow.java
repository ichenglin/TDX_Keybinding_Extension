import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

import javax.swing.*;

public class KeybindingWindow {

    private final JFrame extension_window;

    public KeybindingWindow() {
        this.extension_window = new JFrame();
        this.window_initialize();
    }

    public boolean roblox_focused() {
        HWND   focused_window      = User32.INSTANCE.GetForegroundWindow();
        int    focused_name_length = User32.INSTANCE.GetWindowTextLength(focused_window) + 1;
        char[] focused_name_buffer = new char[focused_name_length];
        User32.INSTANCE.GetWindowText(focused_window, focused_name_buffer, focused_name_length);
        return Native.toString(focused_name_buffer).equals("Roblox");
    }

    private void window_initialize() {
        this.extension_window.setSize                 (400, 200);
        this.extension_window.setTitle                ("TDX Keybinding Extension");
        this.extension_window.setResizable            (false);
        this.extension_window.setAlwaysOnTop          (true);
        this.extension_window.setVisible              (true);
        this.extension_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
