package lt.lukasnakas.seatplanner.util;

import lt.lukasnakas.seatplanner.model.exception.TeamMemberNotFound;
import lt.lukasnakas.seatplanner.model.exception.TeamNotFoundException;

import java.util.function.Supplier;

public final class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static Supplier<TeamNotFoundException> buildTeamNotFoundException(String reference) {
        return () ->
                new TeamNotFoundException(String.format("Team referenced by %s not found", reference));
    }

    public static Supplier<TeamMemberNotFound> buildTeamMemberNotFoundException(String reference) {
        return () ->
                new TeamMemberNotFound(String.format("Team member %s not found", reference));
    }
}
