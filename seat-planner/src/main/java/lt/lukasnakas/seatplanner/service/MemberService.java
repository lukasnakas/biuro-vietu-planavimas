package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.Member;
import lt.lukasnakas.seatplanner.model.request.AddMemberRequest;
import lt.lukasnakas.seatplanner.model.request.EditMemberRequest;
import lt.lukasnakas.seatplanner.model.request.RegisterUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(MemberService.class);
    private final TeamService teamService;
    private final AuthenticationService authenticationService;

    public MemberService(TeamService teamService, AuthenticationService authenticationService) {
        this.teamService = teamService;
        this.authenticationService = authenticationService;
    }

    public List<Member> findTeamlessMembers(String companyId) {
        return teamService.findRoomTeamByName(companyId, "no_team").getMembers();
    }

    public List<Member> findAllMembers(String companyId) {
        List<Member> allMembers = new ArrayList<>();
        teamService.findCompanyById(companyId).getOfficeList().forEach(
                overviewOffice -> overviewOffice.getOverviewFloorList().forEach(
                        overviewFloor -> overviewFloor.getRoomList().forEach(
                                room -> room.getTeams().forEach(
                                        team -> allMembers.addAll(team.getMembers())
                                )
                        )
                ));
        return allMembers;
    }

    public List<Member> findMembersByTeam(String companyId, String officeId, String floorId, String roomId, String teamId) {
        CONSOLE_LOGGER.info("Fetching members in team ID: " + teamId);
        return teamService.findRoomTeamById(companyId, officeId, floorId, roomId, teamId).getMembers();
    }

    public Member findMemberById(String companyId, String officeId, String floorId, String roomId, String teamId, String memberId) {
        CONSOLE_LOGGER.info("Fetching member with ID: " + memberId);
        return teamService.findRoomTeamById(companyId, officeId, floorId, roomId, teamId).getMembers()
                .stream()
                .filter(member -> member.getId().equals(memberId))
                .findFirst()
                .orElseThrow();
    }

    public Member findMemberById(String companyId, String memberId) {
        CONSOLE_LOGGER.info("Fetching member with ID: " + memberId);
        return findAllMembers(companyId).stream()
                .filter(member -> member.getId().equals(memberId))
                .findFirst().orElseThrow();
    }

    public Member addMember(AddMemberRequest addMemberRequest) {
        Member member = new Member();
        member.setFirstName(addMemberRequest.getFirstName());
        member.setLastName(addMemberRequest.getLastName());
        member.setEmail(addMemberRequest.getEmail());
        member.setExperience(addMemberRequest.getExperience());
        CONSOLE_LOGGER.info("Adding member with name: " + member.getFirstName() + " " + member.getLastName());
        teamService.addMember(addMemberRequest, member);
        registerMemberAccount(addMemberRequest.getCompanyId(), member);
        return member;
    }

    public Member editMember(EditMemberRequest editMemberRequest) {
        Member member = findMemberById(editMemberRequest.getCompanyId(), editMemberRequest.getId());
        CONSOLE_LOGGER.info("Editing member with name: " + member.getFirstName() + " " + member.getLastName());
        return teamService.editMember(member, editMemberRequest);
    }

    public void removeMember(String companyId, String officeId, String floorId, String roomId, String teamId, String memberId) {
        Member member = findMemberById(companyId, officeId, floorId, roomId, teamId, memberId);
        CONSOLE_LOGGER.info("Removing member with name: " + member.getFirstName() + " " + member.getLastName());
        teamService.removeMember(companyId, officeId, floorId, roomId, member);
    }

    public void removeMember(String companyId, String memberId) {
        Member member = findMemberById(companyId, memberId);
        CONSOLE_LOGGER.info("Removing member with name: " + member.getFirstName() + " " + member.getLastName());
        teamService.removeMember(companyId, member);
    }

    private void registerMemberAccount(String companyId, Member member) {
        String password = member.getFirstName() + member.getLastName() + member.getMemberCode();
        RegisterUserRequest registerUserRequest = authenticationService.buildRegisterUserRequest(member.getEmail(), password, companyId);
        authenticationService.registerUser(registerUserRequest);
    }

}
