package edu.util;

import edu.dto.administrator.DtoRegisterNewAdministratorRequest;
import edu.dto.administrator.DtoRegisterNewAdministratorResponse;
import edu.dto.administrator.DtoUpdateAdministratorRequest;
import edu.dto.administrator.DtoUpdateAdministratorResponse;
import edu.model.Administrator;
import edu.model.additional.UserType;

public class AdminTestData {
    public static final String DEFAULT_VALID_FIRST_NAME = "Имя-администратора-";
    public static final String DEFAULT_VALID_LAST_NAME = "Фамилия-администратора-";
    public static final String DEFAULT_VALID_PATRONYMIC = "Отчество-администратора-";
    public static final String DEFAULT_VALID_POSITION = "Должность-администратора-";
    public static final String DEFAULT_VALID_PASSWORD = "Пароль-администратора-";
    public static final String DEFAULT_VALID_LOGIN = "AdminLogin";

    public static final String DEFAULT_INVALID_FIRST_NAME = "test_admin_first_name_";
    public static final String DEFAULT_INVALID_LAST_NAME = "test_admin_last_name_";
    public static final String DEFAULT_INVALID_PATRONYMIC = "test_admin_patronymic_";
    public static final String DEFAULT_INVALID_LOGIN = "test_admin_login_";
    public static final String DEFAULT_INVALID_PASSWORD = "test_admin_password_";
    public static final String DEFAULT_INVALID_POSITION = "test_admin_position_";

    public static final String NEW_VALID_FIRST_NAME = "Новое-имя-администратора-";
    public static final String NEW_VALID_LAST_NAME = "Новая-фамилия-администратора-";
    public static final String NEW_VALID_PATRONYMIC = "Новое-отчество-администратора-";
    public static final String NEW_VALID_PASSWORD = "Новый-Пароль-администратора-";
    public static final String NEW_VALID_POSITION = "Новая-Должность-администратора-";
    
    public static final long ID = 0;
    public static final UserType USER_TYPE = UserType.ADMIN;





    public static DtoRegisterNewAdministratorRequest createValidRegisterNewAdministratorDtoRequest() {
        var dtoRequest = new DtoRegisterNewAdministratorRequest();

        dtoRequest.setFirstName(DEFAULT_VALID_FIRST_NAME);
        dtoRequest.setLastName(DEFAULT_VALID_LAST_NAME);
        dtoRequest.setPatronymic(DEFAULT_VALID_PATRONYMIC);
        dtoRequest.setPosition(DEFAULT_VALID_POSITION);
        dtoRequest.setPassword(DEFAULT_VALID_PASSWORD);
        dtoRequest.setLogin(DEFAULT_VALID_LOGIN);

        return dtoRequest;
    }

    public static DtoRegisterNewAdministratorRequest createValidRegisterNewAdministratorDtoRequest(String suffix) {
        var dtoRequest = new DtoRegisterNewAdministratorRequest();

        dtoRequest.setFirstName(DEFAULT_VALID_FIRST_NAME + suffix);
        dtoRequest.setLastName(DEFAULT_VALID_LAST_NAME + suffix);
        dtoRequest.setPatronymic(DEFAULT_VALID_PATRONYMIC + suffix);
        dtoRequest.setPosition(DEFAULT_VALID_POSITION + suffix);
        dtoRequest.setPassword(DEFAULT_VALID_PASSWORD + suffix);
        dtoRequest.setLogin(DEFAULT_VALID_LOGIN + suffix);

        return dtoRequest;
    }

    public static DtoRegisterNewAdministratorResponse createValidRegisterAdministratorDtoResponse() {
        var dtoResponse = new DtoRegisterNewAdministratorResponse();

        dtoResponse.setFirstName(DEFAULT_VALID_FIRST_NAME);
        dtoResponse.setLastName(DEFAULT_VALID_LAST_NAME);
        dtoResponse.setPatronymic(DEFAULT_VALID_PATRONYMIC);
        dtoResponse.setPosition(DEFAULT_VALID_POSITION);
        dtoResponse.setId(ID);
        dtoResponse.setUserType(USER_TYPE);

        return dtoResponse;
    }

