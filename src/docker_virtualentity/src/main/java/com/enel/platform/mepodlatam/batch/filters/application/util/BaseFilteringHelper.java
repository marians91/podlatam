package com.enel.platform.mepodlatam.batch.filters.application.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.lang3.ObjectUtils;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;

import com.enel.platform.mepodlatam.batch.filters.application.exception.ApplicationFilterException;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMappingConfig;

public abstract class BaseFilteringHelper implements FilteringHelper {

	protected static final String DEFAULT_DATE_FILTER_FORMAT = "yyyyMMdd";
	protected static final String UTC_DATE_FILTER_FORMAT = "yyyyMMdd";
	protected static final String DATE_NO_MATCH_FORMAT = "Date %s does not match format %s";
	protected static final String WHERE_CONDITION_KEYWORD = "WHERE";

	public static String buildIncrementalFilter(String fieldName, String fieldValue) {
		return FilterColumnMappingConfig.INCREMENTAL + "field=" + fieldName + ";value1=" + fieldValue;
	}

	public void validateDateFilter(String date, Optional<String> dateFormatHandler) {
		String dateFormat = ObjectUtils.defaultIfNull(dateFormatHandler.orElse(null), DEFAULT_DATE_FILTER_FORMAT);
		SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
		dateFormatter.setLenient(false);
		try {
			dateFormatter.parse(date);
		} catch (ParseException e) {
			throw new ApplicationFilterException(String.format(DATE_NO_MATCH_FORMAT, date, dateFormat));
		}
	}

	public String getOffSetByTimeZone(TimeZone tz) {
		Calendar cal = Calendar.getInstance(tz);
		int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
		String offset = String.format("%02d%02d", Math.abs(offsetInMillis / 3600000),
				Math.abs((offsetInMillis / 60000) % 60));
		offset = (offsetInMillis >= 0 ? "+" : "-") + offset;
		return offset;
	}

	public ZonedDateTime covertDateTimeAtZone(String dateTime, ZoneId zonedId) {
		Preconditions.checkNotNull(dateTime, "dateTime to convert is null");
		Preconditions.checkNotNull(zonedId, "zonedId to convert is null");
		ZonedDateTime utcDate = Instant.parse(dateTime).atZone(ZoneOffset.UTC);		
		return utcDate.withZoneSameInstant(zonedId);

	}
}
