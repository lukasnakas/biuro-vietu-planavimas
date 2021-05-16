package lt.lukasnakas.seatplanner.model.request;

import lt.lukasnakas.seatplanner.model.Member;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class FindSeatRequest {

    @NotBlank(message = "field is mandatory")
    private String teamName;

    @NotBlank(message = "field is mandatory")
    private String location;

    @Min(value = 1, message = "field must be greater than 0")
    private int peopleAmount = 1;

    private List<Member> members = new ArrayList<>();

    @Min(value = 2, message = "maxTeamsInvolved must be greater than 1")
    private int maxTeamsInvolved = 3;

    private boolean splittable = false;

    private String companyId;
    private String officeId;

    public FindSeatRequest(String teamName, String location, int peopleAmount, int maxTeamsInvolved,
                           boolean splittable, String companyId, String officeId, List<Member> members) {
        this.teamName = teamName;
        this.location = location;
        this.peopleAmount = peopleAmount;
        this.maxTeamsInvolved = maxTeamsInvolved;
        this.splittable = splittable;
        this.companyId = companyId;
        this.officeId = officeId;
        this.members = members;
    }

    public FindSeatRequest() {
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPeopleAmount() {
        return peopleAmount;
    }

    public void setPeopleAmount(int peopleAmount) {
        this.peopleAmount = peopleAmount;
    }

    public int getMaxTeamsInvolved() {
        return maxTeamsInvolved;
    }

    public void setMaxTeamsInvolved(int maxTeamsInvolved) {
        this.maxTeamsInvolved = maxTeamsInvolved;
    }

    public boolean isSplittable() {
        return splittable;
    }

    public void setSplittable(boolean splittable) {
        this.splittable = splittable;
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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
