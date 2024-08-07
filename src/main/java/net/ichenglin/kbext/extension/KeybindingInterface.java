package net.ichenglin.kbext.extension;

import net.ichenglin.kbext.object.ProgramRegistry;
import net.ichenglin.kbext.object.ProgramRegistryData;
import net.ichenglin.kbext.object.ValueRange;
import net.ichenglin.kbext.ui.JKeybindingButton;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class KeybindingInterface {

    private final JFrame          extension_interface;
    private final ProgramRegistry extension_registry;
    private       AutoSkipPreset  extension_mode_autoskip;

    private static final List<AutoSkipPreset> autoskip_presets = Arrays.asList(
            new AutoSkipPreset(0, "Easy",         24),
            new AutoSkipPreset(1, "Intermediate", 32),
            new AutoSkipPreset(2, "Elite",        28),
            new AutoSkipPreset(3, "Expert",       40),
            new AutoSkipPreset(4, "Endless",      200)
    );

    public KeybindingInterface(KeybindingHotkey extension_hotkey, ProgramRegistry extension_registry) {
        this.extension_interface     = new JFrame();
        this.extension_registry      = extension_registry;
        this.extension_mode_autoskip = KeybindingInterface.autoskip_presets.get(4);
        this.extension_interface.setLayout(new GridBagLayout());
        EventQueue.invokeLater(() -> {
            this.window_initialize();
            this.general_initialize(extension_hotkey);
            this.advanced_initialize(extension_hotkey);
            this.autoskip_initialize();
            this.monitor_initialize();
        });

    }

    private void window_initialize() {
        // window
        this.extension_interface.setSize                 (660, 300);
        this.extension_interface.setTitle                ("TDX Keybinding Extension");
        this.extension_interface.setResizable            (false);
        this.extension_interface.setAlwaysOnTop          (true);
        this.extension_interface.setVisible              (true);
        this.extension_interface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void general_initialize(KeybindingHotkey keybinding_hotkey) {
        // layout
        JPanel general_panel = new JPanel();
        general_panel.setLayout(new GridBagLayout());
        general_panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("General Settings"),
                BorderFactory.createEmptyBorder (5, 5, 5, 5)
        ));
        this.extension_interface.add(general_panel, KeybindingInterface.gridbag_constraints(0, 0, 1, GridBagConstraints.BOTH, 0));
        // instant upgrade
        JCheckBox instantupgrade_checkbox = new JCheckBox("Instant Upgrade (Keybindings)");
        JLabel    instantupgrade_status   = new JLabel();
        instantupgrade_checkbox.setSelected(true);
        instantupgrade_checkbox.addItemListener((item_event) -> {
            boolean instantupgrade_enabled = (item_event.getStateChange() == ItemEvent.SELECTED);
            keybinding_hotkey  .hotkey_toggle(instantupgrade_enabled);
            KeybindingInterface.status_update(instantupgrade_status, instantupgrade_enabled);
        });
        KeybindingInterface.status_update(instantupgrade_status, true);
        general_panel.add(instantupgrade_checkbox, KeybindingInterface.gridbag_constraints(0, 0, 1, GridBagConstraints.HORIZONTAL, 0));
        general_panel.add(instantupgrade_status,   KeybindingInterface.gridbag_constraints(1, 0, 1, GridBagConstraints.HORIZONTAL, 0));
        // vote skip
        JCheckBox voteskip_checkbox = new JCheckBox("Skip Wave (Keybindings)");
        JLabel    voteskip_status   = new JLabel();
        voteskip_checkbox.setSelected(true);
        voteskip_checkbox.addItemListener((item_event) -> {
            boolean voteskip_enabled = (item_event.getStateChange() == ItemEvent.SELECTED);
            KeybindingInterface.status_update(voteskip_status, voteskip_enabled);
        });
        KeybindingInterface.status_update(voteskip_status, true);
        general_panel.add(voteskip_checkbox, KeybindingInterface.gridbag_constraints(0, 1, 1, GridBagConstraints.HORIZONTAL, 0));
        general_panel.add(voteskip_status,   KeybindingInterface.gridbag_constraints(1, 1, 1, GridBagConstraints.HORIZONTAL, 0));
        // auto skip
        JCheckBox autoskip_checkbox = new JCheckBox("Auto Skip Waves");
        JLabel    autoskip_status   = new JLabel();
        autoskip_checkbox.setSelected(true);
        autoskip_checkbox.addItemListener((item_event) -> {
            boolean autoskip_enabled = (item_event.getStateChange() == ItemEvent.SELECTED);
            KeybindingInterface.status_update(autoskip_status, autoskip_enabled);
        });
        KeybindingInterface.status_update(autoskip_status, true);
        general_panel.add(autoskip_checkbox, KeybindingInterface.gridbag_constraints(0, 2, 1, GridBagConstraints.HORIZONTAL, 0));
        general_panel.add(autoskip_status,   KeybindingInterface.gridbag_constraints(1, 2, 1, GridBagConstraints.HORIZONTAL, 0));
    }

    private void advanced_initialize(KeybindingHotkey keybinding_hotkey) {
        // layout
        JPanel advanced_panel = new JPanel();
        advanced_panel.setLayout(new GridBagLayout());
        advanced_panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Advanced Settings"),
                BorderFactory.createEmptyBorder (5, 5, 5, 5)
        ));
        this.extension_interface.add(advanced_panel, KeybindingInterface.gridbag_constraints(0, 1, 1, GridBagConstraints.BOTH, 0));
        // instant upgrade
        JKeybindingButton instantupgrade_top_button    = new JKeybindingButton(KeyEvent.VK_F, new Integer[]{KeyEvent.VK_E, KeyEvent.VK_R});
        JKeybindingButton instantupgrade_bottom_button = new JKeybindingButton(KeyEvent.VK_T, new Integer[]{KeyEvent.VK_E, KeyEvent.VK_R});
        JKeybindingButton voteskip_button              = new JKeybindingButton(KeyEvent.VK_Z, new Integer[]{KeyEvent.VK_E, KeyEvent.VK_R});
        JLabel            instantupgrade_top_label     = new JLabel("Upgrade Keybinding (Top Path)");
        JLabel            instantupgrade_bottom_label  = new JLabel("Upgrade Keybinding (Bottom Path)");
        JLabel            voteskip_label               = new JLabel("Skip Wave Keybinding");
        instantupgrade_top_button   .setPreferredSize(new Dimension(15, 15));
        instantupgrade_bottom_button.setPreferredSize(new Dimension(15, 15));
        voteskip_button             .setPreferredSize(new Dimension(15, 15));
        instantupgrade_top_button   .setFont(instantupgrade_top_button   .getFont().deriveFont(10f));
        instantupgrade_bottom_button.setFont(instantupgrade_bottom_button.getFont().deriveFont(10f));
        voteskip_button             .setFont(instantupgrade_bottom_button.getFont().deriveFont(10f));
        instantupgrade_top_button   .setMargin(new Insets(0, 0, 0, 0));
        instantupgrade_bottom_button.setMargin(new Insets(0, 0, 0, 0));
        voteskip_button             .setMargin(new Insets(0, 0, 0, 0));
        instantupgrade_top_button   .addKeybindingListener((keybinding_event) -> keybinding_hotkey.hotkey_update(1, null, keybinding_event.get_new(), null));
        instantupgrade_bottom_button.addKeybindingListener((keybinding_event) -> keybinding_hotkey.hotkey_update(2, null, keybinding_event.get_new(), null));
        voteskip_button             .addKeybindingListener((keybinding_event) -> keybinding_hotkey.hotkey_update(3, null, keybinding_event.get_new(), null));
        advanced_panel.add(instantupgrade_top_button,    KeybindingInterface.gridbag_constraints(0, 0, 1, GridBagConstraints.HORIZONTAL, 0));
        advanced_panel.add(instantupgrade_top_label,     KeybindingInterface.gridbag_constraints(1, 0, 1, GridBagConstraints.HORIZONTAL, 0));
        advanced_panel.add(instantupgrade_bottom_button, KeybindingInterface.gridbag_constraints(0, 1, 1, GridBagConstraints.HORIZONTAL, 0));
        advanced_panel.add(instantupgrade_bottom_label,  KeybindingInterface.gridbag_constraints(1, 1, 1, GridBagConstraints.HORIZONTAL, 0));
        advanced_panel.add(voteskip_button,              KeybindingInterface.gridbag_constraints(0, 2, 1, GridBagConstraints.HORIZONTAL, 0));
        advanced_panel.add(voteskip_label,               KeybindingInterface.gridbag_constraints(1, 2, 1, GridBagConstraints.HORIZONTAL, 0));
    }

    private void autoskip_initialize() {
        // layout
        JPanel autoskip_panel = new JPanel();
        autoskip_panel.setLayout(new GridBagLayout());
        autoskip_panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Auto Skip Settings"),
                BorderFactory.createEmptyBorder (5, 5, 5, 5)
        ));
        this.extension_interface.add(autoskip_panel, KeybindingInterface.gridbag_constraints(1, 0, 2, GridBagConstraints.BOTH, 0));
        // skip waves
        String[]          autoskip_modes    = KeybindingInterface.autoskip_presets.stream().map((preset) -> preset.get_name() + " (" + preset.get_waves() + ")").toArray(String[]::new);
        JComboBox<String> autoskip_combo    = new JComboBox<String>(autoskip_modes);
        JList<Integer>    autoskip_list     = new JList   <Integer>(this.extension_mode_autoskip.get_waves_list());
        JScrollPane       autoskip_scroller = new JScrollPane      (autoskip_list);
        autoskip_combo   .setSelectedIndex    (this.extension_mode_autoskip.get_id());
        autoskip_list    .setSelectionMode    (ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        autoskip_list    .setLayoutOrientation(JList.VERTICAL);
        autoskip_list    .setVisibleRowCount  (-1);
        autoskip_scroller.setPreferredSize    (new Dimension(100, 150));
        autoskip_combo   .addActionListener   ((action_event) -> {
            int            selected_index  = autoskip_combo.getSelectedIndex();
            this.extension_mode_autoskip   = KeybindingInterface.autoskip_presets.get(selected_index);
            autoskip_list.setListData(this.extension_mode_autoskip.get_waves_list());
            this.extension_registry.set_data("int_wave", this.extension_mode_autoskip);
        });
        autoskip_panel.add(autoskip_combo,    KeybindingInterface.gridbag_constraints(0, 0, 1, GridBagConstraints.HORIZONTAL, 0));
        autoskip_panel.add(autoskip_scroller, KeybindingInterface.gridbag_constraints(0, 1, 1, GridBagConstraints.HORIZONTAL, 0));
    }

    private void monitor_initialize() {
        // layout
        JPanel monitor_panel = new JPanel();
        monitor_panel.setLayout(new GridBagLayout());
        monitor_panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Program Registry"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        this.extension_interface.add(monitor_panel, KeybindingInterface.gridbag_constraints(2, 0, 2, GridBagConstraints.BOTH, 0));
        JTable       monitor_table         = new JTable(new RegistryTableModel(this.extension_registry));
        JScrollPane  monitor_scroller      = new JScrollPane(monitor_table);
        monitor_table.setFillsViewportHeight(true);
        monitor_scroller.setPreferredSize   (new Dimension(150, 180));
        monitor_panel.add(monitor_scroller, KeybindingInterface.gridbag_constraints(0, 0, 1, GridBagConstraints.HORIZONTAL, 0));
    }

    private static GridBagConstraints gridbag_constraints(int grid_x, int grid_y, int grid_height, int grid_fill, int pad_right) {
        GridBagConstraints layout_constraints = new GridBagConstraints();
        layout_constraints.gridx      = grid_x;
        layout_constraints.gridy      = grid_y;
        layout_constraints.gridheight = grid_height;
        layout_constraints.fill       = grid_fill;
        layout_constraints.insets     = new Insets(2, 3, 2, 3 + pad_right);
        return layout_constraints;
    }

    private static void status_update(JLabel status_label, boolean status_enabled) {
        if (status_enabled) {
            status_label.setText("Enabled");
            status_label.setForeground(new Color(0x15803d));
        } else {
            status_label.setText("Disabled");
            status_label.setForeground(new Color(0xb91c1c));
        }
        status_label.setPreferredSize(new Dimension(50, 15));
    }
}

