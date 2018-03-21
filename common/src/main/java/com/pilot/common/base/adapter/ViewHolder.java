package com.pilot.common.base.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ViewHolder {

	private Map<String, Object> mMap         = null;
	private SparseArray<View>   mViews       = null;
	private View                mConvertView = null;
	private int                 mPosition    = -1;

	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		mPosition = position;
		mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	public static ViewHolder getViewHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
		if (null == convertView) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mPosition = position;
			return viewHolder;
		}
	}

	/**
	 * get view by view id
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (null == view) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	public int getPosition() {
		return mPosition;
	}

	public ViewHolder setTag(int viewId, final Object tag) {
		View view = getView(viewId);
		view.setTag(tag);
		return this;
	}

	public Object getTag(int viewId) {
		View view = getView(viewId);
		return view.getTag();
	}

	public void setMap(String key, Object value) {
		if (mMap == null) {
			mMap = new HashMap<String, Object>();
		}
		mMap.put(key, value);
	}

	public Object getMap(String key) {
		return mMap == null ? null : mMap.get(key);
	}

	public ViewHolder setPadding(int viewId, int left, int top, int right, int bottom) {
		View view = getView(viewId);
		view.setPadding(left, top, right, bottom);
		return this;
	}

	public ViewHolder setVisibility(int viewId, int visibility) {
		View view = getView(viewId);
		view.setVisibility(visibility);
		return this;
	}

	/**
	 * TextView
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView textView = getView(viewId);
		textView.setText(text);
		return this;
	}

	public ViewHolder setText(int viewId, Integer resourceId) {
		TextView textView = getView(viewId);
		textView.setText(resourceId);
		return this;
	}

	public CharSequence getText(int viewId) {
		TextView textView = getView(viewId);
		return textView.getText();
	}

	/**
	 * ImageView
	 */
	public ViewHolder setImageResource(int viewId, Integer resourceId) {
		ImageView imageView = getView(viewId);
		imageView.setImageResource(resourceId);
		return this;
	}

	public ViewHolder setBackgroundResource(int viewId, Integer resourceId) {
		ImageView imageView = getView(viewId);
		imageView.setBackgroundResource(resourceId);
		return this;
	}

	public ViewHolder setBrightness(int viewId, int brightness) {
		ImageView imageView = getView(viewId);
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.set(new float[]{1,
									0,
									0,
									0,
									brightness,
									0,
									1,
									0,
									0,
									brightness,
									0,
									0,
									1,
									0,
									brightness,
									0,
									0,
									0,
									1,
									0});
		imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
		return this;
	}
}
