package com.pilot.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;

public class TextViewUtil {

	/**
	 * 设置字体大小，用px
	 *
	 * @param str    目标字符串
	 * @param start  开始位置
	 * @param end    结束位置
	 * @param pxSize 像素大小
	 */
	public static SpannableString getSizeSpanUsePx(String str, int start, int end, int pxSize) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new AbsoluteSizeSpan(pxSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置字体大小，用dip
	 *
	 * @param str     目标字符串
	 * @param start   开始位置
	 * @param end     结束位置
	 * @param dipSize 像素大小
	 */
	public static SpannableString getSizeSpanUseDip(String str, int start, int end, int dipSize) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new AbsoluteSizeSpan(dipSize, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置字体大小，用sp
	 *
	 * @param str    目标字符串
	 * @param start  开始位置
	 * @param end    结束位置
	 * @param spSize sp大小
	 */
	public static SpannableString getSizeSpanSpToPx(Context context, String str, int start, int end, int spSize) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(context, spSize)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置字体相对大小
	 *
	 * @param str          目标字符串
	 * @param start        开始位置
	 * @param end          结束位置
	 * @param relativeSize 相对大小 如：0.5f，2.0f
	 */
	public static SpannableString getRelativeSizeSpan(String str, int start, int end, float relativeSize) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new RelativeSizeSpan(relativeSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置字体
	 *
	 * @param str      目标字符串
	 * @param start    开始位置
	 * @param end      结束位置
	 * @param typeface 字体类型 如：default，efault-bold,monospace,serif,sans-serif
	 */
	public static SpannableString getTypeFaceSpan(String str, int start, int end, String typeface) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new TypefaceSpan(typeface), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置字体形体
	 *
	 * @param str   目标字符串
	 * @param start 开始位置
	 * @param end   结束位置
	 * @param style 字体类型 如： Typeface.NORMAL正常 Typeface.BOLD粗体 Typeface.ITALIC斜体 Typeface.BOLD_ITALIC粗斜体
	 */
	public static SpannableString getStyleSpan(String str, int start, int end, int style) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置字体下划线
	 *
	 * @param str   目标字符串
	 * @param start 开始位置
	 * @param end   结束位置
	 */
	public static SpannableString getUnderLineSpan(String str, int start, int end) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置字体删除线
	 *
	 * @param str   目标字符串
	 * @param start 开始位置
	 * @param end   结束位置
	 */
	public static SpannableString getDeleteLineSpan(String str, int start, int end) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置上标
	 *
	 * @param str   目标字符串
	 * @param start 开始位置
	 * @param end   结束位置
	 */
	public static SpannableString getSuperscriptSpan(String str, int start, int end) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new SuperscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置放大系数
	 *
	 * @param str   目标字符串
	 * @param start 开始位置
	 * @param end   结束位置
	 * @param scale 放大多少倍，x轴方向，y轴不变 如：0.5f， 2.0f
	 */
	public static SpannableString getScaleSpan(String str, int start, int end, float scale) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new ScaleXSpan(scale), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置下标
	 *
	 * @param str   目标字符串
	 * @param start 开始位置
	 * @param end   结束位置
	 */
	public static SpannableString getSubscriptSpan(String str, int start, int end) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new SubscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置背景色
	 *
	 * @param str   目标字符串
	 * @param start 开始位置
	 * @param end   结束位置
	 * @param color 颜色值 如Color.BLACK
	 */
	public static SpannableString getBackGroundColorSpan(String str, int start, int end, int color) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置背景色
	 *
	 * @param str   目标字符串
	 * @param start 开始位置
	 * @param end   结束位置
	 * @param color 颜色值 如：#CCCCCC
	 */
	public static SpannableString getBackGroundColorSpan(String str, int start, int end, String color) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new BackgroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置前景色
	 *
	 * @param str   目标字符串
	 * @param start 开始位置
	 * @param end   结束位置
	 * @param color 颜色值 如Color.BLACK
	 */
	public static SpannableString getForegroundColorSpan(String str, int start, int end, int color) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置前景色
	 *
	 * @param str   目标字符串
	 * @param start 开始位置
	 * @param end   结束位置
	 * @param color 颜色值 如：#CCCCCC
	 */
	public static SpannableString getForegroundColorSpan(String str, int start, int end, String color) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 设置前景色
	 *
	 * @param str           目标字符串
	 * @param color         颜色值 如：#CCCCCC
	 * @param specialString 可以同时设置多个字符串
	 */
	public static SpannableString getForegroundColorSpan(String str, int color, String... specialString) {
		SpannableString ss = new SpannableString(str);
		for (String special : specialString) {
			int start = str.indexOf(special);
			int end = start + special.length();
			ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return ss;
	}
}
