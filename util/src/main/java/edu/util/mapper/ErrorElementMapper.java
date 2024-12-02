package edu.util.mapper;

import edu.dto.error.DtoErrorResponseElement;
import edu.error.ErrorResponseElement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ErrorElementMapper {
    DtoErrorResponseElement toDto(ErrorResponseElement errorResponseElement);
    ErrorResponseElement toModel(DtoErrorResponseElement dtoErrorResponseElement);
}
