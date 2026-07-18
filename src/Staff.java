import javax.swing.*;
public class Staff extends User{
    private String staffId;
    private String counterLocation;

    public Staff(int userId, String name, String email, String password, String phoneNumber, double reputationScore, String staffId, String counterLocation) {
        super(userId, name, email, password, phoneNumber, reputationScore);
        this.staffId = staffId;
        this.counterLocation = counterLocation;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getCounterLocation() {
        return counterLocation;
    }

    public void setCounterLocation(String counterLocation) {
        this.counterLocation = counterLocation;
    }
    @Override
    public void displayDashboard() {
        JOptionPane.showMessageDialog(null,
                "Name    : " + getName() + "\n" +
                        "Email   : " + getEmail() + "\n" +
                        "Counter : " + getCounterLocation(),
                "Staff Dashboard", JOptionPane.INFORMATION_MESSAGE);
    }
}
