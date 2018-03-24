package com.tuacy.azlist.azlist;


public class AZItemEntity<T> {

	private T      mValue;
	private String mLetters;

	public T getValue() {
		return mValue;
	}

	public void setValue(T value) {
		mValue = value;
	}

	public String getLetters() {
		return mLetters;
	}

	public void setLetters(String letters) {
		mLetters = letters;
	}
}
