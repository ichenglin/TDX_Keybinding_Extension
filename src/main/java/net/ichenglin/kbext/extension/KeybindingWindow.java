package net.ichenglin.kbext.extension;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import net.ichenglin.kbext.util.DwmApi;

import java.awt.*;
import java.util.concurrent.Callable;

public class KeybindingWindow {

    private final HWND window_handle;

    public KeybindingWindow(HWND window_handle) {
        this.window_handle = window_handle;
    }

    public String get_name() {
        int    window_name_length = User32.INSTANCE.GetWindowTextLength(this.window_handle) + 1;
        char[] window_name_buffer = new char[window_name_length];
        User32.INSTANCE.GetWindowText(this.window_handle, window_name_buffer, window_name_length);
        return Native.toString(window_name_buffer);
    }

    public Rectangle get_bounds() {
        RECT window_rectangle = new RECT();
        // ignores window shadow unlike User32 GetWindowRect
        DwmApi.INSTANCE.DwmGetWindowAttribute(this.window_handle, DwmApi.DWMWA_EXTENDED_FRAME_BOUNDS, window_rectangle, window_rectangle.size());
        int window_rectangle_x      = window_rectangle.left;
        int window_rectangle_y      = window_rectangle.top;
        int window_rectangle_width  = (window_rectangle.right  - window_rectangle.left);
        int window_rectangle_height = (window_rectangle.bottom - window_rectangle.top);
        if (window_rectangle_width  <= 0) window_rectangle_width  = 1;
        if (window_rectangle_height <= 0) window_rectangle_height = 1;
        return new Rectangle(window_rectangle_x, window_rectangle_y, window_rectangle_width, window_rectangle_height);
    }

    public static KeybindingWindow window_named_fallback(String window_name) {
        Callable<KeybindingWindow>[] window_fallbacks = new Callable[] {
            () -> KeybindingWindow.window_named(window_name),
            () -> KeybindingWindow.window_focused()
        };
        for (Callable<KeybindingWindow> window_fallback : window_fallbacks) {
            try {
                KeybindingWindow window_found = window_fallback.call();
                if (window_found != null) return window_found;
            } catch (Exception ignored) {}
        }
        return null;
    }

    public static KeybindingWindow window_named(String window_name) {
        HWND window_handle = User32.INSTANCE.FindWindow(null, window_name);
        if (window_handle == null) return null;
        return new KeybindingWindow(window_handle);
    }

    public static KeybindingWindow window_focused() {
        HWND window_handle = User32.INSTANCE.GetForegroundWindow();
        if (window_handle == null) return null;
        return new KeybindingWindow(window_handle);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KeybindingWindow)) return false;
        KeybindingWindow object_window = (KeybindingWindow) object;
        return (object_window.window_handle.equals(this.window_handle));
    }

    @Override
    public String toString() {
        return this.get_name();
    }
}
