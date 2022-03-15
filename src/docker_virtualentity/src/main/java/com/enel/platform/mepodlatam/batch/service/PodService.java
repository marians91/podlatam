package com.enel.platform.mepodlatam.batch.service;

import java.util.Optional;

import com.enel.platform.entity.model.batch.Partition;
import com.enel.platform.entity.repository.batch.BatchRepository;
import com.enel.platform.entity.repository.batch.BatchStagingArea;
import com.enel.platform.entity.service.batch.BatchService;
import com.enel.platform.entity.service.batch.PartitionFilterParser;
import com.enel.platform.entity.utils.batch.ApplicationFilterValidatorHelper;
import com.enel.platform.entity.utils.batch.ColdAreaHelper;
import com.enel.platform.entity.utils.batch.ExecutionPlanHelper;
import com.enel.platform.entity.utils.batch.S3Helper;
import com.enel.platform.entity.utils.batch.TemporaryCredentialsHelper;
import com.enel.platform.entity.utils.batch.WriteTypeHelper;
import com.enel.platform.entity.utils.log.BatchLoggerFactory;
import com.enel.platform.mepodlatam.batch.paging.util.PageHelper;

public class PodService<T> extends BatchService<T> {

	private PageHelper<T> woaSegmentsHelper;

	public PodService(String tableName, PageHelper<T> segmentsHelper, S3Helper s3Helper,
			ExecutionPlanHelper planHelper, WriteTypeHelper writeTypeHelper, BatchRepository<T> batchRepository,
			BatchStagingArea<T> batchStagingArea, TemporaryCredentialsHelper temporaryCredentialsHelper,
			ColdAreaHelper coldAreaHelper, ApplicationFilterValidatorHelper applicationFilterValidatorHelper,
			BatchLoggerFactory loggerFactory) {
		super(tableName, segmentsHelper, s3Helper, planHelper, writeTypeHelper, batchRepository, batchStagingArea,
				temporaryCredentialsHelper, coldAreaHelper, applicationFilterValidatorHelper, loggerFactory);
		
		
		this.woaSegmentsHelper = segmentsHelper;
	}

	@Override
	public Partition[] getPartitions(Optional<String> applicationFilter) {
		int partitionSize = woaSegmentsHelper.segmentsNumber(applicationFilter);
		Partition[] partitions = new Partition[partitionSize];
		for (int i = 0; i < partitionSize; i++) {
			partitions[i] = new Partition((long) i,
					new PartitionFilterParser(i, woaSegmentsHelper.getPageSize()).createPartitionFilter());
		}
		return partitions;
	}
}
