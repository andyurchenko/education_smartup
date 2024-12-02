package edu.util.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/validation.properties")
public class ValidationConfig {
    @Value("${min_password_length}")
    private int minPasswordLength;

    @Value("${max_name_length}")
    private int maxStringLength;

    @Value("${login.regexp}")
    private String loginPattern;

    @Value("${full_name.regexp}")
    private String fullNamePattern;

    @Value("${phone.regexp}")
    private String phonePattern;

    @Value("${email.regexp}")
    private String emailPattern;

    public ValidationConfig() {
    }

    public ValidationConfig(int minPasswordLength, int maxStringLength, String loginPattern, String fullNamePattern, String phonePattern, String emailPattern) {
        this.minPasswordLength = minPasswordLength;
        this.maxStringLength = maxStringLength;
        this.loginPattern = loginPattern;
        this.fullNamePattern = fullNamePattern;
        this.phonePattern = phonePattern;
        this.emailPattern = emailPattern;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public int getMaxStringLength() {
        return maxStringLength;
    }

    public String getLoginPattern() {
        return loginPattern;
    }

    public String getFullNamePattern() {
        return fullNamePattern;
    }

    public String getPhonePattern() {
        return phonePattern;
    }

    public String getEmailPattern() {
        return emailPattern;
    }
}
