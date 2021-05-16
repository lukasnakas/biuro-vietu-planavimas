package lt.lukasnakas.seatplanner.model;

import lt.lukasnakas.seatplanner.model.enumerators.State;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Suggestion {

    private String id;
    private int priority;
    private int teamsInvolved;
    private State state;
    private List<Transfer> transfers;
    private List<String> violatedConstraints;

    public Suggestion(int priority, int teamsInvolved, List<Transfer> transfers, List<String> violatedConstraints) {
        this.id = UUID.randomUUID().toString();
        this.priority = priority;
        this.teamsInvolved = teamsInvolved;
        this.state = State.PENDING;
        this.transfers = transfers;
        this.violatedConstraints = violatedConstraints;
    }

    public Suggestion() {
        this.id = UUID.randomUUID().toString();
        this.state = State.PENDING;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getViolatedConstraints() {
        return violatedConstraints;
    }

    public void setViolatedConstraints(List<String> violatedConstraints) {
        this.violatedConstraints = violatedConstraints;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTeamsInvolved() {
        return teamsInvolved;
    }

    public void setTeamsInvolved(int teamsInvolved) {
        this.teamsInvolved = teamsInvolved;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Suggestion that = (Suggestion) o;
        return priority == that.priority && teamsInvolved == that.teamsInvolved && Objects
                .equals(id, that.id) && Objects.equals(state, that.state) && Objects
                .equals(transfers, that.transfers) && Objects
                .equals(violatedConstraints, that.violatedConstraints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, priority, teamsInvolved, state, transfers, violatedConstraints);
    }
}
