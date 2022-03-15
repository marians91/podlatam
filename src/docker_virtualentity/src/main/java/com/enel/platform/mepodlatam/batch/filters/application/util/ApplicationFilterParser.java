package com.enel.platform.mepodlatam.batch.filters.application.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;

import com.enel.platform.mepodlatam.batch.filters.application.exception.ApplicationFilterException;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMappingConfig;




public class ApplicationFilterParser {

	protected static final String DEFAULT_PARSER_REGEX = "(incremental#field|field)=(\\w+);value1=(.[^;]+)(;value2=(.[^;]+|))?";
	protected static final String DEFAULT_OPTIONAL_PARSER_REGEX = "((incremental#field|field)=(\\w+);value1=(.[^;]+)(;value2=(.[^;]+|))?)?";
	protected static final Pattern DEFAULT_PARSER_REGEX_PATTERN = Pattern.compile(DEFAULT_PARSER_REGEX);
	protected static final String GROUP_SEPARATOR = ";";
	protected static final String ASSIGNMENT_OPERATOR = "=";
	protected static final String OPTIONAL_OPERATOR = "?";
	private static final int DEFAULT_GROUP_COUNT = 5;
	private static final int DEFAULT_OPTIONAL_GROUP_COUNT = 6;

	protected String fieldName;
	protected String fieldValue1;
	protected Optional<String> fieldValue2;
	protected boolean isIncremental;
	private Map<String, String> additionalFieldAndValues;

	private boolean existsAdditionalExp;
	private String additionalExpressions; // sample: ;?field1=value;field2=value
	private boolean applyAddExpressionForIncrementalFilter;
	private boolean applyDefaultOptionalExpression;

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldValue1() {
		return fieldValue1;
	}

	public Optional<String> getFieldValue2() {
		return fieldValue2;
	}

	public boolean isIncremental() {
		return isIncremental;
	}

	public Map<String, String> getAdditionalFieldAndValues() {
		return additionalFieldAndValues;
	}

	public ApplicationFilterParser() {

	}

	public ApplicationFilterParser(String additionalExpressions, boolean applyAddExpressionForIncrementalFilter) {
		existsAdditionalExp = true;
		this.additionalExpressions = additionalExpressions;
		this.applyAddExpressionForIncrementalFilter = applyAddExpressionForIncrementalFilter;
	}

	public ApplicationFilterParser(String additionalExpressions, boolean applyAddExpressionForIncrementalFilter,
			boolean applyDefaultOptionalExpression) {
		existsAdditionalExp = true;
		this.additionalExpressions = additionalExpressions;
		this.applyAddExpressionForIncrementalFilter = applyAddExpressionForIncrementalFilter;
		this.applyDefaultOptionalExpression = applyDefaultOptionalExpression;
	}

	public boolean doMatch(String applicationFilter) {
		Pattern parserRegexPattern = buildRegexPattern(applicationFilter);
		Matcher matcher = parserRegexPattern.matcher(applicationFilter);
		if (matcher.matches()) {
			this.isIncremental = isIncremental(matcher.group(computeGroupId(1)));
			this.fieldName = matcher.group(computeGroupId(2));
			this.fieldValue1 = matcher.group(computeGroupId(3));
			this.fieldValue2 = Optional.ofNullable(matcher.group(computeGroupId(5)));
			if (doExtractAdditionalExp()) {
				extractAdditionalFields(matcher);
			}
		} else {
			throw new ApplicationFilterException("Filter doesn't match expression: " + parserRegexPattern.pattern());
		}
		return true;
	}

	private boolean isIncremental(String fieldGroup) {
		return fieldGroup != null && fieldGroup.startsWith(FilterColumnMappingConfig.INCREMENTAL);
	}

	private Pattern buildRegexPattern(String applicationFilter) {
		if (!applyAddExpressionForIncrementalFilter && applicationFilter.startsWith(FilterColumnMappingConfig.INCREMENTAL)) {
			applyDefaultOptionalExpression = false; // no switch group position
			return Pattern.compile(DEFAULT_PARSER_REGEX);
		} else if (applyDefaultOptionalExpression) {
			return Pattern.compile(
					DEFAULT_OPTIONAL_PARSER_REGEX.concat(ObjectUtils.defaultIfNull(additionalExpressions, "")));
		} else {
			return Pattern.compile(DEFAULT_PARSER_REGEX.concat(ObjectUtils.defaultIfNull(additionalExpressions, "")));
		}
	}

	private void extractAdditionalFields(Matcher matcher) {
		additionalFieldAndValues = new LinkedHashMap<>();
		StringTokenizer tokenizerFilter = new StringTokenizer(additionalExpressions, GROUP_SEPARATOR);
		int groupId = computeGroupCount();
		while (tokenizerFilter.hasMoreElements()) {
			extractSingleFieldAndValue(tokenizerFilter.nextToken().replace(OPTIONAL_OPERATOR, ""), matcher, ++groupId);
		}
	}

	private void extractSingleFieldAndValue(String singleFilter, Matcher matcher, int groupId) {
		String field = singleFilter.substring(0, singleFilter.indexOf(ASSIGNMENT_OPERATOR));
		String value = matcher.group(groupId);
		additionalFieldAndValues.put(field, value);
	}

	private int computeGroupId(int groupId) {
		int result = 0;
		result = applyDefaultOptionalExpression ? ++groupId : groupId;
		return result;
	}

	private int computeGroupCount() {
		return applyDefaultOptionalExpression ? DEFAULT_OPTIONAL_GROUP_COUNT : DEFAULT_GROUP_COUNT;
	}

	private boolean doExtractAdditionalExp() {
		if(existsAdditionalExp) {
			if(isIncremental() && applyAddExpressionForIncrementalFilter) {
				return true;
			}else if(!isIncremental()) {
				return true;
			}else {
				return false;
			}
		}else return false;
	}
	
}
