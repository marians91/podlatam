package com.enel.platform.mepodlatam.util;

import com.google.gson.GsonBuilder;

public class JsonUtils {

	private JsonUtils() {
	}

	public static String objToString(Object obj) {
		return new GsonBuilder().serializeNulls().create().toJson(obj);
	}

}
