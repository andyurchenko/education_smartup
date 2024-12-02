package edu.service;

import edu.configuration.ApplicationConfig;
import edu.dao.ClientDao;
import edu.dao.UserDao;
import edu.dto.client.*;
import edu.error.*;
import edu.model.Client;
import edu.model.User;
import edu.model.UserSession;
import edu.util.mapper.ClientMapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ClientService extends ServiceBase {
    private final ClientDao clientDao;
    private final ClientMapper clientMapper;
    private final ApplicationConfig applicationConfig;

    @Autowired
    public ClientService(ClientDao clientDao, UserDao userDao, ClientMapper clientMapper, ApplicationConfig applicationConfig) {
        super(userDao);
        this.clientDao = clientDao;
        this.clientMapper = clientMapper;
        this.applicationConfig = applicationConfig;
    }

    public Pair<DtoRegisterNewClientResponse, String> registerNewClient(DtoRegisterNewClientRequest dtoRequest) throws ApplicationException {
        Client client = clientMapper.toModelFromRegisterDtoRequest(dtoRequest);
        stripPhoneNumberUnnecessaryCharacters(client);
        Pair<Client, UserSession> clientAndSessionId = clientDao.addNewClientAndStartNewClientSession(client);

        return new ImmutablePair<>(
                clientMapper.toDtoRegisterResponse(clientAndSessionId.getLeft()),
                clientAndSessionId.getRight().getSessionId()
        );
    }

    public DtoUpdateClientResponse updateClientInfo(String sessionId, DtoUpdateClientRequest dtoRequest) throws ApplicationException {
        User user = userDao.getUserBySessionId(sessionId);
        if (user instanceof Client) {
            Client client = updateClientWithNewInfo((Client) user, dtoRequest);
            stripPhoneNumberUnnecessaryCharacters(client);
            clientDao.updateClientInfo(client);

            return clientMapper.toDtoUpdateInfoResponse(client);

        }

        throw new ApplicationException(ApplicationErrorDetails.NOT_ALLOWED_REQUEST, HttpStatus.METHOD_NOT_ALLOWED);
    }

    private Client updateClientWithNewInfo(Client client, DtoUpdateClientRequest dtoRequest) {
        client.setFirstName(dtoRequest.getFirstName());
        client.setLastName(dtoRequest.getLastName());
        client.setPatronymic(dtoRequest.getPatronymic());
        client.setEmail(dtoRequest.getEmail());
        client.setPhone(dtoRequest.getPhone());
        client.setPassword(dtoRequest.getNewPassword());

        return client;
    }

    private void stripPhoneNumberUnnecessaryCharacters(Client client) {
        client.setPhone(
                client.getPhone().replaceAll(applicationConfig.getPhoneCutPattern(), "")
        );
    }
}
