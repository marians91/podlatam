package com.enel.platform.mepodlatam.batch.controller.support;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.enel.platform.entity.conf.S3Config;
import com.enel.platform.entity.model.batch.ExtractionType;
import com.enel.platform.entity.model.batch.Format;
import com.enel.platform.entity.model.batch.WriteType;
import com.enel.platform.entity.utils.batch.ApplicationFilterValidatorHelper;
import com.enel.platform.entity.utils.batch.ColdAreaHelper;
import com.enel.platform.entity.utils.batch.ExecutionPlanHelper;
import com.enel.platform.entity.utils.batch.TemporaryCredentialsHelper;
import com.enel.platform.entity.utils.batch.WriteTypeHelper;
import com.enel.platform.entity.utils.log.BatchLogger;
import com.enel.platform.entity.utils.log.BatchLoggerFactory;
import com.enel.platform.mepodlatam.batch.filters.application.util.ApplicationFilterParser;
import com.enel.platform.mepodlatam.batch.log.BatchSpringBootLoggerFactory;
import com.enel.platform.mepodlatam.batch.repository.PodIncrementalHandlerRepository;
import com.enel.platform.mepodlatam.batch.service.PodIncrementalHandlerService;
import com.enel.platform.mepodlatam.batch.service.PodIncrementalHandlerServiceImpl;

@Configuration
public class ControllerTestConfiguration {

	@Autowired
	protected S3Config s3Config;

	@MockBean
	private PodIncrementalHandlerRepository visitRequestIncrementalHandlerRepository;

	@Value("${batch.api.podrequestlatam.temporary_credentials.duration_in_seconds}")
	private int temporaryCredentialsDuration;

	@Bean("PodRequestTemporaryCredentialsHelper")
	public TemporaryCredentialsHelper getTemporaryCredentialsHelper() {
		return new TemporaryCredentialsHelper(s3Config, temporaryCredentialsDuration);
	}

	@Bean("PodRequestWriteTypeHelper")
	public WriteTypeHelper getWriteType() {
		return request -> WriteType.Cold;
	}

	@Bean
	protected ColdAreaHelper getColdAreaHelper() {
		return () -> Format.Parquet;
	}

	@Bean("PodRequestPlanHelper")
	public ExecutionPlanHelper getExecutionPlanHelper() {
		return request -> ExtractionType.Hot;
	}

	@Bean
	@RequestScope
	public BatchLogger getLogger(InjectionPoint injectionPoint, BatchLoggerFactory loggerFactory) {
		Class<?> classOnWired = injectionPoint.getMember().getDeclaringClass();
		return loggerFactory.getLogger(classOnWired);
	}

	@Bean
	public BatchLoggerFactory getBatchSpringBootLoggerFactory() {
		return new BatchSpringBootLoggerFactory();
	}

	@RequestScope
	@Bean("PodRequestApplicationFilterParser")
	public ApplicationFilterParser applicationFilterParser() {
		return new ApplicationFilterParser();
	}

	@RequestScope
	@Bean("PodRequestFilterValidatorHelper")
	public ApplicationFilterValidatorHelper getApplicationFilterValidatorHelper() {
		return (String applicationFilter) -> {
			ApplicationFilterParser afp = new ApplicationFilterParser();
			afp.doMatch(applicationFilter);
			return true;
		};
	}

	@Bean
	public PodIncrementalHandlerService visitRequestIncrementalHandlerService(
			PodIncrementalHandlerRepository visitRequestIncrementalHandlerRepository) {
		return new PodIncrementalHandlerServiceImpl(visitRequestIncrementalHandlerRepository);
	}

}
