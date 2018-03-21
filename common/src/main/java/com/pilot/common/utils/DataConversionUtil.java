package com.pilot.common.utils;


import java.math.BigDecimal;

public class DataConversionUtil {

	public static float stringDecimal(String data, int decimalNumber) {
		if (data.isEmpty()) {
			return 0.0f;
		}
		try {
			BigDecimal bigDecimal = new BigDecimal(data);
			return bigDecimal.setScale(decimalNumber, BigDecimal.ROUND_HALF_UP).floatValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0f;
	}

	public static float floatDecimal(float data, int decimalNumber) {
		try {
			BigDecimal bigDecimal = new BigDecimal(data);
			return bigDecimal.setScale(decimalNumber, BigDecimal.ROUND_HALF_UP).floatValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0f;
	}

}
