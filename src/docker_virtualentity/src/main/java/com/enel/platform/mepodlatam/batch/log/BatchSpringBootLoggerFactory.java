package com.enel.platform.mepodlatam.batch.log;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enel.platform.entity.utils.log.BatchLogger;
import com.enel.platform.entity.utils.log.BatchLoggerFactory;

@Component
public class BatchSpringBootLoggerFactory implements BatchLoggerFactory {
	    @Override
	    public BatchLogger getLogger(Class<?> cls) {
	        return new BatchSpringBootLogger(LoggerFactory.getLogger(cls));
	    }
	}
