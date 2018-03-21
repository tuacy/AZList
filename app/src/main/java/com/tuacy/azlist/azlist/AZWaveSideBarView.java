package com.tuacy.azlist.azlist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.tuacy.azlist.R;

import java.util.Arrays;
import java.util.List;


public class AZWaveSideBarView extends View {

	private int mBackgroundColor;
	private int mStrokeColor;
	private int mTextColor;
	private int mTextSize;
	private int mSelectTextColor;
	private int mSelectTextSize;
	private int mHintTextColor;
	private int mHintTextSize;
	private int mHintCircleRadius;
	private int mHintCircleColor;
	private int mWaveColor;
	private int mWaveRadius;
	private int mContentPadding;

	private List<String> mLetters;
	private RectF        mSlideBarRect;

	public AZWaveSideBarView(Context context) {
		this(context, null);
	}

	public AZWaveSideBarView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AZWaveSideBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttribute(attrs, defStyleAttr);
		initData();
	}

	private void initAttribute(AttributeSet attrs, int defStyleAttr) {
		TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.AZWaveSideBarView, defStyleAttr, 0);
		mBackgroundColor = typeArray.getColor(R.styleable.AZWaveSideBarView_backgroundColor, Color.parseColor("#FFFFFF"));
		mStrokeColor = typeArray.getColor(R.styleable.AZWaveSideBarView_strokeColor, Color.parseColor("#000000"));
		mTextColor = typeArray.getColor(R.styleable.AZWaveSideBarView_textColor, Color.parseColor("#969696"));
		mTextSize = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_textSize,
													  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
																					  getResources().getDisplayMetrics()));
		mSelectTextColor = typeArray.getColor(R.styleable.AZWaveSideBarView_selectTextColor, Color.parseColor("#FFFFFF"));
		mSelectTextSize = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_selectTextSize,
															(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
																							getResources().getDisplayMetrics()));
		mHintTextColor = typeArray.getColor(R.styleable.AZWaveSideBarView_hintTextColor, Color.parseColor("#000000"));
		mHintTextSize = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_hintTextSize,
														  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
																						  getResources().getDisplayMetrics()));
		mHintCircleRadius = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_hintCircleRadius,
															  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
																							  getResources().getDisplayMetrics()));
		mHintCircleColor = typeArray.getColor(R.styleable.AZWaveSideBarView_hintCircleColor, Color.parseColor("#bef9b81b"));
		mWaveColor = typeArray.getColor(R.styleable.AZWaveSideBarView_waveColor, Color.parseColor("#bef9b81b"));
		mWaveRadius = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_waveRadius,
														(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
																						getResources().getDisplayMetrics()));
		mContentPadding = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_contentPadding,
															(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
																							getResources().getDisplayMetrics()));
		typeArray.recycle();
	}

	private void initData() {
		mLetters = Arrays.asList(getContext().getResources().getStringArray(R.array.slide_bar_value_list));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//绘制slide bar 上字母列表
		drawLetters(canvas);
		//绘制选中时的波纹效果
		drawWave(canvas);
		//绘制选中时的提示信息(圆＋文字)
		drawHint(canvas);
		//绘制选中的slide bar上的那个文字
		drawSelect(canvas);
	}

	/**
	 * 绘制slide bar 上字母列表
	 */
	private void drawLetters(Canvas canvas) {
	}

	/**
	 * 绘制选中时的波纹效果
	 */
	private void drawWave(Canvas canvas) {
	}

	/**
	 * 绘制选中时的提示信息(圆＋文字)
	 */
	private void drawSelect(Canvas canvas) {
	}

	/**
	 * 绘制选中的slide bar上的那个文字
	 */
	private void drawHint(Canvas canvas) {
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		return super.dispatchTouchEvent(event);
	}
}
