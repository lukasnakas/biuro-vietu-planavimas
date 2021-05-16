package lt.lukasnakas.seatplanner.model.request;

import lt.lukasnakas.seatplanner.model.enumerators.Restriction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RestrictionRequest {

    @NotBlank(message = "Team ID must not be blank")
    private String teamId;

    @NotNull(message = "Restriction must not be blank")
    private Restriction restriction;

    private String args;

    public RestrictionRequest(String teamId, Restriction restriction) {
        this.teamId = teamId;
        this.restriction = restriction;
    }
    public RestrictionRequest(String teamId, Restriction restriction, String args) {
        this.teamId = teamId;
        this.restriction = restriction;
        this.args = args;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public void setRestriction(Restriction restriction) {
        this.restriction = restriction;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

}
