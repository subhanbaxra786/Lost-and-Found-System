import java.util.ArrayList;
import java.util.List;

public class AppController {

    private static List<Student> students = new ArrayList<>();
    private static List<Admin> admins = new ArrayList<>();
    private static List<Staff> staffList = new ArrayList<>();
    private static List<LostItem> lostItems = new ArrayList<>();
    private static List<FoundItem> foundItems = new ArrayList<>();
    private static List<Claim> claims = new ArrayList<>();
    private static List<Notification> notifications = new ArrayList<>();

    private static User loggedInUser = null;

    public static void main(String[] args) {
        loadAllData();
        seedData();
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }

    private static void mainLoop() {
        while (true) {
            ConsoleUI.showMainMenu();
            int choice = InputValidator.getMenuChoice("Enter choice: ", 0, 2);

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 0 -> {
                    ConsoleUI.showMessage("Goodbye!");
                    System.exit(0);
                }
            }
        }
    }

    private static void login() {
        String email    = InputValidator.getString("Email: ");
        String password = InputValidator.getString("Password: ");

        User found = findUser(email, password);
        if (found == null) {
            ConsoleUI.showError("Invalid email or password.");
            return;
        }

        loggedInUser = found;
        loggedInUser.displayDashboard();
        AuditLog.log(loggedInUser.getUserId(), "LOGIN");
        ConsoleUI.showSuccess("Welcome, " + loggedInUser.getName() + "!");

        if (found instanceof Student) studentLoop();
        else if (found instanceof Admin) adminLoop();
        else if (found instanceof Staff) staffLoop();
    }

    private static void register() {
        String name     = InputValidator.getString("Full name: ");
        String email    = InputValidator.getString("Email: ");
        String password = InputValidator.getString("Password: ");
        String phone    = InputValidator.getString("Phone: ");
        String studentId = InputValidator.getString("Student ID: ");
        String hostel   = InputValidator.getString("Hostel: ");

        int id = students.size() + 1;
        Student s = new Student(id, name, email, password, phone, 5.0, studentId, hostel);
        students.add(s);
        saveAllData();
        ConsoleUI.showSuccess("Registered successfully. You can now login.");
        AuditLog.log(id, "REGISTER");
    }

    private static void studentLoop() {
        while (true) {
            ConsoleUI.showStudentMenu();
            int choice = InputValidator.getMenuChoice("Enter choice: ", 0, 6);

            switch (choice) {
                case 1 -> reportLostItem();
                case 2 -> reportFoundItem();
                case 3 -> searchItems();
                case 4 -> viewMyItems();
                case 5 -> submitClaim();
                case 6 -> viewNotifications();
                case 0 -> {
                    AuditLog.log(loggedInUser.getUserId(), "LOGOUT");
                    loggedInUser = null;
                    return;
                }
            }
        }
    }

    private static void adminLoop() {
        while (true) {
            ConsoleUI.showAdminMenu();
            int choice = InputValidator.getMenuChoice("Enter choice: ", 0, 6);

            switch (choice) {
                case 1 -> viewAllLostItems();
                case 2 -> viewAllFoundItems();
                case 3 -> viewAllClaims();
                case 4 -> processClaimDecision();
                case 5 -> ReportGenerator.generateSummaryReport(lostItems, foundItems, claims);
                case 6 -> AuditLog.printAll();
                case 0 -> {
                    AuditLog.log(loggedInUser.getUserId(), "LOGOUT");
                    loggedInUser = null;
                    return;
                }
            }
        }
    }

    private static void staffLoop() {
        while (true) {
            ConsoleUI.showStaffMenu();
            int choice = InputValidator.getMenuChoice("Enter choice: ", 0, 3);

            switch (choice) {
                case 1 -> viewAllFoundItems();
                case 2 -> reportFoundItem();
                case 3 -> processHandover();
                case 0 -> {
                    AuditLog.log(loggedInUser.getUserId(), "LOGOUT");
                    loggedInUser = null;
                    return;
                }
            }
        }
    }

    private static void reportLostItem() {
        String title       = InputValidator.getString("Item title: ");
        String description = InputValidator.getString("Description: ");
        String building    = InputValidator.getString("Building: ");
        String floor       = InputValidator.getString("Floor: ");
        String zone        = InputValidator.getString("Zone: ");
        String date        = InputValidator.getString("Date lost (DD/MM/YYYY): ");

        System.out.println("Categories: ");
        for (ItemCategory cat : ItemCategory.values()) {
            System.out.println(cat.ordinal() + 1 + ". " + cat);
        }
        int catChoice = InputValidator.getMenuChoice("Select category: ", 1, ItemCategory.values().length);
        ItemCategory category = ItemCategory.values()[catChoice - 1];

        LocationZone location = new LocationZone(building, floor, zone);
        int id = lostItems.size() + 1;
        LostItem item = new LostItem(id, title, description, category, location, date, ItemStatus.LOST, loggedInUser.getUserId(), null);
        lostItems.add(item);

        List<MatchScore> matches = MatchManager.matchLostItem(item, foundItems);
        if (!matches.isEmpty()) {
            ConsoleUI.showMessage("Potential matches found: " + matches.size());
            notifications.add(new Notification(notifications.size() + 1, loggedInUser.getUserId(),
                    "Match found for your lost item: " + title,
                    NotificationType.MATCH_FOUND, false, new java.util.Date().toString()));
        }

        AuditLog.log(loggedInUser.getUserId(), "REPORTED_LOST_ITEM:" + title);
        ConsoleUI.showSuccess("Lost item reported with ID: " + id);
    }

    private static void reportFoundItem() {
        String title       = InputValidator.getString("Item title: ");
        String description = InputValidator.getString("Description: ");
        String building    = InputValidator.getString("Building: ");
        String floor       = InputValidator.getString("Floor: ");
        String zone        = InputValidator.getString("Zone: ");
        String date        = InputValidator.getString("Date found (DD/MM/YYYY): ");

        System.out.println("Categories: ");
        for (ItemCategory cat : ItemCategory.values()) {
            System.out.println(cat.ordinal() + 1 + ". " + cat);
        }
        int catChoice = InputValidator.getMenuChoice("Select category: ", 1, ItemCategory.values().length);
        ItemCategory category = ItemCategory.values()[catChoice - 1];

        LocationZone location = new LocationZone(building, floor, zone);
        int id = foundItems.size() + 1;
        FoundItem item = new FoundItem(id, title, description, category, location, date, ItemStatus.FOUND, loggedInUser.getUserId());
        foundItems.add(item);

        AuditLog.log(loggedInUser.getUserId(), "REPORTED_FOUND_ITEM:" + title);
        ConsoleUI.showSuccess("Found item reported with ID: " + id);
    }

    private static void searchItems() {
        String keyword = InputValidator.getString("Enter keyword to search: ");
        System.out.println("\n--- Lost Items ---");
        for (LostItem item : lostItems) {
            if (item.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println("[" + item.getItemId() + "] " + item.getTitle() + " | " + item.getCategory() + " | " + item.getDateReported());
            }
        }
        System.out.println("\n--- Found Items ---");
        for (FoundItem item : foundItems) {
            if (item.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println("[" + item.getItemId() + "] " + item.getTitle() + " | " + item.getCategory() + " | " + item.getDateReported());
            }
        }
    }

    private static void viewMyItems() {
        System.out.println("\n--- My Lost Items ---");
        for (LostItem item : lostItems) {
            if (item.getReportedByUserId() == loggedInUser.getUserId()) {
                System.out.println("[" + item.getItemId() + "] " + item.getTitle() + " | " + item.getStatus());
            }
        }
        System.out.println("\n--- My Found Items ---");
        for (FoundItem item : foundItems) {
            if (item.getFoundByUserId() == loggedInUser.getUserId()) {
                System.out.println("[" + item.getItemId() + "] " + item.getTitle() + " | " + item.getStatus());
            }
        }
    }

    private static void submitClaim() {
        int itemId  = InputValidator.getInt("Enter Found Item ID to claim: ");
        String proof = InputValidator.getString("Describe your proof of ownership: ");
        String phone = InputValidator.getString("Your phone number: ");

        int id = claims.size() + 1;
        Claim claim = new Claim(id, loggedInUser.getName(), phone, proof, ClaimStatus.PENDING, new java.util.Date().toString(), itemId);
        claims.add(claim);

        AuditLog.log(loggedInUser.getUserId(), "SUBMITTED_CLAIM:item" + itemId);
        ConsoleUI.showSuccess("Claim submitted with ID: " + id + ". Pending review.");
    }

    private static void viewNotifications() {
        System.out.println("\n--- Your Notifications ---");
        boolean any = false;
        for (Notification n : notifications) {
            if (n.getUserId() == loggedInUser.getUserId()) {
                String read = n.isRead() ? "[READ]" : "[NEW]";
                System.out.println(read + " " + n.getMessage());
                n.setRead(true);
                any = true;
            }
        }
        if (!any) System.out.println("No notifications.");
    }

    private static void viewAllLostItems() {
        System.out.println("\n--- All Lost Items ---");
        for (LostItem item : lostItems) {
            System.out.println("[" + item.getItemId() + "] " + item.getTitle() + " | " + item.getCategory() + " | " + item.getStatus() + " | " + item.getDateReported());
        }
    }

    private static void viewAllFoundItems() {
        System.out.println("\n--- All Found Items ---");
        for (FoundItem item : foundItems) {
            System.out.println("[" + item.getItemId() + "] " + item.getTitle() + " | " + item.getCategory() + " | " + item.getStatus() + " | " + item.getDateReported());
        }
    }

    private static void viewAllClaims() {
        System.out.println("\n--- All Claims ---");
        for (Claim c : claims) {
            System.out.println("[" + c.getClaimId() + "] " + c.getClaimantName() + " | Item: " + c.getItemId() + " | Status: " + c.getStatus());
        }
    }

    private static void processClaimDecision() {
        viewAllClaims();
        int claimId = InputValidator.getInt("Enter Claim ID to process: ");
        Claim target = null;
        for (Claim c : claims) {
            if (c.getClaimId() == claimId) {
                target = c;
                break;
            }
        }
        if (target == null) {
            ConsoleUI.showError("Claim not found.");
            return;
        }
        boolean approve = InputValidator.getYesNo("Approve this claim?");
        target.setStatus(approve ? ClaimStatus.APPROVED : ClaimStatus.REJECTED);
        for (Student s : students) {
            if (s.getName().equals(target.getClaimantName())) {
                String msg = approve
                        ? "Your claim for item ID " + target.getItemId() + " has been APPROVED."
                        : "Your claim for item ID " + target.getItemId() + " has been REJECTED.";
                notifications.add(new Notification(
                        notifications.size() + 1,
                        s.getUserId(),
                        msg,
                        NotificationType.CLAIM_UPDATE,
                        false,
                        new java.util.Date().toString()
                ));
                break;
            }
        }
        AuditLog.log(loggedInUser.getUserId(), "CLAIM_DECISION:" + claimId + ":" + target.getStatus());
        ConsoleUI.showSuccess("Claim " + claimId + " marked as " + target.getStatus());
    }

    private static void processHandover() {
        int itemId = InputValidator.getInt("Enter Found Item ID to hand over: ");
        for (FoundItem item : foundItems) {
            if (item.getItemId() == itemId) {
                item.setStatus(ItemStatus.RESOLVED);
                AuditLog.log(loggedInUser.getUserId(), "HANDOVER:item" + itemId);
                ConsoleUI.showSuccess("Item " + itemId + " marked as resolved.");
                return;
            }
        }
        ConsoleUI.showError("Item not found.");
    }

    private static User findUser(String email, String password) {
        for (Student s : students) {
            if (s.getEmail().equals(email) && s.getPassword().equals(password)) return s;
        }
        for (Admin a : admins) {
            if (a.getEmail().equals(email) && a.getPassword().equals(password)) return a;
        }
        for (Staff st : staffList) {
            if (st.getEmail().equals(email) && st.getPassword().equals(password)) return st;
        }
        return null;
    }

    private static void seedData() {
        admins.add(new Admin(1, "Admin User", "admin@uni.com", "admin123", "03001234567", 5.0, "A001", "IT"));
        staffList.add(new Staff(2, "Staff User", "staff@uni.com", "staff123", "03007654321", 5.0, "S001", "Main Counter"));
    }
    public static void saveAllData() {
        List<String> lostLines = new ArrayList<>();
        for (LostItem i : lostItems) lostLines.add(i.toCSV());
        FileManager.saveAllLines(FileManager.getLostItemsFile(), lostLines);

        List<String> foundLines = new ArrayList<>();
        for (FoundItem i : foundItems) foundLines.add(i.toCSV());
        FileManager.saveAllLines(FileManager.getFoundItemsFile(), foundLines);

        List<String> studentLines = new ArrayList<>();
        for (Student s : students) studentLines.add(s.toCSV());
        FileManager.saveAllLines(FileManager.getUsersFile(), studentLines);

        List<String> claimLines = new ArrayList<>();
        for (Claim c : claims) claimLines.add(c.toCSV());
        FileManager.saveAllLines(FileManager.getClaimsFile(), claimLines);
    }

    private static void loadAllData() {
        for (String line : FileManager.loadLines(FileManager.getUsersFile())) {
            students.add(Student.fromCSV(line));
        }
        for (String line : FileManager.loadLines(FileManager.getLostItemsFile())) {
            lostItems.add(LostItem.fromCSV(line));
        }
        for (String line : FileManager.loadLines(FileManager.getFoundItemsFile())) {
            foundItems.add(FoundItem.fromCSV(line));
        }
        for (String line : FileManager.loadLines(FileManager.getClaimsFile())) {
            claims.add(Claim.fromCSV(line));
        }
    }
    public static User findUserPublic(String email, String password) {
        return findUser(email, password);
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static void registerStudent(String name, String email, String password, String phone, String studentId, String hostel) {
        int id = students.size() + 1;
        Student s = new Student(id, name, email, password, phone, 5.0, studentId, hostel);
        students.add(s);
        saveAllData();
        AuditLog.log(id, "REGISTER");
    }
    public static User getLoggedInUser() { return loggedInUser; }

    public static List<LostItem> getLostItems() { return lostItems; }

    public static List<FoundItem> getFoundItems() { return foundItems; }

    public static List<Notification> getNotifications() { return notifications; }

    public static void reportLostItemGUI(String title, String desc, String building, String floor, String zone, String date, ItemCategory category) {
        LocationZone location = new LocationZone(building, floor, zone);
        int id = lostItems.size() + 1;
        LostItem item = new LostItem(id, title, desc, category, location, date, ItemStatus.LOST, loggedInUser.getUserId(), null);
        lostItems.add(item);
        List<MatchScore> matches = MatchManager.matchLostItem(item, foundItems);
        if (!matches.isEmpty()) {
            notifications.add(new Notification(notifications.size() + 1, loggedInUser.getUserId(),
                    "Match found for your lost item: " + title,
                    NotificationType.MATCH_FOUND, false, new java.util.Date().toString()));
        }
        AuditLog.log(loggedInUser.getUserId(), "REPORTED_LOST_ITEM:" + title);
        saveAllData();
    }

    public static void reportFoundItemGUI(String title, String desc, String building, String floor, String zone, String date, ItemCategory category) {
        LocationZone location = new LocationZone(building, floor, zone);
        int id = foundItems.size() + 1;
        FoundItem item = new FoundItem(id, title, desc, category, location, date, ItemStatus.FOUND, loggedInUser.getUserId());
        foundItems.add(item);
        AuditLog.log(loggedInUser.getUserId(), "REPORTED_FOUND_ITEM:" + title);
        saveAllData();
    }

    public static void submitClaimGUI(int itemId, String proof, String phone) {
        int id = claims.size() + 1;
        Claim claim = new Claim(id, loggedInUser.getName(), phone, proof, ClaimStatus.PENDING, new java.util.Date().toString(), itemId);
        claims.add(claim);
        AuditLog.log(loggedInUser.getUserId(), "SUBMITTED_CLAIM:item" + itemId);
        saveAllData();
    }
    public static List<Claim> getClaims() { return claims; }

    public static boolean processClaimDecisionGUI(int claimId, boolean approve) {
        for (Claim c : claims) {
            if (c.getClaimId() == claimId) {
                c.setStatus(approve ? ClaimStatus.APPROVED : ClaimStatus.REJECTED);
                for (Student s : students) {
                    if (s.getName().equals(c.getClaimantName())) {
                        String msg = approve
                                ? "Your claim for item ID " + c.getItemId() + " has been APPROVED."
                                : "Your claim for item ID " + c.getItemId() + " has been REJECTED.";
                        notifications.add(new Notification(
                                notifications.size() + 1, s.getUserId(), msg,
                                NotificationType.CLAIM_UPDATE, false, new java.util.Date().toString()
                        ));
                        break;
                    }
                }
                AuditLog.log(loggedInUser.getUserId(), "CLAIM_DECISION:" + claimId + ":" + c.getStatus());
                saveAllData();
                return true;
            }
        }
        return false;
    }
    public static boolean processHandoverGUI(int itemId) {
        for (FoundItem item : foundItems) {
            if (item.getItemId() == itemId) {
                item.setStatus(ItemStatus.RESOLVED);
                AuditLog.log(loggedInUser.getUserId(), "HANDOVER:item" + itemId);
                saveAllData();
                return true;
            }
        }
        return false;
    }
}