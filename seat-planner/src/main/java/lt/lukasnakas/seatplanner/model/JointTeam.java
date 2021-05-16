package lt.lukasnakas.seatplanner.model;

import java.util.List;
import java.util.Objects;

public class JointTeam extends Team {

    private Team teamA;
    private Team teamB;

    public JointTeam() {
    }

    public JointTeam(int size, Room room, List<Member> members, String name, String conditions, String stack, Team teamA, Team teamB) {
        super(size, room, members, name, stack, false, conditions);
        this.teamA = teamA;
        this.teamB = teamB;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JointTeam)) {
            return false;
        }
        JointTeam team = (JointTeam) o;

        return Objects.equals(this.getTeamA(), team.getTeamA()) &&
                Objects.equals(this.getTeamB(), team.getTeamB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamA, teamB);
    }

}
