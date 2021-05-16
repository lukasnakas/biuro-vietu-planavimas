package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.Company;
import lt.lukasnakas.seatplanner.model.request.AddCompanyRequest;
import lt.lukasnakas.seatplanner.model.request.EditCompanyRequest;
import lt.lukasnakas.seatplanner.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/planner/companies")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Company>> getCompanies() {
        return ResponseEntity.ok(companyService.findCompanies());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Company> getCompany(@PathVariable String id) {
        return ResponseEntity.ok(companyService.findCompanyById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Company> addCompany(@RequestBody AddCompanyRequest addCompanyRequest) {
        return ResponseEntity.ok(companyService.addCompany(addCompanyRequest));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Company> editCompany(@RequestBody EditCompanyRequest editCompanyRequest) {
        return ResponseEntity.ok(companyService.editCompany(editCompanyRequest));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeCompany(@PathVariable String id) {
        companyService.removeCompany(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
