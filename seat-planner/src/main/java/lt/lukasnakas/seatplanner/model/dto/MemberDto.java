package lt.lukasnakas.seatplanner.model.dto;

import java.util.Objects;

public class MemberDto {

    private String id;
    private String firstName;
    private String lastName;
    private String office;
    private String floor;
    private String roomNo;
    private String teamName;
    private int totalSeats;
    private int occupiedSeats;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getOccupiedSeats() {
        return occupiedSeats;
    }

    public void setOccupiedSeats(int occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto memberDto = (MemberDto) o;
        return totalSeats == memberDto.totalSeats &&
                occupiedSeats == memberDto.occupiedSeats &&
                Objects.equals(id, memberDto.id) &&
                Objects.equals(firstName, memberDto.firstName) &&
                Objects.equals(lastName, memberDto.lastName) &&
                Objects.equals(office, memberDto.office) &&
                Objects.equals(floor, memberDto.floor) &&
                Objects.equals(roomNo, memberDto.roomNo) &&
                Objects.equals(teamName, memberDto.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, office, floor, roomNo, teamName, totalSeats, occupiedSeats);
    }

}
