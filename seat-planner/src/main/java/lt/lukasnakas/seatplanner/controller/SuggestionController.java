package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.Suggestion;
import lt.lukasnakas.seatplanner.service.SuggestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@RequestMapping("/v1/planner/suggestions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SuggestionController {

    private final SuggestionService suggestionService;

    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Suggestion>> getSuggestions(@RequestParam String companyId) {
        return ok(suggestionService.getConfirmedSuggestions(companyId));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Suggestion> getSuggestion(@PathVariable String id, @RequestParam String companyId) {
        return ok(suggestionService.getSuggestionById(companyId, id));
    }

    @PostMapping(value = "/{id}/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Suggestion> confirmSuggestion(@PathVariable String id, @RequestParam String companyId, @RequestParam String correlationId) {
        Suggestion suggestion = suggestionService.confirmSuggestion(companyId, correlationId, id);
        return new ResponseEntity<>(suggestion, HttpStatus.ACCEPTED);
    }

}
