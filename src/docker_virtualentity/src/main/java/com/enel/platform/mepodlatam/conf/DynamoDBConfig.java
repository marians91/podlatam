package com.enel.platform.mepodlatam.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceAsyncClientBuilder;

@Configuration
public class DynamoDBConfig {

	@Value("${spring.aws.region}")
	private String secretRegion;
	@Value("${spring.aws.role.arn}")
	private String secretRoleArn;
	@Value("${spring.aws.role.session.name}")
	private String secretRoleSession;
	@Value("${amazon.dynamodb.endpoint}")
	private String amazonDynamoDBEndpoint;
	@Value("${amazon.dynamodb.region}")
	private String amazonDynamoDBRegion;
	@Value("${spring.environment}")
	private String environment;

	@Autowired(required = false)
	private DynamoDbConfigLocal dynamoDbConfigLocal;

	public STSAssumeRoleSessionCredentialsProvider getSTSAssumeRoleSessionCredentialsProvider() {
		return new STSAssumeRoleSessionCredentialsProvider.Builder(secretRoleArn, secretRoleSession)
				.withStsClient(getAWSSecurityTokenService()).build();

	}

	public AWSSecurityTokenService getAWSSecurityTokenService() {
		return AWSSecurityTokenServiceAsyncClientBuilder.standard().withRegion(secretRegion).build();
	}

	public AWSCredentials amazonAWSCredentials() {
		return new EnvironmentVariableCredentialsProvider().getCredentials();
	}

	@Bean
	public DynamoDB dynamoDB() {
		return new DynamoDB(amazonDynamoDB());
	}

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return getDynamoDBClient();
	}

	private AmazonDynamoDB getDynamoDBClient() {

		if (environment.equals("local")) {
			return AmazonDynamoDBClientBuilder.standard()
					.withEndpointConfiguration(dynamoDbConfigLocal.getEndpointConfiguration())
					.withCredentials(dynamoDbConfigLocal.getCredentialsProvider()).build();
		}

		EndpointConfiguration endpointConfiguration = new EndpointConfiguration(amazonDynamoDBEndpoint,
				amazonDynamoDBRegion);
		AWSCredentialsProvider credentialsProvider = getSTSAssumeRoleSessionCredentialsProvider();

		return AmazonDynamoDBClientBuilder.standard().withCredentials(credentialsProvider)
				.withEndpointConfiguration(endpointConfiguration).build();

	}

}