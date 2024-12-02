package edu.util;

import edu.model.User;
import edu.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserSessionBuilder {
    private final DateTimeBuilder dateTimeBuilder;

    @Autowired
    public UserSessionBuilder(DateTimeBuilder dateTimeBuilder) {
        this.dateTimeBuilder = dateTimeBuilder;
    }

    public UserSession buildNewSessionForUser(User user) {
        return new UserSession(UUID.randomUUID(), user, dateTimeBuilder.getCurrentDateTimeAsString());
    }
}
