package com.pilot.common.widget.energysave;

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

import com.pilot.common.R;
import com.pilot.common.utils.DensityUtils;

import java.util.Locale;

import static android.graphics.Paint.Cap.BUTT;
import static android.graphics.Paint.Cap.ROUND;


public class EnergySaveView extends View {

	private int                  mRadius;
	private int                  mRingWidth;
	private int                  mNormalColor;
	private int                  mSelectColor;
	private int                  mInnerRadius;
	private int                  mInnerColor;
	private String               mInnerTextContent;
	private int                  mInnerTextSize;
	private int                  mInnerTextColor;
	private float                mInnerRadioValue;
	private int                  mInnerRadioSize;
	private int                  mInnerRadioColor;
	private int                  mFlagInnerRadioSize;
	private int                  mFlagInnerRadioColor;
	private int                  mFlagOuterRadioSize;
	private int                  mFlagOuterRadioColor;
	private Paint                mOuterPaint;
	private TextPaint            mInnerTextPain;
	private Paint.FontMetricsInt mInnerContextMetrics;
	private TextPaint            mInnerRadioPaint;
	private Paint.FontMetricsInt mInnerRadioMetrics;
	private RectF                mRect;
	private Paint                mInnerPaint;
	private Paint                mFlagInnerPaint;
	private Paint                mFlagOuterPaint;

	public EnergySaveView(Context context) {
		this(context, null);
	}

