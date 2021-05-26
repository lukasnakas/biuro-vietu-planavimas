package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.Member;
import lt.lukasnakas.seatplanner.model.request.AddMemberRequest;
import lt.lukasnakas.seatplanner.model.request.EditMemberRequest;
import lt.lukasnakas.seatplanner.model.response.PaginationMemberListResponse;
import lt.lukasnakas.seatplanner.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/planner/members")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = "/teamless", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Member>> getTeamlessMembers(@RequestParam String companyId) {
        return ResponseEntity.ok(memberService.findTeamlessMembers(companyId));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationMemberListResponse> getAllMembers(@RequestParam String companyId, @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "10") int pageSize,
                                                                      @RequestParam(defaultValue = "lastName") String sortBy,
                                                                      @RequestParam(defaultValue = "ASC") String sortOrder) {
        return ResponseEntity.ok(memberService.findAllMembers(companyId, page, pageSize, sortBy, sortOrder));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Member>> getMembersByTeamId(@RequestParam String roomId,
                                                           @RequestParam String floorId,
                                                           @RequestParam String officeId,
                                                           @RequestParam String companyId,
                                                           @RequestParam String teamId) {
        return ResponseEntity.ok(memberService.findMembersByTeam(companyId, officeId, floorId, roomId, teamId));
    }

    @GetMapping(value = "/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Member> getMember(@RequestParam String officeId,
                                            @PathVariable String memberId,
                                            @RequestParam String companyId,
                                            @RequestParam String floorId,
                                            @RequestParam String roomId,
                                            @RequestParam String teamId) {
        return ResponseEntity.ok(memberService.findMemberById(companyId, officeId, floorId, roomId, teamId, memberId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Member> addMember(@RequestBody AddMemberRequest addMemberRequest) {
        return ResponseEntity.ok(memberService.addMember(addMemberRequest));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Member> editMember(@RequestBody EditMemberRequest editMemberRequest) {
        return ResponseEntity.ok(memberService.editMember(editMemberRequest));
    }

    @DeleteMapping(value = "/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeMember(@PathVariable String memberId,
                                             @RequestParam String companyId) {
        memberService.removeMember(companyId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
