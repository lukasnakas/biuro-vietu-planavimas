package lt.lukasnakas.seatplanner.helper;

import lt.lukasnakas.seatplanner.model.enumerators.HardSoftScore;
import lt.lukasnakas.seatplanner.model.request.ConstraintAlterationRequest;

public class ConstraintDataHelper {

    public static final String DO_NOT_MOVE = "Do not move";

    public static ConstraintAlterationRequest getValidConstraintAlterationRequest() {
        ConstraintAlterationRequest request = new ConstraintAlterationRequest();
        request.setConstraintId("1");
        request.setActive(true);
        request.setScore(1);
        request.setScoreType(HardSoftScore.HARD);
        return request;
    }

}