    public static DtoRegisterNewAdministratorResponse createValidRegisterAdministratorDtoResponse(String suffix) {
        var dtoResponse = new DtoRegisterNewAdministratorResponse();

        dtoResponse.setFirstName(DEFAULT_VALID_FIRST_NAME + suffix);
        dtoResponse.setLastName(DEFAULT_VALID_LAST_NAME + suffix);
        dtoResponse.setPatronymic(DEFAULT_VALID_PATRONYMIC + suffix);
        dtoResponse.setPosition(DEFAULT_VALID_POSITION + suffix);
        dtoResponse.setId(ID);
        dtoResponse.setUserType(USER_TYPE);

        return dtoResponse;
    }

    public static DtoUpdateAdministratorRequest createValidUpdateAdministratorDtoRequest() {
        var dtoRequest = new DtoUpdateAdministratorRequest();

        dtoRequest.setFirstName(DEFAULT_VALID_FIRST_NAME);
        dtoRequest.setLastName(DEFAULT_VALID_LAST_NAME);
        dtoRequest.setPatronymic(DEFAULT_VALID_PATRONYMIC);
        dtoRequest.setPosition(DEFAULT_VALID_POSITION);
        dtoRequest.setNewPassword(DEFAULT_VALID_PASSWORD);
        dtoRequest.setOldPassword(NEW_VALID_PASSWORD);

        return dtoRequest;
    }

    public static DtoUpdateAdministratorRequest createValidUpdateAdministratorDtoRequest(String suffix) {
        var dtoUpdateRequest = new DtoUpdateAdministratorRequest();
        dtoUpdateRequest.setFirstName(NEW_VALID_FIRST_NAME + suffix);
        dtoUpdateRequest.setLastName(NEW_VALID_LAST_NAME + suffix);
        dtoUpdateRequest.setPatronymic(NEW_VALID_PATRONYMIC + suffix);
        dtoUpdateRequest.setPosition(NEW_VALID_POSITION + suffix);
        dtoUpdateRequest.setOldPassword(DEFAULT_VALID_PASSWORD + suffix);
        dtoUpdateRequest.setNewPassword(NEW_VALID_PASSWORD + suffix);

        return dtoUpdateRequest;
    }

    public static DtoUpdateAdministratorRequest createValidUpdateAdministratorDtoRequest(Administrator administrator) {
        var dtoUpdateRequest = new DtoUpdateAdministratorRequest();
        dtoUpdateRequest.setFirstName(NEW_VALID_FIRST_NAME);
        dtoUpdateRequest.setLastName(NEW_VALID_LAST_NAME);
        dtoUpdateRequest.setPatronymic(NEW_VALID_PATRONYMIC);
        dtoUpdateRequest.setPosition(NEW_VALID_POSITION);
        dtoUpdateRequest.setOldPassword(administrator.getPassword());
        dtoUpdateRequest.setNewPassword(NEW_VALID_PASSWORD);

        return dtoUpdateRequest;
    }

    public static DtoUpdateAdministratorResponse createValidUpdateAdministratorDtoResponse() {
        var dtoRequest = new DtoUpdateAdministratorResponse();

        dtoRequest.setFirstName(DEFAULT_VALID_FIRST_NAME);
        dtoRequest.setLastName(DEFAULT_VALID_LAST_NAME);
        dtoRequest.setPatronymic(DEFAULT_VALID_PATRONYMIC);
        dtoRequest.setPosition(DEFAULT_VALID_POSITION);
        dtoRequest.setUserType(USER_TYPE);

        return dtoRequest;
    }

    public static Administrator createAdminFromPattern(String adminSuffix) {
        Administrator administrator = new Administrator();
        administrator.setFirstName(DEFAULT_INVALID_FIRST_NAME + adminSuffix);
        administrator.setLastName(DEFAULT_INVALID_LAST_NAME + adminSuffix);
        administrator.setPatronymic(DEFAULT_INVALID_PATRONYMIC + adminSuffix);
        administrator.setPosition(DEFAULT_INVALID_POSITION + adminSuffix);
        administrator.setLogin(DEFAULT_INVALID_LOGIN + adminSuffix);
        administrator.setPassword(DEFAULT_INVALID_PASSWORD + adminSuffix);
        administrator.setUserType(USER_TYPE);

        return administrator;
    }
}
