package edu.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {
    @Value("${user_idle_timeout}")
    private int userIdleTimeout;
    @Value("${java_session_id_name}")
    private String javaSessionIdName;
    @Value("${phone_cut_pattern}")
    private String phoneCutPattern;

    @Value("${bus_place_pattern}")
    private String busPlacePattern;

    @Value("${ticket_pattern}")
    private String ticketPattern;

    public ApplicationConfig() {
    }

    public int getUserIdleTimeout() {
        return userIdleTimeout;
    }

    public String getJavaSessionIdName() {
        return javaSessionIdName;
    }

    public String getPhoneCutPattern() {
        return phoneCutPattern;
    }

    public String getBusPlacePattern() {
        return busPlacePattern;
    }

    public String getTicketPattern() {
        return ticketPattern;
    }
}