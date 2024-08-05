package net.ichenglin.kbext.extension;

import java.awt.*;
import java.awt.image.BufferedImage;

public class KeybindingRobot {
    private final Robot macro_robot = new Robot();
    public KeybindingRobot() throws AWTException {};

    public void upgrade_max(int primary_keycode, int secondary_keycode) {
        for (int index = 0; index < 5; index++) this.upgrade_once(primary_keycode);
        for (int index = 0; index < 2; index++) this.upgrade_once(secondary_keycode);
    }

    public void upgrade_once(int upgrade_keycode) {
        this.macro_robot.keyPress  (upgrade_keycode);
        this.macro_robot.keyRelease(upgrade_keycode);
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
        return this.macro_robot.createScreenCapture(screen_rectangle);
    }

    /*private static BufferedImage image_rescale(BufferedImage image_source, int result_width, int result_height) {
        double scale_width  = ((double) result_width  / image_source.getWidth());
        double scale_height = ((double) result_height / image_source.getHeight());
        BufferedImage   image_destination = new BufferedImage(result_width, result_height, image_source.getType());
        AffineTransform scale_transform   = new AffineTransform();
        scale_transform.scale(scale_width, scale_height);
        return new AffineTransformOp(scale_transform, AffineTransformOp.TYPE_BICUBIC).filter(image_source, image_destination);
    }*/
}