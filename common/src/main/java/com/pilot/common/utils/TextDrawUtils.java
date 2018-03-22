package com.pilot.common.utils;


import android.graphics.Paint;
import android.text.TextPaint;

public class TextDrawUtils {
	/**
	 * 获取文字的宽度
	 */
	public static float getTextWidth(String text, TextPaint paint, int size) {
		if (text == null || text.isEmpty()) {
			return 0f;
		}
		paint.setTextSize(size);
		return paint.measureText(text);
	}

	/**
	 * 获取文字的高度
	 */
	public static float getTextHeight(TextPaint paint, int size) {
		paint.setTextSize(size);
		Paint.FontMetrics fontMetrics = paint.getFontMetrics();
		return fontMetrics.bottom - fontMetrics.top;
	}

	/**
	 * 获取文字的bottom
	 */
	public static float getTextBottom(TextPaint paint, int size) {
		paint.setTextSize(size);
		Paint.FontMetrics fontMetrics = paint.getFontMetrics();
		return fontMetrics.bottom;
	}

	/**
	 * 给定文字的top获取文字的base line
	 */
	public static float getTextBaseLineByTop(float top, TextPaint paint, int size) {
		paint.setTextSize(size);
		Paint.FontMetrics fontMetrics = paint.getFontMetrics();
		return top - fontMetrics.top;
	}

	/**
	 * 给定文字的bottom获取文字的base line
	 */
	public static float getTextBaseLineByBottom(float bottom, TextPaint paint, int size) {
		paint.setTextSize(size);
		Paint.FontMetrics fontMetrics = paint.getFontMetrics();
		return bottom - fontMetrics.bottom;
	}

	/**
	 * 给定文字的center获取文字的base line
	 */
	public static float getTextBaseLineByCenter(float center, TextPaint paint, int size) {
		paint.setTextSize(size);
		Paint.FontMetrics fontMetrics = paint.getFontMetrics();
		float height = fontMetrics.bottom - fontMetrics.top;
		return center + height / 2 - fontMetrics.bottom;
	}
}
