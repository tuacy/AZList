package com.pilot.common.log;


public interface Logger {

	Logger methodCount(int methodCount);

	Logger title(String title);

	Logger threadInfo(boolean display);


	/**
	 * easy debug for tuacy
	 *
	 * @param msg log message
	 */
	void tuacy(String msg);

	void d(String tag, String msg);

	void d(String tag, String format, Object... args);

	void d(Class<?> clazz, String format, Object... args);

	void e(String tag, String msg);

	void e(String tag, String format, Object... args);

	void e(Class<?> clazz, String format, Object... args);

	void e(String tag, Throwable t, String format, Object... args);

	void e(Class<?> clazz, Throwable t, String format, Object... args);

	void w(String tag, String msg);

	void w(String tag, String format, Object... args);

	void w(Class<?> clazz, String format, Object... args);

	void i(String tag, String msg);

	void i(String tag, String format, Object... args);

	void i(Class<?> clazz, String format, Object... args);

	void v(String tag, String msg);

	void v(String tag, String format, Object... args);

	void v(Class<?> clazz, String format, Object... args);

	void wtf(String tag, String msg);

	void wtf(String tag, String format, Object... args);

	void wtf(Class<?> clazz, String format, Object... args);

	void json(String tag, String json);

	void json(Class<?> clazz, String json);

	void xml(String tag, String xml);

	void xml(Class<?> clazz, String xml);
}
