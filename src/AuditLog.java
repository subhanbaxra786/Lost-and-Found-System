import java.io.*;
import java.util.*;

public class AuditLog {
    private int logId;
    private int userId;
    private String action;
    private String timestamp;

    public AuditLog(int logId, int userId, String action, String timestamp) {
        this.logId = logId;
        this.userId = userId;
        this.action = action;
        this.timestamp = timestamp;
    }

    public static void log(int userId, String action) {
        String timestamp = new java.util.Date().toString();
        String line = userId + "," + action + "," + timestamp;
        FileManager.saveLine(FileManager.getAuditFile(), line);
    }

    public static void printAll() {
        List<String> lines = FileManager.loadLines(FileManager.getAuditFile());
        System.out.println("\n===== AUDIT LOG =====");
        if (lines.isEmpty()) {
            System.out.println("No entries found.");
        }
        for (String line : lines) {
            String[] parts = line.split(",", 3);
            if (parts.length == 3) {
                System.out.println("User: " + parts[0] + " | Action: " + parts[1] + " | Time: " + parts[2]);
            }
        }
        System.out.println("=====================\n");
    }

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}