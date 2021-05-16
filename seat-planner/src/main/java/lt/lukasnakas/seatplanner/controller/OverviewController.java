package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.dto.OverviewDto;
import lt.lukasnakas.seatplanner.service.OverviewService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/planner/overview")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OverviewController {

    private final OverviewService overviewService;

    public OverviewController(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OverviewDto> getOverviewData(@RequestParam String companyId) {
        return ResponseEntity.ok(overviewService.getOverviewData(companyId));
    }

}
