package com.pilot.common.log.bean;

import android.support.annotation.NonNull;

public class LogInfo {

	public enum LogLevel {
		VERBOSE,
		DEBUG,
		INFO,
		WARN,
		ERROR,
		ASSERT,
	}

	private LogLevel            mLogLevel;
	private String              mTag;
	private String              mMessage;
	private String              mThreadName;
	private String              mTitle;
	private Throwable           mThrowable;
	private StackTraceElement[] mStackTrace;
	private int                 mStackMethodCount;

	public LogInfo() {

	}

	public void apply(Builder builder) {
		this.mLogLevel = builder.mLogLevel;
		this.mTag = builder.mTag;
		this.mMessage = builder.mMessage;
		this.mThreadName = builder.mThreadName;
		this.mTitle = builder.mTitle;
		this.mThrowable = builder.mThrowable;
		this.mStackTrace = builder.mStackTrace;
		this.mStackMethodCount = builder.mStackMethodCount;
	}

	public LogLevel getLogLevel() {
		return mLogLevel;
	}

	public String getTag() {
		return mTag;
	}

	public String getMessage() {
		return mMessage;
	}

	public String getThreadName() {
		return mThreadName;
	}

	public String getTitle() {
		return mTitle;
	}

	public Throwable getThrowable() {
		return mThrowable;
	}

	public StackTraceElement[] getStackTrace() {
		return mStackTrace;
	}

	public int getStackMethodCount() {
		return mStackMethodCount;
	}

	public static class Builder {

		private LogLevel            mLogLevel;
		private String              mTag;
		private String              mMessage;
		private String              mThreadName;
		private String              mTitle;
		private Throwable           mThrowable;
		private StackTraceElement[] mStackTrace;
		private int                 mStackMethodCount;

		public Builder(@NonNull LogLevel logLevel, @NonNull String tag, @NonNull String message) {
			this.mLogLevel = logLevel;
			this.mTag = tag;
			this.mMessage = message;
		}

		public Builder threadName(String threadName) {
			mThreadName = threadName;
			return this;
		}

		public Builder title(String title) {
			mTitle = title;
			return this;
		}

		public Builder throwable(Throwable throwable) {
			mThrowable = throwable;
			return this;
		}

		public Builder stackTrace(StackTraceElement[] traceElements, int methodCount) {
			mStackTrace = traceElements;
			mStackMethodCount = methodCount;
			return this;
		}

		public LogInfo build() {
			LogInfo info = new LogInfo();
			info.apply(this);
			return info;
		}
	}
}
