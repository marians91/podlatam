package com.enel.platform.mepodlatam.batch.util;

import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.entity.model.batch.ExtractionExecutionPlanRequest;
import com.enel.platform.entity.model.batch.ExtractionExecutionPlanRequestDTO;
import com.enel.platform.entity.model.batch.ExtractionType;
import com.enel.platform.entity.model.batch.Source;
import com.enel.platform.mepodlatam.batch.s3.PodS3Helper;

@ExtendWith(MockitoExtension.class)
class PodRequestExecutionPlanHelperTest {

	@Mock
	private PodS3Helper s3Helper;

	@Test
	void getExtractionTypeHot() {
		String apiName = "visitrequest";
		String apiVersion = "v1";
		ExtractionExecutionPlanRequestDTO extractionExecutionPlanRequestDTO = new ExtractionExecutionPlanRequestDTO(
				Optional.empty(), Optional.empty(), Source.External);
		ExtractionExecutionPlanRequest request = ExtractionExecutionPlanRequest
				.fromRequestDTO(extractionExecutionPlanRequestDTO);
		when(s3Helper.s3KeysExist(apiName, apiVersion, Optional.empty())).thenReturn(false);
		PodExecutionPlanHelper planHelper = new PodExecutionPlanHelper(s3Helper, apiName, apiVersion);
		ExtractionType extractionType = planHelper.getExtractionType(request);
		assertEquals(ExtractionType.Hot, extractionType);

	}
	
	@Test
	void getExtractionTypeCold() {
		String apiName = "visitrequest";
		String apiVersion = "v1";
		ExtractionExecutionPlanRequestDTO extractionExecutionPlanRequestDTO = new ExtractionExecutionPlanRequestDTO(
				Optional.empty(), Optional.empty(), Source.External);
		ExtractionExecutionPlanRequest request = ExtractionExecutionPlanRequest
				.fromRequestDTO(extractionExecutionPlanRequestDTO);
		when(s3Helper.s3KeysExist(apiName, apiVersion,  Optional.empty())).thenReturn(true);
		PodExecutionPlanHelper planHelper = new PodExecutionPlanHelper(s3Helper, apiName, apiVersion);
		ExtractionType extractionType = planHelper.getExtractionType(request);
		assertEquals(ExtractionType.Cold, extractionType);

	}

}
