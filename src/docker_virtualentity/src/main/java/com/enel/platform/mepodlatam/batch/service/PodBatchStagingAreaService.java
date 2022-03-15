package com.enel.platform.mepodlatam.batch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enel.platform.entity.model.batch.CommitStatus;
import com.enel.platform.entity.repository.batch.BatchStagingArea;
import com.enel.platform.mepodlatam.model.edes.EDESPod;

@Service
public class PodBatchStagingAreaService implements BatchStagingArea<EDESPod> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PodBatchStagingAreaService.class); 
	
	@Override
	public void stageItem(String uuid, EDESPod pod) {
		LOGGER.info("{} - stage all ", uuid);	
	}

	@Override
	public void commitAll(String uuid) {
		LOGGER.info("{} - commit all ", uuid);
		
	}

	@Override
	public void unstageAll(String uuid) {
		LOGGER.info("{} - unstage all ", uuid);		
	}

	@Override
	public CommitStatus getCommitStatus(String uuid) {
		return CommitStatus.Success;
	}

}
