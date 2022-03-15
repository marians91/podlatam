package com.enel.platform.mepodlatam.batch.paging.util;

import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.entity.repository.batch.BatchRepository;

@ExtendWith(MockitoExtension.class)
class PageHelperImplTest {

	@SuppressWarnings("rawtypes")
	@Mock
	BatchRepository batchRepository;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void segmentsNumber() {
		long recordsCount = 45L;
		PageHelperImpl pageHelper = new PageHelperImpl(50, null, batchRepository);
		int segmentsNumber = pageHelper.segmentsNumber(recordsCount);
		assertEquals(1, segmentsNumber);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void segmentsNumberByZeroRecodsCount() {
		long recordsCount = 0L;
		PageHelperImpl pageHelper = new PageHelperImpl(50, null, batchRepository);
		int segmentsNumber = pageHelper.segmentsNumber(recordsCount);
		assertEquals(0, segmentsNumber);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void segmentsNumberWithMoreThanOneSegment() {
		long recordsCount = 100L;
		PageHelperImpl pageHelper = new PageHelperImpl(50, null, batchRepository);
		int segmentsNumber = pageHelper.segmentsNumber(recordsCount);
		assertEquals(2, segmentsNumber);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void segmentNumber() {
		long recordsCount = 100L;
		PageHelperImpl pageHelper = new PageHelperImpl(50, null, batchRepository);
		when(batchRepository.tableSize(null)).thenReturn(recordsCount);
		int segmentsNumber = pageHelper.segmentsNumber(Optional.empty());
		assertEquals(2, segmentsNumber);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void segmentsNumberWithSameRecordPerPageAndRecordNumber() {
		long recordsCount = 100L;
		PageHelperImpl pageHelper = new PageHelperImpl(100, null, batchRepository);
		int segmentsNumber = pageHelper.segmentsNumber(recordsCount);
		assertEquals(1, segmentsNumber);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void segmentNumberByApplicationFilter() {
		long recordsCount = 100L;
		final String applicationFilter = "field=dtLastModify;value1=20210601;value2=20210630";
		final Optional<String> applicationFilterHolder = Optional.of(applicationFilter);		
		PageHelperImpl pageHelper = new PageHelperImpl(50, null, batchRepository);
		when(batchRepository.tableSize(null,applicationFilterHolder.get())).thenReturn(recordsCount);
		int segmentsNumber = pageHelper.segmentsNumber(applicationFilterHolder);
		assertEquals(2, segmentsNumber);
	}

}
