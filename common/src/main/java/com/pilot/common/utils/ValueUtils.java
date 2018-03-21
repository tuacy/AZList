package com.pilot.common.utils;

public class ValueUtils {

	/**
	 * 取中间值
	 */
	public static float clamp(float min, float value, float max) {
		return Math.min(Math.max(min, value), max);
	}

	public static int clamp(int min, int value, int max) {
		return Math.min(Math.max(min, value), max);
	}

}
