package lt.lukasnakas.seatplanner.service.jms;

import lt.lukasnakas.seatplanner.helper.FindSeatRequestResponseDataHelper;
import lt.lukasnakas.seatplanner.model.exception.SearchActiveException;
import lt.lukasnakas.seatplanner.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jms.core.JmsTemplate;

import static lt.lukasnakas.seatplanner.helper.FindSeatRequestResponseDataHelper.getFindSeatRequest;
import static lt.lukasnakas.seatplanner.helper.FindSeatRequestResponseDataHelper.getStringFindSeatResponseMap;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

class ProducerServiceTest {

    @Mock
    TeamService teamService;

    @Mock
    RoomService roomService;

    @Mock
    JmsTemplate jmsTemplate;

    @Mock
    FloorService floorService;

    @Mock
    ConsumerService consumerService;

    @Mock
    OfficeService officeService;

    @Mock
    TeamRestrictionService teamRestrictionService;

    @InjectMocks
    private ProducerService producerService;

    @BeforeEach
    public void init() {
        initMocks(this);
    }

    @Test
    void shouldReturnSearchActiveException() {
        // given
        String errorMessage = "Search is currently active";
        given(consumerService.getFindSeatResponseMap())
                .willReturn(getStringFindSeatResponseMap
                        (FindSeatRequestResponseDataHelper.getNonCompletedFindSeatResponse()));

        // when
        Exception exception = assertThrows(SearchActiveException.class, () -> {
            producerService.submitToSolver(getFindSeatRequest());
        });

        // then
        assertTrue(errorMessage.contains(exception.getMessage()));
    }
}
