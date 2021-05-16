package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.request.RestrictionRequest;
import lt.lukasnakas.seatplanner.model.response.RestrictionResponse;
import lt.lukasnakas.seatplanner.model.enumerators.Restriction;
import lt.lukasnakas.seatplanner.service.TeamRestrictionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/planner/restrictions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestrictionController {

    private final TeamRestrictionService teamRestrictionService;

    public RestrictionController(TeamRestrictionService teamRestrictionService) {
        this.teamRestrictionService = teamRestrictionService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Restriction>> getAllRestrictions() {
        return ok(teamRestrictionService.getAllRestrictionsValues());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestrictionResponse> addRestriction(@Valid @RequestBody RestrictionRequest restrictionRequest) {
        RestrictionResponse restrictionResponse = teamRestrictionService.addRestriction(restrictionRequest);
        return new ResponseEntity<>(restrictionResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestrictionResponse> removeRestriction(@Valid @RequestBody RestrictionRequest restrictionRequest) {
        return ok(teamRestrictionService.removeRestriction(restrictionRequest));
    }

}
