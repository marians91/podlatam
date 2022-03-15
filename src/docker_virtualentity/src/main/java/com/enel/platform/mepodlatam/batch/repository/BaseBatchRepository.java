package com.enel.platform.mepodlatam.batch.repository;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.core.mapper.RowMapper;

import com.enel.platform.mepodlatam.batch.filters.application.Filter;
import com.enel.platform.mepodlatam.batch.filters.application.FilterFactory;
import com.enel.platform.mepodlatam.batch.filters.application.exception.ApplicationFilterException;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMappingConfig;
import com.enel.platform.mepodlatam.batch.filters.application.util.ApplicationFilterParser;

public abstract class BaseBatchRepository<T> implements PodBatchRepository<T> {

	protected final Jdbi jdbi;
	protected final String dbName;
	protected final ApplicationFilterParser filterParser;
	protected final RowMapper<T> mapper;
	protected final String queriesPath;

	protected static final String COUNT_QUERY_FILE_NAME = "countPod";
	protected static final String COUNT_QUERY_BY_FILTER_FILE_NAME = "countPodByFilter";
	protected static final String FIND_QUERY_FILE_NAME = "findPod";
	protected static final String FIND_QUERY_BY_FILTER_FILE_NAME = "findPodByFilter";
	protected static final String QUERY_FILE_PATH_SEPARATOR = ".";
	protected static final String WHERE_CONDITION_PARAM_NAME = "WHERE_CONDITION";
	
	protected static final String OFFSET_PARAM_NAME = "offset";
	protected static final String FETCH_PARAM_NAME = "fetch";

	protected BaseBatchRepository(Jdbi jdbi, String dbName, String queriesPath, RowMapper<T> mapper,
			ApplicationFilterParser filterParser) {
		this.jdbi = jdbi;
		this.dbName = dbName;
		this.filterParser = filterParser;
		this.mapper = mapper;
		this.queriesPath = queriesPath;
	}

	@Override
	public <S extends T> List<S> saveAll(Iterable<S> var1) {
		throw new UnsupportedOperationException("Saving operations are not permitted.");
	}

	@Override
	public long tableSize(String tableName) {
		return count();
	}

	@Override
	public long tableSize(String tableName, String applicationFilter) {
		String whereCondition = buildWhereCondition(applicationFilter);
		return count(whereCondition);
	}

	@Override
	public long count() {
		return jdbi.withHandle(handle -> handle
				.createQuery(ClasspathSqlLocator.findSqlOnClasspath(buildRelativeQueryPathName(COUNT_QUERY_FILE_NAME)))
				.mapTo(Long.class).findFirst().get());
	}

	@Override
	public long count(String whereCondition) {
		return jdbi.withHandle(handle -> handle
				.createQuery(
						ClasspathSqlLocator.findSqlOnClasspath(buildRelativeQueryPathName(COUNT_QUERY_BY_FILTER_FILE_NAME)))
				.define(WHERE_CONDITION_PARAM_NAME, whereCondition).mapTo(Long.class).findFirst().get());
	}

	@Override
	public List<T> findPaginatedResults(int page, int size) {
		return jdbi.withHandle(handle -> handle
				.createQuery(ClasspathSqlLocator.findSqlOnClasspath(buildRelativeQueryPathName(FIND_QUERY_FILE_NAME)))
				.bind(OFFSET_PARAM_NAME, page).bind(FETCH_PARAM_NAME, size).map(mapper).list());
	}

	@Override
	public List<T> findPaginatedResults(String whereCondition, int page, int size) {
		return jdbi.withHandle(handle -> handle
				.createQuery(ClasspathSqlLocator.findSqlOnClasspath(buildRelativeQueryPathName(FIND_QUERY_BY_FILTER_FILE_NAME)))
				.define(WHERE_CONDITION_PARAM_NAME, whereCondition).bind(OFFSET_PARAM_NAME, page)
				.bind(FETCH_PARAM_NAME, size).map(mapper).list());
	}

	@Override
	public Iterator<T> scanByPartition(String tableName, int page, int size) {
		Iterable<T> iterable = findPaginatedResults(page * size, size);
		return iterable.iterator();
	}

	@Override
	public Iterator<T> scanByPartition(String tableName, int page, int size, String applicationFilter) {
		String whereCondition = buildWhereCondition(applicationFilter);
		Iterable<T> iterable = findPaginatedResults(whereCondition, page * size, size);
		return iterable.iterator();
	}

	protected String buildWhereCondition(String applicationFilter) {
		filterParser.doMatch(applicationFilter);
		Filter filter = FilterFactory.getFilter(resolveFilterName());
		return filter.getCondition(filterParser);
	}

	private String resolveFilterName() {
		String fieldName = filterParser.getFieldName();
		Map.Entry<String, String> entry = null;
		if (fieldName == null) {
			entry = filterParser.getAdditionalFieldAndValues().entrySet().stream().findFirst().orElse(null);
			if (entry == null) {
				throw new ApplicationFilterException("No field in application filter.");
			} else {
				fieldName = entry.getKey();
			}
		}
		return filterParser.isIncremental() ? dbName.concat(FilterColumnMappingConfig.INCREMENTAL).concat(fieldName)
				: dbName.concat(fieldName);
	}
	
	protected String buildRelativeQueryPathName(String queryFileName) {
		return queriesPath.concat(QUERY_FILE_PATH_SEPARATOR).concat(queryFileName);
	}
	
	protected List<String> getListOfConditions(String whereCondition, String tokenSeparator){
		return Collections.list(new StringTokenizer(whereCondition, tokenSeparator)).stream()
			      .map(token -> (String) token)
			      .collect(Collectors.toList());
	}
}
