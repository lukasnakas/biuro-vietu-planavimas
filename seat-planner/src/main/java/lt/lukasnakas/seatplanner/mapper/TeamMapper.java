package lt.lukasnakas.seatplanner.mapper;

import lt.lukasnakas.seatplanner.model.Team;
import org.springframework.stereotype.Service;

@Service
public class TeamMapper {

    public Team buildCurrentTeamCopy(Team currentTeam) {
        Team team = new Team();
        team.setId(currentTeam.getId());
        team.setName(currentTeam.getName());
        team.setRoom(currentTeam.getRoom());
        team.setSize(currentTeam.getSize());
        team.setMembers(currentTeam.getMembers());
        team.setSplit(currentTeam.isSplit());
        return team;
    }

}
