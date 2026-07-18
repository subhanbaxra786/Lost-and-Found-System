public class MatchScore {
    private int lostItemId;
    private int foundItemId;
    private double score;

    public MatchScore(int lostItemId, int foundItemId, double score) {
        this.lostItemId = lostItemId;
        this.foundItemId = foundItemId;
        this.score = score;
    }

    public int getLostItemId() {
        return lostItemId;
    }

    public void setLostItemId(int lostItemId) {
        this.lostItemId = lostItemId;
    }

    public int getFoundItemId() {
        return foundItemId;
    }

    public void setFoundItemId(int foundItemId) {
        this.foundItemId = foundItemId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
