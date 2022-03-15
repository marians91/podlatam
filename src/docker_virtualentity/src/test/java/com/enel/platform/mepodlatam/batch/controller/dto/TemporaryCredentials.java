package com.enel.platform.mepodlatam.batch.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryCredentials {

	private Credentials r;
	private Credentials w;

}
