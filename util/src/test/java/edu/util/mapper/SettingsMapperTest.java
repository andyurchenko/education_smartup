package edu.util.mapper;

import edu.dto.settings.DtoAdminSettingsResponse;
import edu.dto.settings.DtoClientSettingsResponse;
import edu.dto.settings.DtoNotLoggedSettingsResponse;
import edu.util.config.ValidationConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsMapperTest {

    private final int defaultMinPasswordLength = 10;

    private final int defaultMaxStringLength = 20;

    private final String defaultLoginPattern = "defaultLoginPattern";

    private final String defaultFullNamePattern = "defaultFullNamePattern";

    private final String defaultPhonePattern = "defaultPhonePattern";

    private final String defaultEmailPattern = "defaultEmailPattern";

    @Test
    void toAdminDto() {
        DtoAdminSettingsResponse standardDto = createDefaultDtoAdminSettingsResponse();
        ValidationConfig validationConfig = createDefaultValidationConfig();
        DtoAdminSettingsResponse dtoResponseFromMapper = SettingsMapper.INSTANCE.toAdminDto(validationConfig);
        assertEquals(standardDto, dtoResponseFromMapper);
    }

    @Test
    void toClientDto() {
        DtoClientSettingsResponse standardDto = createDefaultDtoClientSettingsResponse();
        ValidationConfig validationConfig = createDefaultValidationConfig();
        DtoClientSettingsResponse dtoResponseFromMapper = SettingsMapper.INSTANCE.toClientDto(validationConfig);
        assertEquals(standardDto, dtoResponseFromMapper);
    }

    @Test
    void toNotLoggedDto() {
        DtoNotLoggedSettingsResponse standardDto = createDefaultDtoNotLoggedSettingsResponse();
        ValidationConfig validationConfig = createDefaultValidationConfig();
        DtoNotLoggedSettingsResponse dtoResponseFromMapper = SettingsMapper.INSTANCE.toNotLoggedDto(validationConfig);
        assertEquals(standardDto, dtoResponseFromMapper);
    }

    private ValidationConfig createDefaultValidationConfig() {
        return new ValidationConfig(
                defaultMinPasswordLength, defaultMaxStringLength, defaultLoginPattern, defaultFullNamePattern, defaultPhonePattern, defaultEmailPattern
        );
    }

    private DtoAdminSettingsResponse createDefaultDtoAdminSettingsResponse() {
        DtoAdminSettingsResponse dto = new DtoAdminSettingsResponse();
        dto.setEmailPattern(defaultEmailPattern);
        dto.setLoginPattern(defaultLoginPattern);
        dto.setFullNamePattern(defaultFullNamePattern);
        dto.setPhonePattern(defaultPhonePattern);
        dto.setMinPasswordLength(defaultMinPasswordLength);
        dto.setMaxStringLength(defaultMaxStringLength);

        return dto;
    }

    private DtoClientSettingsResponse createDefaultDtoClientSettingsResponse() {
        DtoClientSettingsResponse dto = new DtoClientSettingsResponse();
        dto.setMinPasswordLength(defaultMinPasswordLength);
        dto.setMaxStringLength(defaultMaxStringLength);

        return dto;
    }

    private DtoNotLoggedSettingsResponse createDefaultDtoNotLoggedSettingsResponse() {
        DtoNotLoggedSettingsResponse dto = new DtoNotLoggedSettingsResponse();
        dto.setMinPasswordLength(defaultMinPasswordLength);
        dto.setMaxStringLength(defaultMaxStringLength);

        return dto;
    }
}