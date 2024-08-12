package net.ichenglin.kbext.extension;

import net.ichenglin.kbext.object.ProgramRegistry;
import net.ichenglin.kbext.util.Geometry;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
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

    public void click_once(Point mouse_location) {
        this.extension_robot.mouseRelease(KeyEvent.BUTTON2_MASK);
        this.extension_robot.mouseRelease(KeyEvent.BUTTON1_MASK);
        // TODO: Robot.mouseMove doesn't work with multiple screen display
        this.extension_robot.mouseMove   (mouse_location.x, mouse_location.y + 1);
        this.extension_robot.delay       (10);
        this.extension_robot.mouseMove   (mouse_location.x, mouse_location.y);
        this.extension_robot.mousePress  (KeyEvent.BUTTON1_MASK);
        this.extension_robot.mouseRelease(KeyEvent.BUTTON1_MASK);
    }

    public BufferedImage screenshot_capture(Rectangle window_rectangle) {
        Point2D window_center      = Geometry.rectangle_center(window_rectangle);
        double  window_scalefactor = KeybindingRobot.window_scalefactor(window_center);
        window_rectangle           = Geometry.rectangle_scale_origin(window_rectangle, (1 / window_scalefactor));
        this.extension_registry.set_data("ocr_sf", window_scalefactor);
        return this.extension_robot.createScreenCapture(window_rectangle);
    }

    private static double window_scalefactor(Point2D window_center) {
        GraphicsDevice[] screen_devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        GraphicsDevice   window_device  = null;
        for (GraphicsDevice loop_device : screen_devices) {
            Rectangle       loop_device_location  = loop_device.getDefaultConfiguration().getBounds();
            AffineTransform loop_device_transform = loop_device.getDefaultConfiguration().getDefaultTransform();
            Rectangle       loop_device_bounds    = Geometry.rectangle_scale_size(loop_device_location, loop_device_transform.getScaleX(), loop_device_transform.getScaleY());
            if (!Geometry.point_within_rectangle(loop_device_bounds, window_center)) continue;
            window_device = loop_device;
            break;
        }
        if (window_device == null) return 1f;
        return window_device.getDefaultConfiguration().getDefaultTransform().getScaleX();
    }
}