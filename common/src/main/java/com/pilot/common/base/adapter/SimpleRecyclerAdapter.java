package com.pilot.common.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Set;

/**
 * for build simple adapter for {@link RecyclerView}, must implements {@link #newInstanceHolder} and {@link #onViewBind} and {@link
 * #getLayoutId}
 */
public abstract class SimpleRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

	public interface OnItemClickListener {

		void onItemClick(View view, int position);
	}

	protected Context      mContext    = null;
	protected List<T>      mData       = null;
	protected Set<Integer> mSelections = null;
	protected OnItemClickListener mOnItemClickListener;

	public SimpleRecyclerAdapter(Context context) {
		this(context, null);
	}

	public SimpleRecyclerAdapter(Context context, List<T> data) {
		mContext = context;
		mData = data;
	}

	public void setData(List<T> data) {
		mData = data;
		notifyDataSetChanged();
	}

	public List<T> getData() {
		return mData;
	}

	public void setSelections(Set<Integer> selections) {
		mSelections = selections;
		notifyDataSetChanged();
	}

	public void clearSelections() {
		if (mSelections != null) {
			mSelections.clear();
			notifyDataSetChanged();
		}
	}

	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

	@Override
	public int getItemCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public V onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
		return newInstanceHolder(view);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		onViewBind((V) holder, position, mData.get(position));
	}

	protected abstract V newInstanceHolder(View view);

	protected abstract void onViewBind(V holder, int position, T t);

	protected abstract int getLayoutId();
}