	public EnergySaveView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EnergySaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttribute(attrs, defStyleAttr);
		initData();
	}

	private void initAttribute(AttributeSet attrs, int defStyleAttr) {
		TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.EnergySaveView, defStyleAttr, 0);
		mRadius = typeArray.getDimensionPixelSize(R.styleable.EnergySaveView_energy_save_radius, DensityUtils.dp2px(getContext(), 100));
		mRingWidth = typeArray.getDimensionPixelSize(R.styleable.EnergySaveView_energy_save_ring_width,
													 DensityUtils.dp2px(getContext(), 12));
		mNormalColor = typeArray.getColor(R.styleable.EnergySaveView_energy_save_normal_ring_color, Color.parseColor("#EDEDED"));
		mSelectColor = typeArray.getColor(R.styleable.EnergySaveView_energy_save_select_ring_color, Color.parseColor("#27BCFF"));
		mInnerRadius = typeArray.getDimensionPixelSize(R.styleable.EnergySaveView_energy_save_inner_radius,
													   DensityUtils.dp2px(getContext(), 70));
		mInnerColor = typeArray.getColor(R.styleable.EnergySaveView_energy_save_inner_color, Color.parseColor("#FFFFFF"));
		mInnerTextContent = typeArray.getString(R.styleable.EnergySaveView_energy_save_inner_text_content);
		mInnerTextSize = typeArray.getDimensionPixelSize(R.styleable.EnergySaveView_energy_save_inner_text_size,
														 DensityUtils.sp2px(getContext(), 16));
		mInnerTextColor = typeArray.getColor(R.styleable.EnergySaveView_energy_save_inner_text_color, Color.parseColor("#848484"));
		mInnerRadioValue = typeArray.getFloat(R.styleable.EnergySaveView_energy_save_inner_radio_value, 0.6f);
		mInnerRadioSize = typeArray.getDimensionPixelSize(R.styleable.EnergySaveView_energy_save_inner_radio_size,
														  DensityUtils.sp2px(getContext(), 16));
		mInnerRadioColor = typeArray.getColor(R.styleable.EnergySaveView_energy_save_inner_radio_color, Color.parseColor("#FC9630"));
		mFlagInnerRadioSize = typeArray.getDimensionPixelSize(R.styleable.EnergySaveView_energy_save_flag_inner_radio_size,
															  DensityUtils.sp2px(getContext(), 10));
		mFlagInnerRadioColor = typeArray.getColor(R.styleable.EnergySaveView_energy_save_flag_inner_radio_color, Color.parseColor("#27BCFF"));
		mFlagOuterRadioSize = typeArray.getDimensionPixelSize(R.styleable.EnergySaveView_energy_save_flag_outer_radio_size,
															  DensityUtils.sp2px(getContext(), 14));
		mFlagOuterRadioColor = typeArray.getColor(R.styleable.EnergySaveView_energy_save_flag_outer_radio_color, Color.parseColor("#1519a1ff"));
		typeArray.recycle();
	}

	private void initData() {
		mOuterPaint = new Paint();
		mOuterPaint.setStyle(Paint.Style.STROKE);
		mOuterPaint.setAntiAlias(true);
		mRect = new RectF();

		mInnerPaint = new Paint();
		mInnerPaint.setStyle(Paint.Style.FILL);
		mInnerPaint.setAntiAlias(true);
		mInnerPaint.setShadowLayer(10, 0, 0, 0x14000000);

		mInnerTextPain = new TextPaint();
		mInnerTextPain.setAntiAlias(true);
		mInnerTextPain.setTextSize(mInnerTextSize);
		mInnerTextPain.setColor(mInnerTextColor);
		mInnerTextPain.setTextAlign(Paint.Align.CENTER);
		mInnerTextPain.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		mInnerContextMetrics = mInnerTextPain.getFontMetricsInt();

		mInnerRadioPaint = new TextPaint();
		mInnerRadioPaint.setAntiAlias(true);
		mInnerRadioPaint.setTextSize(mInnerRadioSize);
		mInnerRadioPaint.setColor(mInnerRadioColor);
		mInnerRadioPaint.setTextAlign(Paint.Align.CENTER);
		mInnerRadioPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		mInnerRadioMetrics = mInnerRadioPaint.getFontMetricsInt();

		mFlagOuterPaint = new Paint();
		mFlagOuterPaint.setStyle(Paint.Style.FILL);
		mFlagOuterPaint.setAntiAlias(true);
		mFlagOuterPaint.setColor(mFlagOuterRadioColor);

		mFlagInnerPaint = new Paint();
		mFlagInnerPaint.setStyle(Paint.Style.FILL);
		mFlagInnerPaint.setAntiAlias(true);
		mFlagInnerPaint.setColor(mFlagInnerRadioColor);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mRadius * 2 + mFlagOuterRadioSize * 2, mRadius * 2 +mFlagOuterRadioSize*2);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.translate(getWidth() / 2, getHeight() / 2);
		float outerRadius = mRadius - mRingWidth / 2;

		mOuterPaint.setColor(mNormalColor);
		mOuterPaint.setStrokeWidth(mRingWidth);
		mOuterPaint.setStrokeCap(BUTT);
		canvas.drawCircle(0, 0, outerRadius, mOuterPaint);

		mRect.set(-outerRadius, -outerRadius, outerRadius, outerRadius);
		mOuterPaint.setColor(mSelectColor);
		mOuterPaint.setStrokeWidth(mRingWidth);
		mOuterPaint.setStrokeCap(ROUND);
		canvas.drawArc(mRect, 270, -mInnerRadioValue * 360, false, mOuterPaint);

		canvas.save();
		canvas.rotate(-mInnerRadioValue * 360);
		canvas.drawCircle(0, -outerRadius, mFlagOuterRadioSize, mFlagOuterPaint);
		canvas.drawCircle(0, -outerRadius, mFlagInnerRadioSize, mFlagInnerPaint);
		canvas.restore();

		mInnerPaint.setColor(mInnerColor);
		setLayerType(LAYER_TYPE_SOFTWARE, mInnerPaint);
		canvas.drawCircle(0, 0, mInnerRadius, mInnerPaint);

		float titleTextY = -(mInnerRadioMetrics.bottom - mInnerRadioMetrics.top) / 2.0f - mInnerRadioMetrics.bottom;
		String radioText = String.format(Locale.getDefault(), "%.0f%s", mInnerRadioValue * 100, "%");
		canvas.drawText(radioText, 0, titleTextY, mInnerRadioPaint);

		if (mInnerTextContent != null) {
			float innerContentTextY = (mInnerContextMetrics.bottom - mInnerContextMetrics.top) / 2.0f;
			canvas.drawText(mInnerTextContent, 0, innerContentTextY, mInnerTextPain);
		}
	}
}
