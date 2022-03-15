package com.enel.platform.mepodlatam.util;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;

import org.jdbi.v3.core.statement.ParsedSql;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4JSqlLogger implements SqlLogger {
	  
	
	    private final Logger log;

	    public Slf4JSqlLogger() {
	        this(LoggerFactory.getLogger("org.jdbi.sql"));
	    }

	    public Slf4JSqlLogger(Logger log) {
	        this.log = log;
	    }

	    @Override
	    public void logAfterExecution(StatementContext context) {	        
	            log.info("Executed in {} '{}' with parameters '{}'",
	                    format(Duration.between(context.getExecutionMoment(), context.getCompletionMoment())),
	                    context.getParsedSql().getSql(),
	                    context.getBinding());
	        
	    }

	    @Override
	    public void logException(StatementContext context, SQLException ex) {
	        if (log.isErrorEnabled()) {
	            log.error("Exception while executing '{}' with parameters '{}'",
	                    Optional.ofNullable(context.getParsedSql())
	                            .map(ParsedSql::getSql)
	                            .orElse("<not available>"),
	                    context.getBinding(),
	                    ex);
	        }
	    }

	    private static String format(Duration duration) {
	        final long totalSeconds = duration.getSeconds();
	        final long h = totalSeconds / 3600;
	        final long m = (totalSeconds % 3600) / 60;
	        final long s = totalSeconds % 60;
	        final long ms = duration.toMillis() % 1000;
	        return String.format(
	                "%d:%02d:%02d.%03d",
	                h, m, s, ms);
	    }
}