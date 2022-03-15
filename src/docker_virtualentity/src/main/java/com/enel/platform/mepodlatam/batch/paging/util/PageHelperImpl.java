package com.enel.platform.mepodlatam.batch.paging.util;

import java.util.Optional;

import com.enel.platform.entity.repository.batch.BatchRepository;


public class PageHelperImpl<T> implements PageHelper<T> {
	
	private BatchRepository<T> batchRepository;
	
	private int pageSize;	
	private String tableName;

	public PageHelperImpl(int pageSize, String tableName, BatchRepository<T> batchRepository) {
		this.pageSize = pageSize;
		this.tableName = tableName;
		this.batchRepository = batchRepository;
	}
	
	public int segmentsNumber(long recordsCount) {
        return segmentsNumber(recordsCount, pageSize);
    }

	@Override
	public int segmentsNumber(Optional<String> applicationFilter) {
		long recordsCount = 0;
		if(applicationFilter.isPresent()) {			
		   recordsCount = batchRepository.tableSize(tableName, applicationFilter.get());
		}else {
			recordsCount = batchRepository.tableSize(tableName);
		}
		return segmentsNumber(recordsCount, pageSize);
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	private int segmentsNumber(long recordsCount, long recordPerPage) {
		if(recordsCount == 0) {
			return 0;
		}
		double magnitude = (double) recordsCount / (double) recordPerPage;
		return magnitude == 0 ? 1 : (int) Math.ceil(magnitude);
	}

}
