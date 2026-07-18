import java.util.List;
import javax.swing.*;
import java.util.ArrayList;
public class Student extends User implements Searchable {
    private String studentId;
    private String hostel;


    public List<String> search(String keyword) {
        // This will be called from AppController
        // Returns matching item titles as strings
        return new ArrayList<>();
    }

    public Student(int userId, String name, String email, String password, String phoneNumber, double reputationScore, String studentId, String hostel) {
        super(userId, name, email, password, phoneNumber, reputationScore);
        this.studentId = studentId;
        this.hostel = hostel;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String toCSV() {
        return getUserId() + "," + getName() + "," + getEmail() + "," +
                getPassword() + "," + getPhoneNumber() + "," +
                getReputationScore() + "," + studentId + "," + hostel;
    }

    public static Student fromCSV(String line) {
        String[] p = line.split(",");
        return new Student(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4],
                Double.parseDouble(p[5]), p[6], p[7]);
    }
    @Override
    public void displayDashboard() {
        JOptionPane.showMessageDialog(null,
                "Name  : " + getName() + "\n" +
                        "Email : " + getEmail() + "\n" +
                        "Phone : " + getPhoneNumber() + "\n" +
                        "Rep   : " + getReputationScore(),
                "Student Dashboard", JOptionPane.INFORMATION_MESSAGE);
    }
}
