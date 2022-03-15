package com.enel.platform.mepodlatam.batch.repository;

import java.util.List;

import com.enel.platform.entity.repository.batch.BatchRepository;

public interface PodBatchRepository<T> extends BatchRepository<T> {

	public long count();

	public long count(String whereCondition);
	
	public List<T> findPaginatedResults(int page, int size);
	
	public List<T> findPaginatedResults(String whereCondition, int page, int size);
	
}
