package com.pilot.common.widget.dashboard;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.pilot.common.R;
import com.pilot.common.utils.ResourceUtils;

import java.util.List;
import java.util.Locale;

/**
 * 仪表盘View
 */
public class DashboardView extends View {

	private final static int   DEFAULT_RADIUS_DP                 = 95;
	private final static int   DEFAULT_RADIUS_START_ANGLE        = 180;
	private final static int   DEFAULT_RADIUS_SWEEP_ANGLE        = 180;
	private final static int   DEFAULT_LONG_SCALE_COUNT          = 8;
	private final static int   DEFAULT_LONG_SCALE_WIDTH          = 4;
	private final static int   DEFAULT_LONG_SCALE_HEIGHT_DP      = 9;
	private final static int   DEFAULT_SHORT_SCALE_COUNT         = 5;
	private final static int   DEFAULT_SHORT_SCALE_WIDTH         = 2;
	private final static int   DEFAULT_SHORT_SCALE_HEIGHT_DP     = 6;
	private final static int   DEFAULT_LONG_SCALE_VALUE_SPACE_DP = 14;
	private final static int   DEFAULT_SLICE_COLOR               = Color.RED;
	private final static int   DEFAULT_ARC_COLOR                 = Color.RED;
	private final static int   DEFAULT_ARC_STROKE_WIDTH_DP       = 5;
	private final static int   DEFAULT_SCALE_TEXT_SIZE           = 12;
	private final static int   DEFAULT_VALUE_TEXT_SIZE           = 16;
	private final static int   DEFAULT_VALUE_UNIT_SIZE           = 8;
	private final static int   DEFAULT_SCALE_TEXT_COLOR          = Color.BLACK;
	private final static int   DEFAULT_VALUE_TEXT_COLOR          = Color.parseColor("#F34D1D");
	private final static int   DEFAULT_POINT_RADIUS_DP           = 80;
	private final static int   DEFAULT_POINT_COLOR               = Color.parseColor("#00FF00");
	private final static float DEFAULT_CENTER_CIRCLE_RADIO       = 2 / 5f; // 用于显示数据，把数据显示在中心
	private final static int   DEFAULT_CENTER_CIRCLE_COLOR       = Color.WHITE;
	private final static float DEFAULT_MIN_VALUE                 = 0;
	private final static float DEFAULT_MAX_VALUE                 = 1.8f;
	private final static float DEFAULT_REAL_TIME_VALUE           = 0.0f;
	private final static int   DEFAULT_NO_VALUE                  = -1;
	private final static int   DEFAULT_UNIT_PADDING_TOP_DP       = 2;
	private final static int   DEFAULT_EXTRA_SPACE_DP            = 2;
	private final static float DEFAULT_POINT_OFFSET              = 5;

	public interface ValueFormatter {

		String getFormattedValue(float value);
	}


	private Context mContext;

