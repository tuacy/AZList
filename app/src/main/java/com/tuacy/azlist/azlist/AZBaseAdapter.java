package com.tuacy.azlist.azlist;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class AZBaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

	protected List<AZEntity<T>> mDataList;

	public AZBaseAdapter(List<AZEntity<T>> dataList) {
		mDataList = dataList;
	}

	public List<AZEntity<T>> getDataList() {
		return mDataList;
	}

	public void setDataList(List<AZEntity<T>> dataList) {
		mDataList = dataList;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return mDataList == null ? 0 : mDataList.size();
	}
}
