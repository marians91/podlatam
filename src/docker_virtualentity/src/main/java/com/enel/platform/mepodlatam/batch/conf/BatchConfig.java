package com.enel.platform.mepodlatam.batch.conf;

import org.jdbi.v3.core.mapper.RowMapper;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.enel.platform.entity.conf.S3Config;
import com.enel.platform.entity.conf.batch.AppConfig;
import com.enel.platform.entity.model.batch.Format;
import com.enel.platform.entity.model.batch.WriteType;
import com.enel.platform.entity.utils.batch.ApplicationFilterValidatorHelper;
import com.enel.platform.entity.utils.batch.ColdAreaHelper;
import com.enel.platform.entity.utils.batch.SegmentsHelper;
import com.enel.platform.entity.utils.batch.TemporaryCredentialsHelper;
import com.enel.platform.entity.utils.batch.WriteTypeHelper;
import com.enel.platform.entity.utils.log.BatchLogger;
import com.enel.platform.entity.utils.log.BatchLoggerFactory;
import com.enel.platform.mepodlatam.batch.filters.application.util.ApplicationFilterParser;

@Configuration
public abstract class BatchConfig<T> extends AppConfig<T> {
			
	@Autowired
	protected S3Config s3Config;

	@Value("${batch.api.podlatam.temporary_credentials.duration_in_seconds}")
	private int temporaryCredentialsDuration;

	@Value("${batch.s3.bucket.name}")
	protected String bucketName;

	@RequestScope
	@Bean("PodApplicationFilterParser")
	public ApplicationFilterParser applicationFilterParser() {
		return new ApplicationFilterParser();
	}

	@RequestScope
	@Bean("PodFilterValidatorHelper")
	@Override
	public ApplicationFilterValidatorHelper getApplicationFilterValidatorHelper() {
		return (String applicationFilter) -> {
			ApplicationFilterParser afp = new ApplicationFilterParser();
			afp.doMatch(applicationFilter);
			return true;
		};
	}

	@Bean("PodTemporaryCredentialsHelper")
	@Override
	public TemporaryCredentialsHelper getTemporaryCredentialsHelper() {
		return new TemporaryCredentialsHelper(s3Config, temporaryCredentialsDuration);
	}

	@Bean("PodWriteTypeHelper")
	@Override
	public WriteTypeHelper getWriteType() {
		return request -> WriteType.Cold;
	}

	@Bean
	@Override
	protected ColdAreaHelper getColdAreaHelper() {
		return () -> Format.Parquet;
	}

	public SegmentsHelper getSegmentHelper() {
		return null;
	}	

	@Bean
	@RequestScope
	public BatchLogger getLogger(InjectionPoint injectionPoint, BatchLoggerFactory loggerFactory) {
		Class<?> classOnWired = injectionPoint.getMember().getDeclaringClass();
		return loggerFactory.getLogger(classOnWired);
	}
			

	public abstract RowMapper<T> getPodActivityMapper();	

}
