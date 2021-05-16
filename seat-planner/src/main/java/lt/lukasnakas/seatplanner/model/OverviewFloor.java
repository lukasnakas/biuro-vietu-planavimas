package lt.lukasnakas.seatplanner.model;

import org.bson.codecs.pojo.annotations.BsonId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OverviewFloor {

    @BsonId
    private String id;
    private String floorName;
    private List<Room> roomList;

    public OverviewFloor(String floorName,
                         List<Room> roomList) {
        this.floorName = floorName;
        this.roomList = roomList;
        this.id = UUID.randomUUID().toString();
    }

    public OverviewFloor(String floorName) {
        this.id = UUID.randomUUID().toString();
        this.floorName = floorName;
        this.roomList = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    public OverviewFloor() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OverviewFloor that = (OverviewFloor) o;
        return id.equals(that.id) && floorName.equals(that.floorName) && Objects
                .equals(roomList, that.roomList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, floorName, roomList);
    }
}
