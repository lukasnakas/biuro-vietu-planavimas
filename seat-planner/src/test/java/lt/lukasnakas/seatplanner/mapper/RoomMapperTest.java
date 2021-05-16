package lt.lukasnakas.seatplanner.mapper;

import lt.lukasnakas.seatplanner.helper.RoomDataHelper;
import lt.lukasnakas.seatplanner.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomMapperTest {

    private RoomMapper roomMapper;

    @BeforeEach
    void setUp() {
        roomMapper = new RoomMapper();
    }

    @Test
    void shouldBuildRoomWithoutTeams() {
        // given
        Room room = RoomDataHelper.getRoom();

        // when
        Room withoutTeams = roomMapper.buildRoomWithoutTeams(room);

        // then
        assertEquals(0, withoutTeams.getTeams().size());
        assertEquals(withoutTeams.getLocation(), room.getLocation());
        assertEquals(withoutTeams.getId(), room.getId());
        assertEquals(withoutTeams.getCurrentCapacity(), room.getCurrentCapacity());
        assertEquals(withoutTeams.getMaxCapacity(), room.getMaxCapacity());
    }
}
