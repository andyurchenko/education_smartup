package edu.dao.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.Reader;

@Component
public class SqlSessionBuilder {
    private SqlSessionFactory sqlSessionFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlSessionBuilder.class);

    public SqlSessionBuilder() {
        try (Reader r = Resources.getResourceAsReader("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(r);
        } catch (IOException e) {
            LOGGER.error("Error loading mybatis-config.xml", e);
        }
    }

    public SqlSession openSqlSession() {
        return sqlSessionFactory.openSession();
    }
}
