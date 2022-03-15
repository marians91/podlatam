package com.enel.platform.mepodlatam.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestErrorResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> errorMessages;
    private T output;
   

}
