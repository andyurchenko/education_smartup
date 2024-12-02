package edu.util.mapper;

import edu.dto.administrator.DtoGetAdministratorInfoResponse;
import edu.dto.administrator.DtoRegisterNewAdministratorRequest;
import edu.dto.administrator.DtoRegisterNewAdministratorResponse;
import edu.dto.administrator.DtoUpdateAdministratorResponse;
import edu.model.Administrator;
import edu.model.additional.UserType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorMapperTest {
    private final String defaultFirstName = "defaultFirstName";
    private final String defaultLastName = "defaultLastName";
    private final String defaultPatronymic = "defaultPatronymic";
    private final String defaultPosition = "defaultPosition";
    private final String defaultLogin = "defaultLogin";
    private final String defaultPassword = "defaultPassword";
    private final int defaultId = 0;
    private final UserType defaultUserType = UserType.ADMIN;

    @Test
    void toModelFromRegisterDto() {
        Administrator standardAdministrator = createDefaultAdminModel();
        DtoRegisterNewAdministratorRequest dtoRequest = createDefaultRegisterDto();
        Administrator administratorFromMapper = AdministratorMapper.INSTANCE.toModelFromRegisterDto(dtoRequest);
        assertEquals(standardAdministrator, administratorFromMapper);
    }

    @Test
    void toDtoRegisterAdmin() {
        DtoRegisterNewAdministratorResponse standardDto = createDefaultRegisterResponse();
        Administrator standardAdministrator = createDefaultAdminModel();
        DtoRegisterNewAdministratorResponse dtoFromMapper = AdministratorMapper.INSTANCE.toDtoRegisterResponse(standardAdministrator);
        assertEquals(standardDto, dtoFromMapper);
    }

    @Test
    void toDtoGetInfo() {
        DtoGetAdministratorInfoResponse standardDto = createDefaultInfoResponse();
        Administrator standardAdministrator = createDefaultAdminModel();
        DtoGetAdministratorInfoResponse dtoFromMapper = AdministratorMapper.INSTANCE.toDtoGetInfoResponse(standardAdministrator);
        assertEquals(standardDto, dtoFromMapper);
    }

    @Test
    void toDtoUpdateInfo() {
        DtoGetAdministratorInfoResponse standardDto = createDefaultInfoResponse();
        Administrator standardAdministrator = createDefaultAdminModel();
        DtoGetAdministratorInfoResponse dtoFromMapper = AdministratorMapper.INSTANCE.toDtoGetInfoResponse(standardAdministrator);
        assertEquals(standardDto, dtoFromMapper);
    }

    private Administrator createDefaultAdminModel() {
        Administrator administrator = new Administrator();
        administrator.setFirstName(defaultFirstName);
        administrator.setLastName(defaultLastName);
        administrator.setPatronymic(defaultPatronymic);
        administrator.setPosition(defaultPosition);
        administrator.setLogin(defaultLogin);
        administrator.setPassword(defaultPassword);
        administrator.setUserType(defaultUserType);
        administrator.setId(defaultId);

        return administrator;
    }

    private DtoRegisterNewAdministratorRequest createDefaultRegisterDto() {
        DtoRegisterNewAdministratorRequest dto = new DtoRegisterNewAdministratorRequest();
        dto.setFirstName(defaultFirstName);
        dto.setLastName(defaultLastName);
        dto.setPatronymic(defaultPatronymic);
        dto.setPosition(defaultPosition);
        dto.setLogin(defaultLogin);
        dto.setPassword(defaultPassword);

        return dto;
    }

    private DtoRegisterNewAdministratorResponse createDefaultRegisterResponse() {
        DtoRegisterNewAdministratorResponse dto = new DtoRegisterNewAdministratorResponse();
        dto.setId(defaultId);
        dto.setFirstName(defaultFirstName);
        dto.setLastName(defaultLastName);
        dto.setPatronymic(defaultPatronymic);
        dto.setPosition(defaultPosition);
        dto.setUserType(defaultUserType);

        return dto;
    }

    private DtoGetAdministratorInfoResponse createDefaultInfoResponse() {
        DtoGetAdministratorInfoResponse dto = new DtoGetAdministratorInfoResponse();
        dto.setId(defaultId);
        dto.setFirstName(defaultFirstName);
        dto.setLastName(defaultLastName);
        dto.setPatronymic(defaultPatronymic);
        dto.setPosition(defaultPosition);
        dto.setUserType(defaultUserType);

        return dto;
    }

    private DtoUpdateAdministratorResponse createDefaultUpdateResponse() {
        DtoUpdateAdministratorResponse dto = new DtoUpdateAdministratorResponse();
        dto.setFirstName(defaultFirstName);
        dto.setLastName(defaultLastName);
        dto.setPatronymic(defaultPatronymic);
        dto.setPosition(defaultPosition);
        dto.setUserType(defaultUserType);

        return dto;
    }
}