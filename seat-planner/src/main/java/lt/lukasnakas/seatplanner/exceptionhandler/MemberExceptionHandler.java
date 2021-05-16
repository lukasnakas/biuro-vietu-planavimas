package lt.lukasnakas.seatplanner.exceptionhandler;

import lt.lukasnakas.seatplanner.model.apierror.ApiError;
import lt.lukasnakas.seatplanner.model.exception.TeamMemberNotFound;
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
public class MemberExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TeamMemberNotFound.class)
    public ResponseEntity<Object> handleTeamMemberNotFoundException(TeamMemberNotFound ex) {
        String errorMsg = "Team member not present";
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, errorMsg, ex));
    }
}
