package com.enel.platform.mepodlatam.batch.filters.application.util.dd01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DD01FilteringHelperTest {

	@Test
	void getIntervalByDateIncludingExtremes() {

		String fieldName1 = "tmwr.woa_dtLastModify";
		String fieldName2 = "tmworr.dtLastModify";
		String startDate = "2021-01-01 00:00:00.000+0100";
		String endDate = "2021-12-31 23:59:59.000+0100";

		String whereCondition1 = String.format(" %s >= '%s'", fieldName1, startDate)
				.concat(String.format(" AND %s <= '%s'", fieldName1, endDate));

		String whereCondition2 = String.format(" %s >= '%s'", fieldName2, startDate)
				.concat(String.format(" AND %s <= '%s'", fieldName2, endDate));

		String whereConditionExpected = whereCondition1.concat("|").concat(whereCondition2);

		EDESFilteringHelper filteringHelper = new EDESFilteringHelper();
		String whereCondition = filteringHelper.getIntervalByDateIncludingExtremes("dtLastModify", "20210101",
				"20211231");
		assertEquals(whereConditionExpected, whereCondition);
	}

	@Test
	void getIntervalByDateExcludeExtremes() {
		String fieldName1 = "tmwr.woa_dtLastModify";
		String fieldName2 = "tmworr.dtLastModify";
		String lastDate = "2021-10-01 17:01:45.762+0200";

		String whereCondition1 = String.format(" %s > '%s'", fieldName1, lastDate);
		String whereCondition2 = String.format(" %s > '%s'", fieldName2, lastDate);
		String whereConditionExpected = whereCondition1.concat("|").concat(whereCondition2);

		EDESFilteringHelper filteringHelper = new EDESFilteringHelper();
		String whereCondition = filteringHelper.getIntervalByDateExcludingExtremes("dtLastModify",
				"2021-10-01T15:01:45.762Z");
		assertEquals(whereConditionExpected, whereCondition);

	}
}
