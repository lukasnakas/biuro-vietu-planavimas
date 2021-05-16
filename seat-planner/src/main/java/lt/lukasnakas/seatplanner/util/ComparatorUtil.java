package lt.lukasnakas.seatplanner.util;

import lt.lukasnakas.seatplanner.model.Suggestion;

import java.util.Comparator;

public final class ComparatorUtil {

    public static final Comparator<Suggestion> SUGGESTION_COMPARATOR = Comparator.comparingInt(Suggestion::getTeamsInvolved)
            .thenComparingInt(Suggestion::getPriority);

    private ComparatorUtil() {
    }

}
