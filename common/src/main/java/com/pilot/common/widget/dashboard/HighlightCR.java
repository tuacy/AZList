package com.pilot.common.widget.dashboard;

public class HighlightCR {

	private int mWeight;
	private int mArcColor;
	private int mPointerColor;

	public HighlightCR(int weight, int arcColor, int pointerColor) {
		mWeight = weight;
		mArcColor = arcColor;
		mPointerColor = pointerColor;
	}

	public int getWeight() {
		return mWeight;
	}

	public void setWeight(int weight) {
		mWeight = weight;
	}

	public int getArcColor() {
		return mArcColor;
	}

	public void setArcColor(int color) {
		mArcColor = color;
	}

	public int getPointerColor() {
		return mPointerColor;
	}

	public void setPointerColor(int color) {
		mPointerColor = color;
	}
}
