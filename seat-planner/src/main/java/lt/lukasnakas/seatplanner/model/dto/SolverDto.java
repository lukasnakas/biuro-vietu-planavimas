package lt.lukasnakas.seatplanner.model.dto;

import lt.lukasnakas.seatplanner.model.request.FindSeatRequest;
import lt.lukasnakas.seatplanner.model.Team;

import java.util.Set;

public class SolverDto {

    private FindSeatRequest findSeatRequest;
    private Set<Team> teams;
    private Team currentTeam;
    private Team originalTeam;

    public SolverDto(FindSeatRequest findSeatRequest, Set<Team> teams, Team currentTeam) {
        this.findSeatRequest = findSeatRequest;
        this.teams = teams;
        this.currentTeam = currentTeam;
    }

    public SolverDto(FindSeatRequest findSeatRequest, Team currentTeam, Team originalTeam) {
        this.findSeatRequest = findSeatRequest;
        this.currentTeam = currentTeam;
        this.originalTeam = originalTeam;
    }

    public SolverDto() {
    }

    public Team getOriginalTeam() {
        return originalTeam;
    }

    public void setOriginalTeam(Team originalTeam) {
        this.originalTeam = originalTeam;
    }

    public FindSeatRequest getFindSeatRequest() {
        return findSeatRequest;
    }

    public void setFindSeatRequest(FindSeatRequest findSeatRequest) {
        this.findSeatRequest = findSeatRequest;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(Team currentTeam) {
        this.currentTeam = currentTeam;
    }

}
