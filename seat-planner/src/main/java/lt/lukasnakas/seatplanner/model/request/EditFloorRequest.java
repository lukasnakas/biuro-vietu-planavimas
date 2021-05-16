package lt.lukasnakas.seatplanner.model.request;

public class EditFloorRequest {

    private String id;
    private String companyId;
    private String officeId;
    private String name;

    public EditFloorRequest(String id, String officeId, String name) {
        this.id = id;
        this.officeId = officeId;
        this.name = name;
    }

    public EditFloorRequest() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
