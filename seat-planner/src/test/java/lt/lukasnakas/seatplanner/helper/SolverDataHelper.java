package lt.lukasnakas.seatplanner.helper;

import lt.lukasnakas.seatplanner.model.Suggestion;
import lt.lukasnakas.seatplanner.model.Team;
import lt.lukasnakas.seatplanner.model.dto.SolverDto;
import lt.lukasnakas.seatplanner.model.request.FindSeatRequest;
import lt.lukasnakas.seatplanner.model.response.FindSeatResponse;

import java.util.List;
import java.util.Set;

public class SolverDataHelper {

    public static SolverDto getSolverDto() {
        Team team = TeamDataHelper.getTeam();
        FindSeatRequest findSeatRequest = getFindSeatRequest(team);
        SolverDto solverDto = new SolverDto();
        solverDto.setTeams(Set.of(team));
        solverDto.setCurrentTeam(team);
        solverDto.setOriginalTeam(team);
        solverDto.setFindSeatRequest(findSeatRequest);
        return solverDto;
    }

    public static FindSeatResponse getFindSeatResponse(List<Suggestion> suggestions) {
        FindSeatResponse findSeatResponse = new FindSeatResponse();
        findSeatResponse.setSplit(false);
        findSeatResponse.setCompleted(true);
        findSeatResponse.setStatus("100.0%");
        findSeatResponse.setSuggestions(suggestions);
        return findSeatResponse;
    }

    private static FindSeatRequest getFindSeatRequest(Team team) {
        FindSeatRequest findSeatRequest = new FindSeatRequest();
        findSeatRequest.setSplittable(false);
        findSeatRequest.setPeopleAmount(2);
        findSeatRequest.setMaxTeamsInvolved(3);
        findSeatRequest.setTeamName(team.getName());
        findSeatRequest.setLocation("Office");
        return findSeatRequest;
    }

}
