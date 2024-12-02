package edu.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DateTimeBuilderTest {

    private final DateTimeBuilder dateTimeBuilder;

    public DateTimeBuilderTest() {
        dateTimeBuilder = new DateTimeBuilder();
    }

    @Test
    void getCurrentDateTimeAsString() {
        //TIP "yyyy-MM-dd hh:mm:ss" pattern of time
        String currentTime = dateTimeBuilder.getCurrentDateTimeAsString();
        assertTrue(
                currentTime.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}")
        );
    }
}