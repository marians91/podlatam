package com.enel.platform.mepodlatam.batch.controller.dto;

import com.enel.platform.entity.model.batch.Credentials;
import com.enel.platform.entity.model.batch.ExtractionType;
import com.enel.platform.entity.model.batch.Format;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtractionExecutionPlanResponseDTO {

	private Format format;
	private String s3Path;
	private ExtractionType extractionType;
	private ReadTemporaryCredentials temporaryCredentials;

	class ReadTemporaryCredentials {
		Credentials r;

		public ReadTemporaryCredentials() {
			
		}
		public ReadTemporaryCredentials(Credentials r) {
			this.r = r;
		}

		public Credentials getR() {
			return r;
		}

		@Override
		public String toString() {
			return "ReadTemporaryCredentials{" + "r=" + r + '}';
		}
	}
}
