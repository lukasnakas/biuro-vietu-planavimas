package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.Location;
import lt.lukasnakas.seatplanner.model.Team;
import lt.lukasnakas.seatplanner.model.TeamRestriction;
import lt.lukasnakas.seatplanner.model.enumerators.Restriction;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sum;

@Service
public class ConstraintProviderService implements ConstraintProvider {


    public static final String CANNOT_FIT_MULTIPLE_TEAMS = "Negalima sutalpinti kelių komandų";
    public static final String STILL_HAS_CAPACITY_LEFT_OVER = "Vis dar yra laisvos vietos";
    public static final String DIVIDED_TEAM_CLOSE_PROXIMITY = "Išskaidytos komandos turi būti kaip įmanoma arčiau viena kitos";
    public static final String TEAM_SIBLINGS_IN_CLOSE_PROXIMITY = "Visos giminingos komandos turėtų būti viena šalia kitos";
    public static final String DO_NOT_MOVE_RESTRICTION = "Pritaikytas komandos nejudinimo apribojimas";
    public static final String MOVE_AS_A_LAST_RESORT_RESTRICTION = "Pritaikytas komandos judinimo kaip paskutinės galimybės apribojimas";
    public static final String TEAMS_CANNOT_BE_MERGED = "Komandos negali būti sujungtos";
    public static final String DIFFERENT_TECH_STACK_PENALTY = "Skirtingos srities komandos neturėtų būti tame pačiame kabinete";
    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(ConstraintProviderService.class);
    private static ConstraintService constraintService;

    @Autowired
    private ConstraintService injectedReference;

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        if (Optional.ofNullable(constraintService).isEmpty()) {
            CONSOLE_LOGGER.info("Application initialising, constraints skipped at the moment");
            return new Constraint[]{};
        }

        List<String> activeConstraintNames = constraintService.getAllActiveConstraintNames();
        List<Constraint> constraints = getConstraintListings(constraintFactory);
        CONSOLE_LOGGER.info("Defining constraints for the solver. Found {} active constraints", activeConstraintNames.size());

