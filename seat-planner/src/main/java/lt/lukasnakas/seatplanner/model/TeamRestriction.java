package lt.lukasnakas.seatplanner.model;

import lt.lukasnakas.seatplanner.model.enumerators.Restriction;

import java.util.Objects;
import java.util.Set;

public class TeamRestriction {

    private String id;
    private Team team;
    private Set<Restriction> restrictionSet;
    private Team additionalTeam;

    public TeamRestriction() {
    }

    public TeamRestriction(Team team, Set<Restriction> restrictionSet) {
        this.team = team;
        this.restrictionSet = restrictionSet;
    }

    public TeamRestriction(Team team, Set<Restriction> restrictionSet, Team additionalTeam) {
        this.team = team;
        this.restrictionSet = restrictionSet;
        this.additionalTeam = additionalTeam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<Restriction> getRestrictionSet() {
        return restrictionSet;
    }

    public void setRestrictionSet(Set<Restriction> restrictionSet) {
        this.restrictionSet = restrictionSet;
    }

    public void addRestriction(Restriction restriction) {
        this.restrictionSet.add(restriction);
    }

    public void removeRestriction(Restriction restriction) {
        this.restrictionSet.remove(restriction);
    }

    public Team getAdditionalTeam() {
        return additionalTeam;
    }

    public void setAdditionalTeam(Team additionalTeam) {
        this.additionalTeam = additionalTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamRestriction teamRestriction = (TeamRestriction) o;
        return Objects.equals(team, teamRestriction.team) &&
                Objects.equals(restrictionSet, teamRestriction.restrictionSet) &&
                Objects.equals(additionalTeam, teamRestriction.additionalTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, restrictionSet, additionalTeam);
    }

}
