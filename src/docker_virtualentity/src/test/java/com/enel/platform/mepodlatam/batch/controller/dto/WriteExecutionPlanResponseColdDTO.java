package com.enel.platform.mepodlatam.batch.controller.dto;

import com.enel.platform.entity.model.batch.Format;
import com.enel.platform.entity.model.batch.WriteType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WriteExecutionPlanResponseColdDTO {


	private Format format;
    private String writeUri;
    private WriteType writeType;
    private TemporaryCredentials temporaryCredentials;

 

    public Format getFormat() {
        return format;
    }

    public String getWriteUri() {
        return writeUri;
    }

    public WriteType getWriteType() {
        return writeType;
    }

    public TemporaryCredentials getTemporaryCredentials() {
        return temporaryCredentials;
    }


	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TemporaryCredentials {
		private Credentials r;
		private Credentials w;

	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Credentials {
		private String accessKeyID;
		private String secretKey;
		private String sessionToken;
	}
}
