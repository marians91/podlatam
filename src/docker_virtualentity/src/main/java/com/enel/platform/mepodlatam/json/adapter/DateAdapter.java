package com.enel.platform.mepodlatam.json.adapter;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateAdapter extends TypeAdapter<Date> {

	private DateFormat dtFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		 if (value == null) {
	            out.nullValue();
	        } else {
	            String date = dtFormatter.format(value);
	            out.value(date);
	        }
	}

	@Override
	public Date read(JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String dateAsString = reader.nextString();
        Date date=null;
		try {
			date = new Date(dtFormatter.parse(dateAsString).getTime());
		} catch (ParseException e) {
			throw new IOException(e);
		}
		return date;
	}
}