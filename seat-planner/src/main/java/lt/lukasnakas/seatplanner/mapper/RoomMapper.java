package lt.lukasnakas.seatplanner.mapper;

import lt.lukasnakas.seatplanner.model.Room;
import org.springframework.stereotype.Service;

@Service
public class RoomMapper {

    public Room buildRoomWithoutTeams(Room room) {
        Room roomForLocation = new Room();
        roomForLocation.setId(room.getId());
        roomForLocation.setLocation(room.getLocation());
        roomForLocation.setCurrentCapacity(room.getCurrentCapacity());
        roomForLocation.setMaxCapacity(room.getMaxCapacity());
        return roomForLocation;
    }

}
