package com.enel.platform.mepodlatam.conf.base;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import com.enel.platform.mepodlatam.secret.DBSecret;
import com.enel.platform.mepodlatam.util.Slf4JSqlLogger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class BaseDBConfig {

	private final String driverClassName;
	private final String poolName;
	private final String idleTimeout;
	private final String connectionTimeout;
	private final String minimumIdle;
	private final String maximumPoolSize;
	private final String maxLifetime;
	private final String jdbcDBType;
	private final String showSql;
	private String storageEngine;
	private String leakDetectionThreshold;
	private String hibernateDDLAuto;

	protected String connectionString(DBSecret secret, String jdbcDBType) {
		if (secret != null) {
			String port = secret.getPort();
			String host = secret.getHost();
			String dbName = secret.getDbName();
			return "jdbc:" + jdbcDBType + "://" + host + ":" + port + "/" + dbName;
		} else {
			throw new IllegalArgumentException("DB configuration is not found");
		}
	}

	protected DataSource dataSource(DBSecret secret) {

		String connectionString = connectionString(secret, jdbcDBType);
		Properties properties = new Properties();
		properties.setProperty("driverClassName", driverClassName);
		properties.setProperty("jdbcUrl", connectionString);
		properties.setProperty("username", secret.getUsername());
		properties.setProperty("password", secret.getPassword());
		properties.setProperty("idleTimeout", idleTimeout);
		properties.setProperty("connectionTimeout", connectionTimeout);
		properties.setProperty("minimumIdle", minimumIdle);
		properties.setProperty("maximumPoolSize", maximumPoolSize);
		properties.setProperty("poolName", poolName);
		properties.setProperty("maxLifetime", maxLifetime);
		if (leakDetectionThreshold != null) {
			properties.setProperty("leakDetectionThreshold", leakDetectionThreshold);
		}

		HikariConfig config = new HikariConfig(properties);
		return new HikariDataSource(config);

	}

	protected Jdbi jdbi(DataSource datasource, List<JdbiPlugin> plugins,
			List<RowMapper<?>> mappers) {
		TransactionAwareDataSourceProxy proxy = new TransactionAwareDataSourceProxy(datasource);
		Jdbi jdbi = Jdbi.create(proxy);
		plugins.forEach(jdbi::installPlugin);
		mappers.forEach(jdbi::registerRowMapper);
		enableSqlLogger(jdbi);
		return jdbi;
	}

	protected DataSourceTransactionManager transactionManager(DataSource datasource) {
		return new DataSourceTransactionManager(datasource);
	}
	
	protected void enableSqlLogger(Jdbi jdbi) {
		if (Boolean.parseBoolean(showSql)) {
			jdbi.setSqlLogger(new Slf4JSqlLogger());
		}
	}
}
