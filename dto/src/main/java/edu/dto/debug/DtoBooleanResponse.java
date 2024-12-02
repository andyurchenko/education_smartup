package edu.dto.debug;

import java.util.Objects;

public class DtoBooleanResponse {
    private boolean value;

    public DtoBooleanResponse() {
    }

    public DtoBooleanResponse(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoBooleanResponse that)) return false;
        return isValue() == that.isValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isValue());
    }
}
