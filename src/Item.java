public abstract class Item {
    private int itemId;
    private String title;
    private String description;
    private ItemCategory category;
    private LocationZone location;
    private String dateReported;
    private ItemStatus status;

    protected Item(int itemId, String title, String description, ItemCategory category, LocationZone location, String dateReported, ItemStatus status) {
        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.dateReported = dateReported;
        this.status = status;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public LocationZone getLocation() {
        return location;
    }

    public void setLocation(LocationZone location) {
        this.location = location;
    }

    public String getDateReported() {
        return dateReported;
    }

    public void setDateReported(String dateReported) {
        this.dateReported = dateReported;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }
}
