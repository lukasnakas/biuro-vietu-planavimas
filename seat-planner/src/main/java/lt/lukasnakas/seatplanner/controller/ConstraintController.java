package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.request.ConstraintAlterationRequest;
import lt.lukasnakas.seatplanner.model.dto.ConstraintDto;
import lt.lukasnakas.seatplanner.service.ConstraintService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/planner/constraints")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConstraintController {

    private final ConstraintService constraintService;

    public ConstraintController(ConstraintService constraintService) {
        this.constraintService = constraintService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConstraintDto>> fetchConstraints() {
        return ok(constraintService.getAllConstraintDtos());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConstraintDto> fetchConstraint(@PathVariable String id) {
        return ok(constraintService.getConstraintDtoById(id));
    }

    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConstraintDto> updateConstraint(
            @Valid @RequestBody ConstraintAlterationRequest constraintAlterationRequest) {
        return ok(constraintService.updateConstraint(constraintAlterationRequest));
    }
}
