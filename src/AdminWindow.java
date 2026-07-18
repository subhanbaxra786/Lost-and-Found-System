import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminWindow extends JFrame {

    private MainWindow parent;

    public AdminWindow(MainWindow parent) {
        this.parent = parent;
        setTitle("Admin Dashboard");
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

        add(MainWindow.makeHeader("Admin Dashboard — " + AppController.getLoggedInUser().getName()), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 120, 8, 120);

        JButton[] buttons = {
                MainWindow.makeButton("View All Lost Items",   new Color(30, 90, 160)),
                MainWindow.makeButton("View All Found Items",  new Color(40, 140, 80)),
                MainWindow.makeButton("View All Claims",       new Color(100, 60, 160)),
                MainWindow.makeButton("Approve / Reject Claim", new Color(160, 100, 20)),
                MainWindow.makeButton("Generate Report",       new Color(20, 130, 150)),
                MainWindow.makeButton("View Audit Log",        new Color(80, 80, 80)),
                MainWindow.makeButton("Logout",                new Color(180, 50, 50)),
        };

        buttons[0].addActionListener(e -> showItemsDialog("Lost"));
        buttons[1].addActionListener(e -> showItemsDialog("Found"));
        buttons[2].addActionListener(e -> showClaimsDialog());
        buttons[3].addActionListener(e -> showClaimDecisionDialog());
        buttons[4].addActionListener(e -> showReportDialog());
        buttons[5].addActionListener(e -> showAuditLogDialog());
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

    private void showItemsDialog(String type) {
        JDialog dialog = new JDialog(this, "View " + type + " Items", true);
        dialog.setSize(750, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("All " + type + " Items"), BorderLayout.NORTH);

        StringBuilder sb = new StringBuilder();
        if (type.equals("Lost")) {
            for (LostItem item : AppController.getLostItems()) {
                sb.append("[").append(item.getItemId()).append("] ")
                        .append(item.getTitle()).append(" | ")
                        .append(item.getCategory()).append(" | ")
                        .append(item.getStatus()).append(" | ")
                        .append(item.getDateReported()).append("\n");
            }
        } else {
            for (FoundItem item : AppController.getFoundItems()) {
                sb.append("[").append(item.getItemId()).append("] ")
                        .append(item.getTitle()).append(" | ")
                        .append(item.getCategory()).append(" | ")
                        .append(item.getStatus()).append(" | ")
                        .append(item.getDateReported()).append("\n");
            }
        }
        if (sb.length() == 0) sb.append("No items found.");

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

    private void showClaimsDialog() {
        JDialog dialog = new JDialog(this, "All Claims", true);
        dialog.setSize(750, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("All Claims"), BorderLayout.NORTH);

        StringBuilder sb = new StringBuilder();
        for (Claim c : AppController.getClaims()) {
            sb.append("[").append(c.getClaimId()).append("] ")
                    .append(c.getClaimantName()).append(" | Item ID: ")
                    .append(c.getItemId()).append(" | Status: ")
                    .append(c.getStatus()).append(" | ")
                    .append(c.getClaimDate()).append("\n");
        }
        if (sb.length() == 0) sb.append("No claims found.");

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

    private void showClaimDecisionDialog() {
        JDialog dialog = new JDialog(this, "Approve / Reject Claim", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("Approve / Reject Claim"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField claimIdField = new JTextField();
        MainWindow.styleField(claimIdField);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(20, 60, 0, 60);
        form.add(MainWindow.makeLabel("Enter Claim ID:"), gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 60, 0, 60);
        form.add(claimIdField, gbc);

        // Show existing claims for reference
        StringBuilder sb = new StringBuilder("Existing claims:\n");
        for (Claim c : AppController.getClaims()) {
            sb.append("[").append(c.getClaimId()).append("] ")
                    .append(c.getClaimantName()).append(" | Item: ")
                    .append(c.getItemId()).append(" | ")
                    .append(c.getStatus()).append("\n");
        }
        JTextArea claimsArea = new JTextArea(sb.toString());
        claimsArea.setEditable(false);
        claimsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        claimsArea.setBackground(new Color(235, 235, 235));
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 60, 0, 60);
        form.add(new JScrollPane(claimsArea), gbc);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(new Color(245, 245, 245));
        btnPanel.setBorder(new EmptyBorder(15, 60, 15, 60));

        JButton approveBtn = MainWindow.makeButton("Approve", new Color(40, 140, 80));
        JButton rejectBtn  = MainWindow.makeButton("Reject",  new Color(180, 50, 50));

        approveBtn.addActionListener(e -> processDecision(dialog, claimIdField, true));
        rejectBtn.addActionListener(e -> processDecision(dialog, claimIdField, false));

        btnPanel.add(approveBtn);
        btnPanel.add(rejectBtn);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void processDecision(JDialog dialog, JTextField claimIdField, boolean approve) {
        try {
            int claimId = Integer.parseInt(claimIdField.getText().trim());
            boolean success = AppController.processClaimDecisionGUI(claimId, approve);
            if (success) {
                JOptionPane.showMessageDialog(dialog,
                        "Claim " + claimId + " has been " + (approve ? "APPROVED." : "REJECTED."));
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Claim ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Invalid Claim ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReportDialog() {
        JDialog dialog = new JDialog(this, "System Report", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("System Report"), BorderLayout.NORTH);

        StringBuilder sb = new StringBuilder();
        sb.append("Total lost items reported : ").append(AppController.getLostItems().size()).append("\n");
        sb.append("Total found items reported: ").append(AppController.getFoundItems().size()).append("\n");
        sb.append("Total claims submitted    : ").append(AppController.getClaims().size()).append("\n\n");

        long approved = AppController.getClaims().stream().filter(c -> c.getStatus() == ClaimStatus.APPROVED).count();
        long pending  = AppController.getClaims().stream().filter(c -> c.getStatus() == ClaimStatus.PENDING).count();
        long rejected = AppController.getClaims().stream().filter(c -> c.getStatus() == ClaimStatus.REJECTED).count();

        sb.append("Claims approved           : ").append(approved).append("\n");
        sb.append("Claims pending            : ").append(pending).append("\n");
        sb.append("Claims rejected           : ").append(rejected).append("\n");

        if (!AppController.getLostItems().isEmpty()) {
            double rate = ((double) approved / AppController.getLostItems().size()) * 100;
            sb.append(String.format("Recovery rate             : %.1f%%%n", rate));
        }

        sb.append("\n--- Items by Category ---\n");
        for (ItemCategory cat : ItemCategory.values()) {
            long lost  = AppController.getLostItems().stream().filter(i -> i.getCategory() == cat).count();
            long found = AppController.getFoundItems().stream().filter(i -> i.getCategory() == cat).count();
            if (lost > 0 || found > 0) {
                sb.append(cat).append(" -> Lost: ").append(lost).append(", Found: ").append(found).append("\n");
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
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

    private void showAuditLogDialog() {
        JDialog dialog = new JDialog(this, "Audit Log", true);
        dialog.setSize(750, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(MainWindow.makeHeader("Audit Log"), BorderLayout.NORTH);

        StringBuilder sb = new StringBuilder();
        for (String line : FileManager.loadLines(FileManager.getAuditFile())) {
            String[] parts = line.split(",", 3);
            if (parts.length == 3) {
                sb.append("User: ").append(parts[0])
                        .append(" | Action: ").append(parts[1])
                        .append(" | Time: ").append(parts[2]).append("\n");
            }
        }
        if (sb.length() == 0) sb.append("No audit entries found.");

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
}