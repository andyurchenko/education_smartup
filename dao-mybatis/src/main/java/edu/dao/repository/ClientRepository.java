package edu.dao.repository;

import edu.model.Client;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientRepository {
    void insertNewClient(@Param("CLIENT") Client client);
    void deleteAllFromClientTable();
    List<Client> getAllClients();
    void updateClientInfo(@Param("CLIENT") Client client);
}
