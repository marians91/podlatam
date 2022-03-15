package com.enel.platform.mepodlatam.batch.controller.edes;

import org.jdbi.v3.core.mapper.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.enel.platform.entity.conf.S3Config;
import com.enel.platform.mepodlatam.batch.controller.support.ControllerTestConfiguration;
import com.enel.platform.mepodlatam.batch.filters.application.util.FilteringHelper;
import com.enel.platform.mepodlatam.batch.filters.application.util.edes.EDESFilteringHelper;
import com.enel.platform.mepodlatam.batch.repository.mapper.edes.EDESPodMapper;
import com.enel.platform.mepodlatam.batch.service.edes.EDESPodService;
import com.enel.platform.mepodlatam.model.edes.EDESPod;


@Import({ ControllerTestConfiguration.class, EDESPodController.class, EDESPodService.class })
public class EDESControllerTestConfiguration {

	@Value("${batch.api.EDES.pod.name}")
	private String apiName;

	@Value("${batch.api.EDES.pod.record_per_page}")
	private int recordPerPage;
	
	@Autowired
	protected S3Config s3Config;

	@Value("${batch.s3.bucket.name}")
	protected String bucketName;
	
	@Bean("EDESPodRequestTableNameSuffix")
	public String getTableNameSuffix() {
		return apiName;
	}

	@Bean("EDESPodRequestFilteringHelper")
	public FilteringHelper geDD01FilteringHelper() {
		return new EDESFilteringHelper();
	}
	
	@Bean("EDESPodMapper")
	public RowMapper<EDESPod> getVisitRequestMapper(){
	  	return new EDESPodMapper();
	}		
	
}
