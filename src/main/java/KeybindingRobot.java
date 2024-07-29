import java.awt.*;

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
}