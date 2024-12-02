package edu.service;

import edu.dao.SettingsDao;
import edu.dao.UserDao;
import edu.error.ApplicationException;
import edu.util.config.ValidationConfig;
import edu.model.additional.UserType;
import edu.util.mapper.SettingsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService extends ServiceBase {
    private final ValidationConfig validationConfig;
    private final SettingsMapper settingsMapper;
    private final SettingsDao settingsDao;

    @Autowired
    public SettingsService(ValidationConfig validationConfig, SettingsMapper settingsMapper, SettingsDao settingsDao, UserDao userDao) {
        super(userDao);
        this.validationConfig = validationConfig;
        this.settingsMapper = settingsMapper;
        this.settingsDao = settingsDao;
    }

    public Object getServerSettings(String sessionId) throws ApplicationException {
        UserType userType = settingsDao.getUserTypeBySessionId(sessionId);

        if (userType != null) {
            switch (userType) {
                case ADMIN:
                    return settingsMapper.toAdminDto(validationConfig);

                case CLIENT:
                    return settingsMapper.toClientDto(validationConfig);

                default:
                    settingsMapper.toNotLoggedDto(validationConfig);
            }
        }

        return settingsMapper.toNotLoggedDto(validationConfig);
    }
}
