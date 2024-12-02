package edu.dao.impl;

import edu.dao.SettingsDao;
import edu.dao.repository.SettingsRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.*;
import edu.model.additional.UserType;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public class SettingsDaoImpl extends Dao implements SettingsDao {
    private final SqlSessionBuilder sqlSessionBuilder;
    @Autowired
    public SettingsDaoImpl(SqlSessionBuilder sqlSessionBuilder) {
        super(LoggerFactory.getLogger(SettingsDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
    }

    @Override
    public UserType getUserTypeBySessionId(String sessionId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                SettingsRepository repository = sqlSession.getMapper(SettingsRepository.class);
                return repository.getUserTypeBySessionId(sessionId);
            } catch (RuntimeException e) {
                logErrors(e);
                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
    }
}
