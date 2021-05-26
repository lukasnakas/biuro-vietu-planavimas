package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.DetailedSolution;
import lt.lukasnakas.seatplanner.model.SolverTransfer;
import lt.lukasnakas.seatplanner.model.Suggestion;
import lt.lukasnakas.seatplanner.model.Transfer;
import lt.lukasnakas.seatplanner.model.enumerators.State;
import lt.lukasnakas.seatplanner.repository.SolverTransferRepository;
import lt.lukasnakas.seatplanner.util.ComparatorUtil;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Service
public class SuggestionService {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(SuggestionService.class);

    private final SolverTransferRepository solverTransferRepository;
    private final TeamService teamService;

    public SuggestionService(SolverTransferRepository solverTransferRepository, TeamService teamService) {
        this.solverTransferRepository = solverTransferRepository;
        this.teamService = teamService;
    }

    public List<Suggestion> getConfirmedSuggestions(String companyId) {
        List<Suggestion> allSuggestions = new ArrayList<>();
        solverTransferRepository.findAll()
                .stream()
                .filter(solverTransfer -> solverTransfer.getId().equals(companyId))
                .findFirst().orElseThrow()
                .getSolverSuggestionsMap().values()
                .forEach(allSuggestions::addAll);
        return allSuggestions.stream()
                .filter(suggestion -> suggestion.getState() == State.CONFIRMED)
                .collect(Collectors.toList());
    }

    public List<Suggestion> getSuggestions(String companyId) {
        List<Suggestion> allSuggestions = new ArrayList<>();
        solverTransferRepository.findAll()
                .stream()
                .filter(solverTransfer -> solverTransfer.getId().equals(companyId))
                .findFirst().orElseThrow()
                .getSolverSuggestionsMap().values()
                    .forEach(allSuggestions::addAll);
        return allSuggestions;
    }

    public Suggestion getSuggestionById(String companyId, String id) {
        return getSuggestions(companyId)
                .stream().filter(suggestion -> suggestion.getId().equals(id))
                .findFirst().orElseThrow();
    }

    public Suggestion confirmSuggestion(String companyId, String correlationId, String id) {
        SolverTransfer solverTransfer = solverTransferRepository.findById(companyId).orElseThrow();
        List<Suggestion> suggestions = getSuggestions(companyId);
        Suggestion suggestion = suggestions.stream().filter(s -> s.getId().equals(id)).findFirst().orElseThrow();
        suggestion.setState(State.CONFIRMED);
        suggestion.setDate(new Date());
        Map<String, List<Suggestion>> solverSuggestionsMap = solverTransfer.getSolverSuggestionsMap();
        solverSuggestionsMap.put(correlationId, List.of(suggestion));
        solverTransfer.setSolverSuggestionsMap(solverSuggestionsMap);
        solverTransferRepository.save(solverTransfer);
        teamService.relocateTeams(companyId, suggestion);
        return suggestion;
    }

    public void save(String companyId, String correlationId, List<Suggestion> suggestions) {
        solverTransferRepository.findById(companyId).ifPresentOrElse(solverTransfer -> {
            Map<String, List<Suggestion>> solverSuggestionsMap = solverTransfer.getSolverSuggestionsMap();
            solverSuggestionsMap.put(correlationId, suggestions);
            solverTransfer.setSolverSuggestionsMap(solverSuggestionsMap);
            solverTransferRepository.save(solverTransfer);
        }, () -> {
            SolverTransfer transfer = new SolverTransfer(companyId, Map.of(correlationId, suggestions));
            solverTransferRepository.save(transfer);
        });
    }

    public List<Suggestion> getSortedSuggestions(List<DetailedSolution> detailedSolutions) {
        CONSOLE_LOGGER.info("Generating sorted suggestions");
        List<Suggestion> suggestions = generateSuggestions(detailedSolutions);
        suggestions.sort(ComparatorUtil.SUGGESTION_COMPARATOR);
        return suggestions;
    }

    public List<Suggestion> getLimitedSuggestions(List<Suggestion> suggestions, int limit) {
        return suggestions.stream()
                .sorted(ComparatorUtil.SUGGESTION_COMPARATOR)
                .limit(limit)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Suggestion> generateSuggestions(List<DetailedSolution> detailedSolutions) {
        return detailedSolutions.stream()
                .map(detailedSolution -> {
                    Pair<HardSoftScore, List<Transfer>> scoreListPair = detailedSolution.getScoreListPair();
                    Collection<ConstraintMatchTotal> constraintMatchTotals = detailedSolution.getConstraintMatchTotals();
                    return buildSuggestion(scoreListPair.getFirst(), scoreListPair.getSecond(), constraintMatchTotals);
                })
                .collect(Collectors.toList());
    }

    private Suggestion buildSuggestion(HardSoftScore score, List<Transfer> transfers, Collection<ConstraintMatchTotal> constraintTotal) {
        List<String> violatedConstraints = getViolatedConstraintNames(constraintTotal);

        Suggestion suggestion = new Suggestion();
        suggestion.setTeamsInvolved(transfers.size());
        suggestion.setTransfers(transfers);
        suggestion.setPriority(abs(score.getSoftScore()) + 1);
        suggestion.setViolatedConstraints(violatedConstraints);
        return suggestion;
    }

    private List<String> getViolatedConstraintNames(Collection<ConstraintMatchTotal> constraintTotal) {
        return constraintTotal.stream()
                .filter(hasViolatedConstraints())
                .map(ConstraintMatchTotal::getConstraintName)
                .collect(Collectors.toList());
    }

    private Predicate<ConstraintMatchTotal> hasViolatedConstraints() {
        return constraintMatchTotal -> constraintMatchTotal.getConstraintMatchCount() >= 1;
    }

}
