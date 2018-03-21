package com.pilot.common.widget.piegraph;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.pilot.common.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * 饼状图
 * 1. 可以点击
 * 2. 可以旋转
 * 3. 文字尽量不重叠
 * 4. value是0的时候不显示
 */

public class PieGraph extends View {

	/**
	 * 默认在计算高度的时候上面和下面都预留2dp
	 */
	private static final int DEFAULT_PADDING             = 8;
	/**
	 * 文字的间隔
	 */
	private static final int DEFAULT_TEXT_BOTTOM_PADDING = 2;
	/**
	 * 颜色标记圆的半径
	 */
	private static final int DEFAULT_FLAG_RADIUS         = 4;
	/**
	 * 默认文字的大小10sp
	 */
	private static final int DEFAULT_TEXT_SIZE_SP        = 10;
	/**
	 * 默认饼状图的半径 80dp
	 */
	private static final int DEFAULT_RADIUS_DP           = 80;
	/**
	 * 默认文字连接线在半径方向多出来的距离 14dp
	 */
	private static final int DEFAULT_MARKER_LINE1_DP     = 5;

	/**
	 * 饼状图的半径
	 */
	private float               mPieRadius;
	/**
	 * 文字大小
	 */
	private int                 mTextSize;
	/**
	 * 文字颜色
	 */
	private int                 mTextColor;
	/**
	 * 文字的高度
	 */
	private float               mTextHeight;
	/**
	 * 文字的基准线
	 */
	private float               mTextBottom;
	/**
	 * 画百分比还是文字 true的时候之后百分比，false的时候画文字加百分比
	 */
	private boolean             mIsDrawRatio;
	/**
	 * 画文字的时候先在原有的圆上面延伸出来的长度
	 */
	private float               mMarkerLine1;
	/**
	 * 绘制字体的画笔
	 */
	private TextPaint           mTextPaint;
	/**
	 * 画文字连接线的画笔
	 */
	private Paint               mLinePaint;
	/**
	 * 饼状图信息列表
	 */
	private List<PieDataHolder> pieDataHolders;
	/**
	 * 饼状图的画笔
	 */
	private Paint               mPiePaint;
	/**
	 * 上一个画的文字的区域（用来判断文字是否有重叠的情况，为了提升体验重叠的时候我们是不画的）
	 */
	private Rect                mPreTextRect;
	/**
	 * 当前要画的文字的区域（用来判断文字是否有重叠的情况，为了提升体验重叠的时候我们是不画的）
	 */
	private Rect                mCurrentTextRect;
	/**
	 * 第一个文字的区域，在判断最后一个文字的区域是否有重叠的时候，即判断了前一个也判断了第一个
	 */
	private Rect                mFirstTextRect;
	/**
	 * 监听器，监听哪一款是否有选中
	 */
	private OnPieGraphListener  mListener;
	/**
	 * 滑动产生的距离
	 */
	private int                 mTouchSlop;
	/**
	 * 旋转的角度,随手指旋转
	 */
	private float               mRotate;
	/**
	 * 是否可以旋转
	 */
	private boolean             mCanRotate;
	/**
	 * 确定比例保存几位小数
	 */
	private DecimalFormat       mDecimalFormat;
	/**
	 * 外环的宽度
	 */
	private float               mOuterRingWidth;
	/**
	 * 内环正常宽度
	 */
	private float               mInterRingWidthNormal;
	/**
	 * 内环选中宽度
	 */
	private float               mInterRingWidthFocus;
	/**
	 * 外环和内环的间隔
	 */
	private float               mRingSpace;
	/**
	 * 内环画笔
	 */
	private Paint               mRingPaint;
	/**
	 * 颜色标记画笔
	 */
	private Paint               mColorFlagPaint;
	/**
	 * 圆环的路径
	 */
	private Path                mRingPath;
	private RectF               mRectFTemp;

	/**
	 * 选中监听
	 */
	public interface OnPieGraphListener {

		void onPieSelect(PieDataHolder pieDataHolder);

		void onNoPieSelect();
	}

	public PieGraph(Context context) {
		this(context, null);
	}

