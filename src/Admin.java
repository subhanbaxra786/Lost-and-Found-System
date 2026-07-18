import javax.swing.*;
public class Admin extends User{
    private String adminId;
    private String department;

    public Admin(int userId, String name, String email, String password, String phoneNumber, double reputationScore, String adminId, String department) {
        super(userId, name, email, password, phoneNumber, reputationScore);
        this.adminId = adminId;
        this.department = department;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getDepartment() {
        return department;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    @Override
    public void displayDashboard() {
        JOptionPane.showMessageDialog(null,
                "Name      : " + getName() + "\n" +
                        "Email     : " + getEmail() + "\n" +
                        "Department: " + getDepartment(),
                "Admin Dashboard", JOptionPane.INFORMATION_MESSAGE);
    }
}
