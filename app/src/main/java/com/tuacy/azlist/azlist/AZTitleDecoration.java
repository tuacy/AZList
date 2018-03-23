package com.tuacy.azlist.azlist;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.pilot.common.utils.TextDrawUtils;

public class AZTitleDecoration extends RecyclerView.ItemDecoration {

	private int       mTitleHeight;
	private Paint     mTitleBackgroundPaint;
	private TextPaint mTitleTextPaint;
	private int       mTitleTextPadding;

	public AZTitleDecoration(Context context, int itemHeightDp, int textSizeSp, int textColor, int backgroundColor, int textPadding) {
		mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeightDp,
													   context.getResources().getDisplayMetrics());
		mTitleTextPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textPadding,
															context.getResources().getDisplayMetrics());
		mTitleTextPaint = new TextPaint();
		mTitleTextPaint.setTextSize(
			TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeSp, context.getResources().getDisplayMetrics()));
		mTitleTextPaint.setColor(textColor);
		mTitleBackgroundPaint = new Paint();
		mTitleBackgroundPaint.setColor(backgroundColor);
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
		super.onDraw(c, parent, state);
		if (parent.getAdapter() == null || !(parent.getAdapter() instanceof AZBaseAdapter)) {
			return;
		}
		AZBaseAdapter adapter = (AZBaseAdapter) parent.getAdapter();
		if (adapter.getDataList() == null || adapter.getDataList().isEmpty()) {
			return;
		}
		for (int i = 0; i < parent.getChildCount(); i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			int position = params.getViewLayoutPosition();
			if (drawTitleWithChildView(child, parent)) {
				drawTitleItem(c, parent, child, adapter.getLetters(position));
			}
		}
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
		super.onDrawOver(c, parent, state);
		if (parent.getAdapter() == null || !(parent.getAdapter() instanceof AZBaseAdapter)) {
			return;
		}
		AZBaseAdapter adapter = (AZBaseAdapter) parent.getAdapter();
		if (adapter.getDataList() == null || adapter.getDataList().isEmpty()) {
			return;
		}
		View firstView = parent.getChildAt(0);
		int firstAdapterPosition = parent.getChildAdapterPosition(firstView);
		c.save();
		if ((firstAdapterPosition + 1) < adapter.getItemCount()) {
			//当前第一个可见的Item的字母索引，不等于其后一个item的字母索引，说明悬浮的View要切换了
			if (null != adapter.getLetters(firstAdapterPosition) && !adapter.getLetters(firstAdapterPosition).equals(adapter.getLetters(firstAdapterPosition + 1))) {
				//当第一个可见的item在屏幕中剩下的高度小于title的高度时，开始悬浮Title的动画
				if (firstView.getHeight() + firstView.getTop() < mTitleHeight) {
					/**
					 * 下边的索引把上边的索引顶上去的效果
					 */
					c.translate(0, firstView.getHeight() + firstView.getTop() - mTitleHeight);
				}
			}
		}
		c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(),
				   parent.getPaddingTop() + mTitleHeight, mTitleBackgroundPaint);
		c.drawText(adapter.getLetters(firstAdapterPosition), parent.getPaddingLeft() + firstView.getPaddingLeft() + mTitleTextPadding,
				   TextDrawUtils.getTextBaseLineByCenter(parent.getPaddingTop() + mTitleHeight / 2, mTitleTextPaint), mTitleTextPaint);
		c.restore();
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		if (drawTitleWithChildView(view, parent)) {
			outRect.set(0, mTitleHeight, 0, 0);
		} else {
			super.getItemOffsets(outRect, view, parent, state);
		}
	}

	private void drawTitleItem(Canvas c, RecyclerView parent, View child, String letters) {
		final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
		c.drawRect(parent.getPaddingLeft(), child.getTop() - params.bottomMargin - mTitleHeight,
				   parent.getWidth() - parent.getPaddingRight(), child.getTop() - params.bottomMargin, mTitleBackgroundPaint);

		float textCenterY = child.getTop() - params.bottomMargin - mTitleHeight / 2;
		c.drawText(letters, parent.getPaddingLeft() + child.getPaddingLeft() + mTitleTextPadding,
				   TextDrawUtils.getTextBaseLineByCenter(textCenterY, mTitleTextPaint), mTitleTextPaint);
	}

	private boolean drawTitleWithChildView(View view, RecyclerView parent) {
		if (parent.getAdapter() == null || !(parent.getAdapter() instanceof AZBaseAdapter)) {
			return false;
		}
		AZBaseAdapter adapter = (AZBaseAdapter) parent.getAdapter();
		if (adapter.getDataList() == null || adapter.getDataList().isEmpty()) {
			return false;
		}
		int position = parent.getChildAdapterPosition(view);
		//第一个一定要绘制 + 每个都和前面一个去做判断，不等于前一个则要绘制
		return position == 0 ||
			   null != adapter.getDataList().get(position) && !adapter.getLetters(position).equals(adapter.getLetters(position - 1));

	}
}
