package lt.lukasnakas.seatplanner.model.request;


import lt.lukasnakas.seatplanner.model.enumerators.HardSoftScore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ConstraintAlterationRequest {

    @NotBlank(message = "Constraint id must be specified")
    private String constraintId;

    @NotNull(message = "The new constraint status must be specified")
    private boolean active;

    @NotNull(message = "The score type cannot be null")
    private HardSoftScore scoreType;

    @Positive(message = "Constraint penalty score must be a positive")
    private int score;

    public String getConstraintId() {
        return constraintId;
    }

    public void setConstraintId(String constraintId) {
        this.constraintId = constraintId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public HardSoftScore getScoreType() {
        return scoreType;
    }

    public void setScoreType(HardSoftScore scoreType) {
        this.scoreType = scoreType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}