package lt.lukasnakas.seatplanner.service.jms;

import lt.lukasnakas.seatplanner.model.request.FindSeatRequest;
import lt.lukasnakas.seatplanner.model.response.FindSeatResponse;
import lt.lukasnakas.seatplanner.helper.SolverDataHelper;
import lt.lukasnakas.seatplanner.model.Suggestion;
import lt.lukasnakas.seatplanner.model.Team;
import lt.lukasnakas.seatplanner.model.dto.SolverDto;
import lt.lukasnakas.seatplanner.model.exception.FindSeatResponseNotFoundException;
import lt.lukasnakas.seatplanner.service.SolverService;
import lt.lukasnakas.seatplanner.service.SuggestionService;
import lt.lukasnakas.seatplanner.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jms.support.JmsMessageHeaderAccessor;
import org.springframework.messaging.MessageHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

class ConsumerServiceTest {

    @Mock
    TeamService teamService;

    @Mock
    SolverService solverService;

    @Mock
    SuggestionService suggestionService;

    @Mock
    JmsMessageHeaderAccessor jmsMessageHeaderAccessor;

    @InjectMocks
    private ConsumerService consumerService;

    @BeforeEach
    public void init() {
        initMocks(this);
    }

    @Test
    void shouldReturnCompletedFindSeatResponseWhenNotSplittable() {
        // given
        SolverDto solverDto = SolverDataHelper.getSolverDto();
        FindSeatRequest findSeatRequest = solverDto.getFindSeatRequest();
        MessageHeaders messageHeaders = new MessageHeaders(Map.of("subsetsToProcess", 1, "companyId", "abc"));
        String correlationId = UUID.randomUUID().toString();
        List<List<Team>> generatedSubsets = List.of(new ArrayList<>());
        List<Suggestion> suggestions = new ArrayList<>();
        FindSeatResponse expected = SolverDataHelper.getFindSeatResponse(suggestions);
        given(jmsMessageHeaderAccessor.getCorrelationId()).willReturn(correlationId);
        given(teamService.getCurrentTeamInstance(solverDto.getTeams(), solverDto.getCurrentTeam()))
                .willReturn(List.of(solverDto.getCurrentTeam()));
        given(teamService.generateTeamSubsets(eq(solverDto.getTeams()), eq(solverDto.getCurrentTeam()), any(),
                eq(findSeatRequest.isSplittable()), anyInt())).willReturn(generatedSubsets);
        given(solverService.getGeneratedSuggestions(solverDto.getCurrentTeam(), generatedSubsets,
                findSeatRequest.getPeopleAmount(), solverDto.getOriginalTeam().getSize())).willReturn(suggestions);

        // when
        consumerService.initializeConsumerWithCorrelation(correlationId);
        consumerService.consume(solverDto, messageHeaders, jmsMessageHeaderAccessor);
        FindSeatResponse actual = consumerService.getFindSeatResponse(correlationId, 1);

        // then
        assertEquals(expected.getSuggestions().size(), actual.getSuggestions().size());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.isSplit(), actual.isSplit());
        assertEquals(expected.isCompleted(), actual.isCompleted());
    }

    @Test
    void shouldReturnCompletedFindSeatResponseWhenSplittable() {
        // given
        SolverDto solverDto = SolverDataHelper.getSolverDto();
        FindSeatRequest findSeatRequest = solverDto.getFindSeatRequest();
        findSeatRequest.setSplittable(true);
        MessageHeaders messageHeaders = new MessageHeaders(Map.of("subsetsToProcess", 1, "companyId", "abc"));
        String correlationId = UUID.randomUUID().toString();
        List<List<Team>> generatedSubsets = List.of(new ArrayList<>());
        List<Suggestion> suggestions = new ArrayList<>();
        FindSeatResponse expected = SolverDataHelper.getFindSeatResponse(suggestions);
        expected.setSplit(true);
        given(jmsMessageHeaderAccessor.getCorrelationId()).willReturn(correlationId);
        given(teamService.getCurrentTeamInstance(solverDto.getTeams(), solverDto.getCurrentTeam()))
                .willReturn(List.of(solverDto.getCurrentTeam()));
        given(teamService.generateTeamSubsets(eq(solverDto.getTeams()), eq(solverDto.getCurrentTeam()), any(),
                eq(findSeatRequest.isSplittable()), anyInt())).willReturn(generatedSubsets);
        given(solverService.getGeneratedSuggestions(solverDto.getCurrentTeam(), generatedSubsets,
                findSeatRequest.getPeopleAmount(), solverDto.getOriginalTeam().getSize())).willReturn(suggestions);

        // when
        consumerService.initializeConsumerWithCorrelation(correlationId);
        consumerService.consume(solverDto, messageHeaders, jmsMessageHeaderAccessor);
        FindSeatResponse actual = consumerService.getFindSeatResponse(correlationId, 1);

        // then
        assertEquals(expected.getSuggestions().size(), actual.getSuggestions().size());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.isSplit(), actual.isSplit());
        assertEquals(expected.isCompleted(), actual.isCompleted());
    }

    @Test
    void shouldThrowFindSeatResponseNotFoundException() {
        String id = "123";
        int limit = 1;
        assertThrows(FindSeatResponseNotFoundException.class, () -> consumerService.getFindSeatResponse(id, limit));
    }

    @Test
    void shouldReturnFindSeatResponseMap() {
        // given
        FindSeatResponse response = new FindSeatResponse();
        String key = "1";
        response.setCompleted(true);
        consumerService.getFindSeatResponseMap().put(key, response);
        // when
        Map<String, FindSeatResponse> actual = consumerService.getFindSeatResponseMap();
        // then
        assertEquals(response.isCompleted(), actual.get(key).isCompleted());
    }
}
