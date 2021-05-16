package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.OverviewFloor;
import lt.lukasnakas.seatplanner.model.request.AddFloorRequest;
import lt.lukasnakas.seatplanner.model.request.EditFloorRequest;
import lt.lukasnakas.seatplanner.service.FloorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/planner/floors")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FloorController {

    private final FloorService floorService;

    public FloorController(FloorService floorService) {
        this.floorService = floorService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OverviewFloor>> getFloorsByOfficeId(@RequestParam String officeId, @RequestParam String companyId) {
        return ResponseEntity.ok(floorService.findFloorsByOffice(companyId, officeId));
    }

    @GetMapping(value = "/{floorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OverviewFloor> getFloor(@RequestParam String officeId, @PathVariable String floorId, @RequestParam String companyId) {
        return ResponseEntity.ok(floorService.findFloorById(companyId, officeId, floorId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OverviewFloor> addFloor(@RequestBody AddFloorRequest addFloorRequest) {
        return ResponseEntity.ok(floorService.addFloor(addFloorRequest));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OverviewFloor> editFloor(@RequestBody EditFloorRequest editFloorRequest) {
        return ResponseEntity.ok(floorService.editFloor(editFloorRequest));
    }

    @DeleteMapping(value = "/{floorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeFloor(@PathVariable String floorId, @RequestParam String companyId, @RequestParam String officeId) {
        floorService.removeFloor(companyId, officeId, floorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
