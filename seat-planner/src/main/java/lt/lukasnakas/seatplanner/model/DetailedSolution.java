package lt.lukasnakas.seatplanner.model;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.List;

public class DetailedSolution {

    private Pair<HardSoftScore, List<Transfer>> scoreListPair;
    private Collection<ConstraintMatchTotal> constraintMatchTotals;

    public DetailedSolution(
            Pair<HardSoftScore, List<Transfer>> scoreListPair,
            Collection<ConstraintMatchTotal> constraintMatchTotals) {
        this.scoreListPair = scoreListPair;
        this.constraintMatchTotals = constraintMatchTotals;
    }

    public Pair<HardSoftScore, List<Transfer>> getScoreListPair() {
        return scoreListPair;
    }

    public void setScoreListPair(
            Pair<HardSoftScore, List<Transfer>> scoreListPair) {
        this.scoreListPair = scoreListPair;
    }

    public Collection<ConstraintMatchTotal> getConstraintMatchTotals() {
        return constraintMatchTotals;
    }

    public void setConstraintMatchTotals(
            Collection<ConstraintMatchTotal> constraintMatchTotals) {
        this.constraintMatchTotals = constraintMatchTotals;
    }

}
