package com.pilot.common.utils;

import java.math.BigDecimal;

public class DecimalUtils {

	/**
	 * 格式化浮点数，并四舍五入
	 */
	public static float formatDouble(double dbl, int decimal) {
		try {
			BigDecimal bd = new BigDecimal(dbl);
			return bd.setScale(decimal, BigDecimal.ROUND_HALF_UP).floatValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0.0f;
	}

	public static String format(double f,int dec){
		return String.format("%."+dec+"f", f);
	}

}
