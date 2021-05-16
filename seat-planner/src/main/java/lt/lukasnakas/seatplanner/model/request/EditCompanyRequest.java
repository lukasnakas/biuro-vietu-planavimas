package lt.lukasnakas.seatplanner.model.request;

public class EditCompanyRequest {

    private String id;
    private String name;

    public EditCompanyRequest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public EditCompanyRequest() {
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
