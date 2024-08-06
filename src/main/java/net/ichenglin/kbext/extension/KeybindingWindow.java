package net.ichenglin.kbext.extension;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import net.ichenglin.kbext.util.DwmApi;

import java.awt.*;

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

    public static KeybindingWindow window_focused() {
        HWND focused_window = User32.INSTANCE.GetForegroundWindow();
        return new KeybindingWindow(focused_window);
    }
}
