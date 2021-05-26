package lt.lukasnakas.seatplanner.service;

import com.google.common.collect.Sets;
import lt.lukasnakas.seatplanner.mapper.TeamMapper;
import lt.lukasnakas.seatplanner.model.*;
import lt.lukasnakas.seatplanner.model.exception.RoomMaxCapacityExceededException;
import lt.lukasnakas.seatplanner.model.exception.TeamNotFoundException;
import lt.lukasnakas.seatplanner.model.request.*;
import lt.lukasnakas.seatplanner.service.jms.ConsumerService;
import lt.lukasnakas.seatplanner.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static lt.lukasnakas.seatplanner.service.SolverService.TRANSFER_DELIMITER;

@Service
public class TeamService {

    private static final int MINIMUM_TEAM_SUBSET_SIZE = 1;
    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(TeamService.class);
    public static final String DELIMITER = "_";
    public static final String TEAM_JOIN_DELIMITER = "+";
    private final RoomService roomService;
    private final TeamMapper teamMapper;
    private int currentAttempt = 0;

    public TeamService(RoomService roomService, TeamMapper teamMapper) {
        this.roomService = roomService;
        this.teamMapper = teamMapper;
    }

    public Company findCompanyById(String id) {
        return roomService.findCompanyById(id);
    }

    public List<Team> findTeamsByRoom(String companyId, String officeId, String floorId, String roomId) {
        CONSOLE_LOGGER.info("Fetching teams in room ID: " + roomId);
        return roomService.findRoomById(companyId, officeId, floorId, roomId).getTeams().stream().sorted().collect(Collectors.toList());
    }

    public Team findRoomTeamById(String companyId, String officeId, String floorId, String roomId, String teamId) {
        CONSOLE_LOGGER.info("Fetching team with ID: " + teamId);
        return roomService.findRoomById(companyId, officeId, floorId, roomId).getTeams()
                .stream()
                .filter(team -> team.getId().equals(teamId))
                .findFirst()
                .orElseThrow();
    }

