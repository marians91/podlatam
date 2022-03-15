package com.enel.platform.mepodlatam.batch.filters.partition.util;

import com.enel.platform.entity.model.batch.Partition;
import com.enel.platform.mepodlatam.batch.repository.PodBatchRepository;

public class Tenant1PartitionFilterHelper extends PartitionFilterHelper<TestEntity>{

	public Tenant1PartitionFilterHelper(PodBatchRepository<TestEntity> batchRepository,
			int recordPerPage) {
		super(batchRepository, recordPerPage);
	}

	@Override
	public Partition[] buildPartitionsByKey() {		
		return null;
	}

}
