package edu.error;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;
import java.util.Objects;

public class ErrorResponseElement {
    private String errorCode;
    private String field;
    private String message;

    public ErrorResponseElement() {
    }

    public ErrorResponseElement(String errorCode, String field, String message) {
        this.errorCode = errorCode;
        this.field = field;
        this.message = message;
    }

    public ErrorResponseElement(ApplicationErrorDetails errorDetails, Map<String, Object> params) {
        this.errorCode = errorDetails.toString();
        this.field = errorDetails.getField();
        this.message = StringSubstitutor.replace(errorDetails.getMessage(), params);
    }

    public ErrorResponseElement(ApplicationErrorDetails errorDetails) {
        this.errorCode = errorDetails.toString();
        this.field = errorDetails.getField();
        this.message = errorDetails.getMessage();
    }



    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponseElement that)) return false;
        return Objects.equals(getErrorCode(), that.getErrorCode()) && Objects.equals(getField(), that.getField()) && Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getErrorCode(), getField(), getMessage());
    }
}
