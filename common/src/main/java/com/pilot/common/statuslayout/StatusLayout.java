package com.pilot.common.statuslayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.opensource.svgaplayer.SVGAImageView;
import com.pilot.common.R;
import com.pilot.common.widget.SpreadTextView;


public class StatusLayout extends FrameLayout {

	public interface OnRefreshListener {

		void onRefresh(boolean isEmptyRefresh);
	}

	private int               mContentLayoutId;
	private View              mContentView;
	private SVGAImageView     mSVGALoading;
	private int               mLoadingLayoutId;
	private View              mLoadingView;
	private SpreadTextView    mSpreadTextView;
	private SVGAImageView     mSVGAEmpty;
	private int               mEmptyLayoutId;
	private View              mEmptyView;
	private SVGAImageView     mSVGAException;
	private int               mExceptionLayoutId;
	private View              mExceptionView;
	private StatusType        mStatusType;
	private OnRefreshListener mListener;

	public StatusLayout(@NonNull Context context) {
		this(context, null);
	}

	public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttribute(attrs, defStyleAttr);
		initView();
	}

	private void initAttribute(AttributeSet attrs, int defStyleAttr) {
		TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.StatusLayout, defStyleAttr, 0);
		mContentLayoutId = typeArray.getResourceId(R.styleable.StatusLayout_status_content_layout, -1);
		mLoadingLayoutId = typeArray.getResourceId(R.styleable.StatusLayout_status_loading_layout, R.layout.layout_loading_defualt);
		mEmptyLayoutId = typeArray.getResourceId(R.styleable.StatusLayout_status_empty_layout, R.layout.layout_empty_defualt);
		mExceptionLayoutId = typeArray.getResourceId(R.styleable.StatusLayout_status_exception_layout, R.layout.layout_exception_defualt);
		typeArray.recycle();
	}

	private void initView() {
		if (mContentLayoutId == -1) {
			throw new NullPointerException("StatusLayout use should set content layout id");
		}
		mContentView = LayoutInflater.from(getContext()).inflate(mContentLayoutId, this, false);
		mLoadingView = LayoutInflater.from(getContext()).inflate(mLoadingLayoutId, this, false);
		if (mLoadingLayoutId == R.layout.layout_loading_defualt) {
			mSVGALoading = mLoadingView.findViewById(R.id.svg_status_loading);
			mSpreadTextView = mLoadingView.findViewById(R.id.spread_loading_text);
		}
		mEmptyView = LayoutInflater.from(getContext()).inflate(mEmptyLayoutId, this, false);
		if (mEmptyLayoutId == R.layout.layout_empty_defualt) {
			mSVGAEmpty = mEmptyView.findViewById(R.id.svg_status_empty);
			View emptyRefreshView = mEmptyView.findViewById(R.id.text_status_layout_empty_refresh);
			emptyRefreshView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mListener != null) {
						mListener.onRefresh(true);
					}
				}
			});
		}
		mExceptionView = LayoutInflater.from(getContext()).inflate(mExceptionLayoutId, this, false);
		if (mExceptionLayoutId == R.layout.layout_exception_defualt) {
			mSVGAException = mExceptionView.findViewById(R.id.svg_status_exception);
			View exceptionRefreshView = mExceptionView.findViewById(R.id.text_status_layout_exception_refresh);
			exceptionRefreshView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mListener != null) {
						mListener.onRefresh(false);
					}
				}
			});
		}
		addView(mContentView);
		addView(mLoadingView);
		addView(mEmptyView);
		addView(mExceptionView);
		mStatusType = StatusType.CONTENT;
		mContentView.setVisibility(VISIBLE);
		mLoadingView.setVisibility(GONE);
		mEmptyView.setVisibility(GONE);
		mExceptionView.setVisibility(GONE);
		if (mSpreadTextView != null) {
			mSpreadTextView.stopAnimation();
		}
		if (mSVGALoading != null) {
			mSVGALoading.pauseAnimation();
		}
		if (mSVGAEmpty != null) {
			mSVGAEmpty.pauseAnimation();
		}
		if (mSVGAException != null) {
			mSVGAException.pauseAnimation();
		}
	}

	public View getContentView() {
		return mContentView;
	}

	public View getLoadingView() {
		return mLoadingView;
	}

	public View getEmptyView() {
		return mEmptyView;
	}

	public View getExceptionView() {
		return mExceptionView;
	}

	public StatusType getStatusType() {
		return mStatusType;
	}

	public void toggleStatue(StatusType statusType) {
		if (mStatusType != statusType) {
			switch (statusType) {
				case CONTENT:
					if (mSpreadTextView != null) {
						mSpreadTextView.stopAnimation();
					}
					if (mSVGALoading != null) {
						mSVGALoading.pauseAnimation();
					}
					if (mSVGAEmpty != null) {
						mSVGAEmpty.pauseAnimation();
					}
					if (mSVGAException != null) {
						mSVGAException.pauseAnimation();
					}
					mContentView.setVisibility(VISIBLE);
					mLoadingView.setVisibility(GONE);
					mEmptyView.setVisibility(GONE);
					mExceptionView.setVisibility(GONE);
					break;
				case LOADING:
					if (mSpreadTextView != null) {
						mSpreadTextView.startAniamtion();
					}
					if (mSVGALoading != null) {
						mSVGALoading.startAnimation();
					}
					if (mSVGAEmpty != null) {
						mSVGAEmpty.pauseAnimation();
					}
					if (mSVGAException != null) {
						mSVGAException.pauseAnimation();
					}
					mContentView.setVisibility(GONE);
					mLoadingView.setVisibility(VISIBLE);
					mEmptyView.setVisibility(GONE);
					mExceptionView.setVisibility(GONE);
					break;
				case EMPTY:
					if (mSpreadTextView != null) {
						mSpreadTextView.stopAnimation();
					}
					if (mSVGALoading != null) {
						mSVGALoading.pauseAnimation();
					}
					if (mSVGAEmpty != null) {
						mSVGAEmpty.startAnimation();
					}
					if (mSVGAException != null) {
						mSVGAException.pauseAnimation();
					}
					mContentView.setVisibility(GONE);
					mLoadingView.setVisibility(GONE);
					mEmptyView.setVisibility(VISIBLE);
					mExceptionView.setVisibility(GONE);
					break;
				case EXCEPTION:
					if (mSpreadTextView != null) {
						mSpreadTextView.stopAnimation();
					}
					if (mSVGALoading != null) {
						mSVGALoading.pauseAnimation();
					}
					if (mSVGAEmpty != null) {
						mSVGAEmpty.pauseAnimation();
					}
					if (mSVGAException != null) {
						mSVGAException.startAnimation();
					}
					mContentView.setVisibility(GONE);
					mLoadingView.setVisibility(GONE);
					mEmptyView.setVisibility(GONE);
					mExceptionView.setVisibility(VISIBLE);
					break;
			}
			mStatusType = statusType;
		}
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}
}
