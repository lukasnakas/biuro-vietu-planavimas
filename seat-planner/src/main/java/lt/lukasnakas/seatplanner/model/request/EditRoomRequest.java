package lt.lukasnakas.seatplanner.model.request;

public class EditRoomRequest {

    private String id;
    private String companyId;
    private String officeId;
    private String floorId;
    private String name;
    private int maxCapacity;

    public EditRoomRequest(String id, String officeId, String name) {
        this.id = id;
        this.officeId = officeId;
        this.name = name;
    }

    public EditRoomRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
