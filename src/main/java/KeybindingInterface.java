import javax.swing.*;

public class KeybindingInterface {

    private final JFrame extension_interface;

    public KeybindingInterface() {
        this.extension_interface = new JFrame();
        this.window_initialize();
    }

    private void window_initialize() {
        this.extension_interface.setSize                 (400, 200);
        this.extension_interface.setTitle                ("TDX Keybinding Extension");
        this.extension_interface.setResizable            (false);
        this.extension_interface.setAlwaysOnTop          (true);
        this.extension_interface.setVisible              (true);
        this.extension_interface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
