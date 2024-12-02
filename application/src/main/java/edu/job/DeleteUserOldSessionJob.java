package edu.job;

import edu.error.ApplicationException;
import edu.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DeleteUserOldSessionJob {
    private final SessionService sessionService;

    private final Logger logger = LoggerFactory.getLogger(DeleteUserOldSessionJob.class);

    @Autowired
    public DeleteUserOldSessionJob(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(initialDelayString = "${delete_old_sessions_rate}", fixedDelayString = "${delete_old_sessions_rate}", timeUnit = TimeUnit.SECONDS)
    public void deleteOldUserSessions() throws ApplicationException {
        logger
            .atDebug()
            .setMessage("Scheduled JOB to delete user's old session has started.")
            .log();

        sessionService.deleteUserOldSession();
    }
}
