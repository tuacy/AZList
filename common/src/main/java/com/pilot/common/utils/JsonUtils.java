package com.pilot.common.utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtils {

	/**
	 * 注意嵌套的情况
	 */
	public static boolean containsKey(JSONObject jsonObject, String key) {
		if (jsonObject == null) {
			return false;
		}
		Iterator it = jsonObject.keys();
		List<String> keyList = new ArrayList<>();
		while (it.hasNext()) {
			keyList.add(it.next().toString());
		}
		return keyList.contains(key);
	}

}
