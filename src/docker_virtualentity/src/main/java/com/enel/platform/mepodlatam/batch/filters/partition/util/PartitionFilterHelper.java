package com.enel.platform.mepodlatam.batch.filters.partition.util;

import java.util.Optional;

import com.enel.platform.entity.model.batch.Partition;
import com.enel.platform.entity.service.batch.PartitionFilterParser;
import com.enel.platform.mepodlatam.batch.paging.util.PageHelper;
import com.enel.platform.mepodlatam.batch.paging.util.PageHelperImpl;
import com.enel.platform.mepodlatam.batch.repository.PodBatchRepository;


public abstract class PartitionFilterHelper<T> {
	
	private final PageHelper<T> segmentsHelper;
	
	protected PartitionFilterHelper(PodBatchRepository<T> batchRepository,
			int recordPerPage) {
		this.segmentsHelper = new PageHelperImpl<>(recordPerPage, null, batchRepository);
	}

	static final String PARTITION_ID = "partition_id";
	static final String PARTITION_SIZE = "partition_size";
	static final String NAME_VALUE_SEPARATOR = ":";
	static final String SEPARATOR = ",";
	static final String PARTITION_KEY_SEPARATOR = "-";
	
	public static String createPartitionFilter(int partitionKey, int partitionId, int partitionSize) {
		return PARTITION_ID + NAME_VALUE_SEPARATOR + partitionKey + PARTITION_KEY_SEPARATOR + partitionId + SEPARATOR
				+ PARTITION_SIZE + NAME_VALUE_SEPARATOR + partitionSize;

	}
	public static String[] getPartitionIdElements(String partitionId) {
		return partitionId.split(SEPARATOR)[0].split(NAME_VALUE_SEPARATOR)[1].split(PARTITION_KEY_SEPARATOR);

	}

	public static String getPartitionId(String partitionFilter) {
		return partitionFilter.split(SEPARATOR)[0];

	}

	public static Integer getPartitionSize(String partitionFilter) {
		return Integer.parseInt(partitionFilter.split(SEPARATOR)[1].split(NAME_VALUE_SEPARATOR)[1]);

	}
	
	public abstract Partition[] buildPartitionsByKey();
	
	public Partition[] buildPartitionsByFilter(Optional<String> applicationFilter) {
		int partitionSize = segmentsHelper.segmentsNumber(applicationFilter);
		Partition[] partitions = {};
		if (partitionSize > 0) {
			partitions = new Partition[partitionSize];
			for (int i = 0; i < partitionSize; i++) {
				partitions[i] = new Partition((long) i,
						new PartitionFilterParser(i, segmentsHelper.getPageSize()).createPartitionFilter());
			}
		}
		return partitions;

	}
	

}
