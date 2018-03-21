package com.pilot.common.log;


import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.pilot.common.log.bean.LogInfo;
import com.pilot.common.log.printer.Printer;
import com.pilot.common.log.printer.impl.PrettyPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class LoggerImpl implements Logger {

	private Printer mPrinter      = PrettyPrinter.getInstance();
	private boolean mDebugEnabled = true;

	public LoggerImpl() {
	}

	public LoggerImpl(Printer mPrinter, boolean mDebugEnabled) {
		this.mPrinter = mPrinter;
		this.mDebugEnabled = mDebugEnabled;
	}

	public void setCustomLogger(Printer logger) {
		this.mPrinter = logger;
	}

	public void setDebugEnabled(boolean enable) {
		mDebugEnabled = enable;
	}

	public boolean isDebugEnabled() {
		return mDebugEnabled;
	}

	private static final ThreadLocal<Integer> LOCAL_METHOD_COUNT = new ThreadLocal<>();
	private static final ThreadLocal<String>  LOCAL_TITLE        = new ThreadLocal<>();
	private static final ThreadLocal<Boolean> LOCAL_THREAD_INFO  = new ThreadLocal<>();

	@Override
	public Logger methodCount(int methodCount) {
		LOCAL_METHOD_COUNT.set(methodCount);
		return this;
	}

	@Override
	public Logger title(String title) {
		LOCAL_TITLE.set(title);
		return this;
	}

	@Override
	public Logger threadInfo(boolean display) {
		LOCAL_THREAD_INFO.set(display);
		return this;
	}


	@Override
	public void tuacy(String msg) {
		log(LogInfo.LogLevel.DEBUG, "tuacy", msg);
	}

	@Override
	public void d(String tag, String msg) {
		log(LogInfo.LogLevel.DEBUG, tag, msg);
	}

	@Override
	public void d(String tag, String format, Object... args) {
		d(tag, String.format(format, args));
	}

	@Override
	public void d(Class<?> clazz, String format, Object... args) {
		d(clazz.getSimpleName(), format, args);
	}

	@Override
	public void e(String tag, String msg) {
		log(LogInfo.LogLevel.ERROR, tag, msg);
	}

	@Override
	public void e(String tag, String format, Object... args) {
		e(tag, String.format(format, args));
	}

	@Override
	public void e(Class<?> clazz, String format, Object... args) {
		e(clazz.getSimpleName(), format, args);
	}

	@Override
	public void e(String tag, Throwable t, String format, Object... args) {
		log(LogInfo.LogLevel.ERROR, tag, String.format(format, args), t);
	}

	@Override
	public void e(Class<?> clazz, Throwable t, String format, Object... args) {
		log(LogInfo.LogLevel.ERROR, clazz.getSimpleName(), String.format(format, args), t);
	}

	@Override
	public void w(String tag, String msg) {
		log(LogInfo.LogLevel.WARN, tag, msg);
	}

	@Override
	public void w(String tag, String format, Object... args) {
		w(tag, String.format(format, args));
	}

	@Override
	public void w(Class<?> clazz, String format, Object... args) {
		w(clazz.getSimpleName(), format, args);
	}

	@Override
	public void i(String tag, String msg) {
		log(LogInfo.LogLevel.INFO, tag, msg);
	}

	@Override
	public void i(String tag, String format, Object... args) {
		i(tag, String.format(format, args));
	}

	@Override
	public void i(Class<?> clazz, String format, Object... args) {
		i(clazz.getSimpleName(), format, args);
	}

	@Override
	public void v(String tag, String msg) {
		log(LogInfo.LogLevel.VERBOSE, tag, msg);
	}

	@Override
	public void v(String tag, String format, Object... args) {
		v(tag, String.format(format, args));
	}

	@Override
	public void v(Class<?> clazz, String format, Object... args) {
		v(clazz.getSimpleName(), format, args);
	}

	@Override
	public void wtf(String tag, String msg) {
		log(LogInfo.LogLevel.ASSERT, tag, msg);
	}

	@Override
	public void wtf(String tag, String format, Object... args) {
		wtf(tag, String.format(format, args));
	}

	@Override
	public void wtf(Class<?> clazz, String format, Object... args) {
		wtf(clazz.getSimpleName(), format, args);
	}

	@Override
	public void json(String tag, String json) {

		if (TextUtils.isEmpty(json)) {
			log(LogInfo.LogLevel.DEBUG, tag, "Empty/Null json content");
			return;
		}

		try {
			String result;
			if (json.startsWith("{")) {
				JSONObject jsonObject = new JSONObject(json);
				result = jsonObject.toString(4);
			} else if (json.startsWith("[")) {
				JSONArray jsonArray = new JSONArray(json);
				result = jsonArray.toString(4);
			} else {
				result = "Invalid json: \n" + json;
			}
			log(LogInfo.LogLevel.DEBUG, tag, result);
		} catch (JSONException e) {
			log(LogInfo.LogLevel.ERROR, tag, e.getMessage() + "\n" + json);
		}
	}

	@Override
	public void json(Class<?> clazz, String json) {
		json(clazz.getSimpleName(), json);
	}

	@Override
	public void xml(String tag, String xml) {

		if (TextUtils.isEmpty(xml)) {
			log(LogInfo.LogLevel.DEBUG, tag, "Empty/Null xml content");
			return;
		}

		try {
			Source xmlInput = new StreamSource(new StringReader(xml));
			StreamResult xmlOutput = new StreamResult(new StringWriter());
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(xmlInput, xmlOutput);
			log(LogInfo.LogLevel.DEBUG, tag, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
		} catch (TransformerException e) {
			log(LogInfo.LogLevel.ERROR, tag, e.getMessage() + "\n" + xml);
		}
	}

	@Override
	public void xml(Class<?> clazz, String xml) {
		xml(clazz.getSimpleName(), xml);
	}


	private void log(@NonNull LogInfo.LogLevel logLevel, @NonNull String tag, @NonNull String message) {
		log(logLevel, tag, message, null);
	}

	private void log(@NonNull LogInfo.LogLevel logLevel, @NonNull String tag, @NonNull String message, Throwable throwable) {
		if (isDebugEnabled()) {
			LogInfo.Builder builder = new LogInfo.Builder(logLevel, tag, message);

			Boolean displayThreadName = LOCAL_THREAD_INFO.get();
			if (displayThreadName != null && displayThreadName) {
				builder.threadName(Thread.currentThread().getName());
			}

			String title = LOCAL_TITLE.get();
			if (!TextUtils.isEmpty(title)) {
				builder.title(title);
			}

			Integer methodCount = LOCAL_METHOD_COUNT.get();
			if (methodCount != null && methodCount > 0) {
				builder.stackTrace(getStackTrace(), methodCount);
			}

			if (throwable != null) {
				builder.throwable(throwable);
			}

			mPrinter.print(builder.build());
		}

		LOCAL_THREAD_INFO.remove();
		LOCAL_TITLE.remove();
		LOCAL_METHOD_COUNT.remove();
	}

	private StackTraceElement[] getStackTrace() {
		StackTraceElement[] allTraces = Thread.currentThread().getStackTrace();
		int unusedCount = getUnusedStackCount(allTraces);
		StackTraceElement[] trace = new StackTraceElement[allTraces.length - unusedCount];
		System.arraycopy(allTraces, unusedCount, trace, 0, allTraces.length - unusedCount);
		return trace;
	}

	/**
	 * Determines the starting index of the stack trace
	 *
	 * @param trace the stack trace
	 * @return the start index
	 */
	private int getUnusedStackCount(@NonNull StackTraceElement[] trace) {
		/**
		 * The minimum stack trace index, starts at this class after two native calls.
		 */
		final int MIN_STACK_OFFSET = 2;
		int count = MIN_STACK_OFFSET;
		for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
			if (PilotLog.class.getName().equals(trace[i].getClassName()) || LoggerImpl.class.getName().equals(trace[i].getClassName())) {
				count++;
			} else {
				return count;
			}
		}
		return -1;
	}
}
