package lt.lukasnakas.seatplanner.initializer;

import lt.lukasnakas.seatplanner.model.Constraint;
import lt.lukasnakas.seatplanner.repository.ConstraintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

class ConstraintInitializerTest {

    @Mock
    private ConstraintRepository constraintRepository;

    @InjectMocks
    private ConstraintInitializer constraintInitializer;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    void shouldInitializeConstraintWhenEmpty() {
        // given
        Constraint constraint = new Constraint();
        given(constraintRepository.findByName(anyString())).willReturn(Optional.ofNullable(null));
        given(constraintRepository.save(constraint)).willReturn(constraint);

        // when
        constraintInitializer.run();

        // then
        int defaultConstraintTotal = 8;
        verify(constraintRepository, times(defaultConstraintTotal)).save(any());
    }

    @Test
    void shouldNotInitializeWhenConstraintAlreadyPresent() {
        // given
        Constraint constraint = new Constraint();
        given(constraintRepository.findByName(anyString())).willReturn(Optional.of(constraint));

        // when
        constraintInitializer.run();

        // then
        verify(constraintRepository, times(0)).save(constraint);
    }
}
