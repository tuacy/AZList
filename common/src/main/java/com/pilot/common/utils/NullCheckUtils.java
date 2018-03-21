package com.pilot.common.utils;

/**
 * Created by Hans.Chen on 2015-12-24.
 */
public class NullCheckUtils {

	public static void checkNotNull(Object... objects) {
		if (objects == null) {
			return;
		}

		for (Object o : objects) {
			if (o == null) {
				throw new NullPointerException();
			}
		}
	}
}
