package com.gia.utils;

import java.util.Map;

public final class DataRetrival {

	private DataRetrival() {

	}

	public static String getStringValue(String columnName, Map<String, Object> map) {

		String value = "";
		if (map.get(columnName) != null) {
			value = map.get(columnName).toString();
		}

		return value;
	}

	public static int getIntValue(String columnName, Map<String, Object> map) {

		int value = 0;
		if (map.get(columnName) != null) {
			value = Integer.parseInt(map.get(columnName).toString());
		}

		return value;
	}

	public static long getLongValue(String columnName, Map<String, Object> map) {

		long value = 0;
		if (map.get(columnName) != null) {
			value = Long.parseLong(map.get(columnName).toString());
		}

		return value;
	}

	public static float getFloatValue(String columnName, Map<String, Object> map) {

		float value = 0.0f;
		if (map.get(columnName) != null) {
			value = Float.parseFloat(map.get(columnName).toString());
		}

		return value;
	}

	public static double getDoubleValue(String columnName, Map<String, Object> map) {

		double value = 0.0;
		if (map.get(columnName) != null) {
			value = Double.parseDouble(map.get(columnName).toString());
		}

		return value;
	}

	public static boolean getBooleanValue(String columnName, Map<String, Object> map) {

		boolean value = false;
		if (map.get(columnName) != null) {

			int i = Integer.parseInt(map.get(columnName).toString());
			if (i == 1)
				value = true;

		}

		return value;
	}
}

