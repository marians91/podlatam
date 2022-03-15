package com.enel.platform.mepodlatam.json.adapter.edes;

import java.time.format.DateTimeFormatter;

import com.enel.platform.mepodlatam.json.adapter.ZonedDateTimeAdapter;

public class EDESZonedDateTimeAdapter extends ZonedDateTimeAdapter {
	
	public EDESZonedDateTimeAdapter() {	
		super(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
	}
	
}