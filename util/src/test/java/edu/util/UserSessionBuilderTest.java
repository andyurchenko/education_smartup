package edu.util;

import edu.model.User;
import edu.model.UserSession;
import edu.model.additional.UserType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionBuilderTest {
    private final UserSessionBuilder sessionBuilder;
    private final String defaultFirstName = "defaultFirstName";
    private final String defaultLastName = "defaultLastName";
    private final String defaultPatronymic = "defaultPatronymic";
    private final String defaultPosition = "defaultPosition";
    private final String defaultLogin = "defaultLogin";
    private final String defaultPassword = "defaultPassword";
    private final int defaultId = 0;
    private final UserType defaultUserType = UserType.ADMIN;

    public UserSessionBuilderTest() {
        DateTimeBuilder dateTimeBuilder = new DateTimeBuilder();
        sessionBuilder = new UserSessionBuilder(dateTimeBuilder);
    }

    @Test
    void buildNewSessionForUser() {
        User standardUser = createDefaultUser();
        UserSession userSession = sessionBuilder.buildNewSessionForUser(standardUser);
        String currentTime = userSession.getCreationTime();
        assertTrue(
                currentTime.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}")
        );

        assertEquals(standardUser, userSession.getUser());
        String sessionId = userSession.getSessionId();
        assertTrue(
                sessionId.matches("\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}")
        );

    }

    private User createDefaultUser() {
        User user = new User();
        user.setFirstName(defaultFirstName);
        user.setLastName(defaultLastName);
        user.setPatronymic(defaultPatronymic);
        user.setLogin(defaultLogin);
        user.setPassword(defaultPassword);
        user.setUserType(defaultUserType);
        user.setId(defaultId);

        return user;
    }
}