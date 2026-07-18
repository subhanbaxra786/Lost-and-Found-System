public class LocationZone {
    private String buildingName;
    private String floorNumber;
    private String zoneLabel;

    public LocationZone(String buildingName, String floorNumber, String zoneLabel) {
        this.buildingName = buildingName;
        this.floorNumber = floorNumber;
        this.zoneLabel = zoneLabel;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getZoneLabel() {
        return zoneLabel;
    }

    public void setZoneLabel(String zoneLabel) {
        this.zoneLabel = zoneLabel;
    }
}
