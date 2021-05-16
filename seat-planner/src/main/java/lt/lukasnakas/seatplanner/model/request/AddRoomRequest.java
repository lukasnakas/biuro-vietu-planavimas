package lt.lukasnakas.seatplanner.model.request;

public class AddRoomRequest {

    private String companyId;
    private String officeId;
    private String floorId;
    private String name;
    private int maxCapacity;

    public AddRoomRequest(String officeId, String name) {
        this.officeId = officeId;
        this.name = name;
    }

    public AddRoomRequest() {
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
