package com.enel.platform.mepodlatam.batch.controller.dto;

import com.enel.platform.entity.model.batch.ExtractionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtractionExecutionPlanHotResponseDTO {

	private ExtractionType extractionType;
}