	/**
	 * 圆弧半径
	 */
	private int               mRadius;
	/**
	 * 开始角度
	 */
	private float             mStartAngle;
	/**
	 * 扫过的角度
	 */
	private float             mSweepAngle;
	/**
	 * 仪表盘长刻度总数
	 */
	private int               mLongScaleCount;
	/**
	 * 仪表盘长刻度宽度
	 */
	private int               mLongScaleWidth;
	/**
	 * 仪表盘长刻度半径
	 */
	private int               mLongScaleRadius;
	/**
	 * 仪表盘长刻度里面短刻度总数
	 */
	private int               mEachShortScaleCount;
	/**
	 * 仪表盘每个长刻度里面短刻度宽度
	 */
	private int               mShortScaleWidth;
	/**
	 * 刻度宽度的总数
	 */
	private int               mSortScaleCount;
	/**
	 * 仪表盘短刻度半径
	 */
	private int               mShortScaleRadius;
	/**
	 * 长刻度等分角度
	 */
	private float             mEachLongScaleAngle;
	/**
	 * 短刻度等分角度
	 */
	private float             mEachShoutScaleAngle;
	/**
	 * 仪表盘刻度颜色
	 */
	private int               mScaleColor;
	/**
	 * 圆弧的颜色
	 */
	private int               mArcColor;
	/**
	 * 圆弧的线宽
	 */
	private int               mArcStrokeWidth;
	/**
	 * 刻度字体大小
	 */
	private int               mScaleTextSize;
	/**
	 * 刻度字体颜色
	 */
	private int               mScaleTextColor;
	/**
	 * 指针半径
	 */
	private int               mPointerRadius;
	/**
	 * 指针颜色
	 */
	private int               mPointColor;
	/**
	 * 数据中心圆半径
	 */
	private int               mValueCenterRadius;
	/**
	 * 中心圆颜色
	 */
	private int               mCenterColor;
	/**
	 * 最小值
	 */
	private float             mMinValue;
	/**
	 * 最大值
	 */
	private float             mMaxValue;
	/**
	 * 实时值
	 */
	private float             mCurrentValue;
	/**
	 * 数字刻度半径
	 */
	private int               mScaleNumberRadius;
	/**
	 * 高亮范围颜色对象的集合
	 */
	private List<HighlightCR> mStripeHighlight;
	/**
	 * 背景色
	 */
	private int               mBgColor;
	/**
	 * 当前值的单位文本
	 */
	private String            mCurrentValueUnitText;
	/**
	 * 中间值字体大小
	 */
	private int               mValueTextSize;
	/**
	 * 中间值单位大小
	 */
	private int               mValueUnitSize;
	/**
	 * 中间值颜色
	 */
	private int               mValueTextColor;
	/**
	 * 圆弧中心点x坐标
	 */
	private float             mCenterX;
	/**
	 * 圆弧中心点y坐标
	 */
	private float             mCenterY;
	/**
	 * 绘制圆弧的画笔
	 */
	private Paint             mArcPaint;
	/**
	 * 绘制指针的画笔
	 */
	private Paint             mPointPaint;
	/**
	 * 绘制指针的路径
	 */
	private Path              mPointPath;
	/**
	 * 绘制长刻度的画笔
	 */
	private Paint             mLongScalePaint;
	/**
	 * 绘制短刻度的画笔
	 */
	private Paint             mShortScalePaint;
	/**
	 * 绘制刻度值的画笔
	 */
	private Paint             mScaleNumberPaint;
	/**
	 * 绘制中间圆的画笔
	 */
	private Paint             mCenterCirclePaint;
	/**
	 * 绘制当前值的画笔
	 */
	private Paint             mCurrentValuePaint;
	/**
	 * 等分的刻度值
	 */
	private String[]          mGraduations;

	private ValueFormatter mValueFormatter;

	public DashboardView(Context context) {
		this(context, null);
	}

