package lt.lukasnakas.seatplanner.exceptionhandler;

import lt.lukasnakas.seatplanner.model.apierror.ApiError;
import lt.lukasnakas.seatplanner.model.exception.RoomMaxCapacityExceededException;
import lt.lukasnakas.seatplanner.model.exception.RoomMaxCapacityNotFoundException;
import lt.lukasnakas.seatplanner.model.exception.RoomsNotFoundException;
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
public class RoomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RoomMaxCapacityExceededException.class)
    public ResponseEntity<Object> handleRoomMaxCapacityExceededException(RoomMaxCapacityExceededException ex) {
        String errorMsg = "Team room max capacity exceeded";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMsg, ex));
    }

    @ExceptionHandler(RoomMaxCapacityNotFoundException.class)
    public ResponseEntity<Object> handleRoomMaxCapacityNotFoundException(RoomMaxCapacityNotFoundException ex) {
        String errorMsg = "No team located when fetching room max capacity!";
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMsg, ex));
    }

    @ExceptionHandler(RoomsNotFoundException.class)
    public ResponseEntity<Object> handleRoomsNotFoundException(RoomsNotFoundException ex) {
        String errorMsg = "Specified room location not valid";
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, errorMsg, ex));
    }
}
