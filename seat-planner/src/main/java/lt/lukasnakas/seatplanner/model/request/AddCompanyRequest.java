package lt.lukasnakas.seatplanner.model.request;

public class AddCompanyRequest {

    private String name;

    public AddCompanyRequest(String name) {
        this.name = name;
    }

    public AddCompanyRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
