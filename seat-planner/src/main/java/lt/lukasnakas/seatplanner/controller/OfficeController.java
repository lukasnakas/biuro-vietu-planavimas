package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.OverviewOffice;
import lt.lukasnakas.seatplanner.model.request.AddOfficeRequest;
import lt.lukasnakas.seatplanner.model.request.EditOfficeRequest;
import lt.lukasnakas.seatplanner.service.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/planner/offices")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OverviewOffice>> getOffices(@RequestParam String companyId) {
        return ResponseEntity.ok(officeService.findOffices(companyId));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OverviewOffice> getOffice(@PathVariable String id, @RequestParam String companyId) {
        return ResponseEntity.ok(officeService.findOfficeById(companyId, id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OverviewOffice> addOffice(@RequestBody AddOfficeRequest addOfficeRequest) {
        return ResponseEntity.ok(officeService.addOffice(addOfficeRequest));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OverviewOffice> editOffice(@RequestBody EditOfficeRequest editOfficeRequest) {
        return ResponseEntity.ok(officeService.editOffice(editOfficeRequest));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeOffice(@PathVariable String id, @RequestParam String companyId) {
        officeService.removeOffice(companyId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
