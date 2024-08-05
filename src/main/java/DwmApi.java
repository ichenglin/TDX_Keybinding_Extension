import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;

public interface DwmApi extends Library {
    DwmApi INSTANCE = Native.load("dwmapi", DwmApi.class);

    int DWMWA_EXTENDED_FRAME_BOUNDS = 9;

    int DwmGetWindowAttribute(HWND hwnd, int dwAttribute, Object pvAttribute, int cbAttribute);
}