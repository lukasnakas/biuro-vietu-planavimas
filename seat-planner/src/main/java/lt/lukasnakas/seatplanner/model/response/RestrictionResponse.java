package lt.lukasnakas.seatplanner.model.response;

import lt.lukasnakas.seatplanner.model.enumerators.Restriction;

import java.util.Set;

public class RestrictionResponse {

    private String teamId;
    private Set<Restriction> restrictions;

    public RestrictionResponse(String teamId, Set<Restriction> restrictions) {
        this.teamId = teamId;
        this.restrictions = restrictions;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Set<Restriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Set<Restriction> restrictions) {
        this.restrictions = restrictions;
    }

}
