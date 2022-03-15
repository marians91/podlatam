package com.enel.platform.mepodlatam.conf.edes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enel.platform.mepodlatam.conf.base.BaseSecretConfig;
import com.enel.platform.mepodlatam.secret.DBSecret;



@Configuration
public class EDESecretConfig extends BaseSecretConfig {


	static final String EDES_DB_HOST = "EDESDbHost";
	static final String EDES_DB_PORT = "EDESDbPort";
	static final String EDES_DB_DB_NAME = "EDESDbName";
	static final String EDES_DB_USERNAME = "EDESDbUsername";
	static final String EDES_DB_PASSWORD = "EDESDbPassword";

	public EDESecretConfig(@Value("${spring.aws.secretsmanager.secretName}") String secretName,
			@Value("${spring.aws.secretsmanager.endpoint}") String secretEndpoint,
			@Value("${spring.aws.secretsmanager.region}") String secretRegion,
			@Value("${spring.aws.secretsmanager.role.arn}") String secretRoleArn,
			@Value("${spring.aws.secretsmanager.role.session.name}") String secretRoleSession,
			@Value("${spring.aws.sts.endpoint}") String stsEndpoint) {
		super(secretName, secretEndpoint, secretRegion, secretRoleArn, secretRoleSession, stsEndpoint);
	}


	@Bean("EDESDBSecret")
	public DBSecret dBSecret() {
		return buildDBSecret(new String[] { EDES_DB_HOST, EDES_DB_PORT, EDES_DB_DB_NAME,
				EDES_DB_USERNAME, EDES_DB_PASSWORD });
	}

}
