package lt.lukasnakas.seatplanner.initializer;

import lt.lukasnakas.seatplanner.model.Constraint;
import lt.lukasnakas.seatplanner.model.enumerators.HardSoftScore;
import lt.lukasnakas.seatplanner.repository.ConstraintRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static lt.lukasnakas.seatplanner.service.ConstraintProviderService.*;

@Component
public class ConstraintInitializer implements CommandLineRunner {

    private final ConstraintRepository constraintRepository;

    public ConstraintInitializer(ConstraintRepository constraintRepository) {
        this.constraintRepository = constraintRepository;
    }

    @Override
    public void run(String... args) {
        List<Constraint> constraints = List.of(
                cannotFitMultipleTeamsConstraint(),
                stillHasCapacityLeftOver(),
                dividedTeamShouldBeInCloseProximity(),
                subTeamInstancesShouldRemainInCloseProximity(),
                doNotMoveConstraint(),
                lastResortMoveConstraint(),
                doNotMergeTeamConstraint(),
                differentTechStackPenalty());

        constraints.forEach(constraint -> {
            Optional<Constraint> constraintOptional = constraintRepository.findByName(constraint.getName());
            if (constraintOptional.isEmpty()) {
                constraintRepository.save(constraint);
            }
        });
    }

    private Constraint cannotFitMultipleTeamsConstraint() {
        Constraint constraint = new Constraint();
        constraint.setName(CANNOT_FIT_MULTIPLE_TEAMS);
        constraint.setScoreType(HardSoftScore.HARD);
        constraint.setScore(1);
        constraint.setActive(true);
        return constraint;
    }


    private Constraint stillHasCapacityLeftOver() {
        Constraint constraint = new Constraint();
        constraint.setName(STILL_HAS_CAPACITY_LEFT_OVER);
        constraint.setScoreType(HardSoftScore.SOFT);
        constraint.setScore(1);
        constraint.setActive(true);
        return constraint;
    }

    private Constraint dividedTeamShouldBeInCloseProximity() {
        Constraint constraint = new Constraint();
        constraint.setName(DIVIDED_TEAM_CLOSE_PROXIMITY);
        constraint.setScoreType(HardSoftScore.SOFT);
        constraint.setScore(25);
        constraint.setActive(true);
        return constraint;
    }

    private Constraint subTeamInstancesShouldRemainInCloseProximity() {
        Constraint constraint = new Constraint();
        constraint.setName(TEAM_SIBLINGS_IN_CLOSE_PROXIMITY);
        constraint.setScoreType(HardSoftScore.SOFT);
        constraint.setScore(50);
        constraint.setActive(true);
        return constraint;
    }

    private Constraint doNotMoveConstraint() {
        Constraint constraint = new Constraint();
        constraint.setName(DO_NOT_MOVE_RESTRICTION);
        constraint.setScoreType(HardSoftScore.HARD);
        constraint.setScore(1);
        constraint.setActive(true);
        return constraint;
    }

    private Constraint lastResortMoveConstraint() {
        Constraint constraint = new Constraint();
        constraint.setName(MOVE_AS_A_LAST_RESORT_RESTRICTION);
        constraint.setScoreType(HardSoftScore.SOFT);
        constraint.setScore(100);
        constraint.setActive(true);
        return constraint;
    }

    private Constraint doNotMergeTeamConstraint() {
        Constraint constraint = new Constraint();
        constraint.setName(TEAMS_CANNOT_BE_MERGED);
        constraint.setScoreType(HardSoftScore.HARD);
        constraint.setScore(1);
        constraint.setActive(true);
        return constraint;
    }

    private Constraint differentTechStackPenalty() {
        Constraint constraint = new Constraint();
        constraint.setName(DIFFERENT_TECH_STACK_PENALTY);
        constraint.setScoreType(HardSoftScore.SOFT);
        constraint.setScore(10);
        constraint.setActive(true);
        return constraint;
    }
}
