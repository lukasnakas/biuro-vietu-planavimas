package lt.lukasnakas.seatplanner.exceptionhandler;

import lt.lukasnakas.seatplanner.model.apierror.ApiError;
import lt.lukasnakas.seatplanner.model.exception.OfficeNotFoundException;
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
public class OfficeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OfficeNotFoundException.class)
    public ResponseEntity<Object> handleOfficeNotFound(OfficeNotFoundException ex) {
        String errorMsg = "Office not present";
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, errorMsg, ex));
    }
}
