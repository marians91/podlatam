package com.enel.platform.mepodlatam.openapi.schemas;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PodSchema {
	
    private String idWorkOrderActivity;
    private String eidApplication;
    private String eidRequest;
    private Timestamp dtCreation;
    private Timestamp dtCompletion;
    private Integer pointOfDelivery;
    private String serviceTypeDescr;
    private String woaSubstate;
    private String woaSubstateDescr;
    private String woaState;
    private String woaStateDescr;
    private String woaStateGlobal;
    private Timestamp dtLastModify;
    private String woaType;
    private String woaTypeDescr;
    private String woaSubType;
    private String woaSubTypeDescr;
    private String serviceType;
    private String serviceTypeGlobal;
    private String tenant;
    private String customerSegment;

}
