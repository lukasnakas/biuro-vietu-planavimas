package lt.lukasnakas.seatplanner.service.jms;

import lt.lukasnakas.seatplanner.model.Suggestion;
import lt.lukasnakas.seatplanner.model.Team;
import lt.lukasnakas.seatplanner.model.dto.SolverDto;
import lt.lukasnakas.seatplanner.model.exception.FindSeatResponseNotFoundException;
import lt.lukasnakas.seatplanner.model.request.FindSeatRequest;
import lt.lukasnakas.seatplanner.model.response.FindSeatResponse;
import lt.lukasnakas.seatplanner.service.SolverService;
import lt.lukasnakas.seatplanner.service.SuggestionService;
import lt.lukasnakas.seatplanner.service.TeamService;
import lt.lukasnakas.seatplanner.util.MessageHeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsMessageHeaderAccessor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

@Service
public class ConsumerService {

    public static final String STATUS_FORMAT = "%.1f%%";
    public static final String CONSUMER_DESTINATION = "solver";
    public static final int MINIMUM_TEAMS_INVOLVED_SPLITTABLE = 3;
    public static final int MINIMUM_TEAMS_INVOLVED = 2;

    private final SolverService solverService;
    private final TeamService teamService;
    private final SuggestionService suggestionService;
    private final Map<String, AtomicInteger> subsetsProcessedMap = new ConcurrentHashMap<>();
    private final Map<String, FindSeatResponse> findSeatResponseMap = new ConcurrentHashMap<>();
    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(ConsumerService.class);

    public ConsumerService(SolverService solverService, TeamService teamService, SuggestionService suggestionService) {
        this.solverService = solverService;
        this.teamService = teamService;
        this.suggestionService = suggestionService;
    }

    @JmsListener(destination = CONSUMER_DESTINATION)
    public void consume(SolverDto solverDTO, MessageHeaders messageHeaders, JmsMessageHeaderAccessor jmsMessageHeaderAccessor) {
        Team currentTeam = solverDTO.getCurrentTeam();
        Set<Team> teams = solverDTO.getTeams();
        List<Team> currentTeamInstance = teamService.getCurrentTeamInstance(teams, currentTeam);
        Team splitTeam = currentTeamInstance.get(currentTeamInstance.size() - 1);

        FindSeatRequest findSeatRequest = solverDTO.getFindSeatRequest();
        boolean splittable = findSeatRequest.isSplittable();
        int teamsInvolved = findSeatRequest.getMaxTeamsInvolved();
        int subsetsToProcess = MessageHeaderUtil.getHeader("subsetsToProcess", Integer.class, messageHeaders);
        String companyId = MessageHeaderUtil.getHeader("companyId", String.class, messageHeaders);
        int startingIndex = splittable ? MINIMUM_TEAMS_INVOLVED_SPLITTABLE : MINIMUM_TEAMS_INVOLVED;

        String correlationId = jmsMessageHeaderAccessor.getCorrelationId();
        AtomicInteger subsetsProcessedCounter = subsetsProcessedMap.get(correlationId);
        for (int i = startingIndex; i <= teamsInvolved; i++) {
            List<List<Team>> teamSubsets = teamService.generateTeamSubsets(teams, currentTeam, splitTeam, splittable, i);
            List<Suggestion> suggestions = solverService.getGeneratedSuggestions(currentTeam, teamSubsets,
                    findSeatRequest.getPeopleAmount(), solverDTO.getOriginalTeam().getSize());
            int subsetsProcessed = subsetsProcessedCounter.incrementAndGet();
            String status = getStatus(subsetsProcessed, subsetsToProcess, teamsInvolved - startingIndex + 1);
            boolean isCompleted = isSubsetCompleted(subsetsProcessed, findSeatRequest, startingIndex, subsetsToProcess);
            FindSeatResponse findSeatResponse = new FindSeatResponse(correlationId, isCompleted, suggestions, findSeatRequest.isSplittable(), status);
            findSeatResponseMap.merge(correlationId, findSeatResponse, appendSuggestionsFunction());
            suggestionService.save(companyId, correlationId, suggestions);
        }
        CONSOLE_LOGGER.info("Processed subset with index [{}]", subsetsProcessedCounter.intValue() / (teamsInvolved - startingIndex + 1));
    }

    public FindSeatResponse getFindSeatResponse(String id, int limit) {
        FindSeatResponse findSeatResponse = Optional.ofNullable(findSeatResponseMap.get(id)).
                orElseThrow(() -> new FindSeatResponseNotFoundException(String.format("Search [%s] not found", id)));
        List<Suggestion> limitedSuggestions = suggestionService.getLimitedSuggestions(findSeatResponse.getSuggestions(), limit);
        return buildFindSeatResponse(findSeatResponse, limitedSuggestions);
    }

    public void initializeConsumerWithCorrelation(String correlationId) {
        subsetsProcessedMap.put(correlationId, new AtomicInteger());
        findSeatResponseMap.put(correlationId, new FindSeatResponse());
    }

    public Map<String, FindSeatResponse> getFindSeatResponseMap() {
        return findSeatResponseMap;
    }

    private BiFunction<FindSeatResponse, FindSeatResponse, FindSeatResponse> appendSuggestionsFunction() {
        return (old, latest) -> {
            latest.getSuggestions().addAll(old.getSuggestions());
            return latest;
        };
    }

    private FindSeatResponse buildFindSeatResponse(FindSeatResponse findSeatResponse, List<Suggestion> limitedSuggestions) {
        FindSeatResponse updatedFindSeatResponse = new FindSeatResponse();
        updatedFindSeatResponse.setSuggestions(limitedSuggestions);
        updatedFindSeatResponse.setCompleted(findSeatResponse.isCompleted());
        updatedFindSeatResponse.setSplit(findSeatResponse.isSplit());
        updatedFindSeatResponse.setStatus(findSeatResponse.getStatus());
        return updatedFindSeatResponse;
    }

    private String getStatus(int subsetsProcessed, int subsetsToProcess, int currentSubset) {
        double processedSubsetsPercentage = (subsetsProcessed * 100D) / (currentSubset * subsetsToProcess);
        double percentage = BigDecimal.valueOf(processedSubsetsPercentage)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        return String.format(STATUS_FORMAT, percentage);
    }

    private boolean isSubsetCompleted(int subsetProcessed, FindSeatRequest findSeatRequest, int startingIndex, int subsetsToProcess) {
        return subsetProcessed * 1D / (findSeatRequest.getMaxTeamsInvolved() - startingIndex + 1) >= subsetsToProcess;
    }
}