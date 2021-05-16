package lt.lukasnakas.seatplanner.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConstraintNotFoundException extends RuntimeException {

    public ConstraintNotFoundException(String message) {
        super(message);
    }
}
