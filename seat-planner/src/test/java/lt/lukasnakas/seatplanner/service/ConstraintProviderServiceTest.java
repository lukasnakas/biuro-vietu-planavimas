package lt.lukasnakas.seatplanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

class ConstraintProviderServiceTest {

    @Mock
    private ConstraintService injectedReference;

    @InjectMocks
    private ConstraintProviderService providerService;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    void shouldDefineEmptyConstraintsWhenConstraintServiceEmpty() {
        // given
        Constraint[] expected = {};
        ConstraintFactory constraintFactory = Mockito.mock(ConstraintFactory.class);

        // when
        Constraint[] actual = providerService.defineConstraints(constraintFactory);

        // then
        assertEquals(actual.length, expected.length);
    }
}