    public Team findRoomTeamByName(String companyId, String name) {
        CONSOLE_LOGGER.info("Fetching team with name: " + name);
        return roomService.findRoomByName(companyId, name).getTeams()
                .stream()
                .filter(team -> team.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    public Room findRoomByName(String companyId, String name) {
        CONSOLE_LOGGER.info("Fetching team with name: " + name);
        return roomService.findRoomByName(companyId, name);
    }

    public Team addTeam(AddTeamRequest addTeamRequest) {
        Team team = new Team();
        team.setName(addTeamRequest.getName());
        team.setStack(addTeamRequest.getStack());
        team.setConditions(addTeamRequest.getCondition());
        CONSOLE_LOGGER.info("Adding team with name: " + team.getName());
        roomService.addTeam(addTeamRequest, team);
        return team;
    }

    public Team editTeam(EditTeamRequest editTeamRequest) {
        Team team = findRoomTeamById(editTeamRequest.getCompanyId(), editTeamRequest.getOfficeId(), editTeamRequest.getFloorId(),
                editTeamRequest.getRoomId(), editTeamRequest.getId());
        CONSOLE_LOGGER.info("Editing team with name: " + team.getName());
        return roomService.editTeam(team, editTeamRequest);
    }

    public void removeTeam(String companyId, String officeId, String floorId, String roomId, String teamId) {
        Team team = findRoomTeamById(companyId, officeId, floorId, roomId, teamId);
        CONSOLE_LOGGER.info("Removing team with name: " + team.getName());
        roomService.removeTeam(companyId, officeId, floorId, team);
    }

    public List<Team> getAllTeams() {
        List<Team> allTeams = new ArrayList<>();
        List<Company> allCompanies = roomService.findAllCompanies();
        allCompanies.forEach(
                c -> c.getOfficeList().forEach(
                        o -> o.getOverviewFloorList().forEach(
                                floor -> floor.getRoomList().forEach(
                                        r -> allTeams.addAll(r.getTeams())
                                )
                        )
                )
        );
        return allTeams;
    }

    public List<Team> getAllTeamsByCompany(String companyId) {
        List<Team> allTeams = new ArrayList<>();
        roomService.findCompanyById(companyId).getOfficeList().forEach(
                o -> o.getOverviewFloorList().forEach(
                        floor -> floor.getRoomList().forEach(
                                r -> allTeams.addAll(r.getTeams())
                        )
                )
        );
        return allTeams;
    }

    public Room addMember(AddMemberRequest addMemberRequest, Member member) {
        Company company = roomService.findCompanyById(addMemberRequest.getCompanyId());
        OverviewOffice overviewOffice;
        OverviewFloor overviewFloor;
        Room room;
        Team team;

        if (addMemberRequest.isTeamless()) {
            overviewOffice = roomService.findOfficeByName(company.getId(), "no_team");
            overviewFloor = roomService.findFloorByName(company.getId(), "no_team");
            room = roomService.findRoomByName(company.getId(), "no_team");
            team = findRoomTeamByName(company.getId(), "no_team");
        } else {
            team = findAllTeamsByCompany(company.getId()).stream().filter(t -> t.getName().equals(addMemberRequest.getTeamName())).findFirst().orElseThrow();
            room = roomService.findAllRooms().stream().filter(r -> r.getTeams().contains(team)).findFirst().orElseThrow();
            overviewFloor = roomService.findAllFloors().stream().filter(f -> f.getRoomList().contains(room)).findFirst().orElseThrow();
            overviewOffice = roomService.findAllOffices().stream().filter(o -> o.getOverviewFloorList().contains(overviewFloor)).findFirst().orElseThrow();
        }

        if (room.getCurrentCapacity() + 1 <= room.getMaxCapacity()) {
            company.getOfficeList().remove(overviewOffice);
            overviewOffice.getOverviewFloorList().remove(overviewFloor);
            overviewFloor.getRoomList().remove(room);
            room.getTeams().remove(team);
            member.setStack(team.getStack());
            team.getMembers().add(member);
            team.setSize(team.getSize() + 1);
            room.getTeams().add(team);
            room.setCurrentCapacity(room.getCurrentCapacity() + 1);
            overviewFloor.getRoomList().add(room);
            overviewOffice.getOverviewFloorList().add(overviewFloor);
            company.getOfficeList().add(overviewOffice);

            roomService.saveUpdatedCompany(company);
        } else {
            throw new RoomMaxCapacityExceededException("Member does not fit in team's room");
        }

        return room;
    }

    public Member editMember(Member member, EditMemberRequest editMemberRequest) {
        Team team = findAllTeams().stream().filter(t -> t.getMembers().contains(member)).findFirst().orElseThrow();
        if (!editMemberRequest.getTeamName().equals(team.getName())) {
            return editMemberChangeTeam(team, member, editMemberRequest);
        } else {
            return editMemberSameTeam(team, member, editMemberRequest);
        }
    }

    private Member editMemberChangeTeam(Team team, Member member, EditMemberRequest editMemberRequest) {
        Company company = roomService.findCompanyById(editMemberRequest.getCompanyId());
        Team teamToMove = getAllTeamsByCompany(editMemberRequest.getCompanyId())
                .stream()
                .filter(t -> t.getName().equals(editMemberRequest.getTeamName()))
                .findFirst().orElseThrow();

        int memberIndex = team.getMembers().indexOf(member);
        Member updatableMember = team.getMembers().get(memberIndex);

        Room room = roomService.findAllRooms().stream().filter(r -> r.getTeams().contains(team)).findFirst().orElseThrow();
        OverviewFloor overviewFloor = roomService.findAllFloors().stream().filter(f -> f.getRoomList().contains(room)).findFirst().orElseThrow();
        OverviewOffice overviewOffice = roomService.findAllOffices().stream().filter(o -> o.getOverviewFloorList().contains(overviewFloor)).findFirst().orElseThrow();
        int officeFromMoveIndex = company.getOfficeList().indexOf(overviewOffice);
        int floorFromMoveIndex = overviewOffice.getOverviewFloorList().indexOf(overviewFloor);
        int roomFromMoveIndex = overviewFloor.getRoomList().indexOf(room);

        Room roomToMove = roomService.findAllRooms().stream().filter(r -> r.getTeams().contains(teamToMove)).findFirst().orElseThrow();
        OverviewFloor overviewFloorToMove = roomService.findAllFloors().stream().filter(f -> f.getRoomList().contains(roomToMove)).findFirst().orElseThrow();
        OverviewOffice overviewOfficeToMove = roomService.findAllOffices().stream().filter(o -> o.getOverviewFloorList().contains(overviewFloorToMove)).findFirst().orElseThrow();
        int officeToMoveIndex = company.getOfficeList().indexOf(overviewOfficeToMove);
        int floorToMoveIndex = overviewOfficeToMove.getOverviewFloorList().indexOf(overviewFloorToMove);
        int roomToMoveIndex = overviewFloorToMove.getRoomList().indexOf(roomToMove);

        if (roomToMove.getCurrentCapacity() + 1 <= roomToMove.getMaxCapacity()) {
            if (officeFromMoveIndex == officeToMoveIndex) {
                if (floorFromMoveIndex == floorToMoveIndex) {
                    overviewOffice.getOverviewFloorList().remove(overviewFloor);
                    if (roomFromMoveIndex == roomToMoveIndex) {
                        company.getOfficeList().remove(overviewOffice);
                        overviewOffice.getOverviewFloorList().remove(overviewFloor);
                        overviewFloor.getRoomList().remove(room);

                        room.getTeams().remove(team);
                        team.getMembers().remove(memberIndex);
                        team.setSize(team.getSize() - 1);

                        updatableMember.setEmail(editMemberRequest.getEmail());
                        updatableMember.setExperience(editMemberRequest.getExperience());
                        updatableMember.setFirstName(editMemberRequest.getFirstName());
                        updatableMember.setLastName(editMemberRequest.getLastName());
                        updatableMember.setStack(teamToMove.getStack());

                        teamToMove.getMembers().add(updatableMember);
                        teamToMove.setSize(teamToMove.getSize() + 1);
                        room.getTeams().add(teamToMove);

                        overviewFloor.getRoomList().add(room);
                        overviewOffice.getOverviewFloorList().add(overviewFloor);
                        company.getOfficeList().add(overviewOffice);
                    } else {
                        company.getOfficeList().remove(overviewOffice);
                        overviewOffice.getOverviewFloorList().remove(overviewFloor);

                        overviewFloor.getRoomList().remove(room);
                        overviewFloor.getRoomList().remove(roomToMove);

                        room.getTeams().remove(team);
                        team.getMembers().remove(memberIndex);
                        team.setSize(team.getSize() - 1);
                        room.getTeams().add(team);
                        room.setCurrentCapacity(room.getCurrentCapacity() - 1);

                        updatableMember.setEmail(editMemberRequest.getEmail());
                        updatableMember.setExperience(editMemberRequest.getExperience());
                        updatableMember.setFirstName(editMemberRequest.getFirstName());
                        updatableMember.setLastName(editMemberRequest.getLastName());
                        updatableMember.setStack(teamToMove.getStack());

                        roomToMove.getTeams().remove(teamToMove);
                        teamToMove.getMembers().add(updatableMember);
                        teamToMove.setSize(teamToMove.getSize() + 1);
                        roomToMove.getTeams().add(teamToMove);
                        roomToMove.setCurrentCapacity(roomToMove.getCurrentCapacity() + 1);

                        overviewFloor.getRoomList().add(room);
                        overviewFloor.getRoomList().add(roomToMove);

                        overviewOffice.getOverviewFloorList().add(overviewFloor);
                        company.getOfficeList().add(overviewOffice);
                    }
                } else {
                    company.getOfficeList().remove(overviewOffice);

                    overviewOffice.getOverviewFloorList().remove(floorFromMoveIndex);
                    overviewOffice.getOverviewFloorList().remove(floorToMoveIndex);

                    overviewFloor.getRoomList().remove(roomFromMoveIndex);
                    overviewFloorToMove.getRoomList().remove(roomToMoveIndex);

                    room.getTeams().remove(team);
                    team.getMembers().remove(memberIndex);
                    team.setSize(team.getSize() - 1);
                    room.getTeams().add(team);
                    room.setCurrentCapacity(room.getCurrentCapacity() - 1);

                    updatableMember.setEmail(editMemberRequest.getEmail());
                    updatableMember.setExperience(editMemberRequest.getExperience());
                    updatableMember.setFirstName(editMemberRequest.getFirstName());
                    updatableMember.setLastName(editMemberRequest.getLastName());
                    updatableMember.setStack(teamToMove.getStack());

                    roomToMove.getTeams().remove(teamToMove);
                    teamToMove.getMembers().add(updatableMember);
                    teamToMove.setSize(teamToMove.getSize() + 1);
                    roomToMove.getTeams().add(teamToMove);
                    roomToMove.setCurrentCapacity(roomToMove.getCurrentCapacity() + 1);

                    overviewFloor.getRoomList().add(room);
                    overviewFloorToMove.getRoomList().add(roomToMove);

                    overviewOffice.getOverviewFloorList().add(overviewFloor);
                    overviewOffice.getOverviewFloorList().add(overviewFloorToMove);

                    company.getOfficeList().add(overviewOffice);
                }
            } else {
                company.getOfficeList().remove(officeFromMoveIndex);
                company.getOfficeList().remove(officeToMoveIndex);
                overviewOffice.getOverviewFloorList().remove(floorFromMoveIndex);
                overviewOfficeToMove.getOverviewFloorList().remove(floorToMoveIndex);

                overviewFloor.getRoomList().remove(roomFromMoveIndex);
                overviewFloorToMove.getRoomList().remove(roomToMoveIndex);

                room.getTeams().remove(team);
                team.getMembers().remove(memberIndex);
                team.setSize(team.getSize() - 1);
                room.getTeams().add(team);
                room.setCurrentCapacity(room.getCurrentCapacity() - 1);

                updatableMember.setEmail(editMemberRequest.getEmail());
                updatableMember.setExperience(editMemberRequest.getExperience());
                updatableMember.setFirstName(editMemberRequest.getFirstName());
                updatableMember.setLastName(editMemberRequest.getLastName());
                updatableMember.setStack(teamToMove.getStack());

                roomToMove.getTeams().remove(teamToMove);
                teamToMove.getMembers().add(updatableMember);
                teamToMove.setSize(teamToMove.getSize() + 1);
                roomToMove.getTeams().add(teamToMove);
                roomToMove.setCurrentCapacity(roomToMove.getCurrentCapacity() + 1);

                overviewFloor.getRoomList().add(room);
                overviewFloorToMove.getRoomList().add(roomToMove);

                overviewOffice.getOverviewFloorList().add(overviewFloor);
                overviewOfficeToMove.getOverviewFloorList().add(overviewFloorToMove);
                company.getOfficeList().add(overviewOffice);
                company.getOfficeList().add(overviewOfficeToMove);
            }

            roomService.saveUpdatedCompany(company);
            return updatableMember;
        } else {
            throw new RoomMaxCapacityExceededException("Member does not fit in team's room");
        }
    }

    private Member editMemberSameTeam(Team team, Member member, EditMemberRequest editMemberRequest) {
        Room room = roomService.findAllRooms().stream().filter(r -> r.getTeams().contains(team)).findFirst().orElseThrow();
        OverviewFloor overviewFloor = roomService.findAllFloors().stream().filter(f -> f.getRoomList().contains(room)).findFirst().orElseThrow();
        OverviewOffice overviewOffice = roomService.findAllOffices().stream().filter(o -> o.getOverviewFloorList().contains(overviewFloor)).findFirst().orElseThrow();
        Company company = roomService.findCompanyById(editMemberRequest.getCompanyId());

        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(overviewFloor);
        overviewFloor.getRoomList().remove(room);
        room.getTeams().remove(team);
        int memberIndex = team.getMembers().indexOf(member);
        Member updatableMember = team.getMembers().get(memberIndex);
        updatableMember.setStack(team.getStack());
        updatableMember.setEmail(editMemberRequest.getEmail());
        updatableMember.setExperience(editMemberRequest.getExperience());
        updatableMember.setFirstName(editMemberRequest.getFirstName());
        updatableMember.setLastName(editMemberRequest.getLastName());
        team.getMembers().remove(memberIndex);
        team.getMembers().add(updatableMember);
        room.getTeams().add(team);
        overviewFloor.getRoomList().add(room);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        roomService.saveUpdatedCompany(company);
        return updatableMember;
    }

    public void removeMember(String companyId, String officeId, String floorId, String roomId, Member member) {
        Company company = roomService.findCompanyById(companyId);
        OverviewOffice overviewOffice = roomService.findOfficeById(company.getId(), officeId);
        OverviewFloor overviewFloor = roomService.findFloorById(company.getId(), overviewOffice.getId(), floorId);
        Room room = roomService.findRoomById(company.getId(), overviewOffice.getId(), overviewFloor.getId(), roomId);
        Team team = findTeamsByRoom(company.getId(), overviewOffice.getId(), overviewFloor.getId(), room.getId()).stream()
                .filter(t -> t.getMembers().contains(member))
                .findFirst()
                .orElseThrow();

        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(overviewFloor);
        overviewFloor.getRoomList().remove(room);
        room.getTeams().remove(team);
        team.getMembers().remove(member);
        team.setSize(team.getSize() - 1);
        room.getTeams().add(team);
        room.setCurrentCapacity(room.getCurrentCapacity() - 1);
        overviewFloor.getRoomList().add(room);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        roomService.saveUpdatedCompany(company);
    }

    public void removeMember(String companyId, Member member) {
        Team team = findAllTeams().stream().filter(t -> t.getMembers().contains(member)).findFirst().orElseThrow();
        Room room = roomService.findAllRooms().stream().filter(r -> r.getTeams().contains(team)).findFirst().orElseThrow();
        OverviewFloor overviewFloor = roomService.findAllFloors().stream().filter(f -> f.getRoomList().contains(room)).findFirst().orElseThrow();
        OverviewOffice overviewOffice = roomService.findAllOffices().stream().filter(o -> o.getOverviewFloorList().contains(overviewFloor)).findFirst().orElseThrow();
        Company company = roomService.findCompanyById(companyId);

        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(overviewFloor);
        overviewFloor.getRoomList().remove(room);
        room.getTeams().remove(team);
        team.getMembers().remove(member);
        team.setSize(team.getSize() - 1);
        room.getTeams().add(team);
        room.setCurrentCapacity(room.getCurrentCapacity() - 1);
        overviewFloor.getRoomList().add(room);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        roomService.saveUpdatedCompany(company);
    }

    public Team findTeamByName(List<Team> teams, String teamName) {
        return teams.stream()
                .filter(team -> team.getName().equals(teamName))
                .findFirst()
                .orElseThrow(ExceptionUtil.buildTeamNotFoundException(teamName));
    }

    public List<Team> findAllTeams() {
        List<Team> teamsInRooms = new ArrayList<>();
        roomService.findAllCompanies().forEach(
                company -> company.getOfficeList().forEach(
                        overviewOffice -> overviewOffice.getOverviewFloorList().forEach(
                                overviewFloor -> overviewFloor.getRoomList().forEach(
                                        room -> teamsInRooms.addAll(room.getTeams())
                                )
                        )));
        return teamsInRooms;
    }

    public List<Team> findAllTeamsByCompany(String companyId) {
        CONSOLE_LOGGER.info("Fetching all teams in company ID: " + companyId);
        List<Team> allTeams = new ArrayList<>();
        roomService.findCompanyById(companyId).getOfficeList().forEach(
                overviewOffice -> overviewOffice.getOverviewFloorList().forEach(
                        overviewFloor -> overviewFloor.getRoomList().forEach(
                                room -> allTeams.addAll(room.getTeams())
                        )
                ));
        return allTeams;
    }

    public void relocateTeams(String companyId, Suggestion suggestion) {
        suggestion.getTransfers().forEach(transfer -> {
            String splitLocationFrom = transfer.getMoveTo().split(TRANSFER_DELIMITER)[0];
            String[] splitLocationFromParts = splitLocationFrom.split("_");
            Team team = findAllTeams().stream()
                    .filter(t -> t.getName().equals(transfer.getTeamName()))
                    .findFirst().orElseThrow();
            relocateTeam(companyId, team, splitLocationFromParts, true);

            String splitLocationTo = transfer.getMoveTo().split(TRANSFER_DELIMITER)[1];
            String[] splitLocationToParts = splitLocationTo.split("_");
            relocateTeam(companyId, team, splitLocationToParts, false);
        });
    }

    private void relocateTeam(String companyId, Team team, String[] locationParts, boolean remove) {
        Company company = roomService.findCompanyById(companyId);
        OverviewOffice overviewOffice = roomService.findOfficeByName(company.getId(), locationParts[0]);
        OverviewFloor overviewFloor = roomService.findFloorByName(company.getId(), overviewOffice.getId(), locationParts[1]);
        Room room = roomService.findRoomByName(company.getId(), overviewOffice.getId(), overviewFloor.getId(), locationParts[2]);

        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(overviewFloor);
        overviewFloor.getRoomList().remove(room);
        if (remove) {
            room.getTeams().remove(team);
            room.setCurrentCapacity(room.getCurrentCapacity() - team.getSize());
        } else {
            room.getTeams().add(team);
            room.setCurrentCapacity(room.getCurrentCapacity() + team.getSize());
        }

        overviewFloor.getRoomList().add(room);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        roomService.saveUpdatedCompany(company);
    }

    public Team findTeamByName(FindSeatRequest findSeatRequest) {
        OverviewOffice office = roomService.findOfficeById(findSeatRequest.getCompanyId(), findSeatRequest.getOfficeId());
        for (OverviewFloor floor : office.getOverviewFloorList()) {
            for (Room room : floor.getRoomList()) {
                for (Team team : room.getTeams()) {
                    if (team.getName().equals(findSeatRequest.getTeamName())) {
                        return team;
                    }
                }
            }
        }
        throw new TeamNotFoundException(String.format("Team referenced by %s not found", findSeatRequest.getTeamName()));
    }

    public Team findTeamById(String teamId) {
        return findAllTeams().stream()
                .filter(team -> team.getId().equals(teamId))
                .findFirst()
                .orElseThrow(ExceptionUtil.buildTeamNotFoundException(teamId));
    }

    public List<Team> findTeamsStartingWith(String name) {
        return findAllTeams().stream()
                .filter(team -> team.getName().startsWith(name))
                .collect(Collectors.toList());
    }

    public List<Team> findTeamsByOffices(List<OverviewOffice> offices) {
        List<Team> allTeams = new ArrayList<>();
        offices.forEach(office -> office.getOverviewFloorList().forEach(floor -> floor.getRoomList().forEach(room -> {
            List<Team> teams = room.getTeams();
            teams.forEach(team -> team.setRoom(roomService.buildRoomWithoutTeams(room)));
            allTeams.addAll(teams);
        })));
        return allTeams;
    }

    public boolean hasTeamRoomEnoughCapacity(String companyId, String officeId, Team currentTeam, int capacityToBeAdded) {
        Room room = isTeamMerged(currentTeam)
                ? roomService.findByTeam(companyId, officeId, ((JointTeam) currentTeam).getTeamA())
                : roomService.findByTeam(companyId, officeId, currentTeam);
        return room.getMaxCapacity() - room.getCurrentCapacity() > capacityToBeAdded - 1;
    }

    public static boolean isTeamMerged(Team team) {
        return team.getName().contains(TEAM_JOIN_DELIMITER);
    }

    public boolean hasTeamRoomOverflownMaxCapacity(Team currentTeam, int capacityToBeAdded, int maxCapacity) {
        return currentTeam.getSize() + capacityToBeAdded > maxCapacity;
    }

    public Team buildDividedTeam(Team currentTeam) {
        String teamName = currentTeam.getName().split(DELIMITER)[0];
        List<Team> currentTeamInstance = findTeamsStartingWith(teamName);
        String postfix = String.valueOf(currentTeamInstance.size() + 1);

        Team dividedTeam = new Team();
        dividedTeam.setId(currentTeam.getId() + DELIMITER + postfix);
        dividedTeam.setName(teamName + DELIMITER + postfix);
        dividedTeam.setRoom(currentTeam.getRoom());
        dividedTeam.setSize(currentTeam.getSize() / 2);
        dividedTeam.setSplit(true);
        return dividedTeam;
    }

    public static String extractOriginalTeamName(String teamFullName) {
        return teamFullName.split(DELIMITER)[0];
    }

    public List<Team> updateTeamSizes(List<Team> teams, Team currentTeam, int peopleAmount) {
        CONSOLE_LOGGER.info("Updating team sizes");
        return teams.stream()
                .map(team -> buildUpdatedTeam(currentTeam, peopleAmount, team))
                .collect(Collectors.toList());
    }

    public String concatenateOfficeAndRoom(Team team) {
        if (team.getRoom() != null) {
            return team.getRoom().getLocation().getOffice() + DELIMITER +
                    team.getRoom().getLocation().getFloorNumb() + DELIMITER +
                    team.getRoom().getLocation().getRoomNumb();
        } else {
            return "No previous location";
        }
    }

    public List<String> getConditionsOfTeams(List<Team> teamList) {
        return teamList.stream()
                .filter(team -> team.getConditions() != null)
                .map(Team::getConditions)
                .collect(Collectors.toList());
    }

    public List<Team> getTeamsByRestriction(List<Team> teamList, String restriction) {
        return teamList.stream()
                .filter(team -> team.getConditions().equals(restriction))
                .collect(Collectors.toList());
    }

    public Map<Location, List<Team>> getTeamsGroupedByTeamLocation(String companyId, String officeId, List<Team> teams) {
        return teams.stream()
                .collect(groupingBy(team -> roomService.findByTeam(companyId, officeId, team).getLocation()));
    }

    public Team buildCurrentTeamCopy(Team currentTeam) {
        return teamMapper.buildCurrentTeamCopy(currentTeam);
    }

    public List<Set<Team>> getTeamSetList(FindSeatRequest findSeatRequest, Map<Integer, List<String>> floorMap, List<Team> teams, Room currentTeamRoom) {
        List<Set<Team>> listSetOfTeams = new ArrayList<>();
        floorMap.forEach((k, v) -> buildListSetOfTeams(findSeatRequest, floorMap, teams, listSetOfTeams, currentTeamRoom));
        currentAttempt = 0;
        return listSetOfTeams;
    }

    public void resetSplits(List<Team> teams) {
        teams.forEach(team -> team.setSplit(false));
    }

    public void splitMergedTeam(List<Team> teams, int peopleAmount, Team currentTeam, Team originalTeam) {
        List<Team> splitTeams = buildListOfSplitMergedTeams(peopleAmount, currentTeam, originalTeam);
        teams.remove(currentTeam);
        teams.addAll(splitTeams);
        splitTeams.forEach(team -> markAsSplittable(teams, team, true));
    }

    public void splitRegularTeam(List<Team> teams, int peopleAmount, Team currentTeam, Room currentTeamRoom) {
        int undividedSize = currentTeam.getSize() + peopleAmount;
        currentTeam.setSize(undividedSize);
        markAsSplittable(teams, currentTeam, false);
        Team dividedTeam = buildDividedTeam(currentTeam);
        teams.add(dividedTeam);
        currentTeamRoom.getTeams().add(dividedTeam);
    }

    public Optional<Team> convertMoveTogetherTeamsToJointTeam(FindSeatRequest findSeatRequest, List<Team> teams,
                                                              Set<String> offices, Team currentTeam,
                                                              List<TeamRestriction> teamRestrictions) {
        Team updatedCurrentTeam = null;
        for (TeamRestriction teamRestriction : teamRestrictions) {
            Team teamA = teamRestriction.getTeam();
            Team teamB = teamRestriction.getAdditionalTeam();
            Team jointTeam = buildJointTeam(findSeatRequest, teamA, teamB);

            if (isJointTeamRelevantForSearch(offices, jointTeam, currentTeam)) {
                if (jointTeam.getName().contains(currentTeam.getName())) {
                    updatedCurrentTeam = jointTeam;
                }
                teams.removeAll(List.of(teamA, teamB));
                teams.add(jointTeam);
            }
        }
        return Optional.ofNullable(updatedCurrentTeam);
    }

    public List<List<Team>> generateTeamSubsets(Set<Team> teams, Team currentTeam, Team splitTeam, boolean splittable,
                                                int teamsInvolved) {
        List<List<Team>> teamSubsets = Sets.combinations(teams, teamsInvolved).stream()
                .map(ArrayList::new)
                .filter(hasMinimumAmountOfTeamsInvolved(splittable))
                .filter(containsMovableTeam(splittable, currentTeam, splitTeam))
                .sorted(Comparator.comparingInt(List::size))
                .collect(Collectors.toList());

        CONSOLE_LOGGER.info("Generated {} subsets", teamSubsets.size());
        return teamSubsets;
    }

    public List<Team> getCurrentTeamInstance(Set<Team> teams, Team currentTeam) {
        return teams.stream()
                .filter(team -> team.getName().startsWith(extractOriginalTeamName(currentTeam.getName())))
                .sorted(Comparator.comparing(Team::getName))
                .collect(Collectors.toList());
    }

    private Predicate<List<Team>> hasMinimumAmountOfTeamsInvolved(boolean splittable) {
        return splittable
                ? team -> team.size() >= ConsumerService.MINIMUM_TEAMS_INVOLVED_SPLITTABLE
                : team -> team.size() >= ConsumerService.MINIMUM_TEAMS_INVOLVED;
    }

    private Predicate<List<Team>> containsMovableTeam(boolean splittable, Team currentTeam, Team splitTeam) {
        return splittable
                ? teamList -> teamList.stream().anyMatch(team -> team.getId().equals(currentTeam.getId())) &&
                teamList.stream().anyMatch(team -> team.getId().equals(splitTeam.getId()))
                : teamList -> teamList.stream().anyMatch(team -> team.getId().equals(currentTeam.getId()));
    }

    private void markAsSplittable(List<Team> teams, Team currentTeam, boolean wasJoint) {
        Team team = findTeamByName(teams, currentTeam.getName());
        team.setSize(getNewSize(currentTeam, wasJoint));
        team.setSplit(true);
    }

    private void buildListSetOfTeams(FindSeatRequest findSeatRequest, Map<Integer, List<String>> floorMap, List<Team> teams, List<Set<Team>> listSetOfTeams,
                                     Room currentTeamRoom) {
        Set<Team> teamSet = teams.stream()
                .filter(getTeamWhitelistedByFloorPredicate(findSeatRequest, floorMap, currentTeamRoom))
                .collect(Collectors.toSet());
        currentAttempt++;
        if (teamSet.size() > MINIMUM_TEAM_SUBSET_SIZE) {
            listSetOfTeams.add(teamSet);
        }
    }

    private Predicate<Team> getTeamWhitelistedByFloorPredicate(FindSeatRequest findSeatRequest, Map<Integer, List<String>> floorMap, Room currentTeamRoom) {
        return team -> {
            List<String> whiteList = floorMap.get(currentAttempt);
            if (currentTeamRoom.getTeams().contains(team)) {
                return true;
            } else {
                Room room = isTeamMerged(team)
                        ? roomService.findByTeam(findSeatRequest.getCompanyId(), findSeatRequest.getOfficeId(), ((JointTeam) team).getTeamA())
                        : roomService.findByTeam(findSeatRequest.getCompanyId(), findSeatRequest.getOfficeId(), team);
                String floorNumb = room.getLocation().getFloorNumb();
                return whiteList.contains(floorNumb) || currentTeamRoom.getLocation().getFloorNumb().equals(floorNumb);
            }
        };
    }

    private List<Team> buildListOfSplitMergedTeams(int peopleAmount, Team currentTeam, Team originalTeam) {
        JointTeam jointTeam = (JointTeam) currentTeam;
        Team teamA = jointTeam.getTeamA();
        Team teamB = jointTeam.getTeamB();
        teamA.setRoom(jointTeam.getRoom());
        teamB.setRoom(jointTeam.getRoom());
        int newSize = originalTeam.getSize() + peopleAmount;

        if (teamA.getId().equals(originalTeam.getId())) {
            teamA.setSize(newSize);
        } else {
            teamB.setSize(newSize);
        }
        return List.of(teamA, teamB);
    }

    private JointTeam buildJointTeam(FindSeatRequest findSeatRequest, Team teamA, Team teamB) {
        Room room = roomService.findByTeam(findSeatRequest.getCompanyId(), findSeatRequest.getCompanyId(), teamA);
        Room roomWithoutTeams = roomService.buildRoomWithoutTeams(room);
        List<Member> jointMembers = new ArrayList<>();
        jointMembers.addAll(teamA.getMembers());
        jointMembers.addAll(teamB.getMembers());

        JointTeam jointTeam = new JointTeam();
        jointTeam.setId(teamA.getId() + "_joint");
        jointTeam.setName(teamA.getName() + TEAM_JOIN_DELIMITER + teamB.getName());
        jointTeam.setMembers(jointMembers);
        jointTeam.setRoom(roomWithoutTeams);
        jointTeam.setSize(teamA.getSize() + teamB.getSize());
        jointTeam.setTeamA(teamA);
        jointTeam.setTeamB(teamB);
        return jointTeam;
    }

    private boolean isJointTeamRelevantForSearch(Set<String> offices, Team jointTeam, Team currentTeam) {
        return offices.contains(jointTeam.getRoom().getLocation().getOffice())
                || jointTeam.getName().contains(currentTeam.getName());
    }

    private Team buildUpdatedTeam(Team currentTeam, int peopleAmount, Team team) {
        Team updatedTeam = new Team();

        updatedTeam.setId(team.getId());
        updatedTeam.setName(team.getName());
        updatedTeam.setMembers(team.getMembers());
        updatedTeam.setStack(team.getStack());
        updatedTeam.setRoom(null);
        updatedTeam.setSplit(team.isSplit());

        if (team.getId().equals(currentTeam.getId()) && !team.isSplit()) {
            updatedTeam.setSize(team.getSize() + peopleAmount);
        } else {
            updatedTeam.setSize(team.getSize());
        }

        return updatedTeam;
    }

    private int getNewSize(Team currentTeam, boolean wasJoint) {
        return wasJoint
                ? currentTeam.getSize()
                : (int) Math.ceil((double) currentTeam.getSize() / 2);
    }
}