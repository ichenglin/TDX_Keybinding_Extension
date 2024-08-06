package net.ichenglin.kbext.extension;

import net.ichenglin.kbext.ui.JKeybindingButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class KeybindingInterface {

    private final JFrame extension_interface;

    public KeybindingInterface(KeybindingHotkey keybinding_hotkey) {
        this.extension_interface = new JFrame();
        this.window_initialize();
        this.general_initialize();
        this.advanced_initialize(keybinding_hotkey);
    }

    private void window_initialize() {
        // window
        this.extension_interface.setSize                 (400, 300);
        this.extension_interface.setTitle                ("TDX Keybinding Extension");
        this.extension_interface.setResizable            (false);
        this.extension_interface.setAlwaysOnTop          (true);
        this.extension_interface.setVisible              (true);
        this.extension_interface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // layout
        this.extension_interface.setLayout(new GridBagLayout());
    }

    private void general_initialize() {
        // layout
        JPanel general_panel = new JPanel();
        general_panel.setLayout(new GridBagLayout());
        general_panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("General Settings"),
                BorderFactory.createEmptyBorder (5, 5, 5, 5)
        ));
        this.extension_interface.add(general_panel, this.gridbag_constraints(0, 0, 0));
        // instant upgrade
        JCheckBox instantupgrade_checkbox = new JCheckBox("Instant Upgrade (Keybindings)");
        JLabel    instantupgrade_label    = new JLabel   ("Enabled");
        instantupgrade_checkbox.setSelected(true);
        instantupgrade_label.setForeground(new Color(0x15803d));
        general_panel.add(instantupgrade_checkbox, this.gridbag_constraints(0, 0, 0));
        general_panel.add(instantupgrade_label,    this.gridbag_constraints(1, 0, 0));
        // auto skip
        JCheckBox autoskip_checkbox = new JCheckBox("Auto Skip");
        JLabel    autoskip_label    = new JLabel   ("Enabled");
        autoskip_checkbox.setSelected(true);
        autoskip_label.setForeground(new Color(0x15803d));
        general_panel.add(autoskip_checkbox, this.gridbag_constraints(0, 1, 0));
        general_panel.add(autoskip_label,    this.gridbag_constraints(1, 1, 0));
    }

    private void advanced_initialize(KeybindingHotkey keybinding_hotkey) {
        // layout
        JPanel advanced_panel = new JPanel();
        advanced_panel.setLayout(new GridBagLayout());
        advanced_panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Advanced Settings"),
                BorderFactory.createEmptyBorder (5, 5, 5, 5)
        ));
        this.extension_interface.add(advanced_panel, this.gridbag_constraints(0, 1, 0));
        // instant upgrade
        JKeybindingButton instantupgrade_top_button    = new JKeybindingButton(KeyEvent.VK_F);
        JKeybindingButton instantupgrade_bottom_button = new JKeybindingButton(KeyEvent.VK_T);
        JLabel            instantupgrade_top_label     = new JLabel("Upgrade Keybinding (Top Path)");
        JLabel            instantupgrade_bottom_label  = new JLabel("Upgrade Keybinding (Bottom Path)");
        instantupgrade_top_button   .setPreferredSize(new Dimension(15, 15));
        instantupgrade_bottom_button.setPreferredSize(new Dimension(15, 15));
        instantupgrade_top_button   .setFont(instantupgrade_top_button   .getFont().deriveFont(10f));
        instantupgrade_bottom_button.setFont(instantupgrade_bottom_button.getFont().deriveFont(10f));
        instantupgrade_top_button   .setMargin(new Insets(0, 0, 0, 0));
        instantupgrade_bottom_button.setMargin(new Insets(0, 0, 0, 0));
        instantupgrade_top_button   .addKeybindingListener((keybinding_event) -> keybinding_hotkey.hotkey_update(1, null, keybinding_event.get_new(), null));
        instantupgrade_bottom_button.addKeybindingListener((keybinding_event) -> keybinding_hotkey.hotkey_update(2, null, keybinding_event.get_new(), null));
        advanced_panel.add(instantupgrade_top_button,    this.gridbag_constraints(0, 0, 0));
        advanced_panel.add(instantupgrade_top_label,     this.gridbag_constraints(1, 0, 0));
        advanced_panel.add(instantupgrade_bottom_button, this.gridbag_constraints(0, 1, 0));
        advanced_panel.add(instantupgrade_bottom_label,  this.gridbag_constraints(1, 1, 0));
    }

    private GridBagConstraints gridbag_constraints(int grid_x, int grid_y, int pad_right) {
        GridBagConstraints layout_constraints = new GridBagConstraints();
        layout_constraints.gridx  = grid_x;
        layout_constraints.gridy  = grid_y;
        layout_constraints.insets = new Insets(3, 3, 3, 3 + pad_right);
        layout_constraints.fill   = GridBagConstraints.HORIZONTAL;
        return layout_constraints;
    }

}
