package edu.util;

import edu.dto.client.*;
import edu.model.Client;
import edu.model.additional.UserType;

public class ClientTestData {
    public static final String DEFAULT_VALID_FIRST_NAME ="Имя-клиента-";
    public static final String DEFAULT_VALID_LAST_NAME ="Фамилия-клиента-";
    public static final String DEFAULT_VALID_PATRONYMIC ="Отчество-клиента-";
    public static final String DEFAULT_VALID_PHONE ="8(950)123-45-67";
    public static final String DEFAULT_VALID_EMAIL ="client@mail.ru";
    public static final String DEFAULT_VALID_PASSWORD ="Пароль-клиента-";
    public static final String DEFAULT_VALID_NEW_PASSWORD ="Новый-пароль-клиента-";
    public static final String DEFAULT_VALID_LOGIN ="ClientLogin";

    public static final String NEW_VALID_FIRST_NAME ="Иван";
    public static final String NEW_VALID_LAST_NAME ="Иванов";
    public static final String NEW_VALID_PATRONYMIC ="Иванович";
    public static final String NEW_VALID_PHONE ="8(908)987-65-43";
    public static final String NEW_VALID_EMAIL ="ivanov@mail.ru";

    public static final String DEFAULT_INVALID_FIRST_NAME = "test_client_first_name_";
    public static final String DEFAULT_INVALID_LAST_NAME = "test_client_last_name_";
    public static final String DEFAULT_INVALID_PATRONYMIC = "test_client_patronymic_";
    public static final String DEFAULT_INVALID_LOGIN = "test_client_login_";
    public static final String DEFAULT_INVALID_PASSWORD = "test_client_password_";
    public static final String DEFAULT_INVALID_EMAIL = "test_client_email_";
    public static final String DEFAULT_INVALID_PHONE = "test_client_phone_";

    public static final UserType USER_TYPE = UserType.CLIENT;
    public static final long ID = 0;

    public static DtoRegisterNewClientRequest createValidRegisterNewClientDtoRequest() {
        DtoRegisterNewClientRequest dto = new DtoRegisterNewClientRequest();
        dto.setFirstName(DEFAULT_VALID_FIRST_NAME);
        dto.setLastName(DEFAULT_VALID_LAST_NAME);
        dto.setPatronymic(DEFAULT_VALID_PATRONYMIC);
        dto.setEmail(DEFAULT_VALID_EMAIL);
        dto.setPhone(DEFAULT_VALID_PHONE);
        dto.setLogin(DEFAULT_VALID_LOGIN);
        dto.setPassword(DEFAULT_VALID_PASSWORD);

        return dto;
    }

    public static DtoRegisterNewClientRequest createValidRegisterNewClientDtoRequest(String suffix) {
        DtoRegisterNewClientRequest dto = new DtoRegisterNewClientRequest();
        dto.setFirstName(DEFAULT_VALID_FIRST_NAME + suffix);
        dto.setLastName(DEFAULT_VALID_LAST_NAME + suffix);
        dto.setPatronymic(DEFAULT_VALID_PATRONYMIC + suffix);
        dto.setEmail(DEFAULT_VALID_EMAIL);
        dto.setPhone(DEFAULT_VALID_PHONE);
        dto.setLogin(DEFAULT_VALID_LOGIN + suffix);
        dto.setPassword(DEFAULT_VALID_PASSWORD + suffix);

        return dto;
    }

    public static DtoRegisterNewClientResponse createValidRegisterNewClientDtoResponse() {
        DtoRegisterNewClientResponse dto = new DtoRegisterNewClientResponse();
        dto.setFirstName(DEFAULT_VALID_FIRST_NAME);
        dto.setLastName(DEFAULT_VALID_LAST_NAME);
        dto.setPatronymic(DEFAULT_VALID_PATRONYMIC);
        dto.setEmail(DEFAULT_VALID_EMAIL);
        dto.setPhone(DEFAULT_VALID_PHONE);
        dto.setUserType(USER_TYPE);
        dto.setId(ID);

        return dto;
    }

    public static DtoRegisterNewClientResponse createValidRegisterNewClientDtoResponse(String suffix) {
        DtoRegisterNewClientResponse dto = new DtoRegisterNewClientResponse();
        dto.setFirstName(DEFAULT_VALID_FIRST_NAME + suffix);
        dto.setLastName(DEFAULT_VALID_LAST_NAME + suffix);
        dto.setPatronymic(DEFAULT_VALID_PATRONYMIC + suffix);
        dto.setEmail(DEFAULT_VALID_EMAIL);
        dto.setPhone(DEFAULT_VALID_PHONE);
        dto.setUserType(USER_TYPE);
        dto.setId(ID);

        return dto;
    }

    public static DtoUpdateClientRequest createValidUpdateClientDtoRequest() {
        DtoUpdateClientRequest dto = new DtoUpdateClientRequest();
        dto.setFirstName(NEW_VALID_FIRST_NAME);
        dto.setLastName(NEW_VALID_LAST_NAME);
        dto.setPatronymic(NEW_VALID_PATRONYMIC);
        dto.setEmail(NEW_VALID_EMAIL);
        dto.setPhone(NEW_VALID_PHONE);
        dto.setOldPassword(DEFAULT_VALID_PASSWORD);
        dto.setNewPassword(DEFAULT_VALID_NEW_PASSWORD);

        return dto;
    }

    public static DtoUpdateClientResponse createValidUpdateClientDtoResponse() {
        DtoUpdateClientResponse dto = new DtoUpdateClientResponse();
        dto.setFirstName(DEFAULT_VALID_FIRST_NAME);
        dto.setLastName(DEFAULT_VALID_LAST_NAME);
        dto.setPatronymic(DEFAULT_VALID_PATRONYMIC);
        dto.setEmail(DEFAULT_VALID_EMAIL);
        dto.setPhone(DEFAULT_VALID_PHONE);
        dto.setUserType(USER_TYPE);

        return dto;
    }

    public static DtoGetClientInfoResponse createValidGetInfoClientDtoResponse() {
        DtoGetClientInfoResponse dto = new DtoGetClientInfoResponse();
        dto.setFirstName(DEFAULT_VALID_FIRST_NAME);
        dto.setLastName(DEFAULT_VALID_LAST_NAME);
        dto.setPatronymic(DEFAULT_VALID_PATRONYMIC);
        dto.setEmail(DEFAULT_VALID_EMAIL);
        dto.setPhone(DEFAULT_VALID_PHONE);
        dto.setUserType(USER_TYPE);
        dto.setId(ID);

        return dto;
    }

    public static Client createInvalidClientFromPattern(String clientSuffix) {
        Client client = new Client();
        client.setUserType(USER_TYPE);
        client.setPassword(DEFAULT_INVALID_PASSWORD + clientSuffix);
        client.setLogin(DEFAULT_INVALID_LOGIN + clientSuffix);
        client.setFirstName(DEFAULT_INVALID_FIRST_NAME + clientSuffix);
        client.setLastName(DEFAULT_INVALID_LAST_NAME + clientSuffix);
        client.setPatronymic(DEFAULT_INVALID_PATRONYMIC + clientSuffix);
        client.setEmail(DEFAULT_INVALID_EMAIL + clientSuffix);
        client.setPhone(DEFAULT_INVALID_PHONE + clientSuffix);

        return client;
    }

}
