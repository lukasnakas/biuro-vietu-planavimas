package lt.lukasnakas.seatplanner.helper;

import lt.lukasnakas.seatplanner.model.Member;
import lt.lukasnakas.seatplanner.model.Team;

import java.util.Collections;
import java.util.UUID;

import static lt.lukasnakas.seatplanner.helper.RoomDataHelper.getRoom;

public class TeamDataHelper {

    public static Team getTeam() {
        Team team = new Team();
        team.setId(UUID.randomUUID().toString());
        team.setName("TeamA");
        team.setRoom(getRoom());
        team.setMembers(Collections.singletonList(new Member("Lukas", "Nakas")));
        team.setSplit(false);
        return team;
    }

}
