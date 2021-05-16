package lt.lukasnakas.seatplanner.model.response;

import lt.lukasnakas.seatplanner.model.enumerators.Role;

public class LoginUserResponse {

    private boolean isSuccessful;
    private String company;
    private Role role;

    public LoginUserResponse(boolean isSuccessful, String company, Role role) {
        this.isSuccessful = isSuccessful;
        this.company = company;
        this.role = role;
    }

    public LoginUserResponse(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public LoginUserResponse() {
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
