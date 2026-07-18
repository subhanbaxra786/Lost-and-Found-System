import java.util.List;

public class ReportGenerator {

    public static void generateSummaryReport(List<LostItem> lostItems, List<FoundItem> foundItems, List<Claim> claims) {
        System.out.println("\n===== SYSTEM REPORT =====");
        System.out.println("Total lost items reported : " + lostItems.size());
        System.out.println("Total found items reported: " + foundItems.size());
        System.out.println("Total claims submitted    : " + claims.size());

        long approved = claims.stream().filter(c -> c.getStatus() == ClaimStatus.APPROVED).count();
        long pending  = claims.stream().filter(c -> c.getStatus() == ClaimStatus.PENDING).count();
        long rejected = claims.stream().filter(c -> c.getStatus() == ClaimStatus.REJECTED).count();

        System.out.println("Claims approved           : " + approved);
        System.out.println("Claims pending            : " + pending);
        System.out.println("Claims rejected           : " + rejected);

        if (!lostItems.isEmpty()) {
            double recoveryRate = ((double) approved / lostItems.size()) * 100;
            System.out.printf("Recovery rate             : %.1f%%%n", recoveryRate);
        }

        System.out.println("\n--- Items by Category ---");
        for (ItemCategory cat : ItemCategory.values()) {
            long lostCount  = lostItems.stream().filter(i -> i.getCategory() == cat).count();
            long foundCount = foundItems.stream().filter(i -> i.getCategory() == cat).count();
            if (lostCount > 0 || foundCount > 0) {
                System.out.println(cat + " -> Lost: " + lostCount + ", Found: " + foundCount);
            }
        }

        System.out.println("=========================\n");
    }
}