package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.helper.SuggestionDataHelper;
import lt.lukasnakas.seatplanner.model.DetailedSolution;
import lt.lukasnakas.seatplanner.model.Suggestion;
import lt.lukasnakas.seatplanner.model.Transfer;
import lt.lukasnakas.seatplanner.repository.SolverTransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SuggestionServiceTest {

    private SuggestionService suggestionService;
    @Mock
    private SolverTransferRepository solverTransferRepository;
    @Mock
    private TeamService teamService;

    @BeforeEach
    public void setup() {
        suggestionService = new SuggestionService(solverTransferRepository, teamService);
    }

    @Test
    void shouldGetLimitedSuggestions() {
        // given
        int limit = 1;
        List<Suggestion> rawSuggestions = Arrays.asList(new Suggestion(), new Suggestion());

        // when
        List<Suggestion> actual = suggestionService.getLimitedSuggestions(rawSuggestions, limit);

        // then
        assertEquals(limit, actual.size());
    }

    @Test
    void shouldGetSortedSuggestionsWithConstraints() {
        // given
        List<DetailedSolution> detailedSolutions = SuggestionDataHelper.getDetailedSolutions(true);
        List<Suggestion> expected = List.of(
                new Suggestion(1, 1,
                        Collections.singletonList(new Transfer("TeamA", "A -> B", 0)),
                        Collections.singletonList("constraintName1")),
                new Suggestion(2, 1,
                        Collections.singletonList(new Transfer("TeamB", "B -> C", 1)),
                        Collections.singletonList("constraintName2"))
        );

        // when
        List<Suggestion> actual = suggestionService.getSortedSuggestions(detailedSolutions);

        // then
        Suggestion expectedSuggestion = expected.get(0);
        Suggestion actualSuggestion = actual.get(0);
        Transfer expectedTransfer = expectedSuggestion.getTransfers().get(0);
        Transfer actualTransfer = actualSuggestion.getTransfers().get(0);
        assertEquals(expected.size(), actual.size());
        assertEquals(expectedSuggestion.getPriority(), actualSuggestion.getPriority());
        assertEquals(expectedSuggestion.getTeamsInvolved(), actualSuggestion.getTeamsInvolved());
        assertEquals(expectedSuggestion.getViolatedConstraints().get(0), actualSuggestion.getViolatedConstraints().get(0));
        assertEquals(expectedTransfer.getTeamName(), actualTransfer.getTeamName());
        assertEquals(expectedTransfer.getMoveTo(), actualTransfer.getMoveTo());
        assertEquals(expectedTransfer.getPeopleTransferredAmount(), actualTransfer.getPeopleTransferredAmount());
    }

    @Test
    void shouldGetSortedSuggestionsWithoutConstraints() {
        // given
        List<DetailedSolution> detailedSolutions = SuggestionDataHelper.getDetailedSolutions(false);
        List<Suggestion> expected = List.of(
                new Suggestion(1, 1,
                        Collections.singletonList(new Transfer("TeamA", "A -> B", 0)),
                        new ArrayList<>()),
                new Suggestion(2, 1,
                        Collections.singletonList(new Transfer("TeamB", "B -> C", 1)),
                        new ArrayList<>())
        );

        // when
        List<Suggestion> actual = suggestionService.getSortedSuggestions(detailedSolutions);

        // then
        Suggestion expectedSuggestion = expected.get(0);
        Suggestion actualSuggestion = actual.get(0);
        Transfer expectedTransfer = expectedSuggestion.getTransfers().get(0);
        Transfer actualTransfer = actualSuggestion.getTransfers().get(0);
        assertEquals(expected.size(), actual.size());
        assertEquals(expectedSuggestion.getPriority(), actualSuggestion.getPriority());
        assertEquals(expectedSuggestion.getTeamsInvolved(), actualSuggestion.getTeamsInvolved());
        assertEquals(0, actualSuggestion.getViolatedConstraints().size());
        assertEquals(expectedTransfer.getTeamName(), actualTransfer.getTeamName());
        assertEquals(expectedTransfer.getMoveTo(), actualTransfer.getMoveTo());
        assertEquals(expectedTransfer.getPeopleTransferredAmount(), actualTransfer.getPeopleTransferredAmount());
    }

}
