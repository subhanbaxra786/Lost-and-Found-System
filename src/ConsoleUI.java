public class ConsoleUI {

    public static void showMainMenu() {
        System.out.println("\n LOST AND FOUND SYSTEM ");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.println(" ");
    }

    public static void showStudentMenu() {
        System.out.println("\n STUDENT MENU ");
        System.out.println("1. Report Lost Item");
        System.out.println("2. Report Found Item");
        System.out.println("3. Search Items");
        System.out.println("4. View My Items");
        System.out.println("5. Submit Claim");
        System.out.println("6. View My Notifications");
        System.out.println("0. Logout");
        System.out.println(" ");
    }

    public static void showAdminMenu() {
        System.out.println("\n ADMIN MENU ");
        System.out.println("1. View All Lost Items");
        System.out.println("2. View All Found Items");
        System.out.println("3. View All Claims");
        System.out.println("4. Approve / Reject Claim");
        System.out.println("5. Generate Report");
        System.out.println("6. View Audit Log");
        System.out.println("0. Logout");
        System.out.println(" ");
    }

    public static void showStaffMenu() {
        System.out.println("\n STAFF MENU ");
        System.out.println("1. View Found Items");
        System.out.println("2. Register Found Item");
        System.out.println("3. Process Handover");
        System.out.println("0. Logout");
        System.out.println(" ");
    }

    public static void showMessage(String message) {
        System.out.println("\n>> " + message);
    }

    public static void showError(String message) {
        System.out.println("\nERROR " + message);
    }

    public static void showSuccess(String message) {
        System.out.println("\nSUCCESS " + message);
    }
}