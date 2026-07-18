import java.io.*;
import java.util.*;

public class FileManager {

    private static final String USERS_FILE = "users.txt";
    private static final String LOST_ITEMS_FILE = "lost_items.txt";
    private static final String FOUND_ITEMS_FILE = "found_items.txt";
    private static final String CLAIMS_FILE = "claims.txt";
    private static final String NOTIFICATIONS_FILE = "notifications.txt";
    private static final String AUDIT_FILE = "audit_log.txt";
    private static final String REWARDS_FILE = "rewards.txt";

    // Save a single line to a file
    public static void saveLine(String filename, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving to " + filename + ": " + e.getMessage());
        }
    }

    // Load all lines from a file
    public static List<String> loadLines(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
        }
        return lines;
    }

    // Overwrite entire file with new content
    public static void saveAllLines(String filename, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to " + filename + ": " + e.getMessage());
        }
    }

    public static String getUsersFile() { return USERS_FILE; }
    public static String getLostItemsFile() { return LOST_ITEMS_FILE; }
    public static String getFoundItemsFile() { return FOUND_ITEMS_FILE; }
    public static String getClaimsFile() { return CLAIMS_FILE; }
    public static String getNotificationsFile() { return NOTIFICATIONS_FILE; }
    public static String getAuditFile() { return AUDIT_FILE; }
    public static String getRewardsFile() { return REWARDS_FILE; }
}