package com.enel.platform.mepodlatam.conf.base;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.UnableToRegisterMBeanException;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceAsyncClientBuilder;
import com.enel.platform.mepodlatam.secret.DBSecret;
import com.enel.platform.mepodlatam.secret.SecretFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class BaseSecretConfig {

	private static Logger log = LoggerFactory.getLogger(BaseSecretConfig.class.getName());
	
	private final String secretName;
    private final String secretEndpoint;
	private final String secretRegion;
	private final String secretRoleArn;
	private final String secretRoleSession;
	private final String stsEndpoint;
	
	private JsonNode secretsJson;

	protected JsonNode findSecret() {

		if (secretsJson != null) {
			return secretsJson;
		}
		AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(secretEndpoint,
				secretRegion);
		AWSCredentialsProvider credentialsProvider = getSTSAssumeRoleSessionCredentialsProvider();
		AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
		clientBuilder.setEndpointConfiguration(config);
		AWSSecretsManager client = clientBuilder.withCredentials(credentialsProvider).build();

		ObjectMapper objectMapper = new ObjectMapper();

		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);

		GetSecretValueResult getSecretValueResponse = null;

		try {
			getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
		} catch (ResourceNotFoundException e) {
			log.error("The requested secret ".concat(secretName).concat(" was not found"));
		} catch (InvalidRequestException e) {
			log.error("The request was invalid due to: ".concat(e.getMessage()));
		} catch (InvalidParameterException e) {
			log.error("The request had invalid params: ".concat(e.getMessage()));
		} catch (Exception e) {
			log.error("Error retrieving secret manager : ".concat(e.getMessage()));
		}

		if (getSecretValueResponse == null) {
			throw new NullPointerException("The requested secret ".concat(secretName).concat(" was not found"));
		}

		String secret = getSecretValueResponse.getSecretString();

		if (secret != null) {
			try {
				secretsJson = objectMapper.readTree(secret);

			} catch (IOException e) {
				log.error("Exception while retreiving secret values: ".concat(e.getMessage()));
			}
		} else {
			log.error("The Secret String returned is null");
		}

		if (secretsJson == null) {
			throw new NullPointerException("The secret ".concat(secretName).concat(" is empty"));
		}

		return secretsJson;
	}

	protected STSAssumeRoleSessionCredentialsProvider getSTSAssumeRoleSessionCredentialsProvider() {
		return new STSAssumeRoleSessionCredentialsProvider.Builder(secretRoleArn, secretRoleSession)
				.withStsClient(getAWSSecurityTokenService()).build();
	}

	protected AWSSecurityTokenService getAWSSecurityTokenService() {
		AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(stsEndpoint,
				secretRegion);
		return AWSSecurityTokenServiceAsyncClientBuilder.standard().withEndpointConfiguration(config).build();
	}
		
	
	protected DBSecret buildDBSecret(String[] attributeNames) {
		findSecret();
		SecretFactory secretFactory = new SecretFactory(attributeNames, secretsJson);
		try {
			return secretFactory.getObject();
		} catch (Exception e) {
			throw new UnableToRegisterMBeanException("DBSecret cannot be registered", e);
		}
	}
	
	public abstract DBSecret dBSecret();
}
