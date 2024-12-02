package edu.dao.impl;

import edu.dao.ClientDao;
import edu.dao.repository.ClientRepository;
import edu.dao.repository.SessionRepository;
import edu.dao.repository.UserRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.*;
import edu.model.Client;
import edu.model.UserSession;
import edu.util.UserSessionBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClientDaoImpl extends Dao implements ClientDao {
    private final SqlSessionBuilder sqlSessionBuilder;
    private final UserSessionBuilder userSessionBuilder;

    @Autowired
    public ClientDaoImpl(SqlSessionBuilder sqlSessionBuilder, UserSessionBuilder userSessionBuilder) {
        super(LoggerFactory.getLogger(ClientDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
        this.userSessionBuilder = userSessionBuilder;
    }


    @Override
    public Pair<Client, UserSession> addNewClientAndStartNewClientSession(Client client) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                UserRepository userRepository = sqlSession.getMapper(UserRepository.class);
                userRepository.insertNewUser(client);
                ClientRepository clientRepository = sqlSession.getMapper(ClientRepository.class);
                clientRepository.insertNewClient(client);
                SessionRepository sessionRepository = sqlSession.getMapper(SessionRepository.class);
                UserSession userSession = userSessionBuilder.buildNewSessionForUser(client);
                sessionRepository.insertNewSession(userSession);
                sqlSession.commit();

                return new ImmutablePair<>(client, userSession);

            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("LOGIN", client.getLogin());
                        throw new ApplicationException(
                                ApplicationErrorDetails.LOGIN_ALREADY_EXISTS,
                                params,
                                HttpStatus.INTERNAL_SERVER_ERROR
                        );
                    }

                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public void deleteAllFromClientTable() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                ClientRepository clientRepository = sqlSession.getMapper(ClientRepository.class);
                clientRepository.deleteAllFromClientTable();
                sqlSession.commit();
            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public List<Client> getAllClients() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                ClientRepository clientRepository = sqlSession.getMapper(ClientRepository.class);

                return clientRepository.getAllClients();

            } catch (RuntimeException e) {
                logErrors(e);
                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public void updateClientInfo(Client client) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                ClientRepository clientRepository = sqlSession.getMapper(ClientRepository.class);
                clientRepository.updateClientInfo(client);
                sqlSession.commit();
            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
