package lt.lukasnakas.seatplanner.mapper;

import lt.lukasnakas.seatplanner.model.request.ConstraintAlterationRequest;
import lt.lukasnakas.seatplanner.model.Constraint;
import lt.lukasnakas.seatplanner.model.dto.ConstraintDto;
import lt.lukasnakas.seatplanner.model.enumerators.HardSoftScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstraintMapperTest {

    private ConstraintMapper constraintMapper;

    @BeforeEach
    public void setup() {
        constraintMapper = new ConstraintMapper();
    }

    @Test
    void shouldMapToDto() {
        // given
        String constraintId = "1";
        String name = "Do not move";
        boolean active = true;
        int score = 1;
        HardSoftScore hardSoftScore = HardSoftScore.HARD;

        Constraint constraint = new Constraint();
        constraint.setId(constraintId);
        constraint.setName(name);
        constraint.setActive(active);
        constraint.setScore(score);
        constraint.setScoreType(hardSoftScore);

        // when
        ConstraintDto actual = constraintMapper.mapToDto(constraint);

        // then
        assertEquals(actual.getId(), constraintId);
        assertEquals(actual.getName(), name);
        assertEquals(actual.isActive(), active);
        assertEquals(actual.getScore(), score);
        assertEquals(actual.getScoreType(), hardSoftScore);
    }

    @Test
    void shouldMapToDtoList() {
        // given
        String constraintId = "1";
        String name = "Do not move";
        boolean active = true;
        int score = 1;
        HardSoftScore hardSoftScore = HardSoftScore.HARD;

        Constraint constraint = new Constraint();
        constraint.setId(constraintId);
        constraint.setName(name);
        constraint.setActive(active);
        constraint.setScore(score);
        constraint.setScoreType(hardSoftScore);

        // when
        List<ConstraintDto> actual = constraintMapper.mapToDtoList(Collections.singletonList(constraint));

        // then
        assertEquals(1, actual.size());
        ConstraintDto dto = actual.get(0);
        assertEquals(dto.getId(), constraintId);
        assertEquals(dto.getName(), name);
        assertEquals(dto.isActive(), active);
        assertEquals(dto.getScore(), score);
        assertEquals(dto.getScoreType(), hardSoftScore);
    }

    @Test
    void shouldBuildConstraint() {
        // given
        boolean active = true;
        int score = 1;
        HardSoftScore hardSoftScore = HardSoftScore.HARD;
        Constraint constraint = new Constraint();
        ConstraintAlterationRequest request = new ConstraintAlterationRequest();
        request.setActive(active);
        request.setScoreType(hardSoftScore);
        request.setScore(score);

        // when
        Constraint actual = constraintMapper.buildConstraint(constraint, request);

        // then
        assertEquals(actual.getScore(), score);
        assertEquals(actual.getScoreType(), hardSoftScore);
        assertEquals(actual.isActive(), active);
    }
}
