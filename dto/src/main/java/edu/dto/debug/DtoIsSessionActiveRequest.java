package edu.dto.debug;

import java.util.Objects;

public class DtoIsSessionActiveRequest {
    private String sessionId;

    public DtoIsSessionActiveRequest() {
    }

    public DtoIsSessionActiveRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoIsSessionActiveRequest that)) return false;
        return Objects.equals(getSessionId(), that.getSessionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSessionId());
    }
}
