package edu.dao;

import edu.error.ApplicationException;
import edu.model.Client;
import edu.model.UserSession;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface ClientDao {
    Pair<Client, UserSession> addNewClientAndStartNewClientSession(Client client) throws ApplicationException;
    void deleteAllFromClientTable() throws ApplicationException;
    List<Client> getAllClients() throws ApplicationException;
    void updateClientInfo(Client client) throws ApplicationException;
}