	public PieGraph(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PieGraph(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData();
		initAttrs(attrs, defStyleAttr);
		initTextMetrics();
	}

	private void initData() {
		mTextPaint = new TextPaint();
		mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextAlign(Paint.Align.LEFT);
		pieDataHolders = new ArrayList<>();

		mPiePaint = new Paint();
		mPiePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPiePaint.setStyle(Paint.Style.STROKE);
		mPiePaint.setAntiAlias(true);

		mRingPaint = new Paint();
		mRingPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mRingPaint.setStyle(Paint.Style.FILL);
		mRingPaint.setAntiAlias(true);

		mColorFlagPaint = new Paint();
		mColorFlagPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mColorFlagPaint.setStyle(Paint.Style.FILL);
		mColorFlagPaint.setAntiAlias(true);

		mPreTextRect = new Rect();
		mCurrentTextRect = new Rect();
		mFirstTextRect = new Rect();

		mLinePaint = new Paint();
		mLinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mLinePaint.setStyle(Paint.Style.STROKE);
		mLinePaint.setAntiAlias(true);

		ViewConfiguration configuration = ViewConfiguration.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();

		mRotate = 0;
		// 默认保留两位小数
		mDecimalFormat = new DecimalFormat("000.0000");

		mRingPath = new Path();

		mRectFTemp = new RectF();
	}

	/**
	 * 获取xml里面定义的属性
	 */
	private void initAttrs(AttributeSet attrs, int defStyleAttr) {
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PieGraph, defStyleAttr, 0);

		mPieRadius = a.getDimensionPixelSize(R.styleable.PieGraph_pie_circle_radius,
											 (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS_DP,
																			 getResources().getDisplayMetrics()));

		mTextSize = a.getDimensionPixelSize(R.styleable.PieGraph_pie_text_size,
											(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE_SP,
																			getResources().getDisplayMetrics()));
		mOuterRingWidth = a.getDimensionPixelSize(R.styleable.PieGraph_pie_outer_ring_width,
												  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
																				  getResources().getDisplayMetrics()));
		mInterRingWidthNormal = a.getDimensionPixelSize(R.styleable.PieGraph_pie_inter_ring_normal_width,
														(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
																						getResources().getDisplayMetrics()));
		mInterRingWidthFocus = a.getDimensionPixelSize(R.styleable.PieGraph_pie_inter_ring_focus_width,
													   (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
																					   getResources().getDisplayMetrics()));
		mRingSpace = a.getDimensionPixelSize(R.styleable.PieGraph_pie_ring_space,
											 (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
																			 getResources().getDisplayMetrics()));
		mTextColor = a.getColor(R.styleable.PieGraph_pie_text_color, 0xff696969);
		mIsDrawRatio = a.getBoolean(R.styleable.PieGraph_pie_show_radio, false);
		mMarkerLine1 = a.getDimensionPixelSize(R.styleable.PieGraph_pie_marker_line1,
											   (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MARKER_LINE1_DP,
																			   getResources().getDisplayMetrics()));
		mCanRotate = a.getBoolean(R.styleable.PieGraph_pie_can_rotate, true);
		a.recycle();
	}

	/**
	 * 得到位置的高度，基准线啥啥的
	 */
	private void initTextMetrics() {
		mTextPaint.setTextSize(mTextSize);
		Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		mTextHeight = fontMetrics.descent - fontMetrics.ascent;
		mTextBottom = fontMetrics.bottom;
	}

	/**
	 * 测量控件大小,这里宽度我们不测了，只是去测量高度，宽度直接用父控件传过来的大小
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PADDING, getResources().getDisplayMetrics());
		int textPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_BOTTOM_PADDING,
														  getResources().getDisplayMetrics());
		// 半径 + 选中的时候多出来的部分 + 半径延长线 + 文字的高度的一半（文字的高度一半是在外面的）+ 预留的padding
		int height = (int) ((mPieRadius + mMarkerLine1 + mTextHeight + textPadding + padding) * 2);
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);

	}

	/**
	 * 具体的绘制
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawPie(canvas);
		drawText(canvas);
	}

	/**
	 * 画饼状图
	 */
	private void drawPie(Canvas canvas) {
		if (pieDataHolders == null || pieDataHolders.size() <= 0) {
			return;
		}
		for (PieDataHolder pieDataHolder : pieDataHolders) {
			mPiePaint.setColor(pieDataHolder.mColor);

			if (pieDataHolder.mSweepAngel == 0) {
				// 0度的不画
				continue;
			}
			//画外面的环
			mRingPaint.setColor(pieDataHolder.mColor);
			float outInnerRadius = mPieRadius - mOuterRingWidth;
			float outOuterRadius = mPieRadius;
			drawRingSection(pieDataHolder.mStartAngel + mRotate, pieDataHolder.mSweepAngel, outInnerRadius, outOuterRadius, canvas,
							mRingPaint);
			if (pieDataHolder.mIsSelect) {
				//画内部环
				mRingPaint.setColor(pieDataHolder.mColor);
				float innerInnerRadius = mPieRadius - mOuterRingWidth - mRingSpace - mInterRingWidthNormal -
										 (mInterRingWidthFocus - mInterRingWidthNormal) / 2;
				float innerOuterRadius = mPieRadius - mOuterRingWidth - mRingSpace + (mInterRingWidthFocus - mInterRingWidthNormal) / 2;
				drawRingSection(pieDataHolder.mStartAngel + mRotate, pieDataHolder.mSweepAngel, innerInnerRadius, innerOuterRadius, canvas,
								mRingPaint);
			} else {
				//画内部环
				mRingPaint.setColor(pieDataHolder.mColor);
				float innerInnerRadius = mPieRadius - mOuterRingWidth - mRingSpace - mInterRingWidthNormal;
				float innerOuterRadius = mPieRadius - mOuterRingWidth - mRingSpace;
				drawRingSection(pieDataHolder.mStartAngel + mRotate, pieDataHolder.mSweepAngel, innerInnerRadius, innerOuterRadius, canvas,
								mRingPaint);
			}
		}
	}

	private void drawRingSection(float startAngle, float sweepAngle, float innerRadius, float outerRadius, Canvas canvas, Paint paint) {
		final float startInnerX = getWidth() / 2 + (float) (innerRadius * Math.cos(Math.toRadians(startAngle)));
		final float startInnerY = getHeight() / 2 + (float) (innerRadius * Math.sin(Math.toRadians(startAngle)));
		final float startOuterX = getWidth() / 2 + (float) (outerRadius * Math.cos(Math.toRadians(startAngle)));
		final float startOuterY = getHeight() / 2 + (float) (outerRadius * Math.sin(Math.toRadians(startAngle)));
		final float endInnerX = getWidth() / 2 + (float) (innerRadius * Math.cos(Math.toRadians(startAngle + sweepAngle)));
		final float endInnerY = getHeight() / 2 + (float) (innerRadius * Math.sin(Math.toRadians(startAngle + sweepAngle)));
		mRingPath.reset();
		mRingPath.moveTo(startInnerX, startInnerY);
		mRingPath.lineTo(startOuterX, startOuterY);
		mRectFTemp.set(getWidth() / 2 - outerRadius, getHeight() / 2 - outerRadius, getWidth() / 2 + outerRadius,
					   getHeight() / 2 + outerRadius);
		mRingPath.arcTo(mRectFTemp, startAngle, sweepAngle);
		mRingPath.lineTo(endInnerX, endInnerY);
		mRectFTemp.set(getWidth() / 2 - innerRadius, getHeight() / 2 - innerRadius, getWidth() / 2 + innerRadius,
					   getHeight() / 2 + innerRadius);
		mRingPath.arcTo(mRectFTemp, startAngle + sweepAngle, -sweepAngle);
		mRingPath.close();
		canvas.drawPath(mRingPath, paint);
	}

	/**
	 * 画文字
	 */
	private void drawText(Canvas canvas) {
		int textPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_BOTTOM_PADDING,
														  getResources().getDisplayMetrics());
		int flagRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FLAG_RADIUS,
														 getResources().getDisplayMetrics());
		mTextPaint.setColor(mTextColor);
		mLinePaint.setColor(mTextColor);
		mCurrentTextRect.setEmpty();
		mPreTextRect.setEmpty();
		mFirstTextRect.setEmpty();
		for (int index = 0; index < pieDataHolders.size(); index++) {
			PieDataHolder pieDataHolder = pieDataHolders.get(index);
			if (pieDataHolder.mSweepAngel == 0) {
				// 没有比例的不画
				continue;
			}
			String textMarker = String.format(Locale.getDefault(), "%s", pieDataHolder.mMarker);
			if (mIsDrawRatio) {
				String ratio = String.format(Locale.getDefault(), "%.02f%s", pieDataHolder.mRatio * 100, "%");
				textMarker = String.format(Locale.getDefault(), "%s(%s)", textMarker, ratio);
			}
			if (textMarker == null) {
				continue;
			}
			float textWidth = mTextPaint.measureText(textMarker);
			float line2Width = textWidth + flagRadius * 2 + textPadding;
			// 找到圆弧一半的位置，要往这个方向拉出去
			float middle = (pieDataHolder.mStartAngel + pieDataHolder.mSweepAngel / 2 + mRotate) % 360;
			if (middle < 0) {
				middle += 360;
			}
			Path linePath = new Path();
			linePath.close();
			// 找到圆边缘上的点(分选中和没选中两种情况)
			final float startX = (float) (getWidth() / 2 + (mPieRadius - mOuterRingWidth / 2) * Math.cos(Math.toRadians(middle)));
			final float startY = (float) (getHeight() / 2 + (mPieRadius - mOuterRingWidth / 2) * Math.sin(Math.toRadians(middle)));
			canvas.drawCircle(startX, startY, 2, mLinePaint);
			linePath.moveTo(startX, startY);
			final float x = (float) (getWidth() / 2 + (mMarkerLine1 + mOuterRingWidth / 2 + mPieRadius) * Math.cos(Math.toRadians(middle)));
			final float y = (float) (getHeight() / 2 +
									 (mMarkerLine1 + mOuterRingWidth / 2 + mPieRadius) * Math.sin(Math.toRadians(middle)));
			linePath.lineTo(x, y);
			float landLineX;

			if (middle > 0 && middle <= 90) {
				//二象限
				landLineX = x + line2Width;
				// 文字的区域
				mCurrentTextRect.top = (int) (y + textPadding);
				mCurrentTextRect.left = (int) x + textPadding;
				mCurrentTextRect.bottom = (int) (mCurrentTextRect.top + mTextHeight);
				mCurrentTextRect.right = (int) (mCurrentTextRect.left + textWidth);
			} else if (middle > 90 && middle <= 180) {
				//三象限
				landLineX = x - line2Width;
				// 文字的区域
				mCurrentTextRect.top = (int) (y + textPadding);
				mCurrentTextRect.left = (int) (x - textWidth - textPadding);
				mCurrentTextRect.bottom = (int) (mCurrentTextRect.top + mTextHeight);
				mCurrentTextRect.right = (int) (mCurrentTextRect.left + textWidth);
			} else if (middle > 180 && middle <= 270) {
				//四象限
				landLineX = x - line2Width;
				// 文字的区域
				mCurrentTextRect.top = (int) (y - mTextHeight - textPadding);
				mCurrentTextRect.left = (int) (x - textWidth - textPadding);
				mCurrentTextRect.bottom = (int) (mCurrentTextRect.top + mTextHeight);
				mCurrentTextRect.right = (int) (mCurrentTextRect.left + textWidth);
			} else {
				//一象限
				landLineX = x + line2Width;
				// 文字的区域
				mCurrentTextRect.top = (int) (y - mTextHeight - textPadding);
				mCurrentTextRect.left = (int) x + textPadding;
				mCurrentTextRect.bottom = (int) (mCurrentTextRect.top + mTextHeight);
				mCurrentTextRect.right = (int) (mCurrentTextRect.left + textWidth);
			}
			linePath.lineTo(landLineX, y); // 画文字线先确定了
			if (index == 0) {
				// 记录第一个
				mFirstTextRect.set(mCurrentTextRect);
			}
			// 画线和文字 这里会去判断重叠的问题（这里有一点要注意就是当去画最后一个的时候，判断了两次不仅和前面的判断了还和第一个判断了）
			if (index == pieDataHolders.size() - 1 && pieDataHolders.size() > 1) {
				if (mPreTextRect.isEmpty() ||
					(!isCollisionWithRect(mPreTextRect, mCurrentTextRect) && !isCollisionWithRect(mFirstTextRect, mCurrentTextRect))) {
					mPreTextRect.set(mCurrentTextRect);
					canvas.drawPath(linePath, mLinePaint);
					canvas.drawText(textMarker, mCurrentTextRect.left, mCurrentTextRect.top + mTextHeight - mTextBottom, mTextPaint);
					mLinePaint.setColor(pieDataHolder.mColor);
					drawColorFlag(canvas, middle, mCurrentTextRect, pieDataHolder);
				}
			} else {
				if (mPreTextRect.isEmpty() || !isCollisionWithRect(mPreTextRect, mCurrentTextRect)) {
					mPreTextRect.set(mCurrentTextRect);
					canvas.drawPath(linePath, mLinePaint);
					canvas.drawText(textMarker, mCurrentTextRect.left, mCurrentTextRect.top + mTextHeight - mTextBottom, mTextPaint);
					drawColorFlag(canvas, middle, mCurrentTextRect, pieDataHolder);
				}
			}
		}
	}

	private void drawColorFlag(Canvas canvas, float middle, Rect textRect, PieDataHolder pieDataHolder) {
		int textPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_BOTTOM_PADDING,
														  getResources().getDisplayMetrics());
		int flagRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FLAG_RADIUS,
														 getResources().getDisplayMetrics());
		mColorFlagPaint.setColor(pieDataHolder.mColor);
		float x;
		float y = (textRect.top - textRect.bottom) / 2 + textRect.bottom;
		if (middle > 0 && middle <= 90) {
			//二象限
			x = textRect.right + textPadding + flagRadius;
		} else if (middle > 90 && middle <= 180) {
			//三象限
			x = textRect.left - textPadding - flagRadius;
		} else if (middle > 180 && middle <= 270) {
			//四象限
			x = textRect.left - textPadding - flagRadius;
		} else {
			//一象限
			x = textRect.right + textPadding + flagRadius;
		}
		canvas.drawCircle(x, y, flagRadius, mColorFlagPaint);
	}

	/**
	 * 判断两个矩形是否有重叠的部分
	 */
	private boolean isCollisionWithRect(Rect rect1, Rect rect2) {
		return isCollisionWithRect(rect1.left, rect1.top, rect1.width(), rect1.height(), rect2.left, rect2.top, rect2.width(),
								   rect2.height());
	}

	/**
	 * 判断两个矩形是否有重叠的部分
	 */
	private boolean isCollisionWithRect(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
		if (x1 >= x2 && x1 >= x2 + w2) {
			return false;
		} else if (x1 <= x2 && x1 + w1 <= x2) {
			return false;
		} else if (y1 >= y2 && y1 >= y2 + h2) {
			return false;
		} else if (y1 <= y2 && y1 + h1 <= y2) {
			return false;
		}
		return true;
	}

	/**
	 * 设置饼状图数据(给外部调用的)
	 */
	public void setPieData(List<PieDataHolder> pieDataList) {
		if (pieDataList == null || pieDataList.size() == 0) {
			return;
		}
		mRotate = 0;
		pieDataHolders.clear();
		pieDataHolders.addAll(pieDataList);
		// 计算每个饼状图的比例，开始角度，扫过的角度
		float sum = 0;
		for (PieDataHolder pieDataHolder : pieDataHolders) {
			sum += pieDataHolder.mValue;
		}
		float preAngel = 0; // 当前位置之前的总的值，算开始角度用的，总共360
		for (int index = 0; index < pieDataList.size(); index++) {
			PieDataHolder pieDataHolder = pieDataHolders.get(index);
			pieDataHolder.mIsSelect = false;
			pieDataHolder.mPosition = index;
			pieDataHolder.mRatio = Float.parseFloat(mDecimalFormat.format(pieDataHolder.mValue / sum));
			pieDataHolder.mStartAngel = preAngel;
			if (index == pieDataList.size() - 1) {
				// 如果是最后一个 目的是避免精度的问题
				pieDataHolder.mSweepAngel = 360 - pieDataHolder.mStartAngel;
			} else {
				pieDataHolder.mSweepAngel = pieDataHolder.mRatio * 360;
			}
			preAngel = pieDataHolder.mStartAngel + pieDataHolder.mSweepAngel;
		}
		// 这里要调整一下比例，因为精度的原因，有的时候可能加起来不是100%，解决办法呢就是最大的比例直接用100减掉其他的
		int maxRatioPosition = 0;
		float maxRatioValue = 0;
		for (PieDataHolder pieDataHolder : pieDataHolders) {
			if (maxRatioValue < pieDataHolder.mRatio) {
				maxRatioValue = pieDataHolder.mRatio;
				maxRatioPosition = pieDataHolder.mPosition;
			}
		}
		float sumWithOutMax = 0;
		PieDataHolder maxHolder = null;
		for (PieDataHolder pieDataHolder : pieDataHolders) {
			if (pieDataHolder.mPosition != maxRatioPosition) {
				sumWithOutMax += pieDataHolder.mRatio;
			} else {
				maxHolder = pieDataHolder;
			}
		}
		if (maxHolder != null) {
			maxHolder.mRatio = 1 - Float.parseFloat(mDecimalFormat.format(sumWithOutMax));
			;
		}
		invalidate();
	}

	/**
	 * 设置PieGraph的监听（外部调用）
	 */
	public void setOnPieGraphListener(OnPieGraphListener listener) {
		mListener = listener;
	}

	/**
	 * 设置选择
	 */
	public void setSelect(int position) {
		if (pieDataHolders == null || pieDataHolders.size() <= 0) {
			return;
		}
		for (PieDataHolder pieDataHolder : pieDataHolders) {
			if (pieDataHolder.mPosition != position) {
				pieDataHolder.mIsSelect = false;
			} else {
				pieDataHolder.mIsSelect = true;
			}
		}
		invalidate();
	}

	/**
	 * 放出一些事件来
	 */
	private float mPreX;
	private float mPreY;
	private boolean mDealMove = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (pieDataHolders == null || pieDataHolders.size() <= 0) {
			return false;
		}
		float eventX = event.getX();
		float eventY = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN && !inCircle(eventX, eventY)) {
			// down事件的时候不在园内，这个事件我们不要了
			clearHolderSelect(-1);
			invalidate();
			if (mListener != null) {
				mListener.onNoPieSelect();
			}
			return false;
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mPreX = event.getX();
				mPreY = event.getY();
				mDealMove = false;
				return true;
			case MotionEvent.ACTION_MOVE:
				// 想让饼状图旋转起来
				if (!inCircle(eventX, eventY)) {
					// 没有在园内 这个事件结束了，我们不要了
					mDealMove = false;
					return false;
				}
				float offsetX = eventX - mPreX;
				float offsetY = eventY - mPreY;
				if (mCanRotate && Math.sqrt(offsetX * offsetX + offsetY * offsetY) >= mTouchSlop) {
					if (!mDealMove) {
						mDealMove = true;
					}
					mRotate = (float) (mRotate + action2Angle(eventX, eventY) - action2Angle(mPreX, mPreY));
					mPreX = eventX;
					mPreY = eventY;
					invalidate();
				}
				return true;
			case MotionEvent.ACTION_UP:
				if (!mDealMove) {
					// 这里我们去判断是否是点击事件
					if (inCircle(eventX, eventY)) {
						int position = getHolderPositionByAngle((float) action2Angle(eventX, eventY));
						clearHolderSelect(position);
						PieDataHolder holder = getHolderByPosition(position);
						if (holder != null) {
							holder.mIsSelect = !holder.mIsSelect;
						}
					} else {
						// 不在圆内，清空掉以前的选择
						clearHolderSelect(-1);
					}
					if (mListener != null) {
						// 找出选中的那个
						PieDataHolder holder = findSelectHolder();
						if (holder == null) {
							mListener.onNoPieSelect();
						} else {
							mListener.onPieSelect(holder);
						}
					}
					invalidate();

				}
				break;
		}
		return true;
	}

	/**
	 * 点击的点映射到圆上的角度
	 */
	private double action2Angle(float x, float y) {
		double angle = 0;
		// 第一象限
		if (x >= getMeasuredWidth() / 2 && y >= getMeasuredHeight() / 2) {
			angle = (Math.atan((y - getMeasuredHeight() / 2) * 1.0f / (x - getMeasuredWidth() / 2)) * 180 / Math.PI);
		}
		// 第二象限
		if (x <= getMeasuredWidth() / 2 && y >= getMeasuredHeight() / 2) {
			angle = (Math.atan((getMeasuredWidth() / 2 - x) / (y - getMeasuredHeight() / 2)) * 180 / Math.PI + 90);
		}
		// 第三象限
		if (x <= getMeasuredWidth() / 2 && y <= getMeasuredHeight() / 2) {
			angle = (Math.atan((getMeasuredHeight() / 2 - y) / (getMeasuredWidth() / 2 - x)) * 180 / Math.PI + 180);
		}
		// 第四象限
		if (x >= getMeasuredWidth() / 2 && y <= getMeasuredHeight() / 2) {
			angle = (Math.atan((x - getMeasuredWidth() / 2) / (getMeasuredHeight() / 2 - y)) * 180 / Math.PI + 270);
		}
		return angle;
	}

	/**
	 * 通过角度去找我们holder
	 */
	private int getHolderPositionByAngle(float angle) {
		if (pieDataHolders == null || pieDataHolders.size() <= 0) {
			return -1;
		}
		for (PieDataHolder pieDataHolder : pieDataHolders) {
			// 这里我们拿到真正的开始角度
			float realStartAngel = (pieDataHolder.mStartAngel + mRotate) % 360;
			if (realStartAngel < 0) {
				realStartAngel += 360;
			}
			if (realStartAngel + pieDataHolder.mSweepAngel > 360) {
				if (angle >= realStartAngel) {
					return pieDataHolder.mPosition;
				} else {
					if (angle < realStartAngel + pieDataHolder.mSweepAngel - 360) {
						return pieDataHolder.mPosition;
					}
				}
			} else {
				if (angle >= realStartAngel && angle < realStartAngel + pieDataHolder.mSweepAngel) {
					return pieDataHolder.mPosition;
				}
			}
		}
		return -1;
	}

	/**
	 * 通过position去找到holder
	 */
	private PieDataHolder getHolderByPosition(int position) {
		if (pieDataHolders == null || pieDataHolders.size() <= 0) {
			return null;
		}
		for (PieDataHolder pieDataHolder : pieDataHolders) {
			if (pieDataHolder.mPosition == position) {
				return pieDataHolder;
			}
		}
		return null;
	}

	/**
	 * 找到选中的那个holder
	 */
	private PieDataHolder findSelectHolder() {
		if (pieDataHolders == null || pieDataHolders.size() <= 0) {
			return null;
		}
		for (PieDataHolder pieDataHolder : pieDataHolders) {
			if (pieDataHolder.mIsSelect) {
				return pieDataHolder;
			}
		}
		return null;
	}

	/**
	 * 清除掉之前的选择，position位置的状态保留
	 *
	 * @param position: 这个位置的状态不清除
	 */
	private void clearHolderSelect(int position) {
		for (PieDataHolder pieDataHolder : pieDataHolders) {
			if (position != pieDataHolder.mPosition) {
				pieDataHolder.mIsSelect = false;
			}
		}
	}

	/**
	 * 是否在饼图园范围内
	 */
	private boolean inCircle(float x, float y) {
		return Math.sqrt(Math.pow(x - getWidth() / 2, 2) + Math.pow(y - getHeight() / 2, 2)) < mPieRadius;
	}

	/**
	 * 饼状图里面每个饼的信息
	 */
	public static final class PieDataHolder {

		/**
		 * 具体的值
		 */
		private float mValue;

		/**
		 * 比例
		 */
		private float mRatio;

		/**
		 * 颜色
		 */
		private int mColor;

		/**
		 * 文字标记
		 */
		private String mMarker;

		/**
		 * 起始弧度
		 */
		private float mStartAngel;

		/**
		 * 扫过的弧度
		 */
		private float mSweepAngel;

		/**
		 * 是否选中
		 */
		private boolean mIsSelect;

		/**
		 * 位置下标
		 */
		private int mPosition;

		public PieDataHolder(float value, int color, String label) {
			mValue = value;
			mColor = color;
			mMarker = label;
		}

		public int getPosition() {
			return mPosition;
		}

		public float getValue() {
			return mValue;
		}

		public float getRatio() {
			return mRatio;
		}

		public int getColor() {
			return mColor;
		}

		public String getMarker() {
			return mMarker;
		}
	}
}
