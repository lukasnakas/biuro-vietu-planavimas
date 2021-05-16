package lt.lukasnakas.seatplanner.model.apierror;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"httpStatus", "statusCode", "time", "message", "errorDetails", "debugMessage"})
public class ApiError {

    private HttpStatus httpStatus;
    private int statusCode;
    private String message;
    private String debugMessage;

    @JsonProperty("errorDetails")
    private List<ApiSubError> apiSubErrorList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime time;


    private ApiError() {
        time = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus) {
        this();
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
    }

    public ApiError(HttpStatus httpStatus, Throwable ex) {
        this();
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
        this.message = "Unexpected error";
        if (ex.getLocalizedMessage() != null) {
            this.debugMessage = ex.getLocalizedMessage();
        } else {
            this.debugMessage = ex.toString();
        }
    }

    public ApiError(HttpStatus httpStatus, String message, Throwable ex) {
        this();
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public List<ApiSubError> getApiSubErrorList() {
        return apiSubErrorList;
    }

    public void setApiSubErrorList(List<ApiSubError> apiSubErrorList) {
        this.apiSubErrorList = apiSubErrorList;
    }
}