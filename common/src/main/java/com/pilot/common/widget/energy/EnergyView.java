package com.pilot.common.widget.energy;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.pilot.common.R;
import com.pilot.common.utils.DensityUtils;

import java.util.Locale;

public class EnergyView extends View {

	private Context mContext;
	private float   mRadius;
	private float   mOuterCircleRadius;
	private int     mOuterCircleColor;
	private int     mOuterCircleSpace;
	private float   mBaseLineWidth;
	private int     mBaseLineColor;
	private float   mFocusLineWidth;
	private int     mFocusLineColor;
	private float   mFocusRadius;
	private int     mTitleTextColor;
	private float   mTitleTextSize;
	private String  mTitleTextContent;
	private int     mValueTextColor;
	private float   mValueTextSize;
	private float   mValueCurrent;
	private float   mValueTotal;
	private int     mUnitTextColor;
	private float   mUnitTextSize;
	private String  mUnitTextContent;
	private float   mTextSpace;

	private Paint                mBaseLinePaint;
	private Paint                mFocusLinePaint;
	private RectF                mFocusRectF;
	private Paint                mFocusCirclePaint;
	private Paint                mOuterCirclePaint;
	private TextPaint            mTitlePaint;
	private Paint.FontMetricsInt mTextTileMetrics;
	private TextPaint            mValuePaint;
	private Paint.FontMetricsInt mTextValueMetrics;
	private TextPaint            mUnitPaint;
	private Paint.FontMetricsInt mTextUnitMetrics;

	public EnergyView(Context context) {
		this(context, null);
	}

