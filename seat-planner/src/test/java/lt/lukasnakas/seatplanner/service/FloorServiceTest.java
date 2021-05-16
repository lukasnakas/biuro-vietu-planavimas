package lt.lukasnakas.seatplanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

class FloorServiceTest {

    @Mock
    private OfficeService officeService;

    private FloorService floorService;

    @BeforeEach
    public void init() {
        initMocks(this);
        floorService = new FloorService(officeService);
    }

    @Test
    void shouldExtractFloorNumbWithoutDelimiter() {
        // given
        String fullFloorNumb = "6 aukštas_Lvovo";
        String expected = "6 aukštas";

        // when
        String actual = FloorService.extractFloorNumbWithoutDelimiter(fullFloorNumb);

        // then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldCompareFloorSubsetsOfDifferentSizes() {
        // given
        List<String> floorSubsetA = List.of("3 aukštas", "4 aukštas");
        List<String> floorSubsetB = List.of("3 aukštas");

        // when
        int actual = floorService.compare(floorSubsetA, floorSubsetB);

        // then
        assertEquals(1, actual);
    }

    @Test
    void shouldCompareFloorSubsetsOfSameSizesAndDifferentValues() {
        // given
        List<String> floorSubsetA = List.of("3 aukštas", "4 aukštas");
        List<String> floorSubsetB = List.of("3 aukštas", "5 aukštas");

        // when
        int actual = floorService.compare(floorSubsetA, floorSubsetB);

        // then
        assertEquals(-1, actual);
    }

    @Test
    void shouldCompareFloorSubsetsOfSameSizesAndSameValues() {
        // given
        List<String> floorSubsetA = List.of("3 aukštas", "4 aukštas");
        List<String> floorSubsetB = List.of("3 aukštas", "4 aukštas");

        // when
        int actual = floorService.compare(floorSubsetA, floorSubsetB);

        // then
        assertEquals(0, actual);
    }
}
