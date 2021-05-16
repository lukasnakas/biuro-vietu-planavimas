package lt.lukasnakas.seatplanner.model.request;

public class AddOfficeRequest {

    private String companyId;
    private String name;

    public AddOfficeRequest(String name) {
        this.name = name;
    }

    public AddOfficeRequest() {
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
