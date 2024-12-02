package edu.util.mapper;

import edu.dto.client.DtoGetClientInfoResponse;
import edu.dto.client.DtoRegisterNewClientRequest;
import edu.dto.client.DtoRegisterNewClientResponse;
import edu.dto.client.DtoUpdateClientResponse;
import edu.model.Client;
import edu.model.additional.UserType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {
    private final String defaultFirstName = "defaultFirstName";
    private final String defaultLastName = "defaultLastName";
    private final String defaultPatronymic = "defaultPatronymic";
    private final String defaultLogin = "defaultLogin";
    private final String defaultPassword = "defaultPassword";
    private final int defaultId = 0;
    private final UserType defaultUserType = UserType.CLIENT;
    private final String defaultEmail = "defaultEmail";
    private final String defaultPhone = "defaultPhone";

    @Test
    void toModelFromRegisterDto() {
        DtoRegisterNewClientRequest dtoRequest = createDefaultRequestDto();
        Client standardClient = createDefaultClientModel();
        Client clientFromMapper = ClientMapper.INSTANCE.toModelFromRegisterDtoRequest(dtoRequest);
        assertEquals(standardClient, clientFromMapper);
    }

    @Test
    void toDtoRegisterClientResponse() {
        DtoRegisterNewClientResponse standardDto = createDefaultRegisterNewClientDtoResponse();
        Client client = createDefaultClientModel();
        DtoRegisterNewClientResponse dtoFromMapper = ClientMapper.INSTANCE.toDtoRegisterResponse(client);
        assertEquals(standardDto, dtoFromMapper);
    }

    @Test
    void toDtoGetInfoResponse() {
        DtoGetClientInfoResponse standardDto = createDefaultGetInfoClientDtoResponse();
        Client client = createDefaultClientModel();
        DtoGetClientInfoResponse dtoFromMapper = ClientMapper.INSTANCE.toDtoGetInfoResponse(client);
        assertEquals(standardDto, dtoFromMapper);
    }

    @Test
    void toDtoUpdateInfoResponse() {
        DtoUpdateClientResponse standardDto = createDefaultUpdateClientDtoResponse();
        Client client = createDefaultClientModel();
        DtoUpdateClientResponse dtoFromMapper = ClientMapper.INSTANCE.toDtoUpdateInfoResponse(client);
        assertEquals(standardDto, dtoFromMapper);
    }

    @Test
    void toDtoGetClientListResponse() {
        List<DtoGetClientInfoResponse> standardDtoList = new ArrayList<>();
        standardDtoList.add(createDefaultGetInfoClientDtoResponse());
        standardDtoList.add(createDefaultGetInfoClientDtoResponse());

        List<Client> clients = new ArrayList<>();
        clients.add(createDefaultClientModel());
        clients.add(createDefaultClientModel());

        List<DtoGetClientInfoResponse> dtoListFromMapper = ClientMapper.INSTANCE.toDtoGetClientListResponse(clients);

        assertEquals(2, dtoListFromMapper.size());
        assertEquals(standardDtoList, dtoListFromMapper);
    }

    private Client createDefaultClientModel() {
        Client client = new Client();
        client.setId(defaultId);
        client.setFirstName(defaultFirstName);
        client.setLastName(defaultLastName);
        client.setPatronymic(defaultPatronymic);
        client.setLogin(defaultLogin);
        client.setPassword(defaultPassword);
        client.setPhone(defaultPhone);
        client.setEmail(defaultEmail);
        client.setUserType(defaultUserType);

        return client;
    }

    private DtoRegisterNewClientRequest createDefaultRequestDto() {
        DtoRegisterNewClientRequest dto = new DtoRegisterNewClientRequest();
        dto.setFirstName(defaultFirstName);
        dto.setLastName(defaultLastName);
        dto.setPatronymic(defaultPatronymic);
        dto.setEmail(defaultEmail);
        dto.setPhone(defaultPhone);
        dto.setLogin(defaultLogin);
        dto.setPassword(defaultPassword);

        return dto;
    }

    private DtoRegisterNewClientResponse createDefaultRegisterNewClientDtoResponse() {
        DtoRegisterNewClientResponse dto = new DtoRegisterNewClientResponse();
        dto.setFirstName(defaultFirstName);
        dto.setLastName(defaultLastName);
        dto.setPatronymic(defaultPatronymic);
        dto.setEmail(defaultEmail);
        dto.setPhone(defaultPhone);
        dto.setUserType(defaultUserType);
        dto.setId(defaultId);

        return dto;
    }

    private DtoGetClientInfoResponse createDefaultGetInfoClientDtoResponse() {
        DtoGetClientInfoResponse dto = new DtoGetClientInfoResponse();
        dto.setFirstName(defaultFirstName);
        dto.setLastName(defaultLastName);
        dto.setPatronymic(defaultPatronymic);
        dto.setEmail(defaultEmail);
        dto.setPhone(defaultPhone);
        dto.setUserType(defaultUserType);
        dto.setId(defaultId);

        return dto;
    }

    private DtoUpdateClientResponse createDefaultUpdateClientDtoResponse() {
        DtoUpdateClientResponse dto = new DtoUpdateClientResponse();
        dto.setFirstName(defaultFirstName);
        dto.setLastName(defaultLastName);
        dto.setPatronymic(defaultPatronymic);
        dto.setEmail(defaultEmail);
        dto.setPhone(defaultPhone);
        dto.setUserType(defaultUserType);

        return dto;
    }
}