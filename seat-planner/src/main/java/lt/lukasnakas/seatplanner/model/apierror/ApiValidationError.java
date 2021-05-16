package lt.lukasnakas.seatplanner.model.apierror;

public class ApiValidationError implements ApiSubError {

    private String object;
    private String field;
    private String message;
    private Object rejectedValue;

    public ApiValidationError() {
    }

    public ApiValidationError(String object, String field, String message, Object rejectedValue) {
        this.object = object;
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }
}