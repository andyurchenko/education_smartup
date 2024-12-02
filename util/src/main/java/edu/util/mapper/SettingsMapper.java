package edu.util.mapper;

import edu.dto.settings.DtoAdminSettingsResponse;
import edu.dto.settings.DtoClientSettingsResponse;
import edu.dto.settings.DtoNotLoggedSettingsResponse;
import edu.util.config.ValidationConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SettingsMapper {
    SettingsMapper INSTANCE = Mappers.getMapper(SettingsMapper.class);
    DtoAdminSettingsResponse toAdminDto(ValidationConfig validationConfig);
    DtoClientSettingsResponse toClientDto(ValidationConfig validationConfig);
    DtoNotLoggedSettingsResponse toNotLoggedDto(ValidationConfig validationConfig);
}