	public DashboardView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		initAttribute(attrs, defStyleAttr);
		initData();
	}

	private void initAttribute(AttributeSet attrs, int defStyleAttr) {
		TypedArray typeArray = mContext.obtainStyledAttributes(attrs, R.styleable.DashboardView, defStyleAttr, 0);
		mRadius = typeArray.getDimensionPixelSize(R.styleable.DashboardView_radius, dpToPx(DEFAULT_RADIUS_DP));
		mStartAngle = typeArray.getFloat(R.styleable.DashboardView_startAngle, DEFAULT_RADIUS_START_ANGLE);
		mSweepAngle = typeArray.getFloat(R.styleable.DashboardView_sweepAngle, DEFAULT_RADIUS_SWEEP_ANGLE);
		mLongScaleCount = typeArray.getInteger(R.styleable.DashboardView_longScaleCount, DEFAULT_LONG_SCALE_COUNT);
		mLongScaleWidth = typeArray.getDimensionPixelSize(R.styleable.DashboardView_longScaleWidth, dpToPx(DEFAULT_LONG_SCALE_WIDTH));
		int longScaleHeight = typeArray.getDimensionPixelSize(R.styleable.DashboardView_longScaleHeight,
															  dpToPx(DEFAULT_LONG_SCALE_HEIGHT_DP));
		mLongScaleRadius = mRadius - longScaleHeight;
		mEachShortScaleCount = typeArray.getInteger(R.styleable.DashboardView_shortScaleCount, DEFAULT_SHORT_SCALE_COUNT);
		mSortScaleCount = mLongScaleCount * mEachShortScaleCount;
		mShortScaleWidth = typeArray.getDimensionPixelSize(R.styleable.DashboardView_shortScaleWidth, dpToPx(DEFAULT_SHORT_SCALE_WIDTH));
		int shortScaleHeight = typeArray.getDimensionPixelSize(R.styleable.DashboardView_shortScaleHeight,
															   dpToPx(DEFAULT_SHORT_SCALE_HEIGHT_DP));
		mShortScaleRadius = mRadius - shortScaleHeight; //短刻度长度
		int scaleNumberSpace = typeArray.getDimensionPixelSize(R.styleable.DashboardView_scaleNumberSpaceHeight,
															   dpToPx(DEFAULT_LONG_SCALE_VALUE_SPACE_DP));
		mScaleNumberRadius = mRadius - scaleNumberSpace;
		mScaleColor = typeArray.getDimensionPixelSize(R.styleable.DashboardView_sliceColor, dpToPx(DEFAULT_SLICE_COLOR));
		mArcColor = typeArray.getColor(R.styleable.DashboardView_arcColor, DEFAULT_ARC_COLOR);
		mArcStrokeWidth = typeArray.getDimensionPixelSize(R.styleable.DashboardView_arcStrokeWidth, dpToPx(DEFAULT_ARC_STROKE_WIDTH_DP));

		mScaleTextSize = typeArray.getDimensionPixelSize(R.styleable.DashboardView_scaleTextSize, spToPx(DEFAULT_SCALE_TEXT_SIZE));
		mScaleTextColor = typeArray.getColor(R.styleable.DashboardView_scaleTextColor, DEFAULT_SCALE_TEXT_COLOR);
		mValueTextSize = typeArray.getDimensionPixelSize(R.styleable.DashboardView_valueTextSize, spToPx(DEFAULT_VALUE_TEXT_SIZE));
		mValueUnitSize = typeArray.getDimensionPixelSize(R.styleable.DashboardView_valueUnitSize, spToPx(DEFAULT_VALUE_UNIT_SIZE));
		mValueTextColor = typeArray.getColor(R.styleable.DashboardView_valueTextColor, DEFAULT_VALUE_TEXT_COLOR);
		mPointerRadius = typeArray.getDimensionPixelSize(R.styleable.DashboardView_pointerRadius, DEFAULT_POINT_RADIUS_DP);

		mPointColor = typeArray.getColor(R.styleable.DashboardView_pointerColor, DEFAULT_POINT_COLOR);
		float valueCenterRadiusRadio = typeArray.getFloat(R.styleable.DashboardView_circleValueRadiusRatio, DEFAULT_CENTER_CIRCLE_RADIO);
		mValueCenterRadius = (int) (mRadius * valueCenterRadiusRadio);
		mCenterColor = typeArray.getColor(R.styleable.DashboardView_circleColor, DEFAULT_CENTER_CIRCLE_COLOR);
		mMinValue = typeArray.getFloat(R.styleable.DashboardView_minValue, DEFAULT_MIN_VALUE);
		mMaxValue = typeArray.getFloat(R.styleable.DashboardView_maxValue, DEFAULT_MAX_VALUE);
		mCurrentValue = typeArray.getFloat(R.styleable.DashboardView_currentValue, DEFAULT_REAL_TIME_VALUE);
		mCurrentValueUnitText = typeArray.getString(R.styleable.DashboardView_currentValueUnit);
		mCurrentValueUnitText = mCurrentValueUnitText == null ? "" : mCurrentValueUnitText;
		mBgColor = typeArray.getColor(R.styleable.DashboardView_bgColor, DEFAULT_NO_VALUE);
		typeArray.recycle();
	}

	/**
	 * 获取分段之后每个长刻度的具体值
	 */
	private String[] getEachLongScaleNumberValue() {
		String[] strings = new String[mLongScaleCount + 1];
		for (int i = 0; i <= mLongScaleCount; i++) {
			if (i == 0) {
				strings[i] = String.valueOf(mValueFormatter.getFormattedValue(mMinValue));
			} else if (i == mLongScaleCount) {
				strings[i] = String.valueOf(mValueFormatter.getFormattedValue(mMaxValue));
			} else {
				strings[i] = String.valueOf(mValueFormatter.getFormattedValue(((mMaxValue - mMinValue) / mLongScaleCount) * i));
			}
		}
		return strings;
	}

	private void initData() {
		mValueFormatter = new ValueFormatter() {

			@Override
			public String getFormattedValue(float value) {
				return String.format(Locale.US, "%.02f", value);
			}
		};
		/**
		 * 初始化绘制圆弧相关
		 */
		mArcPaint = new Paint();
		mArcPaint.setAntiAlias(true);
		mArcPaint.setColor(mArcColor);
		mArcPaint.setStyle(Paint.Style.STROKE);
		mArcPaint.setStrokeWidth(mArcStrokeWidth);
		mArcPaint.setStrokeCap(Paint.Cap.ROUND);//当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式, 这里设置为圆形样式
		/**
		 * 初始化指针的相关
		 */
		mPointPaint = new Paint();
		mPointPaint.setAntiAlias(true);
		mPointPaint.setStyle(Paint.Style.FILL);
		mPointPaint.setColor(mPointColor);
		mPointPath = new Path();
		/**
		 * 初始化长刻度绘制相关
		 */
		mLongScalePaint = new Paint();
		mLongScalePaint.setAntiAlias(true);
		mLongScalePaint.setColor(mScaleColor);
		mLongScalePaint.setStyle(Paint.Style.STROKE);
		mLongScalePaint.setStrokeWidth(mLongScaleWidth);
		/**
		 * 初始化短刻度绘制相关
		 */
		mShortScalePaint = new Paint();
		mShortScalePaint.setAntiAlias(true);
		mShortScalePaint.setColor(mScaleColor);
		mShortScalePaint.setStyle(Paint.Style.STROKE);
		mShortScalePaint.setStrokeWidth(mShortScaleWidth);
		/**
		 * 初始化刻度值绘制相关
		 */
		mScaleNumberPaint = new Paint();
		mScaleNumberPaint.setAntiAlias(true);
		mScaleNumberPaint.setTextSize(mScaleTextSize);
		mScaleNumberPaint.setColor(mScaleTextColor);
		/**
		 * 初始化中心圆绘制相关
		 */
		mCenterCirclePaint = new Paint();
		mCenterCirclePaint.setAntiAlias(true);
		mCenterCirclePaint.setColor(mCenterColor);
		/**
		 * 初始化当前值包括当前值的单位绘制相关
		 */
		mCurrentValuePaint = new Paint();
		mCurrentValuePaint.setAntiAlias(true);
		mCurrentValuePaint.setColor(mValueTextColor);
		mCurrentValuePaint.setTextAlign(Paint.Align.CENTER);
		mCurrentValuePaint.setTextSize(mValueTextSize);

		if (mSweepAngle > 360) {
			throw new IllegalArgumentException("sweepAngle must less than 360 degree");
		}
		mEachLongScaleAngle = mSweepAngle * 1.0f / mLongScaleCount;
		mEachShoutScaleAngle = mEachLongScaleAngle / mEachShortScaleCount;
		mGraduations = getEachLongScaleNumberValue();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		float viewWidth, viewHeight;

		if (widthMode == MeasureSpec.EXACTLY) {
			viewWidth = widthSize;
		} else {
			int tempWidth = mRadius * 2 + getPaddingLeft() + getPaddingRight() + 2 * dpToPx(DEFAULT_EXTRA_SPACE_DP);
			viewWidth = Math.min(tempWidth, widthSize);
		}
		mCenterX = viewWidth / 2.0f;

		if (heightMode == MeasureSpec.EXACTLY) {
			viewHeight = heightSize;
		} else {
			float[] point1 = getCoordinatePoint(mRadius, mStartAngle);
			float[] point2 = getCoordinatePoint(mRadius, mStartAngle + mSweepAngle);
			float point1y = mRadius + point1[1];
			float point2y = mRadius + point2[1];
			if (point1y < mRadius && point2y < mRadius) {
				// 说明都是在上方的
				viewHeight = mRadius + mValueCenterRadius +
							 getPaddingTop() + getPaddingBottom() + 2 * dpToPx(DEFAULT_EXTRA_SPACE_DP);
				mCenterY = mRadius + getPaddingTop() + dpToPx(DEFAULT_EXTRA_SPACE_DP);
			} else {
				float max = Math.max(point1y, point2y);
				if (max < mRadius + mValueCenterRadius) {
					viewHeight = mRadius + mValueCenterRadius +
								 getPaddingTop() + getPaddingBottom() + 2 * dpToPx(DEFAULT_EXTRA_SPACE_DP);
					mCenterY = mRadius + getPaddingTop() + dpToPx(DEFAULT_EXTRA_SPACE_DP);
				} else {
					viewHeight = max +
								 getPaddingTop() + getPaddingBottom() + 2 * dpToPx(DEFAULT_EXTRA_SPACE_DP);
					mCenterY = mRadius + getPaddingTop() + dpToPx(DEFAULT_EXTRA_SPACE_DP);
				}
			}
			viewHeight = Math.min(viewHeight, widthSize);
		}

		setMeasuredDimension((int) viewWidth, (int) viewHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 设置背景演示
		if (mBgColor != DEFAULT_NO_VALUE) {
			canvas.drawColor(mBgColor);
		}
		//绘制圆弧
		drawArc(canvas);
		//绘制指针
		drawPointer(canvas);
		//绘制刻度盘
		drawScale(canvas);
		//画中心圆
		drawCenterCircle(canvas);
	}

	/**
	 * 根据角度去获取刻度的颜色
	 */
	private int getScaleColor(float angle) {
		int scaleColor = mScaleColor;
		int weightCount = 0;
		if (mStripeHighlight != null && mStripeHighlight.size() > 0) {
			for (HighlightCR highlightCR : mStripeHighlight) {
				weightCount = weightCount + highlightCR.getWeight();
			}
			float preStartAngle = mStartAngle;
			float preSweepAngle = 0;
			for (HighlightCR highlightCR : mStripeHighlight) {
				float ratio = highlightCR.getWeight() * 1.0f / weightCount;
				float startAngle = preStartAngle + preSweepAngle;
				float sweepAngle = mSweepAngle * ratio;
				preStartAngle = startAngle;
				preSweepAngle = sweepAngle;
				if (angle < startAngle + sweepAngle) {
					scaleColor = highlightCR.getArcColor();
					break;
				}
			}
		}
		return scaleColor;
	}

	/**
	 * 根据角度去获取刻度值的颜色
	 */
	private Paint.Align getScaleTextAlign(float angle) {
		Paint.Align align;
		if (angle % 360 > 135 && angle % 360 < 215) {
			align = Paint.Align.LEFT;
		} else if ((angle % 360 >= 0 && angle % 360 < 45) || (angle % 360 > 325 && angle % 360 <= 360)) {
			align = Paint.Align.RIGHT;
		} else {
			align = Paint.Align.CENTER;
		}
		return align;
	}

	/**
	 * 绘制刻度盘
	 */
	private void drawScale(Canvas canvas) {
		//绘制大刻度
		for (int i = 0; i <= mLongScaleCount; i++) {
			float angle = i * mEachLongScaleAngle + mStartAngle;
			float[] point1 = getCoordinatePoint(mCenterX, mCenterY, mRadius, angle);
			float[] point2 = getCoordinatePoint(mCenterX, mCenterY, mLongScaleRadius, angle);

			mLongScalePaint.setColor(getScaleColor(angle));
			canvas.drawLine(point1[0], point1[1], point2[0], point2[1], mLongScalePaint);

			//绘制大刻度上的数字
			String number = mGraduations[i];
			Rect textRect = new Rect();
			mScaleNumberPaint.getTextBounds(number, 0, number.length(), textRect);
			mScaleNumberPaint.setTextAlign(getScaleTextAlign(angle));
			float[] numberPoint = getCoordinatePoint(mCenterX, mCenterY, mScaleNumberRadius, angle);
			if (i == 0 || i == mLongScaleCount) {
				canvas.drawText(number, numberPoint[0], numberPoint[1] + (textRect.height() / 2), mScaleNumberPaint);
			} else {
				canvas.drawText(number, numberPoint[0], numberPoint[1] + textRect.height(), mScaleNumberPaint);
			}
		}

		//绘制小的子刻度
		for (int i = 0; i < mSortScaleCount; i++) {
			if (i % mEachShortScaleCount != 0) {
				float angle = i * mEachShoutScaleAngle + mStartAngle;
				float[] point1 = getCoordinatePoint(mCenterX, mCenterY, mRadius, angle);
				float[] point2 = getCoordinatePoint(mCenterX, mCenterY, mShortScaleRadius, angle);
				mShortScalePaint.setColor(getScaleColor(angle));
				canvas.drawLine(point1[0], point1[1], point2[0], point2[1], mShortScalePaint);
			}
		}
	}

	/**
	 * 绘制刻度盘的弧形
	 */
	private void drawArc(Canvas canvas) {
		RectF rectArc = new RectF(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);
		if (mStripeHighlight != null && mStripeHighlight.size() > 0) {
			int weightCount = 0;
			for (HighlightCR highlightCR : mStripeHighlight) {
				weightCount = weightCount + highlightCR.getWeight();
			}
			// 圆弧颜色分段
			float preStartAngle = mStartAngle;
			float preSweepAngle = 0;
			for (HighlightCR highlightCR : mStripeHighlight) {
				mArcPaint.setColor(highlightCR.getArcColor());
				float ratio = highlightCR.getWeight() * 1.0f / weightCount;
				float startAngle = preStartAngle + preSweepAngle;
				float sweepAngle = mSweepAngle * ratio;
				preStartAngle = startAngle;
				preSweepAngle = sweepAngle;
				canvas.drawArc(rectArc, startAngle, sweepAngle, false, mArcPaint);
			}
		} else {
			mArcPaint.setColor(mArcColor);
			canvas.drawArc(rectArc, mStartAngle, mSweepAngle, false, mArcPaint);
		}
	}

	/**
	 * 根据当前值去获取字体的颜色
	 */
	private int getValueTextColor() {
		int valueTextColor = mValueTextColor;
		if (mStripeHighlight != null && mStripeHighlight.size() > 0) {
			int weightCount = 0;
			for (HighlightCR highlightCR : mStripeHighlight) {
				weightCount = weightCount + highlightCR.getWeight();
			}
			float preValue = mMinValue;
			for (HighlightCR highlightCR : mStripeHighlight) {
				float ratio = highlightCR.getWeight() * 1.0f / weightCount;
				float value = preValue + ratio * (mMaxValue - mMinValue);
				if (mCurrentValue > preValue && mCurrentValue <= value) {
					valueTextColor = highlightCR.getPointerColor();
					break;
				}
				preValue = value;

			}
		}

		return valueTextColor;
	}

	/**
	 * 绘制圆和文字读数
	 */
	private void drawCenterCircle(Canvas canvas) {
		//数据中间圆圈绘制
		setLayerType(LAYER_TYPE_SOFTWARE, mCenterCirclePaint);
		mCenterCirclePaint.setShadowLayer(mValueCenterRadius, 0, 0, ResourceUtils.getColor(mContext, R.color.bash_board_center_shader));
		canvas.drawCircle(mCenterX, mCenterY, mValueCenterRadius, mCenterCirclePaint);
//		canvas.restore();
		//		setLayerType(LAYER_TYPE_HARDWARE, mCurrentValuePaint);
		//根据当前仪表表值所在的范围，设置指定的颜色
		mCurrentValuePaint.setColor(getValueTextColor());//当前值文字颜色
		//绘制读数
		mCurrentValuePaint.setTextSize(mValueTextSize);
		canvas.drawText(mValueFormatter.getFormattedValue(mCurrentValue) + "", mCenterX, mCenterY, mCurrentValuePaint);
		//绘制单位
		mCurrentValuePaint.setTextSize(mValueUnitSize);
		Rect unitRect = new Rect();
		mCurrentValuePaint.getTextBounds(mCurrentValueUnitText, 0, mCurrentValueUnitText.length(), unitRect);
		canvas.drawText(mCurrentValueUnitText, mCenterX, mCenterY + unitRect.height() + dpToPx(DEFAULT_UNIT_PADDING_TOP_DP),
						mCurrentValuePaint);
	}

	/**
	 * 获取指针的颜色
	 */
	private int getPointColor() {
		int pointColor = mPointColor;
		if (mStripeHighlight != null && mStripeHighlight.size() > 0) {
			int weightCount = 0;
			for (HighlightCR highlightCR : mStripeHighlight) {
				weightCount = weightCount + highlightCR.getWeight();
			}
			float preValue = mMinValue;
			for (HighlightCR highlightCR : mStripeHighlight) {
				float ratio = highlightCR.getWeight() * 1.0f / weightCount;
				float value = preValue + ratio * (mMaxValue - mMinValue);
				if (mCurrentValue > preValue && mCurrentValue <= value) {
					pointColor = highlightCR.getPointerColor();
					break;
				}
				preValue = value;

			}
		}
		return pointColor;
	}

	/**
	 * 绘制指针
	 */
	private void drawPointer(Canvas canvas) {
		float valueAngle = getAngleFromResult(mCurrentValue);
		mPointPaint.setColor(getPointColor());
		mPointPath.reset();
		float[] point1 = getCoordinatePoint(mCenterX, mCenterY, mValueCenterRadius, valueAngle + DEFAULT_POINT_OFFSET);
		mPointPath.moveTo(point1[0], point1[1]);
		float[] point2 = getCoordinatePoint(mCenterX, mCenterY, mValueCenterRadius, valueAngle - DEFAULT_POINT_OFFSET);
		mPointPath.lineTo(point2[0], point2[1]);
		float[] point3 = getCoordinatePoint(mCenterX, mCenterY, mPointerRadius, valueAngle);
		mPointPath.lineTo(point3[0], point3[1]);
		mPointPath.close();
		canvas.drawPath(mPointPath, mPointPaint);
	}

	/**
	 * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
	 */
	public float[] getCoordinatePoint(int radius, float cirAngle) {
		return getCoordinatePoint(0, 0, radius, cirAngle);
	}

	/**
	 * 获取弧对应的坐标
	 */
	public float[] getCoordinatePoint(float centerX, float centerY, int radius, float cirAngle) {
		float[] point = new float[2];
		double arcAngle = Math.toRadians(cirAngle); //将角度转换为弧度
		if (cirAngle < 90) {
			point[0] = (float) (centerX + Math.cos(arcAngle) * radius);
			point[1] = (float) (centerY + Math.sin(arcAngle) * radius);
		} else if (cirAngle == 90) {
			point[0] = centerX;
			point[1] = centerY + radius;
		} else if (cirAngle > 90 && cirAngle < 180) {
			arcAngle = Math.PI * (180 - cirAngle) / 180.0;
			point[0] = (float) (centerX - Math.cos(arcAngle) * radius);
			point[1] = (float) (centerY + Math.sin(arcAngle) * radius);
		} else if (cirAngle == 180) {
			point[0] = centerX - radius;
			point[1] = centerY;
		} else if (cirAngle > 180 && cirAngle < 270) {
			arcAngle = Math.PI * (cirAngle - 180) / 180.0;
			point[0] = (float) (centerX - Math.cos(arcAngle) * radius);
			point[1] = (float) (centerY - Math.sin(arcAngle) * radius);
		} else if (cirAngle == 270) {
			point[0] = centerX;
			point[1] = centerY - radius;
		} else {
			arcAngle = Math.PI * (360 - cirAngle) / 180.0;
			point[0] = (float) (centerX + Math.cos(arcAngle) * radius);
			point[1] = (float) (centerY - Math.sin(arcAngle) * radius);
		}

		return point;
	}

	/**
	 * 通过数值得到角度位置
	 */
	private float getAngleFromResult(float result) {
		if (result > mMaxValue) {
			return mMaxValue;
		}
		return mSweepAngle * (result - mMinValue) / (mMaxValue - mMinValue) + mStartAngle;
	}

	public void setMaxValue(float maxValue) {
		mMaxValue = maxValue;
		mGraduations = getEachLongScaleNumberValue();
	}

	public void setValueFormatter(ValueFormatter valueFormatter) {
		mValueFormatter = valueFormatter;
		getEachLongScaleNumberValue();
	}

	public void animatorSetValue(float currentValue) {
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, mMaxValue, currentValue);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float animatorValue = (float) animation.getAnimatedValue();
				setValue(animatorValue);
			}
		});
		valueAnimator.setInterpolator(new OvershootInterpolator());
		valueAnimator.setDuration(mContext.getResources().getInteger(R.integer.set_dash_board_view_duration));
		valueAnimator.start();
	}

	public void setValue(float value) {
		value = value > mMaxValue ? mMaxValue : value;
		mCurrentValue = value;
		invalidate();
	}

	public void setHighlightCRList(List<HighlightCR> stripeHighlightList) {
		mStripeHighlight = stripeHighlightList;
	}

	private int dpToPx(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

	private int spToPx(int sp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
	}

}