package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.Room;
import lt.lukasnakas.seatplanner.model.request.AddRoomRequest;
import lt.lukasnakas.seatplanner.model.request.EditRoomRequest;
import lt.lukasnakas.seatplanner.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/planner/rooms")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Room>> getRoomsByFloorId(@RequestParam String floorId, @RequestParam String officeId, @RequestParam String companyId) {
        return ResponseEntity.ok(roomService.findRoomsByFloor(companyId, officeId, floorId));
    }

    @GetMapping(value = "/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Room> getRoom(@RequestParam String officeId, @PathVariable String roomId, @RequestParam String companyId, @RequestParam String floorId) {
        return ResponseEntity.ok(roomService.findRoomById(companyId, officeId, floorId, roomId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Room> addRoom(@RequestBody AddRoomRequest addRoomRequest) {
        return ResponseEntity.ok(roomService.addRoom(addRoomRequest));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Room> editRoom(@RequestBody EditRoomRequest editRoomRequest) {
        return ResponseEntity.ok(roomService.editRoom(editRoomRequest));
    }

    @DeleteMapping(value = "/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeRoom(@PathVariable String roomId, @RequestParam String companyId, @RequestParam String officeId, @RequestParam String floorId) {
        roomService.removeRoom(companyId, officeId, floorId, roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
