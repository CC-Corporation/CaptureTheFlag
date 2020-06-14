package de.flag.utils;

import java.util.HashMap;
import java.util.Map.Entry;

public class StringData {
	public StringData() {

	}

	public void load(String s) {
		stg.clear();
		for (String s2 : s.split(";;;")) {
			String[] s3 = s2.split("/=/");
			stg.put(s3[0], s3[1]);
		}
	}

	public String save() {
		if (stg.isEmpty())
			return "";
		String s2 = "";
		for (Entry<String, String> e : stg.entrySet()) {
			s2 += e.getKey() + "/=/" + e.getValue() + ";;;";
		}

		return s2;
	}

	public String getString(String key) {
		return stg.get(key);
	}
	public void addString(String key, String value) {
		stg.put(key, value);
	}

	HashMap<String, String> stg = new HashMap<String, String>();

}
