package com.enel.platform.mepodlatam.batch.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OnDemandAccessResponseDTO {

	private String fullPath;
    private TemporaryCredentials temporaryCredentials;
   

}
