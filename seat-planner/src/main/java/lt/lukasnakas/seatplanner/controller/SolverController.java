package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.request.FindSeatRequest;
import lt.lukasnakas.seatplanner.model.response.FindSeatResponse;
import lt.lukasnakas.seatplanner.model.response.SearchResponse;
import lt.lukasnakas.seatplanner.service.jms.ConsumerService;
import lt.lukasnakas.seatplanner.service.jms.ProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@RequestMapping("/v1/planner")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SolverController {

    private final ProducerService producerService;
    private final ConsumerService consumerService;

    public SolverController(ProducerService producerService, ConsumerService consumerService) {
        this.producerService = producerService;
        this.consumerService = consumerService;
    }

    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchResponse> submit(@Valid @RequestBody FindSeatRequest findSeatRequest) {
        SearchResponse searchResponse = producerService.submitToSolver(findSeatRequest);
        return new ResponseEntity<>(searchResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/results/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FindSeatResponse> getResults(@PathVariable String id, @RequestParam(defaultValue = "5") int limit) {
        FindSeatResponse findSeatResponse = consumerService.getFindSeatResponse(id, limit);
        return ok(findSeatResponse);
    }

}
