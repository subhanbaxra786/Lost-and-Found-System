import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Lost and Found Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        showMainMenu();
    }

    public void showMainMenu() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 90, 160));
        header.setBorder(new EmptyBorder(30, 20, 30, 20));
        JLabel title = new JLabel("Lost and Found Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // Center panel
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1;

        JLabel subtitle = new JLabel("Welcome! Please choose an option.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(80, 80, 80));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        center.add(subtitle, gbc);

        gbc.insets = new Insets(8, 80, 8, 80);

        JButton loginBtn    = makeButton("Login",    new Color(30, 90, 160));
        JButton registerBtn = makeButton("Register", new Color(40, 140, 80));
        JButton exitBtn     = makeButton("Exit",     new Color(180, 50, 50));

        loginBtn.addActionListener(e -> showLoginForm());
        registerBtn.addActionListener(e -> showRegisterForm());
        exitBtn.addActionListener(e -> { AppController.saveAllData(); System.exit(0); });

        gbc.gridy = 1; center.add(loginBtn, gbc);
        gbc.gridy = 2; center.add(registerBtn, gbc);
        gbc.gridy = 3; center.add(exitBtn, gbc);

        add(center, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(new Color(220, 220, 220));
        footer.setBorder(new EmptyBorder(8, 0, 8, 0));
        JLabel footerLabel = new JLabel("UCP Lost and Found System  |  OOP Project", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 100));
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void showLoginForm() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        add(makeHeader("Login to Your Account"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 80, 10, 80);
        gbc.weightx = 1;

        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();

        styleField(emailField);
        styleField(passField);

        gbc.gridx = 0; gbc.gridy = 0; center.add(makeLabel("Email:"), gbc);
        gbc.gridy = 1; center.add(emailField, gbc);
        gbc.gridy = 2; center.add(makeLabel("Password:"), gbc);
        gbc.gridy = 3; center.add(passField, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 80, 5, 80);
        JButton loginBtn = makeButton("Login", new Color(30, 90, 160));
        loginBtn.addActionListener(e -> {
            String email    = emailField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            User user = AppController.findUserPublic(email, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                AppController.setLoggedInUser(user);
                user.displayDashboard();
                AuditLog.log(user.getUserId(), "LOGIN");
                if (user instanceof Student) { new StudentWindow(this).setVisible(true); setVisible(false); }
                else if (user instanceof Admin) { new AdminWindow(this).setVisible(true); setVisible(false); }
                else if (user instanceof Staff) { new StaffWindow(this).setVisible(true); setVisible(false); }
            }
        });
        center.add(loginBtn, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(5, 80, 10, 80);
        JButton backBtn = makeButton("Back", new Color(120, 120, 120));
        backBtn.addActionListener(e -> showMainMenu());
        center.add(backBtn, gbc);

        add(center, BorderLayout.CENTER);
        add(makeFooter(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void showRegisterForm() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        add(makeHeader("Create New Account"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField nameField     = new JTextField();
        JTextField emailField    = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField phoneField    = new JTextField();
        JTextField idField       = new JTextField();
        JTextField hostelField   = new JTextField();

        for (JTextField f : new JTextField[]{nameField, emailField, phoneField, idField, hostelField})
            styleField(f);
        styleField(passField);

        String[] labels = {"Full Name:", "Email:", "Password:", "Phone:", "Student ID:", "Hostel:"};
        JComponent[] fields = {nameField, emailField, passField, phoneField, idField, hostelField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i * 2;
            gbc.insets = new Insets(8, 80, 0, 80);
            center.add(makeLabel(labels[i]), gbc);
            gbc.gridy = i * 2 + 1;
            gbc.insets = new Insets(2, 80, 0, 80);
            center.add(fields[i], gbc);
        }

        gbc.gridy = labels.length * 2;
        gbc.insets = new Insets(20, 80, 5, 80);
        JButton registerBtn = makeButton("Register", new Color(40, 140, 80));
        registerBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AppController.registerStudent(
                    nameField.getText().trim(), emailField.getText().trim(),
                    new String(passField.getPassword()).trim(), phoneField.getText().trim(),
                    idField.getText().trim(), hostelField.getText().trim()
            );
            JOptionPane.showMessageDialog(this, "Registered successfully! You can now login.");
            showMainMenu();
        });
        center.add(registerBtn, gbc);

        gbc.gridy = labels.length * 2 + 1;
        gbc.insets = new Insets(5, 80, 10, 80);
        JButton backBtn = makeButton("Back", new Color(120, 120, 120));
        backBtn.addActionListener(e -> showMainMenu());
        center.add(backBtn, gbc);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(245, 245, 245));
        add(scroll, BorderLayout.CENTER);
        add(makeFooter(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }


    public static JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 42));
        btn.setOpaque(true);
        return btn;
    }

    public static JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl.setForeground(new Color(60, 60, 60));
        return lbl;
    }

    public static void styleField(JComponent field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    public static JPanel makeHeader(String text) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 90, 160));
        header.setBorder(new EmptyBorder(25, 20, 25, 20));
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        return header;
    }

    public static JPanel makeFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(new Color(220, 220, 220));
        footer.setBorder(new EmptyBorder(8, 0, 8, 0));
        JLabel lbl = new JLabel("UCP Lost and Found System  |  OOP Project", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(new Color(100, 100, 100));
        footer.add(lbl);
        return footer;
    }
}