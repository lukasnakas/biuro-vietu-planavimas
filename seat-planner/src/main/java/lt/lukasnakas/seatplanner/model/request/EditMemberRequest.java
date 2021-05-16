package lt.lukasnakas.seatplanner.model.request;

public class EditMemberRequest {

    private String id;
    private String companyId;
    private String firstName;
    private String lastName;
    private String email;
    private int experience;
    private boolean teamless;

    public EditMemberRequest() {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public boolean isTeamless() {
        return teamless;
    }

    public void setTeamless(boolean teamless) {
        this.teamless = teamless;
    }
}
