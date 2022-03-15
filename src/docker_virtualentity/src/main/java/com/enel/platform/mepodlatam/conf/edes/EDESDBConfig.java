package com.enel.platform.mepodlatam.conf.edes;

import java.util.List;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.enel.platform.mepodlatam.conf.base.BaseDBConfig;
import com.enel.platform.mepodlatam.secret.DBSecret;

@Configuration
public class EDESDBConfig extends BaseDBConfig {

	@Value("${EDES.datasource.hikari.leakDetectionThreshold}")
	private String leakDetectionThreshold;
	
	

	private static final String JDBC_DB_TYPE = "informix-sqli";

	public EDESDBConfig(@Value("${EDES.datasource.driver_class}") String driverClassName,
			@Value("${EDES.datasource.hikari.poolName}") String poolName,
			@Value("${EDES.datasource.hikari.idleTimeout}") String idleTimeout,
			@Value("${EDES.datasource.hikari.connectionTimeout}") String connectionTimeout,
			@Value("${EDES.datasource.hikari.minimumIdle}") String minimumIdle,
			@Value("${EDES.datasource.hikari.maximumPoolSize}") String maximumPoolSize,
			@Value("${EDES.datasource.hikari.maxLifetime}") String maxLifetime,
			@Value("${show-sql}") String showSql) {

		super(driverClassName, poolName, idleTimeout, connectionTimeout, minimumIdle, maximumPoolSize, maxLifetime,JDBC_DB_TYPE, showSql);

		super.setLeakDetectionThreshold(leakDetectionThreshold);
	}

	@Bean("EDESDatasource")
	@Primary
	@Override
	protected DataSource dataSource(@Qualifier("EDESDBSecret") DBSecret secret) {
		return super.dataSource(secret);
	}

	@Bean("EDESTransactionManager")
    @Primary
    @Override
	public DataSourceTransactionManager transactionManager(@Qualifier("EDESDatasource") DataSource datasource) {
		return new DataSourceTransactionManager(datasource);
	}

	@Bean("EDESJdbi")
	@Override
	public Jdbi jdbi(@Qualifier("EDESDatasource") DataSource datasource, List<JdbiPlugin> plugins,
			List<RowMapper<?>> mappers) {
		return super.jdbi(datasource, plugins, mappers);
	}

}
