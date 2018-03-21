package com.pilot.common.widget;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import com.pilot.common.R;
import com.pilot.common.utils.DensityUtils;

public class SpreadTextView extends View {

	private Context       mContext;
	private Paint         mPaint;
	private Xfermode      mMode;
	private String        mText;
	private int           mSize;
	private int           mColor;
	private Rect          mRectTemp;
	private int           mStartShade;
	private ValueAnimator mValueAnimator;

	public SpreadTextView(Context context) {
		this(context, null);
	}

	public SpreadTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SpreadTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		initAttribute(attrs, defStyleAttr);
		init();
	}

	private void initAttribute(AttributeSet attrs, int defStyleAttr) {
		TypedArray typeArray = mContext.obtainStyledAttributes(attrs, R.styleable.SpreadTextView, defStyleAttr, 0);
		mText = typeArray.getString(R.styleable.SpreadTextView_spread_text_value);
		mSize = typeArray.getDimensionPixelSize(R.styleable.SpreadTextView_spread_text_size, DensityUtils.sp2px(getContext(), 14));
		mColor = typeArray.getColor(R.styleable.SpreadTextView_spread_text_color, Color.parseColor("#FFFFFF"));
		typeArray.recycle();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
		mMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
		mPaint.setColor(mColor);
		mPaint.setTextSize(mSize);
		mRectTemp = new Rect();
		mStartShade = 0;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
		startAniamtion();
	}

	private int measureWidth(int measureSpec) {
		int result = getPaddingLeft() + getPaddingRight();
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		switch (specMode) {
			case MeasureSpec.AT_MOST:
				if (mText == null || mText.isEmpty()) {
					break;
				}
				mPaint.getTextBounds(mText, 0, mText.length(), mRectTemp);
				result += mRectTemp.width();
				break;
			case MeasureSpec.UNSPECIFIED:
			case MeasureSpec.EXACTLY:
				result = specSize;
				break;
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = getPaddingTop() + getPaddingBottom();
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		switch (specMode) {
			case MeasureSpec.AT_MOST:
				if (mText == null || mText.isEmpty()) {
					break;
				}
				mPaint.getTextBounds(mText, 0, mText.length(), mRectTemp);
				result += mRectTemp.height();
				break;
			case MeasureSpec.UNSPECIFIED:
			case MeasureSpec.EXACTLY:
				result = specSize;
				break;
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mText == null || mText.isEmpty()) {
			return;
		}
		int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
		mPaint.setColor(mColor);
		mPaint.setTextAlign(Paint.Align.LEFT);
		mPaint.getTextBounds(mText, 0, mText.length(), mRectTemp);
		Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
		int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
		canvas.drawText(mText, getMeasuredWidth() / 2 - mRectTemp.width() / 2, baseline, mPaint);
		mPaint.setXfermode(mMode);
		mPaint.setColor(Color.RED);
		canvas.drawRect(mStartShade, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
		mPaint.setXfermode(null);
		canvas.restoreToCount(saveCount);
	}

	public void setStartShade(int startShade) {
		mStartShade = startShade;
		if (mText != null && !mText.isEmpty()) {
			postInvalidate();
		}
	}

	public void startAniamtion() {
		if (mValueAnimator != null) {
			mValueAnimator.cancel();
		}
		mValueAnimator = ValueAnimator.ofInt(0, getMeasuredWidth());
		mValueAnimator.setDuration(2000);
		mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int currentValue = (Integer) animation.getAnimatedValue();
				setStartShade(currentValue);
			}
		});
		mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
		mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
		mValueAnimator.start();
	}

	public void stopAnimation() {
		if (mValueAnimator != null) {
			mValueAnimator.cancel();
		}
	}

}
