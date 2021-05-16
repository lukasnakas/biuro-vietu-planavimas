package lt.lukasnakas.seatplanner.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lt.lukasnakas.seatplanner.model.enumerators.Role;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.util.List;

public class User {

    @BsonId
    @JsonSerialize(using = NoObjectIdSerializer.class)
    private ObjectId id;

    private String company;

    private String email;

    private String password;

    private List<Role> roles;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
