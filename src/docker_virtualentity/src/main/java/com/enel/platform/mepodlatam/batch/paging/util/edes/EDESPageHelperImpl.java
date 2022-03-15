package com.enel.platform.mepodlatam.batch.paging.util.edes;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enel.platform.entity.repository.batch.BatchRepository;
import com.enel.platform.mepodlatam.batch.paging.util.PageHelper;
import com.enel.platform.mepodlatam.batch.paging.util.PageHelperImpl;
import com.enel.platform.mepodlatam.model.edes.EDESPod;

@Component
public class EDESPageHelperImpl extends PageHelperImpl<EDESPod>
		implements PageHelper<EDESPod> {
	public EDESPageHelperImpl(@Value("${batch.api.EDES.mepodlatam.record_per_page}") int pageSize,
			@Qualifier("EDESPodTableNameSuffix") String tableName,
			BatchRepository<EDESPod> batchRepository) {
		super(pageSize, tableName, batchRepository);
	}

}
