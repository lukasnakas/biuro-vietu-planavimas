package lt.lukasnakas.seatplanner.model.dto;

import lt.lukasnakas.seatplanner.model.enumerators.HardSoftScore;

public class ConstraintDto {

    private String id;
    private String name;
    private boolean isActive;
    private HardSoftScore scoreType;
    private Integer score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public HardSoftScore getScoreType() {
        return scoreType;
    }

    public void setScoreType(HardSoftScore scoreType) {
        this.scoreType = scoreType;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
