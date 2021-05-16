package lt.lukasnakas.seatplanner.helper;

import lt.lukasnakas.seatplanner.model.DetailedSolution;
import lt.lukasnakas.seatplanner.model.Suggestion;
import lt.lukasnakas.seatplanner.model.Transfer;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.springframework.data.util.Pair;

import java.util.*;

public class SuggestionDataHelper {

    public static Suggestion getSuggestion() {
        Suggestion suggestion = new Suggestion();
        suggestion.setPriority(3);
        suggestion.setTeamsInvolved(3);
        suggestion.setTransfers(TransferDataHelper.getTransfers());
        suggestion.setViolatedConstraints(new ArrayList<>());
        return suggestion;
    }

    public static List<Suggestion> getSuggestions() {
        List<Suggestion> suggestions = new ArrayList<>();
        suggestions.add(getSuggestion());
        return suggestions;
    }

    public static List<DetailedSolution> getDetailedSolutions(boolean withConstraints) {
        List<Transfer> transfers1 = Collections.singletonList(
                new Transfer("TeamA", "A -> B", 0));
        Pair<HardSoftScore, List<Transfer>> scoreListPair1 = Pair.of(HardSoftScore.ofSoft(0), transfers1);

        ConstraintMatchTotal constraint1 = new ConstraintMatchTotal("package", "constraintName1", HardSoftScore.ofSoft(0));
        if (withConstraints) {
            constraint1.addConstraintMatch(new ArrayList<>(), HardSoftScore.ofSoft(0));
        }
        Collection<ConstraintMatchTotal> constraintMatchTotals1 = Collections.singletonList(constraint1);
        DetailedSolution detailedSolution1 = new DetailedSolution(scoreListPair1, constraintMatchTotals1);

        List<Transfer> transfers2 = Collections.singletonList(
                new Transfer("TeamB", "B -> C", 1));
        Pair<HardSoftScore, List<Transfer>> scoreListPair2 = Pair.of(HardSoftScore.ofSoft(1), transfers2);

        ConstraintMatchTotal constraint2 = new ConstraintMatchTotal("package", "constraintName2", HardSoftScore.ofSoft(1));
        if (withConstraints) {
            constraint2.addConstraintMatch(new ArrayList<>(), HardSoftScore.ofSoft(1));
        }
        Collection<ConstraintMatchTotal> constraintMatchTotals2 = Collections.singletonList(constraint2);
        DetailedSolution detailedSolution2 = new DetailedSolution(scoreListPair2, constraintMatchTotals2);

        List<DetailedSolution> detailedSolutions = new ArrayList<>();
        detailedSolutions.add(detailedSolution1);
        detailedSolutions.add(detailedSolution2);
        return detailedSolutions;
    }

}
