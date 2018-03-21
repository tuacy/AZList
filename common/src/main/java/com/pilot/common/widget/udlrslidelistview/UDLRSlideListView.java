package com.pilot.common.widget.udlrslidelistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.pilot.common.R;

/**
 * 既可以上下滑动，又可以左右滑动的ListView（UDLRSlideListView）
 */
public class UDLRSlideListView extends ListView implements AbsListView.OnScrollListener, UDLRSlideAdapter.AdapterDataChangeListener {

	private static final int SNAP_VELOCITY = 500;//速度


	/**
	 * 是否固定title
	 */
	private boolean          mPinTitle;
	/**
	 * 从那一列（column）开始可以左右滑动
	 */
	private int              mSlideStart;
	/**
	 * 固定在顶部的View
	 */
	private View             mLayoutTitleSection;
	/**
	 * list view的width mode
	 */
	private int              mWidthMode;
	private OnScrollListener mScrollListener;
	private Scroller         mScroller;
	private VelocityTracker  mVelocityTracker;
	private int              mTouchSlop;
	private float            mLastMotionDownX;
	private float            mLastMotionDownY;
	private float            mLastMotionX;
	private boolean          mInSlideMode;
	private UDLRSlideAdapter mAdapter;

	public UDLRSlideListView(Context context) {
		this(context, null);
	}

