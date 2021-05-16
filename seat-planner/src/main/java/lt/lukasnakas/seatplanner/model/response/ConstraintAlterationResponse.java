package lt.lukasnakas.seatplanner.model.response;

public class ConstraintAlterationResponse {

    private String constraintId;
    private boolean oldState;
    private boolean newState;

    public ConstraintAlterationResponse(String constraintId, boolean oldState, boolean newState) {
        this.constraintId = constraintId;
        this.oldState = oldState;
        this.newState = newState;
    }

    public ConstraintAlterationResponse() {
    }

    public String getConstraintId() {
        return constraintId;
    }

    public void setConstraintId(String constraintId) {
        this.constraintId = constraintId;
    }

    public boolean getOldState() {
        return oldState;
    }

    public void setOldState(boolean oldState) {
        this.oldState = oldState;
    }

    public boolean getNewState() {
        return newState;
    }

    public void setNewState(boolean newState) {
        this.newState = newState;
    }

}
