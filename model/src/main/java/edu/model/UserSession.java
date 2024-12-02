package edu.model;

import java.util.Objects;
import java.util.UUID;

public class UserSession {
    private String sessionId;
    private User user;
    private String creationTime;

    public UserSession() {
    }

    public UserSession(UUID sessionId, User user, String creationTime) {
        this.sessionId = sessionId.toString();
        this.user = user;
        this.creationTime = creationTime;
    }

    public String getSessionId() {
        return sessionId;
    }


    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId.toString();
    }

    public long getUserId() {
        return user.getId();
    }

    public void setUserId(long userId) {
        this.user.setId(userId);
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSession that)) return false;
        return Objects.equals(getSessionId(), that.getSessionId()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getCreationTime(), that.getCreationTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSessionId(), getUser(), getCreationTime());
    }
}
