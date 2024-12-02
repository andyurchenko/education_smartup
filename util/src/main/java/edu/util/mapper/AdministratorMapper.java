package edu.util.mapper;


import edu.dto.administrator.*;
import edu.model.Administrator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AdministratorMapper {
    AdministratorMapper INSTANCE = Mappers.getMapper(AdministratorMapper.class);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "patronymic", target = "patronymic")
    @Mapping(source = "login", target = "login")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "userType", constant = "ADMIN")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountActive", ignore = true)
    Administrator toModelFromRegisterDto(DtoRegisterNewAdministratorRequest dto);

    DtoRegisterNewAdministratorResponse toDtoRegisterResponse(Administrator model);

    DtoAdministratorResponse toDtoAdministratorResponse(Administrator model);

    DtoGetAdministratorInfoResponse toDtoGetInfoResponse(Administrator model);

    DtoUpdateAdministratorResponse toDtoUpdateInfoResponse(Administrator model);
}
