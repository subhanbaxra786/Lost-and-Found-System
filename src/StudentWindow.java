import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentWindow extends JFrame {

    private MainWindow parent;

    public StudentWindow(MainWindow parent) {
        this.parent = parent;
        setTitle("Student Dashboard");
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

        add(MainWindow.makeHeader("Student Dashboard — " + AppController.getLoggedInUser().getName()), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 120, 8, 120);

        JButton[] buttons = {
                MainWindow.makeButton("Report Lost Item",   new Color(30, 90, 160)),
                MainWindow.makeButton("Report Found Item",  new Color(40, 140, 80)),
                MainWindow.makeButton("Search Items",       new Color(100, 60, 160)),
                MainWindow.makeButton("View My Items",      new Color(160, 100, 20)),
                MainWindow.makeButton("Submit Claim",       new Color(20, 130, 150)),
                MainWindow.makeButton("View Notifications", new Color(80, 80, 80)),
                MainWindow.makeButton("Logout",             new Color(180, 50, 50)),
        };

        buttons[0].addActionListener(e -> showReportLostForm());
        buttons[1].addActionListener(e -> showReportFoundForm());
        buttons[2].addActionListener(e -> showSearchDialog());
        buttons[3].addActionListener(e -> showMyItemsDialog());
        buttons[4].addActionListener(e -> showClaimForm());
        buttons[5].addActionListener(e -> showNotificationsDialog());
        buttons[6].addActionListener(e -> {
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

    private void showReportLostForm() {
        JDialog dialog = new JDialog(this, "Report Lost Item", true);
        dialog.setSize(600, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("Report Lost Item"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));
        form.setBorder(new EmptyBorder(10, 0, 10, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(4, 60, 2, 60);

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

        JButton submitBtn = MainWindow.makeButton("Submit", new Color(30, 90, 160));
        JButton cancelBtn = MainWindow.makeButton("Cancel", new Color(120, 120, 120));

        submitBtn.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Title is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AppController.reportLostItemGUI(
                    titleField.getText().trim(), descField.getText().trim(),
                    buildingField.getText().trim(), floorField.getText().trim(),
                    zoneField.getText().trim(), dateField.getText().trim(),
                    (ItemCategory) categoryBox.getSelectedItem()
            );
            JOptionPane.showMessageDialog(dialog, "Lost item reported successfully.");
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

    private void showReportFoundForm() {
        JDialog dialog = new JDialog(this, "Report Found Item", true);
        dialog.setSize(600, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("Report Found Item"), BorderLayout.NORTH);

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
            JOptionPane.showMessageDialog(dialog, "Found item reported successfully.");
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

    private void showSearchDialog() {
        JDialog dialog = new JDialog(this, "Search Items", true);
        dialog.setSize(700, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("Search Items"), BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.setBorder(new EmptyBorder(15, 40, 10, 40));

        JTextField keywordField = new JTextField();
        MainWindow.styleField(keywordField);
        JButton searchBtn = MainWindow.makeButton("Search", new Color(100, 60, 160));

        searchPanel.add(keywordField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(resultArea);

        searchBtn.addActionListener(e -> {
            String keyword = keywordField.getText().trim();
            if (keyword.isEmpty()) return;
            StringBuilder sb = new StringBuilder();
            sb.append("--- Lost Items ---\n");
            for (LostItem item : AppController.getLostItems()) {
                if (item.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                    sb.append("[").append(item.getItemId()).append("] ")
                            .append(item.getTitle()).append(" | ")
                            .append(item.getCategory()).append(" | ")
                            .append(item.getDateReported()).append("\n");
                }
            }
            sb.append("\n--- Found Items ---\n");
            for (FoundItem item : AppController.getFoundItems()) {
                if (item.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                    sb.append("[").append(item.getItemId()).append("] ")
                            .append(item.getTitle()).append(" | ")
                            .append(item.getCategory()).append(" | ")
                            .append(item.getDateReported()).append("\n");
                }
            }
            resultArea.setText(sb.toString());
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245, 245, 245));
        btnPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JButton closeBtn = MainWindow.makeButton("Close", new Color(120, 120, 120));
        closeBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(closeBtn);

        dialog.add(searchPanel, BorderLayout.NORTH);
        dialog.add(scroll, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showMyItemsDialog() {
        JDialog dialog = new JDialog(this, "My Items", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("My Items"), BorderLayout.NORTH);

        StringBuilder sb = new StringBuilder();
        sb.append("--- My Lost Items ---\n");
        for (LostItem item : AppController.getLostItems()) {
            if (item.getReportedByUserId() == AppController.getLoggedInUser().getUserId()) {
                sb.append("[").append(item.getItemId()).append("] ")
                        .append(item.getTitle()).append(" | ")
                        .append(item.getCategory()).append(" | ")
                        .append(item.getStatus()).append(" | ")
                        .append(item.getDateReported()).append("\n");
            }
        }
        sb.append("\n--- My Found Items ---\n");
        for (FoundItem item : AppController.getFoundItems()) {
            if (item.getFoundByUserId() == AppController.getLoggedInUser().getUserId()) {
                sb.append("[").append(item.getItemId()).append("] ")
                        .append(item.getTitle()).append(" | ")
                        .append(item.getCategory()).append(" | ")
                        .append(item.getStatus()).append(" | ")
                        .append(item.getDateReported()).append("\n");
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBorder(new EmptyBorder(10, 10, 10, 10));

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

    private void showClaimForm() {
        JDialog dialog = new JDialog(this, "Submit Claim", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("Submit Claim"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField itemIdField = new JTextField();
        JTextField proofField  = new JTextField();
        JTextField phoneField  = new JTextField();

        for (JTextField f : new JTextField[]{itemIdField, proofField, phoneField})
            MainWindow.styleField(f);

        String[] labels = {"Found Item ID:", "Proof of Ownership:", "Your Phone:"};
        JTextField[] fields = {itemIdField, proofField, phoneField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i * 2;
            gbc.insets = new Insets(12, 60, 0, 60);
            form.add(MainWindow.makeLabel(labels[i]), gbc);
            gbc.gridy = i * 2 + 1;
            gbc.insets = new Insets(2, 60, 0, 60);
            form.add(fields[i], gbc);
        }

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(new Color(245, 245, 245));
        btnPanel.setBorder(new EmptyBorder(15, 60, 15, 60));

        JButton submitBtn = MainWindow.makeButton("Submit", new Color(20, 130, 150));
        JButton cancelBtn = MainWindow.makeButton("Cancel", new Color(120, 120, 120));

        submitBtn.addActionListener(e -> {
            try {
                int itemId = Integer.parseInt(itemIdField.getText().trim());
                AppController.submitClaimGUI(itemId, proofField.getText().trim(), phoneField.getText().trim());
                JOptionPane.showMessageDialog(dialog, "Claim submitted successfully.");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid item ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(cancelBtn);
        btnPanel.add(submitBtn);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showNotificationsDialog() {
        JDialog dialog = new JDialog(this, "Notifications", true);
        dialog.setSize(700, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("Your Notifications"), BorderLayout.NORTH);

        StringBuilder sb = new StringBuilder();
        boolean any = false;
        for (Notification n : AppController.getNotifications()) {
            if (n.getUserId() == AppController.getLoggedInUser().getUserId()) {
                sb.append(n.isRead() ? "[READ]  " : "[NEW]   ")
                        .append(n.getMessage()).append("\n\n");
                n.setRead(true);
                any = true;
            }
        }
        if (!any) sb.append("No notifications yet.");

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(15, 20, 15, 20));

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
}