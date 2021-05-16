package lt.lukasnakas.seatplanner.helper;

import lt.lukasnakas.seatplanner.model.request.FindSeatRequest;
import lt.lukasnakas.seatplanner.model.response.FindSeatResponse;

import java.util.Map;

public class FindSeatRequestResponseDataHelper {
    public static Map<String, FindSeatResponse> getStringFindSeatResponseMap(FindSeatResponse findSeatResponse) {
        return Map.of("1", findSeatResponse);
    }

    public static FindSeatResponse getFindSeatResponse() {
        FindSeatResponse findSeatResponse = new FindSeatResponse();
        findSeatResponse.setCompleted(true);
        findSeatResponse.setSplit(false);
        findSeatResponse.setSuggestions(SuggestionDataHelper.getSuggestions());
        findSeatResponse.setStatus("Test status");
        return findSeatResponse;
    }

    public static FindSeatResponse getNonCompletedFindSeatResponse() {
        FindSeatResponse findSeatResponse = getFindSeatResponse();
        findSeatResponse.setCompleted(false);
        return findSeatResponse;
    }

    public static FindSeatRequest getFindSeatRequest() {
        FindSeatRequest findSeatRequest = new FindSeatRequest();
        findSeatRequest.setSplittable(false);
        findSeatRequest.setLocation("Office");
        findSeatRequest.setTeamName("TeamA");
        findSeatRequest.setMaxTeamsInvolved(3);
        findSeatRequest.setPeopleAmount(5);
        return findSeatRequest;
    }
}
