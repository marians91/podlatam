package com.enel.platform.mepodlatam.batch.repository.edes;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.core.mapper.RowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.enel.platform.mepodlatam.batch.filters.application.mapping.edes.EDESFilterColumnMapping;
import com.enel.platform.mepodlatam.batch.filters.application.util.ApplicationFilterParser;
import com.enel.platform.mepodlatam.batch.filters.application.util.edes.EDESFilteringHelper;
import com.enel.platform.mepodlatam.batch.repository.BaseBatchRepository;
import com.enel.platform.mepodlatam.batch.repository.PodBatchRepository;
import com.enel.platform.mepodlatam.model.edes.EDESPod;

@Repository
public class EDESPodBatchRepositoryImpl extends BaseBatchRepository<EDESPod>
		implements PodBatchRepository<EDESPod> {

	public EDESPodBatchRepositoryImpl(@Qualifier("EDESJdbi") Jdbi jdbi, RowMapper<EDESPod> rowMapper,
			@Qualifier("PodApplicationFilterParser") ApplicationFilterParser filterParser) {
		super(jdbi, EDESFilterColumnMapping.DB_NAME, EDESFilterColumnMapping.QUERIES_FILE_PATH, rowMapper,
				filterParser);
	}

	@Override
	public long count(String whereCondition) {
		List<String> whereConditions = getListOfConditions(whereCondition, EDESFilteringHelper.WHERE_CONDITION_SEPARATOR);
		return jdbi.withHandle(handle -> handle
				.createQuery(ClasspathSqlLocator
						.findSqlOnClasspath(buildRelativeQueryPathName(COUNT_QUERY_BY_FILTER_FILE_NAME)))
				.define("WHERE_CONDITION_1", whereConditions.get(0)).define("WHERE_CONDITION_2", whereConditions.get(1))
				.mapTo(Long.class).findFirst().get());
	}

	@Override
	public List<EDESPod> findPaginatedResults(String whereCondition, int page, int size) {
		List<String> whereConditions = getListOfConditions(whereCondition, EDESFilteringHelper.WHERE_CONDITION_SEPARATOR);
		return jdbi.withHandle(handle -> handle
				.createQuery(ClasspathSqlLocator
						.findSqlOnClasspath(buildRelativeQueryPathName(FIND_QUERY_BY_FILTER_FILE_NAME)))
				.define("WHERE_CONDITION_1", whereConditions.get(0)).define("WHERE_CONDITION_2", whereConditions.get(1))
				.bind(OFFSET_PARAM_NAME, page).bind(FETCH_PARAM_NAME, size).map(mapper).list());
	}
		

}
