package com.pilot.common.widget.udlrslidelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.pilot.common.R;

import java.util.List;


public abstract class UDLRSlideAdapter<T> extends BaseAdapter {

	public interface AdapterDataChangeListener {

		void onAdapterDataChange();
	}

	protected Context                   mContext;
	protected List<List<T>>             mData;
	protected int                       mSlideStartColumn;
	protected int                       mSlideLength;
	private   AdapterDataChangeListener mDataChangeListener;

	public UDLRSlideAdapter(Context context) {
		this(context, null);
	}

	public UDLRSlideAdapter(Context context, List<List<T>> data) {
		mContext = context;
		mData = data;
	}

	public void setData(List<List<T>> data) {
		mData = data;
		mSlideLength = 0;
		notifyDataSetChanged();
	}

	public void cleanData() {
		mData = null;
		notifyDataSetChanged();
	}

	public void setSlideColumnStart(int start) {
		mSlideStartColumn = start;
		notifyDataSetChanged();
	}

	public void setOnAdapterDataChangeListener(AdapterDataChangeListener listener) {
		mDataChangeListener = listener;
	}

	public void setSlideLength(int setX) {
		mSlideLength = setX;
	}


	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (mDataChangeListener != null) {
			mDataChangeListener.onAdapterDataChange();
		}
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public List<T> getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		List<T> itemData = getItem(position);
		UDLRSlideViewHolder holder;
		//todo:待优化
		if (position == 0 || convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_udlr_slide, parent, false);
			//设置item高度
			AbsListView.LayoutParams params = (AbsListView.LayoutParams) convertView.getLayoutParams();
			if (position == 0) {
				params.height = getItemViewTitleHeight();
			} else {
				params.height = getItemViewNormalHeight();
			}
			convertView.setLayoutParams(params);
			onCrateConvertViewFinish(convertView, position);
			holder = new UDLRSlideViewHolder(convertView, position);
			UDLRSlideRowLayout rowLayout = (UDLRSlideRowLayout) convertView.findViewById(R.id.item_udls_slide_row);
			//组合每一行的View,包含两部分，一个是固定的LinearLayout,一个是可滑动的LinearLayout
			if (itemData != null && !itemData.isEmpty()) {
				for (int index = 0; index < itemData.size(); index++) {
					View columnView;
					if (index < mSlideStartColumn) {
						columnView = getColumnView(position, index, itemData.size(), rowLayout.getFixLayout());
						columnView.setLayoutParams(getColumnViewParams(position, index, itemData.size()));
						rowLayout.getFixLayout().addView(columnView);
					} else {
						columnView = getColumnView(position, index, itemData.size(), rowLayout.getSlideLayout());
						columnView.setLayoutParams(getColumnViewParams(position, index, itemData.size()));
						rowLayout.getSlideLayout().addView(columnView);
					}
					holder.addColumnView(index, columnView);
				}
			}
			convertView.setTag(holder);
		} else {
			holder = (UDLRSlideViewHolder) convertView.getTag();
			//更新下holder position的位置
			holder.setPosition(position);
		}
		//复用的时候不能滑倒指定位置
		UDLRSlideRowLayout slideLayout = (UDLRSlideRowLayout) holder.getConvertView().findViewById(R.id.item_udls_slide_row);
		slideLayout.slideSet(mSlideLength);

		if (itemData != null && !itemData.isEmpty()) {
			for (int index = 0; index < itemData.size(); index++) {
				if (holder.getColumnView(index) != null) {
					convertColumnViewData(position, index, holder.getColumnView(index), convertView, itemData.get(index), itemData);
				}
			}
		}
		convertRowViewData(position, convertView);
		return convertView;
	}

	protected void onCrateConvertViewFinish(View rowLayout, int position) {

	}

	/**
	 * list view title item的高度
	 *
	 * @return item height
	 */
	public abstract int getItemViewTitleHeight();

	/**
	 * list view每一个item的高度
	 *
	 * @return item height
	 */
	public abstract int getItemViewNormalHeight();

	/**
	 * 行里面，每个column的宽度
	 *
	 * @param position    item position
	 * @param columnIndex column下标
	 * @param columnCount 总数
	 * @return column params
	 */
	public abstract LinearLayout.LayoutParams getColumnViewParams(int position, int columnIndex, int columnCount);

	/**
	 * column view
	 *
	 * @param position          item position
	 * @param columnIndex       column 下标
	 * @param columnCount       总数
	 * @param fixedColumnLayout 固定column的父布局
	 * @return column view
	 */
	public abstract View getColumnView(int position, int columnIndex, int columnCount, LinearLayout fixedColumnLayout);

	/**
	 * 绑定数据
	 *
	 * @param position       item position
	 * @param columnIndex    column 下标
	 * @param columnView     column view
	 * @param rowView        row view
	 * @param columnData     adapter data
	 * @param columnDataList column list data
	 */
	public abstract void convertColumnViewData(int position,
											   int columnIndex,
											   View columnView,
											   View rowView,
											   T columnData,
											   List<T> columnDataList);

	public abstract void convertRowViewData(int position, View rowView);

}
