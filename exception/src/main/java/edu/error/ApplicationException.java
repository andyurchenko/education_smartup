package edu.error;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ApplicationException extends Exception {
    private final ErrorResponseElement errorResponseElement;
    private final HttpStatus httpErrorStatus;

    public ApplicationException(ApplicationErrorDetails appErrorDetails, HttpStatus httpErrorStatus) {
        this.errorResponseElement = new ErrorResponseElement(appErrorDetails);
        this.httpErrorStatus = httpErrorStatus;
    }

    public ApplicationException(ApplicationErrorDetails appErrorDetails, Map<String, Object> params, HttpStatus httpErrorStatus) {
        this.errorResponseElement = new ErrorResponseElement(appErrorDetails, params);
        this.httpErrorStatus = httpErrorStatus;
    }

    public ErrorResponseElement getErrorElement() {
        return errorResponseElement;
    }

    public HttpStatus getHttpErrorStatus() {
        return httpErrorStatus;
    }
}
