package lt.lukasnakas.seatplanner.service.jms;

import lt.lukasnakas.seatplanner.model.*;
import lt.lukasnakas.seatplanner.model.dto.SolverDto;
import lt.lukasnakas.seatplanner.model.enumerators.Restriction;
import lt.lukasnakas.seatplanner.model.exception.InsufficientTeamsInvolvedException;
import lt.lukasnakas.seatplanner.model.exception.SearchActiveException;
import lt.lukasnakas.seatplanner.model.request.FindSeatRequest;
import lt.lukasnakas.seatplanner.model.response.SearchResponse;
import lt.lukasnakas.seatplanner.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProducerService {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(ProducerService.class);

    public static final int MINIMUM_AMOUNT_OF_TEAMS_INVOLVED_WHEN_SPLITTING = 3;
    private final TeamService teamService;
    private final RoomService roomService;
    private final JmsTemplate jmsTemplate;
    private final FloorService floorService;
    private final ConsumerService consumerService;
    private final OfficeService officeService;
    private final TeamRestrictionService teamRestrictionService;

    private Team originalTeam;
    private Team currentTeam;
    private Room currentTeamRoom;
    private boolean splittable;

    public ProducerService(TeamService teamService,
                           RoomService roomService,
                           JmsTemplate jmsTemplate,
                           FloorService floorService,
                           ConsumerService consumerService,
                           OfficeService officeService,
                           TeamRestrictionService teamRestrictionService) {
        this.teamService = teamService;
        this.roomService = roomService;
        this.jmsTemplate = jmsTemplate;
        this.floorService = floorService;
        this.officeService = officeService;
        this.consumerService = consumerService;
        this.teamRestrictionService = teamRestrictionService;
    }

    public SearchResponse submitToSolver(FindSeatRequest findSeatRequest) {
        if (searchActive()) {
            throw new SearchActiveException("Search is currently active");
        }
        List<OverviewOffice> offices = officeService.findOfficesByLocation(findSeatRequest);
        List<Team> teams = teamService.findTeamsByOffices(offices);
        currentTeam = teamService.findTeamByName(findSeatRequest);

        if (!findSeatRequest.getMembers().isEmpty()) {
            currentTeam.getMembers().addAll(findSeatRequest.getMembers());
        }

        currentTeamRoom = roomService.findByTeam(findSeatRequest.getCompanyId(), findSeatRequest.getOfficeId(), currentTeam);
        originalTeam = teamService.buildCurrentTeamCopy(currentTeam);
        splittable = findSeatRequest.isSplittable();

        roomService.setCurrentTeamRoom(currentTeam, currentTeamRoom);
        if (!teamListContainsCurrentTeam(teams)) {
            teams.add(currentTeam);
        }

        Set<String> officeNames = offices.stream().map(OverviewOffice::getOfficeName).collect(Collectors.toSet());
        handleJointTeams(findSeatRequest, officeNames, teams);

        if (teamService.hasTeamRoomEnoughCapacity(findSeatRequest.getCompanyId(), findSeatRequest.getOfficeId(),
                currentTeam, findSeatRequest.getPeopleAmount()) && !splittable) {
            return new SearchResponse("New member fits in current location");
        }

        handleSplittableTeams(findSeatRequest, teams);
        findSeatRequest.setSplittable(splittable);

        Map<Integer, List<String>> floorMap = initFloorMap(currentTeam, findSeatRequest);
        List<Set<Team>> teamSetList = teamService.getTeamSetList(findSeatRequest, floorMap, teams, currentTeamRoom);

        SolverDto solverDTO = new SolverDto(findSeatRequest, currentTeam, originalTeam);
        return submit(teamSetList, solverDTO, findSeatRequest.getCompanyId());
    }

    private boolean teamListContainsCurrentTeam(List<Team> teams) {
        return teams.stream().anyMatch(team -> team.getId().equals(currentTeam.getId()));
    }

    private Map<Integer, List<String>> initFloorMap(Team currentTeam, FindSeatRequest findSeatRequest) {
        CONSOLE_LOGGER.info("Initiating floor map");
        Room currentTeamRoom = TeamService.isTeamMerged(currentTeam)
                ? roomService.findByTeam(findSeatRequest.getCompanyId(), findSeatRequest.getOfficeId(), ((JointTeam) currentTeam).getTeamA())
                : roomService.findByTeam(findSeatRequest.getCompanyId(), findSeatRequest.getOfficeId(), currentTeam);
        Set<String> floors = fetchFloors(findSeatRequest);
        List<ArrayList<String>> floorSubsets = floorService.generateFloorSubsets(currentTeamRoom, floors, findSeatRequest.getLocation());
        return floorService.populateFloorMap(floorSubsets);
    }

    private Set<String> fetchFloors(FindSeatRequest findSeatRequest) {
        CONSOLE_LOGGER.info("Fetching floors");
        return officeService.findOfficeById(findSeatRequest.getCompanyId(), findSeatRequest.getOfficeId()).getOverviewFloorList()
                .stream()
                .map(OverviewFloor::getFloorName)
                .collect(Collectors.toSet());
    }

    private boolean searchActive() {
        return consumerService.getFindSeatResponseMap().values().stream()
                .anyMatch(findSeatResponse -> !findSeatResponse.isCompleted());
    }

    private void handleJointTeams(FindSeatRequest findSeatRequest, Set<String> offices, List<Team> teams) {
        List<TeamRestriction> teamRestrictions = teamRestrictionService.findMoveRestrictions(Restriction.MOVE_TOGETHER);
        Optional<Team> updatedCurrentTeam = teamService.convertMoveTogetherTeamsToJointTeam(findSeatRequest, teams, offices, currentTeam,
                teamRestrictions);
        updatedCurrentTeam.ifPresent(team -> currentTeam = team);
    }

    private void handleSplittableTeams(FindSeatRequest findSeatRequest, List<Team> teams) {
        int peopleAmount = findSeatRequest.getPeopleAmount();
        int teamsInvolved = findSeatRequest.getMaxTeamsInvolved();

        teamService.resetSplits(teams);
        int maxRoomCapacityInOffice = roomService.findMaxRoomCapacityInOffice(findSeatRequest.getCompanyId(), findSeatRequest.getOfficeId());
        if (teamService.hasTeamRoomOverflownMaxCapacity(currentTeam, peopleAmount, maxRoomCapacityInOffice)) {
            splittable = true;
        }

        if (splittable) {
            if (teamsInvolved < MINIMUM_AMOUNT_OF_TEAMS_INVOLVED_WHEN_SPLITTING) {
                throw new InsufficientTeamsInvolvedException(String.format("If Team split is requested" +
                        " the minimum amount of teamsInvolved should be %d", MINIMUM_AMOUNT_OF_TEAMS_INVOLVED_WHEN_SPLITTING));
            }

            splitTeams(teams, peopleAmount);
        }
    }

    private void splitTeams(List<Team> teams, int peopleAmount) {
        if (currentTeam instanceof JointTeam) {
            teamService.splitMergedTeam(teams, peopleAmount, currentTeam, originalTeam);
            currentTeam = originalTeam;
        } else {
            teamService.splitRegularTeam(teams, peopleAmount, currentTeam, currentTeamRoom);
        }
    }

    private SearchResponse submit(List<Set<Team>> teamSetList, SolverDto solverDTO, String companyId) {
        UUID id = UUID.randomUUID();
        consumerService.initializeConsumerWithCorrelation(id.toString());

        teamSetList.forEach(
                teamSet -> {
                    solverDTO.setTeams(teamSet);
                    MessagePostProcessor headers = buildMessageHeaders(id, teamSetList.size(), companyId);
                    jmsTemplate.convertAndSend(ConsumerService.CONSUMER_DESTINATION, solverDTO, headers);
                }
        );

        return new SearchResponse(id);
    }

    private MessagePostProcessor buildMessageHeaders(UUID id, int teamSetListSize, String companyId) {
        return header -> {
            header.setJMSCorrelationID(id.toString());
            header.setIntProperty("subsetsToProcess", teamSetListSize);
            header.setStringProperty("companyId", companyId);
            return header;
        };
    }
}