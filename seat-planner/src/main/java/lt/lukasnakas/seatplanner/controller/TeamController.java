package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.Team;
import lt.lukasnakas.seatplanner.model.request.AddTeamRequest;
import lt.lukasnakas.seatplanner.model.request.EditTeamRequest;
import lt.lukasnakas.seatplanner.service.TeamRestrictionService;
import lt.lukasnakas.seatplanner.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/planner/teams")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TeamController {

    private final TeamService teamService;
    private final TeamRestrictionService teamRestrictionService;

    public TeamController(TeamService teamService, TeamRestrictionService teamRestrictionService) {
        this.teamService = teamService;
        this.teamRestrictionService = teamRestrictionService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Team>> getTeamsByFloorId(@RequestParam String roomId, @RequestParam String floorId, @RequestParam String officeId, @RequestParam String companyId) {
        return ResponseEntity.ok(teamService.findTeamsByRoom(companyId, officeId, floorId, roomId));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Team>> getAllTeamNames(@RequestParam String companyId) {
        return ResponseEntity.ok(teamService.findAllTeamsByCompany(companyId));
    }

    @GetMapping(value = "/{teamId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Team> getTeam(@RequestParam String officeId, @PathVariable String teamId, @RequestParam String companyId, @RequestParam String floorId, @RequestParam String roomId) {
        return ResponseEntity.ok(teamService.findRoomTeamById(companyId, officeId, floorId, roomId, teamId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Team> addTeam(@RequestBody AddTeamRequest addTeamRequest) {
        Team team = teamService.addTeam(addTeamRequest);
        return ResponseEntity.ok(team);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Team> editTeam(@RequestBody EditTeamRequest editTeamRequest) {
        Team team = teamService.editTeam(editTeamRequest);
        return ResponseEntity.ok(team);
    }

    @DeleteMapping(value = "/{teamId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeTeam(@PathVariable String teamId, @RequestParam String companyId, @RequestParam String officeId, @RequestParam String floorId, @RequestParam String roomId) {
        teamService.removeTeam(companyId, officeId, floorId, roomId, teamId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
