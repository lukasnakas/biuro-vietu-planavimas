package lt.lukasnakas.seatplanner.model.planning;

import lt.lukasnakas.seatplanner.model.Room;
import lt.lukasnakas.seatplanner.model.Team;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class RoomAssignment {

    private List<Team> teams;
    private List<Room> rooms;

    private HardSoftScore score;

    public RoomAssignment(List<Team> teams, List<Room> rooms) {
        this.teams = teams;
        this.rooms = rooms;
    }

    public RoomAssignment() {
    }

    @PlanningEntityCollectionProperty
    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @ValueRangeProvider(id = "roomRange")
    @ProblemFactCollectionProperty
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    public static RoomAssignment buildEmptyHardScoredRoomAssignment() {
        RoomAssignment roomAssignment = new RoomAssignment();
        roomAssignment.setScore(HardSoftScore.ofHard(1));
        return roomAssignment;
    }

}
