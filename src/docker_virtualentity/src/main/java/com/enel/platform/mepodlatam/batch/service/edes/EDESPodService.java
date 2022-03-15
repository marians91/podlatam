package com.enel.platform.mepodlatam.batch.service.edes;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enel.platform.entity.repository.batch.BatchStagingArea;
import com.enel.platform.entity.utils.batch.ApplicationFilterValidatorHelper;
import com.enel.platform.entity.utils.batch.ColdAreaHelper;
import com.enel.platform.entity.utils.batch.ExecutionPlanHelper;
import com.enel.platform.entity.utils.batch.S3Helper;
import com.enel.platform.entity.utils.batch.TemporaryCredentialsHelper;
import com.enel.platform.entity.utils.batch.WriteTypeHelper;
import com.enel.platform.entity.utils.log.BatchLoggerFactory;
import com.enel.platform.mepodlatam.batch.paging.util.PageHelperImpl;
import com.enel.platform.mepodlatam.batch.repository.PodBatchRepository;
import com.enel.platform.mepodlatam.batch.service.PodService;
import com.enel.platform.mepodlatam.model.edes.EDESPod;

@Service
public class EDESPodService extends PodService<EDESPod> {

	public EDESPodService(@Qualifier("EDESPodTableNameSuffix") String tableName,
			@Qualifier("EDESPodS3Helper") S3Helper s3Helper,
			@Qualifier("EDESPodPlanHelper") ExecutionPlanHelper planHelper,
			PodBatchRepository<EDESPod> podBatchRepository,
			BatchStagingArea<EDESPod> podBatchStagingArea,
			@Qualifier("PodTemporaryCredentialsHelper") TemporaryCredentialsHelper temporaryCredentialsHelper,
			ColdAreaHelper coldAreaHelper, @Qualifier("PodWriteTypeHelper") WriteTypeHelper writeTypeHelper,
			@Qualifier("PodFilterValidatorHelper") ApplicationFilterValidatorHelper filterValidatorHelper,			
			@Value("${batch.api.EDES.podlatam.record_per_page}") int recordPerPage,
			BatchLoggerFactory loggerFactory) {

		super(tableName, new PageHelperImpl<>(recordPerPage, tableName, podBatchRepository), s3Helper, planHelper, writeTypeHelper, podBatchRepository, podBatchStagingArea,
				temporaryCredentialsHelper, coldAreaHelper, filterValidatorHelper, loggerFactory);

	}

	

}