        return constraints.stream()
                .filter(constraint -> activeConstraintNames.contains(constraint.getConstraintName()))
                .toArray(Constraint[]::new);
    }

    private List<Constraint> getConstraintListings(ConstraintFactory constraintFactory) {
        return List.of(
                sameRoomOverflowConflict(constraintFactory),
                stillHasLeftOverCapacity(constraintFactory),
                dividedTeamShouldBeInCloseProximity(constraintFactory),
                doNotMoveConstraint(constraintFactory),
                lastResortMoveConstraint(constraintFactory),
                doNotMergeTeamConstraint(constraintFactory),
                differentTechStackPenalty(constraintFactory));
    }

    /**
     * Checks whether a solution is viable in real life conditions - i.e.
     * teams assigned to a room should fit within its maximum capacity limit.
     *
     * @param constraintFactory ConstraintFactory
     * @return Constraint
     */
    private Constraint sameRoomOverflowConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Team.class)
                .groupBy(Team::getRoom, sum(Team::getSize))
                .filter((room, sum) -> room.getCurrentCapacity() + sum > room.getMaxCapacity())
                .penalize(CANNOT_FIT_MULTIPLE_TEAMS, constraintService.findHardSoftScore(CANNOT_FIT_MULTIPLE_TEAMS),
                        (room, sum) -> room.getCurrentCapacity() + sum - room.getMaxCapacity());
    }

    /**
     * Gives preference to solutions that arrange the teams in a way that
     * leaves rooms with as little extra seats as possible (preferably none).
     *
     * @param constraintFactory ConstraintFactory
     * @return Constraint
     */
    private Constraint stillHasLeftOverCapacity(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Team.class)
                .groupBy(Team::getRoom, sum(Team::getSize))
                .filter((room, sum) -> room.getCurrentCapacity() + sum < room.getMaxCapacity())
                .penalize(STILL_HAS_CAPACITY_LEFT_OVER, constraintService.findHardSoftScore(STILL_HAS_CAPACITY_LEFT_OVER),
                        (room, sum) -> (room.getMaxCapacity() - room.getCurrentCapacity()) - sum);
    }

    /**
     * Checks whether two team's components (two divided parts) are in close proximity - i.e.
     * preference is given to solutions that keep the two parts in the same city, office and floor.
     *
     * @param constraintFactory ConstraintFactory
     * @return Constraint
     */
    private Constraint dividedTeamShouldBeInCloseProximity(ConstraintFactory constraintFactory) {
        return constraintFactory.fromUniquePair(Team.class)
                .filter((teamA, teamB) -> teamA.isSplit() && teamB.isSplit())
                .filter((teamA, teamB) -> isNotWithinCloseProximity().test(teamA.getRoom().getLocation(), teamB.getRoom().getLocation()))
                .penalize(DIVIDED_TEAM_CLOSE_PROXIMITY, constraintService.findHardSoftScore(DIVIDED_TEAM_CLOSE_PROXIMITY));
    }

    /**
     * Checks whether a team has team constraint DO_NOT_MOVE,
     * in which case it would be ignored as a possible solution
     *
     * @param constraintFactory ConstraintFactory
     * @return Constraint
     */
    private Constraint doNotMoveConstraint(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Team.class)
                .filter(isTeamMoveRestricted(Restriction.DO_NOT_MOVE))
                .penalize(DO_NOT_MOVE_RESTRICTION, constraintService.findHardSoftScore(DO_NOT_MOVE_RESTRICTION));
    }

    /**
     * Checks whether a team has team constraint LAST_RESORT_MOVE,
     * in which case it would be applied soft score of LAST_RESORT_MOVE_SOFT_SCORE
     *
     * @param constraintFactory ConstraintFactory
     * @return Constraint
     */
    private Constraint lastResortMoveConstraint(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Team.class)
                .filter(isTeamMoveRestricted(Restriction.LAST_RESORT_MOVE))
                .penalize(MOVE_AS_A_LAST_RESORT_RESTRICTION, constraintService.findHardSoftScore(MOVE_AS_A_LAST_RESORT_RESTRICTION));
    }

    /**
     * Checks whether a team in a unique pair has team constraint DO_NOT_MERGE,
     * in which case it would be ignored as a possible solution
     *
     * @param constraintFactory ConstraintFactory
     * @return Constraint
     */
    private Constraint doNotMergeTeamConstraint(ConstraintFactory constraintFactory) {
        return constraintFactory.fromUniquePair(Team.class)
                .filter(areTeamsRestricted(Restriction.DO_NOT_MERGE))
                .penalize(TEAMS_CANNOT_BE_MERGED, constraintService.findHardSoftScore(TEAMS_CANNOT_BE_MERGED));
    }

    /**
     * Checks whether a team in a unique pair has same tech stack,
     * in which case it would be ignored as a possible solution
     *
     * @param constraintFactory ConstraintFactory
     * @return Constraint
     */
    private Constraint differentTechStackPenalty(ConstraintFactory constraintFactory) {
        return constraintFactory.fromUniquePair(Team.class)
                .filter(isTechStackAvailable())
                .filter((teamA, teamB) -> isInTheSameRoom().test(teamA.getRoom().getLocation(), teamB.getRoom().getLocation()))
                .filter(isTechStackDifferent())
                .penalize(DIFFERENT_TECH_STACK_PENALTY, constraintService.findHardSoftScore(DIFFERENT_TECH_STACK_PENALTY));
    }

    private Predicate<Team> isTeamMoveRestricted(Restriction restriction) {
        return team -> {
            Optional<TeamRestriction> moveRestriction = constraintService.fetchMoveRestrictionByTeam(team);
            return moveRestriction.isPresent() && moveRestriction.get().getRestrictionSet().contains(restriction);
        };
    }

    private BiPredicate<Team, Team> areTeamsRestricted(Restriction restriction) {
        return (teamA, teamB) -> {
            Optional<TeamRestriction> moveRestrictionA = constraintService.fetchMoveRestrictionByTeam(teamA);
            Optional<TeamRestriction> moveRestrictionB = constraintService.fetchMoveRestrictionByTeam(teamB);
            return (moveRestrictionA.isPresent() && moveRestrictionA.get().getRestrictionSet().contains(restriction)) ||
                    (moveRestrictionB.isPresent() && moveRestrictionB.get().getRestrictionSet().contains(restriction));
        };
    }

    private BiPredicate<Location, Location> isNotWithinCloseProximity() {
        return (locationA, locationB) ->
                !locationA.getFloorNumb().equals(locationB.getFloorNumb()) ||
                        !locationA.getOffice().equals(locationB.getOffice());
    }

    private BiPredicate<Team, Team> isTechStackAvailable() {
        return (teamA, teamB) -> teamA.getStack() != null && teamB.getStack() != null;
    }

    private BiPredicate<Location, Location> isInTheSameRoom() {
        return Location::equals;
    }

    private BiPredicate<Team, Team> isTechStackDifferent() {
        return (teamA, teamB) ->
                !teamA.getStack().equalsIgnoreCase(teamB.getStack());
    }

    @PostConstruct
    private void postInitConstraintServiceUpdating() {
        copyInjectedReference(injectedReference);
    }

    private static void copyInjectedReference(ConstraintService injectedReference) {
        constraintService = injectedReference;
    }
}