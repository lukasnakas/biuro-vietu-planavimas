package lt.lukasnakas.seatplanner.helper;

import lt.lukasnakas.seatplanner.model.Room;

import java.util.ArrayList;
import java.util.UUID;

public class RoomDataHelper {

    public static Room getRoom() {
        Room room = new Room();
        room.setId(UUID.randomUUID().toString());
        room.setMaxCapacity(14);
        room.setCurrentCapacity(5);
        room.setLocation(LocationDataHelper.getValidLocation());
        room.setTeams(new ArrayList<>());
        return room;
    }

}
