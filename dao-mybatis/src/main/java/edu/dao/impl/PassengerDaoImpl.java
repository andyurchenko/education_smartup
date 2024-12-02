package edu.dao.impl;

import edu.dao.PassengerDao;
import edu.dao.repository.PassengerRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import edu.model.additional.Passenger;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public class PassengerDaoImpl extends Dao implements PassengerDao {
    private final SqlSessionBuilder sqlSessionBuilder;

    public PassengerDaoImpl(SqlSessionBuilder sqlSessionBuilder) {
        super(LoggerFactory.getLogger(PassengerDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
    }

    @Override
    public Passenger getPassengerByFullNameAndPassportAndOrderId(String firstName, String lastName, String passport, Long orderId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                PassengerRepository passengerRepository = sqlSession.getMapper(PassengerRepository.class);

                return passengerRepository.selectPassengerByFullNameAndPassport(firstName, lastName, passport, orderId);

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
