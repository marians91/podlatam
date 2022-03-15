package com.enel.platform.mepodlatam.batch.conf.edes;

import org.jdbi.v3.core.mapper.RowMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enel.platform.entity.utils.batch.ExecutionPlanHelper;
import com.enel.platform.mepodlatam.batch.conf.BatchConfig;
import com.enel.platform.mepodlatam.batch.filters.application.util.FilteringHelper;
import com.enel.platform.mepodlatam.batch.filters.application.util.edes.EDESFilteringHelper;
import com.enel.platform.mepodlatam.batch.repository.mapper.edes.EDESPodMapper;
import com.enel.platform.mepodlatam.batch.s3.PodS3Helper;
import com.enel.platform.mepodlatam.batch.util.PodExecutionPlanHelper;
import com.enel.platform.mepodlatam.model.edes.EDESPod;

@Configuration
public class EDESPodBatchConfig extends BatchConfig<EDESPod> {
	
	@Value("${batch.api.EDES.mepodlatam.name}")
	private String apiName;

	@Value("${batch.api.EDES.mepodlatam.version}")
	private String apiVersion;

	@Value("${batch.api.EDES.mepodlatam.record_per_page}")
	private int recordPerPage;

	@Bean("EDESPodTableNameSuffix")
	@Override
	public String getTableNameSuffix() {
		return apiName;
	}

	@Bean("EDESPodFilteringHelper")
	public FilteringHelper getEDESFilteringHelper() {
		return new EDESFilteringHelper();
	}
	
	@Bean("EDESPodMapper")
	@Override
	public RowMapper<EDESPod> getPodActivityMapper(){
	  	return new EDESPodMapper();
	}	
	
	
	@Bean("EDESPodS3Helper")
	@Override
	public PodS3Helper getS3Helper() {
		return new PodS3Helper(s3Config.getS3Client(), bucketName, apiName, apiVersion);
	}
	
	@Bean("EDESPodPlanHelper")
	@Override
	public ExecutionPlanHelper getExecutionPlanHelper() {
		return new PodExecutionPlanHelper(getS3Helper(),apiName, apiVersion);
	}

}
