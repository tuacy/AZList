package com.tuacy.azlist.azlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import com.pilot.common.utils.TextDrawUtils;

public class AZTitleDecoration extends RecyclerView.ItemDecoration {

	private TextPaint       mTitleTextPaint;
	private Paint           mBackgroundPaint;
	private TitleAttributes mTitleAttributes;

	public AZTitleDecoration(TitleAttributes attributes) {
		mTitleAttributes = attributes;
		mTitleTextPaint = new TextPaint();
		mTitleTextPaint.setTextSize(mTitleAttributes.mTextSize);
		mTitleTextPaint.setColor(mTitleAttributes.mTextColor);
		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(mTitleAttributes.mBackgroundColor);
	}

	/**
	 * 绘制标题
	 */
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
			int position = parent.getChildAdapterPosition(child);
			if (titleAttachView(child, parent)) {
				drawTitleItem(c, parent, child, adapter.getLetters(position));
			}
		}
	}

	/**
	 * 绘制悬浮标题
	 */
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
		//找到下一个标题对应的adapter position
		int nextLetterAdapterPosition = adapter.getNextLetterPosition(firstAdapterPosition);
		if (nextLetterAdapterPosition != -1) {
			//下一个标题view index
			int nextLettersViewIndex = nextLetterAdapterPosition - firstAdapterPosition;
			if (nextLettersViewIndex < parent.getChildCount()) {
				View nextLettersView = parent.getChildAt(nextLettersViewIndex);
				final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) nextLettersView.getLayoutParams();
				int nextToTop = nextLettersView.getTop() - params.bottomMargin - parent.getPaddingTop();
				if (nextToTop < mTitleAttributes.mItemHeight * 2) {
					//有重叠
					c.translate(0, nextToTop - mTitleAttributes.mItemHeight * 2);
				}
			}
		}
		c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(),
				   parent.getPaddingTop() + mTitleAttributes.mItemHeight, mBackgroundPaint);
		c.drawText(adapter.getLetters(firstAdapterPosition),
				   parent.getPaddingLeft() + firstView.getPaddingLeft() + mTitleAttributes.mTextPadding,
				   TextDrawUtils.getTextBaseLineByCenter(parent.getPaddingTop() + mTitleAttributes.mItemHeight / 2, mTitleTextPaint),
				   mTitleTextPaint);
		c.restore();
	}

	/**
	 * 设置空出绘制标题的区域
	 */
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		if (titleAttachView(view, parent)) {
			outRect.set(0, mTitleAttributes.mItemHeight, 0, 0);
		} else {
			super.getItemOffsets(outRect, view, parent, state);
		}
	}

	/**
	 * 绘制标题信息
	 */
	private void drawTitleItem(Canvas c, RecyclerView parent, View child, String letters) {
		final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
		//绘制背景
		c.drawRect(parent.getPaddingLeft(), child.getTop() - params.bottomMargin - mTitleAttributes.mItemHeight,
				   parent.getWidth() - parent.getPaddingRight(), child.getTop() - params.bottomMargin, mBackgroundPaint);

		float textCenterY = child.getTop() - params.bottomMargin - mTitleAttributes.mItemHeight / 2;
		//绘制标题文字
		c.drawText(letters, parent.getPaddingLeft() + child.getPaddingLeft() + mTitleAttributes.mTextPadding,
				   TextDrawUtils.getTextBaseLineByCenter(textCenterY, mTitleTextPaint), mTitleTextPaint);
	}

	/**
	 * 判断指定view的上方是否要空出绘制标题的位置
	 *
	 * @param view   　指定的view
	 * @param parent 父view
	 */
	private boolean titleAttachView(View view, RecyclerView parent) {
		if (parent.getAdapter() == null || !(parent.getAdapter() instanceof AZBaseAdapter)) {
			return false;
		}
		AZBaseAdapter adapter = (AZBaseAdapter) parent.getAdapter();
		if (adapter.getDataList() == null || adapter.getDataList().isEmpty()) {
			return false;
		}
		int position = parent.getChildAdapterPosition(view);
		//第一个一定要空出区域 + 每个都和前面一个去做判断，不等于前一个则要空出区域
		return position == 0 ||
			   null != adapter.getDataList().get(position) && !adapter.getLetters(position).equals(adapter.getLetters(position - 1));

	}

	public static class TitleAttributes {

		Context mContext;
		/**
		 * 标题高度
		 */
		int     mItemHeight;
		/**
		 * 文字的padding
		 */
		int     mTextPadding;
		/**
		 * 标题文字大小
		 */
		int     mTextSize;
		/**
		 * 标题文字颜色
		 */
		int     mTextColor;
		/**
		 * 标题背景
		 */
		int     mBackgroundColor;

		public TitleAttributes(Context context) {
			mContext = context;
			mItemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
			mTextPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
			mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics());
			mTextColor = Color.parseColor("#FF000000");
			mBackgroundColor = Color.parseColor("#FFDFDFDF");
		}

		public TitleAttributes setItemHeight(int heightDp) {
			mItemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDp,
														  mContext.getResources().getDisplayMetrics());
			return this;
		}

		public TitleAttributes setTextPadding(int paddingDp) {
			mTextPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingDp,
														   mContext.getResources().getDisplayMetrics());
			return this;
		}

		public TitleAttributes setTextSize(int sizeSp) {
			mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sizeSp, mContext.getResources().getDisplayMetrics());
			return this;
		}

		public TitleAttributes setTextColor(int color) {
			mTextColor = color;
			return this;
		}

		public TitleAttributes setBackgroundColor(int color) {
			mBackgroundColor = color;
			return this;
		}


	}
}
