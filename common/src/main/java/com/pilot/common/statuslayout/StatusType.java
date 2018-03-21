package com.pilot.common.statuslayout;

public enum  StatusType {
	/**
	 * 内容
	 */
	CONTENT(0),
	/**
	 * 加载
	 */
	LOADING(1),
	/**
	 * 空数据
	 */
	EMPTY(2),
	/**
	 * 异常
	 */
	EXCEPTION(3);

	private int mType;

	private StatusType(int type) {
		mType = type;
	}

	public int type() {
		return mType;
	}
}
