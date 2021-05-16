package lt.lukasnakas.seatplanner.exceptionhandler;

import lt.lukasnakas.seatplanner.model.apierror.ApiError;
import lt.lukasnakas.seatplanner.model.exception.InsufficientTeamsInvolvedException;
import lt.lukasnakas.seatplanner.model.exception.TeamNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static lt.lukasnakas.seatplanner.exceptionhandler.RestExceptionHandler.buildResponseEntity;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class TeamExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InsufficientTeamsInvolvedException.class)
    public ResponseEntity<Object> handleInsufficientTeamsInvolvedException(InsufficientTeamsInvolvedException ex) {
        String errorMsg = "Insufficient teamsInvolved";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMsg, ex));
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<Object> handleTeamNotFoundException(TeamNotFoundException ex) {
        String errorMsg = "Team not found";
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, errorMsg, ex));
    }
}
