package lt.lukasnakas.seatplanner.exceptionhandler;

import lt.lukasnakas.seatplanner.model.apierror.ApiError;
import lt.lukasnakas.seatplanner.model.exception.ConstraintNotFoundException;
import lt.lukasnakas.seatplanner.model.exception.IllegalConstraintStateException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static lt.lukasnakas.seatplanner.exceptionhandler.RestExceptionHandler.buildResponseEntity;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ConstraintExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintNotFoundException.class)
    public ResponseEntity<Object> handleConstraintNotFoundException(ConstraintNotFoundException ex) {
        String errorMsg = "Unknown Constraint";
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, errorMsg, ex));
    }

    @ExceptionHandler(IllegalConstraintStateException.class)
    public ResponseEntity<Object> handleIllegalConstraintStateException(
            IllegalConstraintStateException ex) {
        String errorMsg = "Incorrect boolean value passed for constraint activation";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMsg, ex));
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMsg = "Constraint validation failed";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMsg, ex));
    }
}
