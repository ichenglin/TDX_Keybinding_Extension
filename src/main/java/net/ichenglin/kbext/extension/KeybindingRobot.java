package net.ichenglin.kbext.extension;

import net.ichenglin.kbext.object.ProgramRegistry;

import java.awt.*;
import java.awt.image.BufferedImage;

public class KeybindingRobot {

    private final Robot           extension_robot;
    private final ProgramRegistry extension_registry;

    public KeybindingRobot(ProgramRegistry extension_registry) {
        try {
            this.extension_robot    = new Robot();
            this.extension_registry = extension_registry;
        } catch (AWTException exception) {
            throw new RuntimeException("Failed to initialize robot");
        }
    };

    public void upgrade_max(int primary_keycode, int secondary_keycode) {
        for (int index = 0; index < 5; index++) this.upgrade_once(primary_keycode);
        for (int index = 0; index < 2; index++) this.upgrade_once(secondary_keycode);
    }

    public void upgrade_once(int upgrade_keycode) {
        this.extension_robot.keyPress  (upgrade_keycode);
        this.extension_robot.keyRelease(upgrade_keycode);
    }

    public BufferedImage screenshot_capture() {
        Rectangle        screen_rectangle = new Rectangle(0, 0, 0, 0);
        GraphicsDevice[] screen_devices   = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (GraphicsDevice loop_device : screen_devices) {
            screen_rectangle = screen_rectangle.union(loop_device.getDefaultConfiguration().getBounds());
        }
        return this.screenshot_capture(screen_rectangle);
    }

    public BufferedImage screenshot_capture(Rectangle screen_rectangle) {
        int version_jre = (int) this.extension_registry.get_data_default("jre_ver", 8);
        if (version_jre >= 9) {
            float screen_scalefactor = (Toolkit.getDefaultToolkit().getScreenResolution() / 96f);
            screen_rectangle = new Rectangle(
                (int) (screen_rectangle.getX()      / screen_scalefactor),
                (int) (screen_rectangle.getY()      / screen_scalefactor),
                (int) (screen_rectangle.getWidth()  / screen_scalefactor),
                (int) (screen_rectangle.getHeight() / screen_scalefactor)
            );
        }
        return this.extension_robot.createScreenCapture(screen_rectangle);
    }
}