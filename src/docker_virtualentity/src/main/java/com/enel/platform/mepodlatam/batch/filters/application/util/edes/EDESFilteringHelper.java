package com.enel.platform.mepodlatam.batch.filters.application.util.edes;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

import com.enel.platform.mepodlatam.batch.filters.application.util.BaseFilteringHelper;
import com.enel.platform.mepodlatam.batch.filters.application.util.FilteringHelper;

public class EDESFilteringHelper extends BaseFilteringHelper implements FilteringHelper {

	public static final String INCREMENT_DATE_FILTER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String INCREMENT_DB_DATE_FILTER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String WHERE_CONDITION_SEPARATOR = "|";
	public static final String TIME_ZONE = "Europe/Rome";

	public String getIntervalByDateIncludingExtremes(String fieldName, String startDate, String endDate) {
		String condition1 = buildWhereCondition("tmwr.woa_".concat(fieldName), startDate, endDate);
		String condition2 = buildWhereCondition("tmworr.".concat(fieldName), startDate, endDate);
		return condition1.concat(WHERE_CONDITION_SEPARATOR).concat(condition2);
	}

	private String convertDateFormat(String date) {
		return date.substring(0, 4).concat("-").concat(date.substring(4, 6)).concat("-").concat(date.substring(6, 8));
	}

	private String buildWhereCondition(String fieldName, String startDate, String endDate) {
		validateDateFilter(startDate, Optional.empty());
		String offset = getOffSetByTimeZone(TimeZone.getTimeZone(TIME_ZONE));
		String convStartDate = convertDateFormat(startDate).concat(" 00:00:00.000").concat(offset);
		String whereCondition = String.format(" %s >= '%s'", fieldName, convStartDate);
		if (!Objects.isNull(endDate) && !endDate.isEmpty()) {
			validateDateFilter(endDate, Optional.empty());
			String convEndDate = convertDateFormat(endDate).concat(" 23:59:59.000").concat(offset);
			whereCondition = whereCondition.concat(String.format(" AND %s <= '%s'", fieldName, convEndDate));
		}
		return whereCondition;
	}

	public String getIntervalByDateExcludingExtremes(String fieldName, String lastDate) {
		String condition1 = buildWhereCondition("tmwr.woa_".concat(fieldName), lastDate);
		String condition2 = buildWhereCondition("tmworr.".concat(fieldName), lastDate);
		return condition1.concat("|").concat(condition2);
	}

	private String buildWhereCondition(String fieldName, String lastDate) {
		validateDateFilter(lastDate, Optional.of(INCREMENT_DATE_FILTER_FORMAT));		
		ZonedDateTime dateInRome = covertDateTimeAtZone(lastDate, ZoneId.of(TIME_ZONE));
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(INCREMENT_DB_DATE_FILTER_FORMAT);
		String formattedString = dateInRome.format(dateTimeFormatter);
		return String.format(" %s > '%s'", fieldName, formattedString.replaceFirst("T", " "));
	}

}
