package com.enel.platform.mepodlatam.conf;

import com.enel.platform.entity.conf.S3Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * Instantiate the client to interact with the S3 storage.
 */
@Configuration
public class S3ConfigImpl extends S3Config {

    public S3ConfigImpl(@Value("${spring.environment}") String environment,
                        @Autowired Optional<com.enel.platform.entity.conf.S3ConfigLocal> s3ConfigLocal,
                        @Value("${spring.aws.region}") String secretRegion,
                        @Value("${spring.aws.role.arn}") String secretRoleArn,
                        @Value("${spring.aws.role.session.name}") String secretRoleSession) {
        super(environment, s3ConfigLocal, secretRegion, secretRoleArn, secretRoleSession);
    }

}