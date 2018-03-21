package com.pilot.common.widget.udlrslidelistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.pilot.common.R;

public class UDLRSlideRowLayout extends LinearLayout {

	private LinearLayout mFixLayout;
	private LinearLayout mSlideLayout;

	public UDLRSlideRowLayout(Context context) {
		this(context, null);
	}

	public UDLRSlideRowLayout(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UDLRSlideRowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.layout_udlr_slide_row, this, true);
		mFixLayout = (LinearLayout) findViewById(R.id.udlr_row_fix_layout);
		mSlideLayout = (LinearLayout) findViewById(R.id.udlr_row_slide_layout);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	public LinearLayout getFixLayout() {
		return mFixLayout;
	}

	public LinearLayout getSlideLayout() {
		return mSlideLayout;
	}

	/**
	 * 获取所有slide layout 的总宽度，所有的view都是横向排列的，我们的知道所有view的总的长度，才好去判断滑动的距离
	 *
	 * @return 宽度
	 */
	private int getSlideLayoutRealWidth() {
		int realWidth = 0;
		for (int index = 0; index < mSlideLayout.getChildCount(); index++) {
			realWidth += mSlideLayout.getChildAt(index).getWidth();
		}
		return realWidth;
	}


	public int getSlideMaxLength() {
		return getSlideLayoutRealWidth() - mSlideLayout.getWidth();
	}

	public int getSlideCurrentLength() {
		return mSlideLayout.getScrollX();
	}

	public boolean canSlide() {
		return getSlideCurrentLength() <= getSlideMaxLength() && getSlideCurrentLength() >= 0;
	}

	public void slideMove(int deltaX) {
		if (canSlide()) {
			mSlideLayout.scrollBy(deltaX, 0);
			//避免有些情况滑到范围之外去
			if (mSlideLayout.getScrollX() > getSlideMaxLength()) {
				mSlideLayout.scrollBy(getSlideMaxLength() - mSlideLayout.getScrollX(), 0);
			}
			if (mSlideLayout.getScrollX() < 0) {
				mSlideLayout.scrollBy(-mSlideLayout.getScrollX(), 0);
			}
		}
	}

	public void slideSet(int setX) {
		mSlideLayout.scrollTo(setX, 0);
	}

}

