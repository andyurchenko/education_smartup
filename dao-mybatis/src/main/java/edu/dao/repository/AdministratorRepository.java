package edu.dao.repository;


import edu.model.Administrator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdministratorRepository {
    Administrator getAdministratorById(@Param("ID") long id);
    void insertNewAdministrator(@Param("ADMINISTRATOR") Administrator administrator);
    void deleteAllFromAdministratorTable();
    Integer getAdministratorCount();
    void updateAdministratorInfo(@Param("ADMINISTRATOR") Administrator administrator);
}
