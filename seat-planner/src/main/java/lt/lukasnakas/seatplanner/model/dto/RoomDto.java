package lt.lukasnakas.seatplanner.model.dto;

import lt.lukasnakas.seatplanner.model.Location;

import java.util.List;

public class RoomDto {

    private String id;
    private Location location;
    private List<TeamDto> teamDtos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<TeamDto> getTeamDtos() {
        return teamDtos;
    }

    public void setTeamDtos(List<TeamDto> teamDtos) {
        this.teamDtos = teamDtos;
    }

}
