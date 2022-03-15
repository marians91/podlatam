package com.enel.platform.mepodlatam.batch.util;

import com.enel.platform.entity.model.batch.ExtractionExecutionPlanRequest;
import com.enel.platform.entity.model.batch.ExtractionType;
import com.enel.platform.entity.utils.batch.ExecutionPlanHelper;
import com.enel.platform.mepodlatam.batch.s3.PodS3Helper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PodExecutionPlanHelper implements ExecutionPlanHelper {

	private final PodS3Helper s3Helper;
	private final String apiName;
	private final String version;

	@Override
	public ExtractionType getExtractionType(ExtractionExecutionPlanRequest request) {
		if (s3Helper.s3KeysExist(apiName, version, request.getApplicationFilter())) {
			return ExtractionType.Cold;
		} else {
			return ExtractionType.Hot;
		}
	}

}
