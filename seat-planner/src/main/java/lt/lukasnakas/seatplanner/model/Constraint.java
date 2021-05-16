package lt.lukasnakas.seatplanner.model;


import lt.lukasnakas.seatplanner.model.enumerators.HardSoftScore;

public class Constraint {

    private String id;
    private String name;
    private boolean isActive;
    private HardSoftScore scoreType;
    private int score;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", scoreType=" + scoreType +
                ", score=" + score +
                '}';
    }
}
