package lt.lukasnakas.seatplanner.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MessageHeaderNotFoundException extends RuntimeException {

    public MessageHeaderNotFoundException(String message) {
        super(message);
    }
}
