package com.pilot.common.widget.udlrslidelistview;

import android.util.SparseArray;
import android.view.View;


public class UDLRSlideViewHolder {
	/**
	 * list view item view
	 */
	private View              mConvertView = null;
	/**
	 * list view item position
	 */
	private int               mPosition    = -1;
	/**
	 * list view item all column view
	 */
	private SparseArray<View> mColumnViews = null;

	public UDLRSlideViewHolder(View contentView, int position) {
		mConvertView = contentView;
		mPosition = position;
	}

	public View getConvertView() {
		return mConvertView;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		mPosition = position;
	}

	/**
	 * 记录每一列所有column的View
	 *
	 * @param columnIndex 列的位置
	 * @param view        列的View
	 */
	public void addColumnView(int columnIndex, View view) {
		if (null == mColumnViews) {
			mColumnViews = new SparseArray<>();
		}
		mColumnViews.put(columnIndex, view);
	}

	/**
	 * 获取指定列的View
	 *
	 * @param columnPosition 列的位置
	 * @return 列的View
	 */
	public View getColumnView(int columnPosition) {
		if (null == mColumnViews) {
			return null;
		}
		return mColumnViews.get(columnPosition);
	}
}
