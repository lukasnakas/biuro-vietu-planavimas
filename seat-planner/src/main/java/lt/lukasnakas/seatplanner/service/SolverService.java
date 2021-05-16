package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.*;
import lt.lukasnakas.seatplanner.model.exception.SolvingFailedException;
import lt.lukasnakas.seatplanner.model.planning.RoomAssignment;
import org.apache.commons.lang3.StringUtils;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class SolverService {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(SolverService.class);
    private final SolverManager<RoomAssignment, UUID> solverManager;
    private final RoomService roomService;
    private final TeamService teamService;
    private final SuggestionService suggestionService;
    private final SolverFactory<RoomAssignment> solverFactory;

    public static final String TRANSFER_DELIMITER = " --> ";
    private static final int AMOUNT_OF_SUGGESTIONS_PER_ITERATION = 5;
    public static final String SPACE = " ";

    public SolverService(
            SolverManager<RoomAssignment, UUID> solverManager,
            RoomService roomService, TeamService teamService,
            SuggestionService suggestionService,
            SolverFactory<RoomAssignment> solverFactory) {
        this.solverManager = solverManager;
        this.roomService = roomService;
        this.teamService = teamService;
        this.suggestionService = suggestionService;
        this.solverFactory = solverFactory;
    }

    public List<Suggestion> getGeneratedSuggestions(Team currentTeam, List<List<Team>> teamSubsets, int peopleAmount,
                                                    int originalTeamSize) {
        List<DetailedSolution> detailedSolutions = new ArrayList<>();
        ScoreDirectorFactory<RoomAssignment> scoreDirectorFactory = solverFactory.getScoreDirectorFactory();
        ScoreDirector<RoomAssignment> roomAssignmentScoreDirector = scoreDirectorFactory.buildScoreDirector();

        for (List<Team> subset : teamSubsets) {
            List<Room> rooms = roomService.subtractCurrentTeamSizes(subset, currentTeam, originalTeamSize);
            List<Team> teams = teamService.updateTeamSizes(subset, currentTeam, peopleAmount);
            RoomAssignment roomAssignment = solve(teams, rooms);
            HardSoftScore bestScore = roomAssignment.getScore();

            if (bestScore.getHardScore() == 0) {
                List<Transfer> transfers = generateTransfers(subset, roomAssignment.getTeams());
                if (!containsDuplicates(transfers)) {
                    Pair<HardSoftScore, List<Transfer>> hardSoftScoreTransferListPair = Pair.of(bestScore, transfers);
                    Collection<ConstraintMatchTotal> constraintTotal = getConstraintTotal(roomAssignment,
                            roomAssignmentScoreDirector);
                    DetailedSolution detailedSolution = new DetailedSolution(hardSoftScoreTransferListPair, constraintTotal);
                    detailedSolutions.add(detailedSolution);
                }
            }

            if (detailedSolutions.size() >= AMOUNT_OF_SUGGESTIONS_PER_ITERATION) {
                break;
            }
        }
        return suggestionService.getSortedSuggestions(detailedSolutions);
    }

    private Collection<ConstraintMatchTotal> getConstraintTotal(RoomAssignment roomAssignment,
                                                                ScoreDirector<RoomAssignment> roomAssignmentScoreDirector) {
        roomAssignmentScoreDirector.setWorkingSolution(roomAssignment);
        return roomAssignmentScoreDirector.getConstraintMatchTotals();
    }

    private boolean containsDuplicates(List<Transfer> transfers) {
        for (Transfer transfer : transfers) {
            String[] rooms = transfer.getMoveTo().split(TRANSFER_DELIMITER);

            int occurrences = numOfRoomOccurrencesInTransfers(transfers, rooms[0]);
            if (occurrences == 2 && rooms[0].equals(rooms[1])) {
                return true;
            }

        }
        return false;
    }

    private int numOfRoomOccurrencesInTransfers(List<Transfer> transfers, String room) {
        int occurrences = 0;
        for (Transfer transfer : transfers) {
            occurrences += StringUtils.countMatches(transfer.getMoveTo(), room);
        }
        return occurrences;
    }

    private RoomAssignment solve(List<Team> teams, List<Room> rooms) {
        RoomAssignment unsolvedRoomAssignment = new RoomAssignment(teams, rooms);
        UUID problemId = UUID.randomUUID();
        SolverJob<RoomAssignment, UUID> solverJob = solverManager.solve(problemId, unsolvedRoomAssignment);

        try {
            return solverJob.getFinalBestSolution();
        } catch (ExecutionException e) {
            throw new SolvingFailedException(String.format("Error occurred while attempting solving: %s", e.getLocalizedMessage()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return RoomAssignment.buildEmptyHardScoredRoomAssignment();
        }
    }

    private List<Transfer> generateTransfers(List<Team> oldTeams, List<Team> newTeams) {
        CONSOLE_LOGGER.info("Generating transfers");
        return newTeams.stream()
                .map(updatedTeam -> {
                    Team oldTeam = teamService.findTeamByName(oldTeams, updatedTeam.getName());
                    return generateTransfer(oldTeam, updatedTeam);
                })
                .sorted()
                .collect(Collectors.toList());
    }

    private Transfer generateTransfer(Team oldTeam, Team updatedTeam) {
        String oldOfficeRoom = teamService.concatenateOfficeAndRoom(oldTeam);
        String newOfficeRoom = teamService.concatenateOfficeAndRoom(updatedTeam);
        String transfer = oldOfficeRoom + TRANSFER_DELIMITER + newOfficeRoom;
        return new Transfer(updatedTeam.getName(), transfer, updatedTeam.getSize());
    }

}