	public UDLRSlideListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UDLRSlideListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttrs(context, attrs);
		initData();
	}

	/**
	 * 自定义属性的获取
	 */
	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.UDLRSlideListView);
		mPinTitle = attributes.getBoolean(R.styleable.UDLRSlideListView_pin_title, false);
		mSlideStart = attributes.getInt(R.styleable.UDLRSlideListView_slide_start, 0);
		attributes.recycle();
	}

	private void initData() {
		mWidthMode = MeasureSpec.EXACTLY;
		super.setOnScrollListener(this);
		mScroller = new Scroller(getContext());
		mInSlideMode = false;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();//大于getScaledTouchSlop这个距离时才认为是触发事件
	}


	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		if (!(adapter instanceof UDLRSlideAdapter)) {
			throw new IllegalArgumentException("adapter should abstract SlideBaseAdapter");
		}
		mAdapter = (UDLRSlideAdapter) adapter;
		mAdapter.setSlideColumnStart(mSlideStart);
		mAdapter.setOnAdapterDataChangeListener(this);
	}

	@Override
	public void setOnScrollListener(OnScrollListener listener) {
		mScrollListener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
	}

	/**
	 * dispatchTouchEvent函数中记录ACTION_DOWN的位置,因为当子View有处理事件的时候ACTION_DOWN传递不到onTouchEvent，
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				// 首先停止滚动
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}
				mInSlideMode = false;
				mLastMotionX = x;
				mLastMotionDownX = x;
				mLastMotionDownY = y;
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
			case MotionEvent.ACTION_MOVE:
				final int xDiff = (int) Math.abs(x - mLastMotionDownX);
				final int yDiff = (int) Math.abs(y - mLastMotionDownY);
				if (!mInSlideMode) {
					if (xDiff > mTouchSlop && xDiff > yDiff) {
						return true;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean handler = false;
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();//跟踪触摸事件滑动的帮助类
		}
		mVelocityTracker.addMovement(ev);
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
			case MotionEvent.ACTION_MOVE:
				final int xDiff = (int) Math.abs(x - mLastMotionDownX);
				final int yDiff = (int) Math.abs(y - mLastMotionDownY);
				if (!mInSlideMode) {
					if (xDiff > mTouchSlop && xDiff > yDiff) {
						getParent().requestDisallowInterceptTouchEvent(true);
						mInSlideMode = true;
					}
				}

				if (mInSlideMode) {
					final int deltaX = (int) (mLastMotionX - x);//滑动的距离
					prepareSlideMove(deltaX);
					mLastMotionX = x;
					handler = true;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mInSlideMode) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					velocityTracker.computeCurrentVelocity(1000);//1000毫秒移动了多少像素
					int velocityX = (int) velocityTracker.getXVelocity();//当前的速度
					if (canSlide()) {
						if (Math.abs(velocityX) < SNAP_VELOCITY) {
							//TODO:
						} else {
							prepareFling(-velocityX);
						}
					}
					if (mVelocityTracker != null) {
						mVelocityTracker.recycle();
						mVelocityTracker = null;
					}
					ev.setAction(MotionEvent.ACTION_CANCEL);
					super.onTouchEvent(ev);
					return true;
				}
				mInSlideMode = false;
				break;
			case MotionEvent.ACTION_CANCEL:
				mInSlideMode = false;
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				break;
		}
		return handler || super.onTouchEvent(ev);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		if (getAdapter() == null || !(getAdapter() instanceof UDLRSlideAdapter) || mLayoutTitleSection == null || !mPinTitle) {
			return;
		}
		int saveCount = canvas.save();
		canvas.clipRect(0, 0, getWidth(), mLayoutTitleSection.getMeasuredHeight());
		mLayoutTitleSection.draw(canvas);
		canvas.restoreToCount(saveCount);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
		if (getAdapter() != null && !(getAdapter() instanceof UDLRSlideAdapter)) {
			return;
		}
		int headerViewCount = getHeaderViewsCount();
		if (getAdapter() == null || !mPinTitle || firstVisibleItem < headerViewCount) {
			/**
			 * 第一个section都还没出来
			 */
			mLayoutTitleSection = null;
			for (int i = 0; i < visibleItemCount; i++) {
				View itemView = getChildAt(i);
				if (itemView != null) {
					itemView.setVisibility(VISIBLE);
				}
			}
			return;
		}
		if (getAdapter().getCount() <= 0) {
			return;
		}

		if (mLayoutTitleSection == null) {
			mLayoutTitleSection = getTitleSectionLayout(0);
			ensurePinViewLayout(mLayoutTitleSection);
		}

		if (mLayoutTitleSection == null) {
			return;
		}
		invalidate();
	}


	@Override
	public void onAdapterDataChange() {
		mLayoutTitleSection = null;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			prepareSlideSet(mScroller.getCurrX());
			postInvalidate();
		}
	}

	private boolean canSlide() {
		for (int i = 0; i < getChildCount(); i++) {
			UDLRSlideRowLayout rowLayout = (UDLRSlideRowLayout) getChildAt(i).findViewById(R.id.item_udls_slide_row);
			if (rowLayout != null && rowLayout.canSlide()) {
				return true;
			}
		}
		return false;
	}

	private void prepareSlideMove(int deltaX) {
		boolean notifySlideChange = false;
		for (int i = 0; i < getChildCount(); i++) {
			UDLRSlideRowLayout rowLayout = (UDLRSlideRowLayout) getChildAt(i).findViewById(R.id.item_udls_slide_row);
			if (rowLayout != null && rowLayout.canSlide()) {
				rowLayout.slideMove(deltaX);
				if (!notifySlideChange && mAdapter != null) {
					mAdapter.setSlideLength(rowLayout.getSlideCurrentLength());
					if (mLayoutTitleSection != null) {
						UDLRSlideRowLayout titleRowLayout = (UDLRSlideRowLayout) mLayoutTitleSection.findViewById(R.id.item_udls_slide_row);
						titleRowLayout.slideSet(rowLayout.getSlideCurrentLength());
						invalidate();
					}
					notifySlideChange = true;
				}
			}
		}
	}

	private void prepareSlideSet(int setX) {
		boolean notifySlideChange = false;
		for (int i = 0; i < getChildCount(); i++) {
			UDLRSlideRowLayout rowLayout = (UDLRSlideRowLayout) getChildAt(i).findViewById(R.id.item_udls_slide_row);
			if (rowLayout != null) {
				rowLayout.slideSet(setX);
				if (!notifySlideChange && mAdapter != null) {
					mAdapter.setSlideLength(rowLayout.getSlideCurrentLength());
					if (mLayoutTitleSection != null) {
						UDLRSlideRowLayout titleRowLayout = (UDLRSlideRowLayout) mLayoutTitleSection.findViewById(R.id.item_udls_slide_row);
						titleRowLayout.slideSet(rowLayout.getSlideCurrentLength());
						invalidate();
					}
					notifySlideChange = true;
				}
			}
		}
	}

	private int getSlideCurrentLength() {
		for (int i = 0; i < getChildCount(); i++) {
			UDLRSlideRowLayout rowLayout = (UDLRSlideRowLayout) getChildAt(i).findViewById(R.id.item_udls_slide_row);
			if (rowLayout != null) {
				return rowLayout.getSlideCurrentLength();
			}
		}
		return 0;
	}

	private int getSlideMaxLength() {
		for (int i = 0; i < getChildCount(); i++) {
			UDLRSlideRowLayout rowLayout = (UDLRSlideRowLayout) getChildAt(i).findViewById(R.id.item_udls_slide_row);
			if (rowLayout != null) {
				return rowLayout.getSlideMaxLength();
			}
		}
		return 0;
	}

	private void prepareFling(int velocityX) {
		mScroller.fling(getSlideCurrentLength(), 0, velocityX, 0, 0, getSlideMaxLength(), 0, 0);
		invalidate();
	}

	/**
	 * 获取固定在顶部的View
	 *
	 * @return View
	 */
	private View getTitleSectionLayout(int adapterPosition) {
		if (getAdapter() == null) {
			return null;
		}
		/**
		 * getView的第二个参数一定要传空，因为我们不能用复用的View
		 */
		return getAdapter().getView(adapterPosition, null, this);
	}

	private void ensurePinViewLayout(View pinView) {
		if (pinView.isLayoutRequested()) {
			/**
			 * 用的是list view的宽度测量，和list view的宽度一样
			 */
			int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), mWidthMode);

			int heightSpec;
			ViewGroup.LayoutParams layoutParams = pinView.getLayoutParams();
			if (layoutParams != null && layoutParams.height > 0) {
				heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
			} else {
				heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
			}
			pinView.measure(widthSpec, heightSpec);
			pinView.layout(0, 0, pinView.getMeasuredWidth(), pinView.getMeasuredHeight());
		}
	}
}
