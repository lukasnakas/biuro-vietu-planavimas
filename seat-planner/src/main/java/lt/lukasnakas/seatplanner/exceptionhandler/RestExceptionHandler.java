package lt.lukasnakas.seatplanner.exceptionhandler;

import lt.lukasnakas.seatplanner.model.apierror.ApiError;
import lt.lukasnakas.seatplanner.model.apierror.ApiSubError;
import lt.lukasnakas.seatplanner.model.apierror.ApiValidationError;
import lt.lukasnakas.seatplanner.model.exception.MessageHeaderNotFoundException;
import lt.lukasnakas.seatplanner.model.exception.MissingHttpRequestBodyParameters;
import lt.lukasnakas.seatplanner.model.exception.SearchActiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMsg = "Method Arguments Are Malformed";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMsg, ex));
    }

    @ExceptionHandler(MessageHeaderNotFoundException.class)
    public ResponseEntity<Object> handleMessageHeaderNotFoundException(MessageHeaderNotFoundException ex) {
        String errorMsg = "Message header not found";
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMsg, ex));
    }

    @ExceptionHandler(MissingHttpRequestBodyParameters.class)
    public ResponseEntity<Object> handleMissingHttpRequestBodyParametersException(MissingHttpRequestBodyParameters ex) {
        String errorMsg = "Missing request body parameters";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMsg, ex));
    }

    @ExceptionHandler(SearchActiveException.class)
    public ResponseEntity<Object> handleSearchActiveException(SearchActiveException ex) {
        String errorMsg = "Search is currently still actively solving";
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, errorMsg, ex));
    }

    public static ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        logApiErrorDetails(apiError);
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String errorMsg = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMsg, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String errorMsg = "JSON request validation failed";
        List<ApiSubError> apiValidationErrorList = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> apiValidationErrorList
                .add(new ApiValidationError(
                        error.getObjectName(),
                        ((FieldError) error).getField(),
                        error.getDefaultMessage(),
                        ((FieldError) error).getRejectedValue())
                ));

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMsg, ex);
        apiError.setApiSubErrorList(apiValidationErrorList);
        return buildResponseEntity(apiError);
    }

    private static void logApiErrorDetails(ApiError apiError) {
        HttpStatus httpStatus = apiError.getHttpStatus();
        int statusCode = apiError.getStatusCode();
        String errorMessage = apiError.getMessage();
        List<ApiSubError> subErrors = apiError.getApiSubErrorList();

        if (!CollectionUtils.isEmpty(subErrors)) {
            List<ApiValidationError> validationErrors = subErrors.stream()
                    .filter(ApiValidationError.class::isInstance)
                    .map(ApiValidationError.class::cast)
                    .collect(Collectors.toList());

            validationErrors.forEach(apiValidationError -> CONSOLE_LOGGER.error("Field \"{}\": {}, rejected supplied value \"{}\"",
                    apiValidationError.getField(),
                    apiValidationError.getMessage(),
                    apiValidationError.getRejectedValue()));
        }

        CONSOLE_LOGGER.error("Encountered error \"{}\" with status code {} ({}).", errorMessage, statusCode, httpStatus);
    }

}