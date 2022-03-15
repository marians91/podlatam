package com.enel.platform.mepodlatam.json.adapter;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {

	private DateTimeFormatter dateTimeFormatter = null;

	public ZonedDateTimeAdapter() {
		dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
	}

	public ZonedDateTimeAdapter(DateTimeFormatter dtTimeFormatter) {
		this.dateTimeFormatter = dtTimeFormatter;
	}

	@Override
	public void write(JsonWriter out, ZonedDateTime value) throws IOException {
		if (value == null) {
			out.nullValue();
		} else {
			ZonedDateTime utcDate = value.toInstant().atZone(ZoneOffset.UTC);
			String date = utcDate.format(dateTimeFormatter);
			out.value(date);
		}
	}

	@Override
	public ZonedDateTime read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		String dateAsString = in.nextString();		
		return ZonedDateTime.parse(dateAsString, dateTimeFormatter);
	}

}