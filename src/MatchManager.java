import java.util.ArrayList;
import java.util.List;

public class MatchManager {

    private static final double MATCH_THRESHOLD = 50.0;

    public static List<MatchScore> matchLostItem(LostItem lost, List<FoundItem> foundItems) {
        List<MatchScore> results = new ArrayList<>();

        for (FoundItem found : foundItems) {
            double score = calculateScore(lost, found);
            if (score >= MATCH_THRESHOLD) {
                results.add(new MatchScore(lost.getItemId(), found.getItemId(), score));
            }
        }

        return results;
    }

    private static double calculateScore(LostItem lost, FoundItem found) {
        double score = 0;

        // Category match — worth 40 points
        if (lost.getCategory() == found.getCategory()) {
            score += 40;
        }

        // Location match — worth 30 points
        if (lost.getLocation().getBuildingName()
                .equalsIgnoreCase(found.getLocation().getBuildingName())) {
            score += 30;
        }

        // Keyword match in title — worth 30 points
        String lostTitle = lost.getTitle().toLowerCase();
        String foundTitle = found.getTitle().toLowerCase();
        String[] lostWords = lostTitle.split(" ");

        for (String word : lostWords) {
            if (word.length() > 3 && foundTitle.contains(word)) {
                score += 10;
                if (score >= 30) break;
            }
        }

        return Math.min(score, 100);
    }
}