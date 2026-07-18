public class LostItem extends Item{
    private int reportedByUserId;
    private Reward reward;

    public LostItem(int itemId, String title, String description, ItemCategory category, LocationZone location, String dateReported, ItemStatus status, int reportedByUserId, Reward reward) {
        super(itemId, title, description, category, location, dateReported, status);
        this.reportedByUserId = reportedByUserId;
        this.reward = reward;
    }

    public int getReportedByUserId() {
        return reportedByUserId;
    }

    public void setReportedByUserId(int reportedByUserId) {
        this.reportedByUserId = reportedByUserId;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }
    public String toCSV() {
        String rewardId = (reward != null) ? String.valueOf(reward.getRewardId()) : "null";
        return getItemId() + "," + getTitle() + "," + getDescription() + "," +
                getCategory() + "," + getLocation().getBuildingName() + "," +
                getLocation().getFloorNumber() + "," + getLocation().getZoneLabel() + "," +
                getDateReported() + "," + getStatus() + "," +
                reportedByUserId + "," + rewardId;
    }

    public static LostItem fromCSV(String line) {
        String[] p = line.split(",");
        LocationZone loc = new LocationZone(p[4], p[5], p[6]);
        return new LostItem(Integer.parseInt(p[0]), p[1], p[2],
                ItemCategory.valueOf(p[3]), loc, p[7],
                ItemStatus.valueOf(p[8]), Integer.parseInt(p[9]), null);
    }
    @Override
    public String toString() {
        return "[LostItem] ID: " + getItemId() + " | Title: " + getTitle() +
                " | Category: " + getCategory() + " | Status: " + getStatus();
    }
}
