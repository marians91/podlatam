package com.enel.platform.mepodlatam.batch.paging.util.edes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.entity.repository.batch.BatchRepository;
import com.enel.platform.mepodlatam.model.edes.EDESPod;

@ExtendWith(MockitoExtension.class)
class EDESPageHelperImplTest {

	
	@Mock
	BatchRepository<EDESPod> batchRepository;
	
	@Test
	void segmentNumber() {
		final long recordsCount = 100L;
		final String tableName = "dd01_visitrequest";
		EDESPageHelperImpl pageHelper = new EDESPageHelperImpl(50, tableName, batchRepository);
		when(batchRepository.tableSize(tableName)).thenReturn(recordsCount);
		int segmentsNumber = pageHelper.segmentsNumber(Optional.empty());
		assertEquals(2, segmentsNumber);
	}
	
	
	@Test
	void segmentNumberByApplicationFilter() {
		final long recordsCount = 100L;
		final String tableName = "DD01_visitrequest";
		final String applicationFilter = "field=dtLastModify;value1=20210601;value2=20210630";
		final Optional<String> applicationFilterHolder = Optional.of(applicationFilter);		
		EDESPageHelperImpl pageHelper = new EDESPageHelperImpl(50, tableName, batchRepository);
		when(batchRepository.tableSize(tableName,applicationFilterHolder.get())).thenReturn(recordsCount);
		int segmentsNumber = pageHelper.segmentsNumber(applicationFilterHolder);
		assertEquals(2, segmentsNumber);
	}
}
