package edu.dao.impl;

import org.slf4j.Logger;

public abstract class Dao {
    protected final Logger logger;
    protected final int SUCCESS = 1;
    protected final int FAILURE = 0;

    public Dao(Logger logger) {
        this.logger = logger;
    }

    protected void logErrors(RuntimeException e) {
        logger.atError().setMessage(e.getMessage()).log();
        logger.atError().setCause(e.getCause()).log();
    }
}
