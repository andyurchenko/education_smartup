package edu.util;

import edu.model.User;
import edu.model.UserSession;
import edu.model.additional.UserType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

class UserSessionBuilderMockTest {
    private final String defaultFirstName = "defaultFirstName";
    private final String defaultLastName = "defaultLastName";
    private final String defaultPatronymic = "defaultPatronymic";
    private final String defaultPosition = "defaultPosition";
    private final String defaultLogin = "defaultLogin";
    private final String defaultPassword = "defaultPassword";
    private final int defaultId = 0;
    private final UserType defaultUserType = UserType.ADMIN;

    @Test
    void buildNewSessionForUser() {
        String dateTime = "2000-10-10 15:45:00";
        DateTimeBuilder dateTimeBuilderMock = Mockito.mock(DateTimeBuilder.class);
        Mockito
                .when(dateTimeBuilderMock.getCurrentDateTimeAsString())
                .thenReturn(dateTime);
        UserSessionBuilder userSessionBuilder = new UserSessionBuilder(dateTimeBuilderMock);
        User defaultUser = createDefaultUser();
        UserSession session = userSessionBuilder.buildNewSessionForUser(defaultUser);
        Mockito
                .verify(dateTimeBuilderMock, Mockito.times(1))
                .getCurrentDateTimeAsString();
        assertEquals(dateTime, session.getCreationTime());
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