package com.enel.platform.mepodlatam.conf;

import com.enel.platform.entity.conf.S3ConfigLocal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Local S3 configuration used for tests.
 */
@Configuration
@ConditionalOnProperty(
        value = "spring.environment",
        havingValue = "local",
        matchIfMissing = false)
public class S3ConfigLocalImpl extends S3ConfigLocal {

    public S3ConfigLocalImpl(@Value("${amazon.s3.region}")
                                     String amazonS3Region,
                             @Value("${amazon.s3.endpoint}")
                                     String amazonS3Endpoint,
                             @Value("${amazon.s3.access.key}")
                                     String amazonS3AccessKey,
                             @Value("${amazon.s3.secret.key}")
                                     String amazonS3SecretKey) {
        super(amazonS3Region, amazonS3Endpoint, amazonS3AccessKey, amazonS3SecretKey);
    }

    public S3ConfigLocalImpl() {
        super();
    }

}