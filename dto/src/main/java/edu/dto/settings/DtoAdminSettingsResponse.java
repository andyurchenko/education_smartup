package edu.dto.settings;

import java.util.Objects;

public class DtoAdminSettingsResponse {
    private int minPasswordLength;
    private int maxStringLength;
    private String loginPattern;
    private String fullNamePattern;
    private String phonePattern;
    private String emailPattern;

    public DtoAdminSettingsResponse() {
    }

    public DtoAdminSettingsResponse(int minPasswordLength, int maxStringLength, String loginPattern, String fullNamePattern, String phonePattern, String emailPattern) {
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

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    public int getMaxStringLength() {
        return maxStringLength;
    }

    public void setMaxStringLength(int maxStringLength) {
        this.maxStringLength = maxStringLength;
    }

    public String getLoginPattern() {
        return loginPattern;
    }

    public void setLoginPattern(String loginPattern) {
        this.loginPattern = loginPattern;
    }

    public String getFullNamePattern() {
        return fullNamePattern;
    }

    public void setFullNamePattern(String fullNamePattern) {
        this.fullNamePattern = fullNamePattern;
    }

    public String getPhonePattern() {
        return phonePattern;
    }

    public void setPhonePattern(String phonePattern) {
        this.phonePattern = phonePattern;
    }

    public String getEmailPattern() {
        return emailPattern;
    }

    public void setEmailPattern(String emailPattern) {
        this.emailPattern = emailPattern;
    }

    @Override
    public String toString() {
        return "DtoAdminSettingsResponse{" +
                "minPasswordLength=" + minPasswordLength +
                ", maxStringLength=" + maxStringLength +
                ", loginPattern='" + loginPattern + '\'' +
                ", fullNamePattern='" + fullNamePattern + '\'' +
                ", phonePattern='" + phonePattern + '\'' +
                ", emailPattern='" + emailPattern + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoAdminSettingsResponse that)) return false;
        return getMinPasswordLength() == that.getMinPasswordLength() && getMaxStringLength() == that.getMaxStringLength() && Objects.equals(getLoginPattern(), that.getLoginPattern()) && Objects.equals(getFullNamePattern(), that.getFullNamePattern()) && Objects.equals(getPhonePattern(), that.getPhonePattern()) && Objects.equals(getEmailPattern(), that.getEmailPattern());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMinPasswordLength(), getMaxStringLength(), getLoginPattern(), getFullNamePattern(), getPhonePattern(), getEmailPattern());
    }
}
