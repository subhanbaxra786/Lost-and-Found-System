import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StaffWindow extends JFrame {

    private MainWindow parent;

    public StaffWindow(MainWindow parent) {
        this.parent = parent;
        setTitle("Staff Dashboard");
        setMinimumSize(new Dimension(400, 300));
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        showDashboard();
    }

    private void showDashboard() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        add(MainWindow.makeHeader("Staff Dashboard — " + AppController.getLoggedInUser().getName()), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 120, 8, 120);

        JButton[] buttons = {
                MainWindow.makeButton("View Found Items",    new Color(30, 90, 160)),
                MainWindow.makeButton("Register Found Item", new Color(40, 140, 80)),
                MainWindow.makeButton("Process Handover",    new Color(160, 100, 20)),
                MainWindow.makeButton("Logout",              new Color(180, 50, 50)),
        };

        buttons[0].addActionListener(e -> showFoundItemsDialog());
        buttons[1].addActionListener(e -> showRegisterFoundForm());
        buttons[2].addActionListener(e -> showHandoverDialog());
        buttons[3].addActionListener(e -> {
            AuditLog.log(AppController.getLoggedInUser().getUserId(), "LOGOUT");
            AppController.setLoggedInUser(null);
            parent.setVisible(true);
            parent.showMainMenu();
            dispose();
        });

        for (int i = 0; i < buttons.length; i++) {
            gbc.gridy = i;
            center.add(buttons[i], gbc);
        }

        add(center, BorderLayout.CENTER);
        add(MainWindow.makeFooter(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void showFoundItemsDialog() {
        JDialog dialog = new JDialog(this, "Found Items", true);
        dialog.setSize(750, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("All Found Items"), BorderLayout.NORTH);

        StringBuilder sb = new StringBuilder();
        for (FoundItem item : AppController.getFoundItems()) {
            sb.append("[").append(item.getItemId()).append("] ")
                    .append(item.getTitle()).append(" | ")
                    .append(item.getCategory()).append(" | ")
                    .append(item.getStatus()).append(" | ")
                    .append(item.getDateReported()).append("\n");
        }
        if (sb.length() == 0) sb.append("No found items yet.");

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBorder(new EmptyBorder(10, 15, 10, 15));

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245, 245, 245));
        btnPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JButton closeBtn = MainWindow.makeButton("Close", new Color(120, 120, 120));
        closeBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(closeBtn);

        dialog.add(new JScrollPane(area), BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showRegisterFoundForm() {
        JDialog dialog = new JDialog(this, "Register Found Item", true);
        dialog.setSize(600, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("Register Found Item"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));
        form.setBorder(new EmptyBorder(10, 0, 10, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField titleField    = new JTextField();
        JTextField descField     = new JTextField();
        JTextField buildingField = new JTextField();
        JTextField floorField    = new JTextField();
        JTextField zoneField     = new JTextField();
        JTextField dateField     = new JTextField("DD/MM/YYYY");
        JComboBox<ItemCategory> categoryBox = new JComboBox<>(ItemCategory.values());
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryBox.setPreferredSize(new Dimension(200, 36));

        for (JTextField f : new JTextField[]{titleField, descField, buildingField, floorField, zoneField, dateField})
            MainWindow.styleField(f);

        String[] labels = {"Title:", "Description:", "Building:", "Floor:", "Zone:", "Date (DD/MM/YYYY):", "Category:"};
        JComponent[] fields = {titleField, descField, buildingField, floorField, zoneField, dateField, categoryBox};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i * 2;
            gbc.insets = new Insets(8, 60, 0, 60);
            form.add(MainWindow.makeLabel(labels[i]), gbc);
            gbc.gridy = i * 2 + 1;
            gbc.insets = new Insets(2, 60, 0, 60);
            form.add(fields[i], gbc);
        }

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(new Color(245, 245, 245));
        btnPanel.setBorder(new EmptyBorder(15, 60, 15, 60));

        JButton submitBtn = MainWindow.makeButton("Submit", new Color(40, 140, 80));
        JButton cancelBtn = MainWindow.makeButton("Cancel", new Color(120, 120, 120));

        submitBtn.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Title is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AppController.reportFoundItemGUI(
                    titleField.getText().trim(), descField.getText().trim(),
                    buildingField.getText().trim(), floorField.getText().trim(),
                    zoneField.getText().trim(), dateField.getText().trim(),
                    (ItemCategory) categoryBox.getSelectedItem()
            );
            JOptionPane.showMessageDialog(dialog, "Found item registered successfully.");
            dialog.dispose();
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(cancelBtn);
        btnPanel.add(submitBtn);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(245, 245, 245));
        dialog.add(scroll, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showHandoverDialog() {
        JDialog dialog = new JDialog(this, "Process Handover", true);
        dialog.setSize(600, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("Process Item Handover"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField itemIdField = new JTextField();
        MainWindow.styleField(itemIdField);

        StringBuilder sb = new StringBuilder("Found items available for handover:\n");
        for (FoundItem item : AppController.getFoundItems()) {
            if (item.getStatus() == ItemStatus.FOUND) {
                sb.append("[").append(item.getItemId()).append("] ")
                        .append(item.getTitle()).append(" | ")
                        .append(item.getCategory()).append("\n");
            }
        }

        JTextArea listArea = new JTextArea(sb.toString());
        listArea.setEditable(false);
        listArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        listArea.setBackground(new Color(235, 235, 235));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(15, 60, 5, 60);
        form.add(new JScrollPane(listArea), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(15, 60, 0, 60);
        form.add(MainWindow.makeLabel("Enter Found Item ID to hand over:"), gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(4, 60, 0, 60);
        form.add(itemIdField, gbc);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(new Color(245, 245, 245));
        btnPanel.setBorder(new EmptyBorder(15, 60, 15, 60));

        JButton confirmBtn = MainWindow.makeButton("Confirm Handover", new Color(160, 100, 20));
        JButton cancelBtn  = MainWindow.makeButton("Cancel", new Color(120, 120, 120));

        confirmBtn.addActionListener(e -> {
            try {
                int itemId = Integer.parseInt(itemIdField.getText().trim());
                boolean success = AppController.processHandoverGUI(itemId);
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Item " + itemId + " marked as resolved.");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Item ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid item ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(cancelBtn);
        btnPanel.add(confirmBtn);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
