package com.enel.platform.mepodlatam.batch.filters.partition.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.entity.model.batch.Partition;
import com.enel.platform.mepodlatam.batch.repository.PodBatchRepository;

@ExtendWith(MockitoExtension.class)
class PartitionFilterHelperTest {

	@Mock
	private PodBatchRepository<TestEntity> batchRepository;

	@Test
	void createPartitionFilter() {

		final String partionFilterExpected = "partition_id:2018-0,partition_size:50";
		final String partionFilter = PartitionFilterHelper.createPartitionFilter(2018, 0, 50);
		assertEquals(partionFilterExpected, partionFilter);
	}

	@Test
	void getPartitionIdElements() {
		String[] partitionIdElements = PartitionFilterHelper.getPartitionIdElements("partition_id:2018-0");
		assertEquals("2018", partitionIdElements[0]);
		assertEquals("0", partitionIdElements[1]);
	}

	@Test
	void getPartitionId() {
		String partitionId = PartitionFilterHelper.getPartitionId("partition_id:2018-0,partition_size:50");
		assertEquals("partition_id:2018-0", partitionId);
	}

	@Test
	void getPartitionSize() {
		Integer partitionSize = PartitionFilterHelper.getPartitionSize("partition_id:2018-0,partition_size:50");
		assertEquals(50, partitionSize);
	}

	@Test
	void buildPartitionsByFilter() {

		final String applicationFilter = "field=dtLastModify;value1=20210601;value2=20210630";
		final int recordPerPage = 50;
		final Optional<String> applicationFilterHolder = Optional.of(applicationFilter);
		Tenant1PartitionFilterHelper partitionFilterHelper = new Tenant1PartitionFilterHelper(batchRepository, recordPerPage);
		when(batchRepository.tableSize(null, applicationFilterHolder.get())).thenReturn(100L);

		Partition[] partitions = partitionFilterHelper.buildPartitionsByFilter(applicationFilterHolder);

		assertEquals(2, partitions.length);
		assertEquals(0, partitions[0].getId());
		assertEquals("partition_id:0,partition_size:50", partitions[0].getPartitionFilter());
		assertEquals(1, partitions[1].getId());
		assertEquals("partition_id:1,partition_size:50", partitions[1].getPartitionFilter());

	}

}
