package edu.dto.error;

import java.util.Objects;

public class DtoErrorResponseElement {
    private String errorCode;
    private String field;
    private String message;

    public DtoErrorResponseElement() {
    }

    public DtoErrorResponseElement(String errorCode, String field, String message) {
        this.errorCode = errorCode;
        this.field = field;
        this.message = message;
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
        if (!(o instanceof DtoErrorResponseElement that)) return false;
        return Objects.equals(getErrorCode(), that.getErrorCode()) && Objects.equals(getField(), that.getField()) && Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getErrorCode(), getField(), getMessage());
    }
}
