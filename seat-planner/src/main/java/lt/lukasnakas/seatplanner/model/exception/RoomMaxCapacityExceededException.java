package lt.lukasnakas.seatplanner.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoomMaxCapacityExceededException extends RuntimeException {

    public RoomMaxCapacityExceededException(String message) {
        super(message);
    }
}
