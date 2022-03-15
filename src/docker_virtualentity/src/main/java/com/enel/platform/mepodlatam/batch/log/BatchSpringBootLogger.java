package com.enel.platform.mepodlatam.batch.log;

import org.slf4j.Logger;

import com.enel.platform.entity.utils.log.BatchLogger;

public class BatchSpringBootLogger implements BatchLogger {
    
	private final Logger logger;
 
    public BatchSpringBootLogger(Logger logger) {
        this.logger = logger;
    }
 
    @Override
    public void error(String message) {
        this.logger.error(message);
    }
 
    @Override
    public void error(String message, Throwable t) {
        this.logger.error(message, t);
    }
 
    @Override
    public void warn(String message) {
        this.logger.warn(message);
    }
 
    @Override
    public void info(String message) {
        this.logger.info(message);
    }
 
    @Override
    public void debug(String message) {
        this.logger.debug(message);
    }
 
    @Override
    public void trace(String message) {
        this.logger.trace(message);
    }
}