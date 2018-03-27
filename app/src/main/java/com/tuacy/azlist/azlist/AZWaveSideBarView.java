package com.tuacy.azlist.azlist;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.pilot.common.utils.TextDrawUtils;
import com.tuacy.azlist.R;

import java.util.Arrays;
import java.util.List;


public class AZWaveSideBarView extends View {

	private static final double ANGLE_45 = Math.PI * 45 / 180;

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
	private int mBarPadding;
	private int mBarWidth;

	private List<String>           mLetters;
	private RectF                  mSlideBarRect;
	private TextPaint              mTextPaint;
	private Paint                  mPaint;
	private Paint                  mWavePaint;
	private Path                   mWavePath;
	private int                    mSelect;
	private int                    mPreSelect;
	private int                    mNewSelect;
	private ValueAnimator          mRatioAnimator;
	private float                  mAnimationRatio;
	private OnLetterChangeListener mListener;
	private int                    mTouchY;

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
		mBackgroundColor = typeArray.getColor(R.styleable.AZWaveSideBarView_backgroundColor, Color.parseColor("#F9F9F9"));
		mStrokeColor = typeArray.getColor(R.styleable.AZWaveSideBarView_strokeColor, Color.parseColor("#000000"));
		mTextColor = typeArray.getColor(R.styleable.AZWaveSideBarView_textColor, Color.parseColor("#969696"));
		mTextSize = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_textSize,
													  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
																					  getResources().getDisplayMetrics()));
		mSelectTextColor = typeArray.getColor(R.styleable.AZWaveSideBarView_selectTextColor, Color.parseColor("#FFFFFF"));
		mSelectTextSize = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_selectTextSize,
															(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
																							getResources().getDisplayMetrics()));
		mHintTextColor = typeArray.getColor(R.styleable.AZWaveSideBarView_hintTextColor, Color.parseColor("#FFFFFF"));
		mHintTextSize = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_hintTextSize,
														  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
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
															(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
																							getResources().getDisplayMetrics()));
		mBarPadding = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_barPadding,
														(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
																						getResources().getDisplayMetrics()));
		mBarWidth = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_barWidth,
													  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
																					  getResources().getDisplayMetrics()));
		if (mBarWidth == 0) {
			mBarWidth = 2 * mTextSize;
		}
		typeArray.recycle();
	}

	private void initData() {
		mLetters = Arrays.asList(getContext().getResources().getStringArray(R.array.slide_bar_value_list));
		mTextPaint = new TextPaint();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mWavePaint = new Paint();
		mWavePaint.setAntiAlias(true);
		mWavePath = new Path();
		mSelect = -1;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mSlideBarRect == null) {
			mSlideBarRect = new RectF();
		}
		float contentLeft = getMeasuredWidth() - mBarWidth - mBarPadding;
		float contentRight = getMeasuredWidth() - mBarPadding;
		float contentTop = mBarPadding;
		float contentBottom = (float) (getMeasuredHeight() - mBarPadding);
		mSlideBarRect.set(contentLeft, contentTop, contentRight, contentBottom);

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
		//绘制圆角矩形
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mBackgroundColor);
		canvas.drawRoundRect(mSlideBarRect, mBarWidth / 2.0f, mBarWidth / 2.0f, mPaint);
		//绘制描边
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(mStrokeColor);
		canvas.drawRoundRect(mSlideBarRect, mBarWidth / 2.0f, mBarWidth / 2.0f, mPaint);
		//顺序绘制文字
		float itemHeight = (mSlideBarRect.bottom - mSlideBarRect.top - mContentPadding * 2) / mLetters.size();
		for (int index = 0; index < mLetters.size(); index++) {
			float baseLine = TextDrawUtils.getTextBaseLineByCenter(
				mSlideBarRect.top + mContentPadding + itemHeight * index + itemHeight / 2, mTextPaint, mTextSize);
			mTextPaint.setColor(mTextColor);
			mTextPaint.setTextSize(mTextSize);
			mTextPaint.setTextAlign(Paint.Align.CENTER);
			float pointX = mSlideBarRect.left + (mSlideBarRect.right - mSlideBarRect.left) / 2.0f;
			canvas.drawText(mLetters.get(index), pointX, baseLine, mTextPaint);
		}
	}

	/**
	 * 绘制选中时的波纹效果
	 */
	private void drawWave(Canvas canvas) {
		mWavePath.reset();
		// 移动到起始点
		int startX = getMeasuredWidth();
		int startY = mTouchY - 3 * mWaveRadius;
		mWavePath.moveTo(startX, startY);
		//计算上部控制点的Y轴位置
		int topControlX = getMeasuredWidth();
		int topControlY = mTouchY - 2 * mWaveRadius;
		int topEndX = (int) (getMeasuredWidth() - mWaveRadius * Math.cos(ANGLE_45) * mAnimationRatio);
		int topEndY = (int) (topControlY + mWaveRadius * Math.sin(ANGLE_45));
		mWavePath.quadTo(topControlX, topControlY, topEndX, topEndY);
		//计算中心控制点的坐标
		int centerControlX = (int) (getMeasuredWidth() - 1.8f * mWaveRadius * mAnimationRatio);
		int centerControlY = mTouchY;
		int centerEndX = topEndX;
		int centerEndY = (int) (mTouchY + 2 * mWaveRadius - mWaveRadius * Math.cos(ANGLE_45));
		mWavePath.quadTo(centerControlX, centerControlY, centerEndX, centerEndY);
		//计算下部结束点的坐标
		int bottomEndX = getMeasuredWidth();
		int bottomEndY = mTouchY + 3 * mWaveRadius;
		int bottomControlX = getMeasuredWidth();
		int bottomControlY = mTouchY + 2 * mWaveRadius;
		mWavePath.quadTo(bottomControlX, bottomControlY, bottomEndX, bottomEndY);
		mWavePath.close();
		mWavePaint.setStyle(Paint.Style.FILL);
		mWavePaint.setColor(mWaveColor);
		canvas.drawPath(mWavePath, mWavePaint);
	}

	/**
	 * 绘制选中时的提示信息(圆＋文字)
	 */
	private void drawSelect(Canvas canvas) {
		if (mSelect != -1) {
			mTextPaint.setColor(mSelectTextColor);
			mTextPaint.setTextSize(mSelectTextSize);
			mTextPaint.setTextAlign(Paint.Align.CENTER);
			float itemHeight = (mSlideBarRect.bottom - mSlideBarRect.top - mContentPadding * 2) / mLetters.size();
			float baseLine = TextDrawUtils.getTextBaseLineByCenter(
				mSlideBarRect.top + mContentPadding + itemHeight * mSelect + itemHeight / 2, mTextPaint, mTextSize);
			float pointX = mSlideBarRect.left + (mSlideBarRect.right - mSlideBarRect.left) / 2.0f;
			canvas.drawText(mLetters.get(mSelect), pointX, baseLine, mTextPaint);
		}
	}

	/**
	 * 绘制选中的slide bar上的那个文字
	 */
	private void drawHint(Canvas canvas) {
		//x轴的移动路径
		float circleCenterX = (getMeasuredWidth() + mHintCircleRadius) - (2.0f * mWaveRadius + 2.0f * mHintCircleRadius) * mAnimationRatio;
		mWavePaint.setStyle(Paint.Style.FILL);
		mWavePaint.setColor(mHintCircleColor);
		canvas.drawCircle(circleCenterX, mTouchY, mHintCircleRadius, mWavePaint);
		// 绘制提示字符
		if (mAnimationRatio >= 0.9f && mSelect != -1) {
			String target = mLetters.get(mSelect);
			float textY = TextDrawUtils.getTextBaseLineByCenter(mTouchY, mTextPaint, mHintTextSize);
			mTextPaint.setColor(mHintTextColor);
			mTextPaint.setTextSize(mHintTextSize);
			mTextPaint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(target, circleCenterX, textY, mTextPaint);
		}
	}



	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final float y = event.getY();
		final float x = event.getX();
		mPreSelect = mSelect;
		mNewSelect = (int) (y / (mSlideBarRect.bottom - mSlideBarRect.top) * mLetters.size());
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//保证down的时候在bar区域才相应事件
				if (x < mSlideBarRect.left || y < mSlideBarRect.top || y > mSlideBarRect.bottom) {
					return false;
				}
				mTouchY = (int) y;
				startAnimator(1.0f);
				break;
			case MotionEvent.ACTION_MOVE:
				mTouchY = (int) y;
				if (mPreSelect != mNewSelect && mNewSelect >= 0 && mNewSelect < mLetters.size()) {
					mSelect = mNewSelect;
					if (mListener != null) {
						mListener.onLetterChange(mLetters.get(mNewSelect));
					}
				}
				invalidate();
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				startAnimator(0f);
				mSelect = -1;
				break;
			default:
				break;
		}
		return true;
	}

	private void startAnimator(float value) {
		if (mRatioAnimator == null) {
			mRatioAnimator = new ValueAnimator();
		}
		mRatioAnimator.cancel();
		mRatioAnimator.setFloatValues(value);
		mRatioAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator value) {
				mAnimationRatio = (float) value.getAnimatedValue();
				//球弹到位的时候，并且点击的位置变了，即点击的时候显示当前选择位置
				if (mAnimationRatio == 1f && mPreSelect != mNewSelect) {
					if (mNewSelect >= 0 && mNewSelect < mLetters.size()) {
						mSelect = mNewSelect;
						if (mListener != null) {
							mListener.onLetterChange(mLetters.get(mNewSelect));
						}
					}
				}
				invalidate();
			}
		});
		mRatioAnimator.start();
	}

	public void setOnLetterChangeListener(OnLetterChangeListener listener) {
		this.mListener = listener;
	}

	public interface OnLetterChangeListener {

		void onLetterChange(String letter);
	}
}
