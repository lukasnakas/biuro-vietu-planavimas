package lt.lukasnakas.seatplanner.model.request;

public class EditOfficeRequest {

    private String companyId;
    private String id;
    private String name;

    public EditOfficeRequest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public EditOfficeRequest() {
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
