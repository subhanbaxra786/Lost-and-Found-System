public class FoundItem extends Item{
    private int foundByUserId;

    public FoundItem(int itemId, String title, String description, ItemCategory category, LocationZone location, String dateReported, ItemStatus status, int foundByUserId) {
        super(itemId, title, description, category, location, dateReported, status);
        this.foundByUserId = foundByUserId;
    }

    public int getFoundByUserId() {
        return foundByUserId;
    }

    public void setFoundByUserId(int foundByUserId) {
        this.foundByUserId = foundByUserId;
    }
    public String toCSV() {
        return getItemId() + "," + getTitle() + "," + getDescription() + "," +
                getCategory() + "," + getLocation().getBuildingName() + "," +
                getLocation().getFloorNumber() + "," + getLocation().getZoneLabel() + "," +
                getDateReported() + "," + getStatus() + "," + foundByUserId;
    }

    public static FoundItem fromCSV(String line) {
        String[] p = line.split(",");
        LocationZone loc = new LocationZone(p[4], p[5], p[6]);
        return new FoundItem(Integer.parseInt(p[0]), p[1], p[2],
                ItemCategory.valueOf(p[3]), loc, p[7],
                ItemStatus.valueOf(p[8]), Integer.parseInt(p[9]));
    }
    @Override
    public String toString() {
        return "[FoundItem] ID: " + getItemId() + " | Title: " + getTitle() +
                " | Category: " + getCategory() + " | Status: " + getStatus();
    }
}
