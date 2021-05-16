package lt.lukasnakas.seatplanner.model.request;

public class RegisterUserRequest {

    private String company;
    private String email;
    private String password;
    private String passwordConfirm;

    public RegisterUserRequest(String company, String email, String password, String passwordConfirm) {
        this.company = company;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public RegisterUserRequest() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
