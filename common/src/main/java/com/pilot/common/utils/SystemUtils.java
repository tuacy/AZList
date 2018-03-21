package com.pilot.common.utils;


public class SystemUtils {

	/**
	 * recommend default thread pool size according to system available processors
	 **/
	public static final int DEFAULT_THREAD_POOL_SIZE = getDefaultThreadPoolSize();

	private SystemUtils() {
		throw new AssertionError();
	}

	/**
	 * get recommend default thread pool size
	 */
	public static int getDefaultThreadPoolSize() {
		return getDefaultThreadPoolSize(8);
	}

	/**
	 * get recommend default thread pool size
	 *
	 * @return if 2 * availableProcessors + 1 less than max, return it, else return max;
	 */
	public static int getDefaultThreadPoolSize(int max) {
		int availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1;
		return availableProcessors > max ? max : availableProcessors;
	}
}
