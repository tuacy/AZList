package com.tuacy.azlist.azlist;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

public class AZTitleDecoration extends RecyclerView.ItemDecoration {

	private int       mTitleHeight;
	private TextPaint mTextPaint;
	private int       mTitleTextPadding;
	private Rect      mBounds;

	public AZTitleDecoration(Context context, int itemHeightDp, int textSizeSp, int textColor, int textPadding) {
		mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeightDp,
													   context.getResources().getDisplayMetrics());
		mTitleTextPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textPadding,
															context.getResources().getDisplayMetrics());
		mTextPaint = new TextPaint();
		mTextPaint.setTextSize(
			TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeSp, context.getResources().getDisplayMetrics()));
		mTextPaint.setColor(textColor);
		mBounds = new Rect();
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
		super.onDraw(c, parent, state);
		if (parent.getAdapter() == null || !(parent.getAdapter() instanceof AZBaseAdapter)) {
			return;
		}
		AZBaseAdapter adapter = (AZBaseAdapter) parent.getAdapter();
		List<AZEntity<?>> dataList = adapter.getDataList();
		if (dataList == null || dataList.isEmpty()) {
			return;
		}
		final int left = parent.getPaddingLeft();
		final int right = parent.getWidth() - parent.getPaddingRight();
		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			int position = params.getViewLayoutPosition();
			if (position > -1) {
				if (position == 0) {
					//第一个一定要绘制
					drawTitle(c, left, right, child, params, dataList.get(position).getLetters());
				} else {
					//每个都和前面一个去做判断，不等于前一个则要绘制
					if (null != dataList.get(position).getLetters() &&
						!dataList.get(position).getLetters().equals(dataList.get(position - 1).getLetters())) {
						drawTitle(c, left, right, child, params, dataList.get(position).getLetters());
					}
				}
			}
		}
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
		super.onDrawOver(c, parent, state);
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		if (itemWithTitle(view, parent)) {
			outRect.set(0, mTitleHeight, 0, 0);
		} else {
			super.getItemOffsets(outRect, view, parent, state);
		}
	}

	private void drawTitle(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, String letters) {
		mTextPaint.getTextBounds(letters, 0, letters.length(), mBounds);
		c.drawText(letters, left + child.getPaddingLeft() + mTitleTextPadding,
				   child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2), mTextPaint);
	}

	private boolean itemWithTitle(View view, RecyclerView parent) {
		if (parent.getAdapter() == null || !(parent.getAdapter() instanceof AZBaseAdapter)) {
			return false;
		}
		AZBaseAdapter adapter = (AZBaseAdapter) parent.getAdapter();
		List<AZEntity<?>> dataList = adapter.getDataList();
		if (dataList == null || dataList.isEmpty()) {
			return false;
		}
		int position = parent.getChildAdapterPosition(view);
		//第一个一定要绘制 + 每个都和前面一个去做判断，不等于前一个则要绘制
		return position == 0 || null != adapter.getDataList().get(position) &&
								!dataList.get(position).getLetters().equals(dataList.get(position - 1).getLetters());

	}
}
