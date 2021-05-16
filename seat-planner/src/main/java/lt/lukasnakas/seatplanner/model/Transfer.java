package lt.lukasnakas.seatplanner.model;

import java.util.Objects;

public class Transfer implements Comparable<Transfer> {

    private String teamName;
    private String moveTo;
    private int peopleTransferredAmount;

    public Transfer(String teamName, String moveTo, int peopleTransferredAmount) {
        this.teamName = teamName;
        this.moveTo = moveTo;
        this.peopleTransferredAmount = peopleTransferredAmount;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(String moveTo) {
        this.moveTo = moveTo;
    }

    public int getPeopleTransferredAmount() {
        return peopleTransferredAmount;
    }

    public void setPeopleTransferredAmount(int peopleTransferredAmount) {
        this.peopleTransferredAmount = peopleTransferredAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transfer transfer = (Transfer) o;
        return peopleTransferredAmount == transfer.peopleTransferredAmount && Objects
                .equals(teamName, transfer.teamName) && Objects.equals(moveTo, transfer.moveTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamName, moveTo, peopleTransferredAmount);
    }

    @Override
    public int compareTo(Transfer o) {
        return this.getTeamName().compareTo(o.getTeamName());
    }
}