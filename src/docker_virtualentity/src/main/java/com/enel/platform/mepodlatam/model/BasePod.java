package com.enel.platform.mepodlatam.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePod implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SerializedName("pointcode")
	private String pointCode;	
	@SerializedName("idworkorderactivity")
	private String idWorkOrderActivity;	
	@SerializedName("idvisit")
	private String idVisit;	
	@SerializedName("codvisittype")
	private String codVisitType;	
	@SerializedName("codselectiontype")
	private String codSelectionType;	
	@SerializedName("codterritorialdivision")
	private String codTerritorialDivision;	
	@SerializedName("statusresult")
	private String statusResult;	
	@SerializedName("resultdescription")
	private String resultDescription;	
	@SerializedName("eidactivityrequest")
	private String eidActivityRequest;	
	@SerializedName("dtwoacreation")
	private ZonedDateTime dtWoaCreation;	
	@SerializedName("flgprocessed")
	private String flgProcessed;	
	@SerializedName("tenant")
	private String tenant;
	       
}
