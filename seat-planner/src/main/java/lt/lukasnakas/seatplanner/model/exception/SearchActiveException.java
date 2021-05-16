package lt.lukasnakas.seatplanner.model.exception;

public class SearchActiveException extends RuntimeException {
    public SearchActiveException(String message) {
        super(message);
    }
}
