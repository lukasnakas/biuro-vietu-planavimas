package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.mapper.ConstraintMapper;
import lt.lukasnakas.seatplanner.model.Constraint;
import lt.lukasnakas.seatplanner.model.Team;
import lt.lukasnakas.seatplanner.model.TeamRestriction;
import lt.lukasnakas.seatplanner.model.dto.ConstraintDto;
import lt.lukasnakas.seatplanner.model.exception.ConstraintNotFoundException;
import lt.lukasnakas.seatplanner.model.request.ConstraintAlterationRequest;
import lt.lukasnakas.seatplanner.repository.ConstraintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lt.lukasnakas.seatplanner.helper.ConstraintDataHelper.DO_NOT_MOVE;
import static lt.lukasnakas.seatplanner.model.enumerators.HardSoftScore.HARD;
import static lt.lukasnakas.seatplanner.model.enumerators.HardSoftScore.SOFT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

class ConstraintServiceTest {

    @Mock
    private TeamRestrictionService teamRestrictionService;
    @Mock
    private ConstraintRepository constraintRepository;
    @Mock
    private ConstraintMapper constraintMapper;

    @InjectMocks
    private ConstraintService constraintService;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    void shouldUpdateConstraint() {
        // given
        String id = "1";
        ConstraintAlterationRequest request = new ConstraintAlterationRequest();
        request.setConstraintId(id);
        Constraint constraint = new Constraint();
        ConstraintDto expected = new ConstraintDto();
        Optional<Constraint> constraintOptional = Optional.of(constraint);
        given(constraintRepository.findById(id)).willReturn(constraintOptional);
        given(constraintMapper.buildConstraint(constraintOptional.get(), request)).willReturn(constraint);
        given(constraintRepository.save(constraint)).willReturn(constraint);
        given(constraintMapper.mapToDto(constraint)).willReturn(expected);

        // when
        ConstraintDto actual = constraintService.updateConstraint(request);

        // then
        assertEquals(actual, expected);
    }

    @Test
    void shouldFindAll() {
        // given
        List<Constraint> expected = new ArrayList<>();
        given(constraintRepository.findAll()).willReturn(expected);

        // when
        List<Constraint> actual = constraintService.findAll();

        // then
        assertEquals(actual.size(), expected.size());
    }

    @Test
    void shouldFindByName() {
        // given
        Optional<Constraint> expected = Optional.of(new Constraint());
        given(constraintRepository.findByName(DO_NOT_MOVE)).willReturn(expected);

        // when
        Optional<Constraint> actual = constraintService.findByName(DO_NOT_MOVE);

        // then
        assertEquals(actual, expected);
    }

    @Test
    void shouldGetAllConstraints() {
        // given
        List<Constraint> constraints = new ArrayList<>();
        List<ConstraintDto> expected = new ArrayList<>();
        given(constraintRepository.findAll()).willReturn(constraints);
        given(constraintMapper.mapToDtoList(constraints)).willReturn(expected);

        // when
        List<ConstraintDto> actual = constraintService.getAllConstraintDtos();

        // then
        assertEquals(actual.size(), expected.size());
    }

    @Test
    void shouldThrowConstraintNotFoundException() {
        // given
        String id = "1";
        String exMessage = "Constraint " + id + " not found!";
        String expected = "Constraint 1 not found!";
        given(constraintRepository.findById(id)).willThrow(new ConstraintNotFoundException(exMessage));

        // when
        ConstraintNotFoundException actual = assertThrows(ConstraintNotFoundException.class, () -> constraintService.findById(id));

        // then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldFindConstraintDtoById() {
        // given
        String id = "1";
        Constraint constraint = new Constraint();
        ConstraintDto expected = new ConstraintDto();
        given(constraintRepository.findById(id)).willReturn(Optional.of(constraint));
        given(constraintMapper.mapToDto(constraint)).willReturn(expected);

        // when
        ConstraintDto actual = constraintService.getConstraintDtoById(id);

        // then
        assertEquals(actual, expected);
    }

    @Test
    void shouldFetchMoveRestrictionByTeam() {
        // given
        Team team = new Team();
        Optional<TeamRestriction> expected = Optional.of(new TeamRestriction());
        given(teamRestrictionService.findMoveRestriction(team)).willReturn(expected);

        // when
        Optional<TeamRestriction> actual = constraintService.fetchMoveRestrictionByTeam(team);

        // then
        assertEquals(actual, expected);
    }

    @Test
    void shouldGetAllActiveConstraintNames() {
        // given
        Constraint constraint = new Constraint();
        constraint.setActive(true);
        constraint.setName(DO_NOT_MOVE);
        List<Constraint> constraints = List.of(constraint);
        given(constraintRepository.findAll()).willReturn(constraints);

        // when
        List<String> constraintNames = constraintService.getAllActiveConstraintNames();

        // then
        assertEquals(constraints.size(), constraintNames.size());
        assertEquals(DO_NOT_MOVE, constraintNames.get(0));
    }

    @Test
    void shouldFindHardSoftScoreWithNoConstraint() {
        // given
        Optional<Constraint> emptyConstraint = Optional.empty();
        given(constraintRepository.findByName(DO_NOT_MOVE)).willReturn(emptyConstraint);
        HardSoftScore expected = HardSoftScore.ONE_HARD;

        // when
        HardSoftScore actual = constraintService.findHardSoftScore(DO_NOT_MOVE);

        // then
        assertEquals(expected.getHardScore(), actual.getHardScore());
    }

    @Test
    void shouldFindHardSoftScoreWithExistingHardConstraint() {
        // given
        Constraint constraint = new Constraint();
        constraint.setScoreType(HARD);
        constraint.setScore(1);
        Optional<Constraint> constraintOptional = Optional.of(constraint);
        given(constraintRepository.findByName(DO_NOT_MOVE)).willReturn(constraintOptional);
        HardSoftScore expected = HardSoftScore.ofHard(1);

        // when
        HardSoftScore actual = constraintService.findHardSoftScore(DO_NOT_MOVE);

        // then
        assertEquals(expected.getHardScore(), actual.getHardScore());
        assertEquals(0, actual.getSoftScore());
    }

    @Test
    void shouldFindHardSoftScoreWithExistingSoftConstraint() {
        // given
        String name = "Last resort move";
        Constraint constraint = new Constraint();
        constraint.setScoreType(SOFT);
        constraint.setScore(1);
        Optional<Constraint> constraintOptional = Optional.of(constraint);
        given(constraintRepository.findByName(name)).willReturn(constraintOptional);
        HardSoftScore expected = HardSoftScore.ofSoft(1);

        // when
        HardSoftScore actual = constraintService.findHardSoftScore(name);

        // then
        assertEquals(expected.getSoftScore(), actual.getSoftScore());
        assertEquals(0, actual.getHardScore());
    }
}
