package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.Member;
import lt.lukasnakas.seatplanner.model.request.AddMemberRequest;
import lt.lukasnakas.seatplanner.model.request.EditMemberRequest;
import lt.lukasnakas.seatplanner.model.request.RegisterUserRequest;
import lt.lukasnakas.seatplanner.model.response.PaginationMemberListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        return teamService.findRoomTeamByName(companyId, "no_team").getMembers().stream().sorted().collect(Collectors.toList());
    }

    public PaginationMemberListResponse findAllMembers(String companyId, int pageNo, int pageSize, String sortBy, String sortOrder) {
        List<Member> allMembers = new ArrayList<>();
        teamService.findCompanyById(companyId).getOfficeList().forEach(
                overviewOffice -> overviewOffice.getOverviewFloorList().forEach(
                        overviewFloor -> overviewFloor.getRoomList().forEach(
                                room -> room.getTeams().forEach(
                                        team -> {
                                            List<Member> membersWithTeamName = new ArrayList<>();
                                            team.getMembers().forEach(member -> {
                                                member.setTeamName(team.getName());
                                                membersWithTeamName.add(member);
                                            });
                                            allMembers.addAll(membersWithTeamName);
                                        }
                                )
                        )
                ));
        switch (sortBy) {
            case "firstName":
                allMembers.sort(Comparator.comparing(Member::getFirstName));
                break;
            case "lastName":
                allMembers.sort(Comparator.comparing(Member::getLastName));
                break;
            case "email":
                allMembers.sort(Comparator.comparing(Member::getEmail));
                break;
            case "teamName":
                allMembers.sort(Comparator.comparing(Member::getTeamName));
                break;
            case "experience":
                allMembers.sort(Comparator.comparing(Member::getExperience));
                break;
            case "stack":
                allMembers.sort(Comparator.comparing(Member::getStack));
                break;
            default:
                allMembers.sort(Comparator.comparing(Member::getFirstName));
                break;
        }

        if (sortOrder.equals("DESC")) {
            Collections.reverse(allMembers);
        }

        pageNo--;
        Sort sort = sortOrder.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), allMembers.size());
        Page<Member> page = new PageImpl<>(allMembers.subList(start, end), paging, allMembers.size());
        CONSOLE_LOGGER.info("Fetching employees with paging options: sort: " + sort.toString());
        return new PaginationMemberListResponse(page.getTotalElements(), page.getContent());
    }

    public List<Member> findAllMembers(String companyId) {
        List<Member> allMembers = new ArrayList<>();
        teamService.findCompanyById(companyId).getOfficeList().forEach(
                overviewOffice -> overviewOffice.getOverviewFloorList().forEach(
                        overviewFloor -> overviewFloor.getRoomList().forEach(
                                room -> room.getTeams().forEach(
                                        team -> {
                                            List<Member> membersWithTeamName = new ArrayList<>();
                                            team.getMembers().forEach(member -> {
                                                member.setTeamName(team.getName());
                                                membersWithTeamName.add(member);
                                            });
                                            allMembers.addAll(membersWithTeamName);
                                        }
                                )
                        )
                ));
        return allMembers.stream().sorted().collect(Collectors.toList());
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
