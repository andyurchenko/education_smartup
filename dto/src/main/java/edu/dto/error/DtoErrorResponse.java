package edu.dto.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DtoErrorResponse {

    private final List<DtoErrorResponseElement> errors;

    public DtoErrorResponse() {
        errors = new ArrayList<>();
    }

    public void addError(DtoErrorResponseElement e) {
        errors.add(e);
    }

    public List<DtoErrorResponseElement> getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoErrorResponse that)) return false;
        return Objects.equals(getErrors(), that.getErrors());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getErrors());
    }
}
