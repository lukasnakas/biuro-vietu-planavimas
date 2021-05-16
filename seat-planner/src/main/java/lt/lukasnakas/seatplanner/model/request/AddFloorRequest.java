package lt.lukasnakas.seatplanner.model.request;

public class AddFloorRequest {

    private String companyId;
    private String officeId;
    private String name;

    public AddFloorRequest(String officeId, String name) {
        this.officeId = officeId;
        this.name = name;
    }

    public AddFloorRequest() {
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
