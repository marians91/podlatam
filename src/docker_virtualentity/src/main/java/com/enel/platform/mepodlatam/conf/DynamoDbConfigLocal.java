package com.enel.platform.mepodlatam.conf;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@ConditionalOnProperty(value = "spring.environment", havingValue = "local", matchIfMissing = false)
public class DynamoDbConfigLocal {

    @Value("${amazon.dynamodb.region}")
    private String amazonDynamoRegion;

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoEndpoint;

    @Value("${amazon.dynamodb.access.local.key}")
    private String amazonDynamoAccessKey;

    @Value("${amazon.dynamodb.secret.local.key}")
    private String amazonDynamoSecretKey;

    public AWSCredentialsProvider getCredentialsProvider() {
        AWSCredentials testCredendials = new BasicAWSCredentials(amazonDynamoAccessKey, amazonDynamoSecretKey);
        return new AWSStaticCredentialsProvider(testCredendials);
    }

    protected EndpointConfiguration getEndpointConfiguration() {
    	return new EndpointConfiguration(amazonDynamoEndpoint, amazonDynamoRegion);        
    }
}