	public EnergyView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EnergyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		initAttribute(attrs, defStyleAttr);
		initData();
	}

	private void initAttribute(AttributeSet attrs, int defStyleAttr) {
		TypedArray typeArray = mContext.obtainStyledAttributes(attrs, R.styleable.EnergyView, defStyleAttr, 0);
		mRadius = typeArray.getDimensionPixelSize(R.styleable.EnergyView_energy_radius, DensityUtils.dp2px(getContext(), 90));
		mOuterCircleRadius = typeArray.getDimensionPixelSize(R.styleable.EnergyView_energy_outerCircleRadius,
															 DensityUtils.dp2px(getContext(), 1));
		mOuterCircleSpace = typeArray.getDimensionPixelSize(R.styleable.EnergyView_energy_outerCircleSpace,
															DensityUtils.dp2px(getContext(), 10));
		mOuterCircleColor = typeArray.getColor(R.styleable.EnergyView_energy_outerCircleColor, Color.parseColor("#FFFFFF"));
		mBaseLineWidth = typeArray.getDimensionPixelSize(R.styleable.EnergyView_energy_baseLineWidth, DensityUtils.dp2px(getContext(), 3));
		mBaseLineColor = typeArray.getColor(R.styleable.EnergyView_energy_baseLineColor, Color.parseColor("#1A8DCF"));
		mFocusLineWidth = typeArray.getDimensionPixelSize(R.styleable.EnergyView_energy_focusLineWidth,
														  DensityUtils.dp2px(getContext(), 3));
		mFocusLineColor = typeArray.getColor(R.styleable.EnergyView_energy_focusLineColor, Color.parseColor("#FFFFFF"));
		mFocusRadius = typeArray.getDimensionPixelSize(R.styleable.EnergyView_energy_focusRadius, DensityUtils.dp2px(getContext(), 4));
		mTitleTextColor = typeArray.getColor(R.styleable.EnergyView_energy_titleTextColor, Color.parseColor("#FFFFFF"));
		mTitleTextSize = typeArray.getDimensionPixelSize(R.styleable.EnergyView_energy_titleTextSize, DensityUtils.sp2px(getContext(), 20));
		mTitleTextContent = typeArray.getString(R.styleable.EnergyView_energy_titleTextContent);
		mValueTextColor = typeArray.getColor(R.styleable.EnergyView_energy_valueTextColor, Color.parseColor("#B1EF18"));
		mValueTextSize = typeArray.getDimensionPixelSize(R.styleable.EnergyView_energy_valueTextSize, DensityUtils.sp2px(getContext(), 24));
		mValueCurrent = typeArray.getFloat(R.styleable.EnergyView_energy_valueCurrent, 0);
		mValueTotal = typeArray.getFloat(R.styleable.EnergyView_energy_valueTotal, 0);
		mUnitTextColor = typeArray.getColor(R.styleable.EnergyView_energy_unitTextColor, Color.parseColor("#FFFFFF"));
		mUnitTextSize = typeArray.getDimensionPixelSize(R.styleable.EnergyView_energy_unitTextSize, DensityUtils.sp2px(getContext(), 20));
		mUnitTextContent = typeArray.getString(R.styleable.EnergyView_energy_unitTextContent);
		mTextSpace = typeArray.getDimensionPixelSize(R.styleable.EnergyView_energy_text_space, 12);
		typeArray.recycle();
	}

	private void initData() {
		mBaseLinePaint = new Paint();
		mBaseLinePaint.setAntiAlias(true);
		mBaseLinePaint.setColor(mBaseLineColor);
		mBaseLinePaint.setStrokeWidth(mBaseLineWidth);
		mBaseLinePaint.setStyle(Paint.Style.STROKE);

		mFocusLinePaint = new Paint();
		mFocusLinePaint.setAntiAlias(true);
		mFocusLinePaint.setColor(mFocusLineColor);
		mFocusLinePaint.setStrokeWidth(mFocusLineWidth);
		mFocusLinePaint.setStyle(Paint.Style.STROKE);
		mFocusLinePaint.setShadowLayer(10, 0, 0, mFocusLineColor);

		mFocusCirclePaint = new Paint();
		mFocusCirclePaint.setAntiAlias(true);
		mFocusCirclePaint.setColor(mFocusLineColor);
		mFocusCirclePaint.setStyle(Paint.Style.FILL);
		mFocusCirclePaint.setShadowLayer(8, 0, 0, mFocusLineColor);

		mOuterCirclePaint = new Paint();
		mOuterCirclePaint.setAntiAlias(true);
		mOuterCirclePaint.setColor(mOuterCircleColor);
		mOuterCirclePaint.setStyle(Paint.Style.FILL);

		mFocusRectF = new RectF();

		mTitlePaint = new TextPaint();
		mTitlePaint.setColor(mTitleTextColor);
		mTitlePaint.setTextSize(mTitleTextSize);
		mTitlePaint.setTextAlign(Paint.Align.CENTER);
		mValuePaint = new TextPaint();
		mValuePaint.setColor(mValueTextColor);
		mValuePaint.setTextSize(mValueTextSize);
		mValuePaint.setTextAlign(Paint.Align.CENTER);
		mValuePaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		mUnitPaint = new TextPaint();
		mUnitPaint.setColor(mUnitTextColor);
		mUnitPaint.setTextSize(mUnitTextSize);
		mUnitPaint.setTextAlign(Paint.Align.CENTER);

		mTextValueMetrics = mValuePaint.getFontMetricsInt();
		mTextTileMetrics = mTitlePaint.getFontMetricsInt();
		mTextUnitMetrics = mUnitPaint.getFontMetricsInt();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = (int) ((mRadius + mOuterCircleSpace + mOuterCircleRadius * 2) * 2);
		int height = (int) ((mRadius + mOuterCircleSpace + mOuterCircleRadius * 2) * 2);
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float centerX = getWidth() / 2;
		float centerY = getHeight() / 2;
		canvas.translate(centerX, centerY);
		//画base线
		canvas.drawCircle(0, 0, mRadius, mBaseLinePaint);
		//计算角度
		float valueRatio;
		if (mValueTotal == 0) {
			valueRatio = 0;
		} else {
			if (mValueTotal <= mValueCurrent) {
				valueRatio = 1;
			} else {
				valueRatio = mValueCurrent / mValueTotal;
			}
		}
		setLayerType(View.LAYER_TYPE_SOFTWARE, mFocusLinePaint);
		//画focus线
		mFocusRectF.set(-mRadius, -mRadius, mRadius, mRadius);
		canvas.drawArc(mFocusRectF, 270, -360 * valueRatio, false, mFocusLinePaint);
		//画focus圆
		setLayerType(View.LAYER_TYPE_SOFTWARE, mFocusCirclePaint);
		canvas.save();
		canvas.rotate(-360 * valueRatio);
		canvas.drawCircle(0, -mRadius, mFocusRadius, mFocusCirclePaint);
		canvas.restore();

		//画外面的圆
		for (int index = 0; index < 36; index++) {
			canvas.save();
			canvas.rotate(-index * 10);
			canvas.drawCircle(0, -mRadius - mOuterCircleSpace - mOuterCircleRadius, mOuterCircleRadius, mOuterCirclePaint);
			canvas.restore();
		}

		//画文字
		//画value值
			String valueText = String.format(Locale.getDefault(), "%.03f%s", mValueCurrent, getContext().getString(R.string.common_million));
			float baseLineOffset = (mTextValueMetrics.bottom - mTextValueMetrics.top) / 2.0f - mTextValueMetrics.bottom;
			canvas.drawText(valueText, 0, baseLineOffset, mValuePaint);

		//画title
		if (mTitleTextContent != null) {
			float titleTextY = -(mTextValueMetrics.bottom - mTextValueMetrics.top) / 2.0f - mTextSpace - mTextTileMetrics.bottom;
			canvas.drawText(mTitleTextContent, 0, titleTextY, mTitlePaint);
		}
		//画unit
		if (mUnitTextContent != null) {
			float unitTextY = (mTextValueMetrics.bottom - mTextValueMetrics.top) / 2.0f + mTextSpace - mTextUnitMetrics.top;
			canvas.drawText(mUnitTextContent, 0, unitTextY, mUnitPaint);
		}
	}

	public void setValue(float value, float total) {
		mValueCurrent = value;
		mValueTotal = total;
		invalidate();
	}

	/**
	 * 动态去设置值
	 *
	 * @param value 目标值
	 * @param total 最大值
	 */
	public void animatorSetValue(float value, final float total) {
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, total, value);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float animatorValue = (float) animation.getAnimatedValue();
				setValue(animatorValue, total);
			}
		});
		valueAnimator.setInterpolator(new OvershootInterpolator());
		valueAnimator.setDuration(mContext.getResources().getInteger(R.integer.set_dash_board_view_duration));
		valueAnimator.start();
	}
}