class RegistryTableModel extends AbstractTableModel {

    private final ProgramRegistry extension_registry;
    private       int             extension_registry_cache = -1;

    public RegistryTableModel(ProgramRegistry extension_registry) {
        this.extension_registry = extension_registry;
        this.extension_registry.add_listener((registry_event) -> {
            ProgramRegistryData[] registry_data = this.extension_registry.get_all();
            boolean registry_new = (registry_data.length != extension_registry_cache);
            // update whole table
            if (registry_new) {
                super.fireTableDataChanged();
                return;
            }
            // update changed rows
            for (int data_index = 0; data_index < registry_data.length; data_index++) {
                if (!registry_data[data_index].get_key().equals(registry_event.get_key())) continue;
                super.fireTableRowsUpdated(data_index, data_index);
                break;
            }
        });
    }

    @Override
    public int getRowCount() {
        return extension_registry.get_all().length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int index_column) {
        if      (index_column == 0) return "Property";
        else if (index_column == 1) return "Value";
        return null;
    }

    @Override
    public Object getValueAt(int index_row, int index_column) {
        ProgramRegistryData registry_data = this.extension_registry.get_all()[index_row];
        if      (index_column == 0) return RegistryTableModel.value_translate(registry_data.get_key());
        else if (index_column == 1) return RegistryTableModel.value_translate(registry_data.get_value());
        return null;
    }

    private static Object value_translate(Object value_original) {
        try {
            Class<?> stringify_class = value_original.getClass().getMethod("toString").getDeclaringClass();
            if (stringify_class != Object.class) return value_original;
            return "0x" + Integer.toHexString(value_original.hashCode());
        } catch (NoSuchMethodException ignored) {}
        return null;
    }
}

class AutoSkipPreset {
    private final int    gamemode_id;
    private final String gamemode_name;
    private final int    gamemode_waves;

    public AutoSkipPreset(int gamemode_id, String gamemode_name, int gamemode_waves) {
        this.gamemode_id    = gamemode_id;
        this.gamemode_name  = gamemode_name;
        this.gamemode_waves = gamemode_waves;
    }

    public int get_id() {
        return this.gamemode_id;
    }

    public String get_name() {
        return this.gamemode_name;
    }

    public int get_waves() {
        return this.gamemode_waves;
    }

    public Integer[] get_waves_list() {
        return new ValueRange(1, this.gamemode_waves).get_array();
    }
}