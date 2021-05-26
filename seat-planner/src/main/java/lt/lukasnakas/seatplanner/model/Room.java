package lt.lukasnakas.seatplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Room implements Comparable<Room> {

    @PlanningId
    private String id;
    private Location location;
    private int maxCapacity;
    private int currentCapacity;
    @JsonIgnoreProperties({"split","room"})
    private List<Team> teams;

    public Room(Location location, int maxCapacity, int currentCapacity,
                List<Team> teams) {
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.teams = teams;
    }

    public Room() {
        this.id = UUID.randomUUID().toString();
        this.teams = new ArrayList<>();
    }

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

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return maxCapacity == room.maxCapacity &&
                currentCapacity == room.currentCapacity &&
                Objects.equals(id, room.id) &&
                Objects.equals(location, room.location) &&
                Objects.equals(teams, room.teams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, maxCapacity, currentCapacity, teams);
    }

    @Override
    public int compareTo(Room o) {
        return this.getLocation().getRoomNumb().compareTo(o.getLocation().getRoomNumb());
    }
}
