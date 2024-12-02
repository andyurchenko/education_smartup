package edu.dto.settings;

import java.util.Objects;

public class DtoClientSettingsResponse {
    private int minPasswordLength;
    private int maxStringLength;

    public DtoClientSettingsResponse() {
    }

    public DtoClientSettingsResponse(int minPasswordLength, int maxStringLength) {
        this.minPasswordLength = minPasswordLength;
        this.maxStringLength = maxStringLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    public int getMaxStringLength() {
        return maxStringLength;
    }

    public void setMaxStringLength(int maxStringLength) {
        this.maxStringLength = maxStringLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoClientSettingsResponse that)) return false;
        return getMinPasswordLength() == that.getMinPasswordLength() && getMaxStringLength() == that.getMaxStringLength();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMinPasswordLength(), getMaxStringLength());
    }
}
