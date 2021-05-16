package lt.lukasnakas.seatplanner.exceptionhandler;

import lt.lukasnakas.seatplanner.model.apierror.ApiError;
import lt.lukasnakas.seatplanner.model.exception.SolvingFailedException;
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
public class SolverExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SolvingFailedException.class)
    public ResponseEntity<Object> handleSolvingFailedException(SolvingFailedException ex) {
        String errorMsg = "Solving failed";
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMsg, ex));
    }
}
