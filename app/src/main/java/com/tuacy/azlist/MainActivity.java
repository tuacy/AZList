package com.tuacy.azlist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pilot.common.utils.PinyinUtils;
import com.tuacy.azlist.azlist.AZEntity;
import com.tuacy.azlist.azlist.AZTitleDecoration;
import com.tuacy.azlist.azlist.AZWaveSideBarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private Context                mContext;
	private RecyclerView           mRecyclerView;
	private AZWaveSideBarView      mBarList;
	private List<AZEntity<String>> mDateList;
	private ItemAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
		initData();
	}

	private void initView() {
		mRecyclerView = findViewById(R.id.recycler_list);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		mRecyclerView.addItemDecoration(
			new AZTitleDecoration(new AZTitleDecoration.TitleAttributes(mContext)));
		mBarList = findViewById(R.id.bar_list);
	}

	private void initEvent() {
		mBarList.setOnLetterChangeListener(new AZWaveSideBarView.OnLetterChangeListener() {
			@Override
			public void onLetterChange(String letter) {
				int position = mAdapter.getLettersFirstPosition(letter);
				if (position != -1) {
					if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
						LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
						manager.scrollToPositionWithOffset(position, 0);
					} else {
						mRecyclerView.getLayoutManager().scrollToPosition(position);
					}
				}
			}
		});
	}

	private void initData() {
		mDateList = filledData(getResources().getStringArray(R.array.date));
		Collections.sort(mDateList, new PinyinComparator());
		mRecyclerView.setAdapter(mAdapter = new ItemAdapter(mDateList));
	}

	private List<AZEntity<String>> filledData(String[] date) {
		List<AZEntity<String>> mSortList = new ArrayList<>();
		for (String aDate : date) {
			AZEntity<String> sortModel = new AZEntity<>();
			sortModel.setValue(aDate);
			//汉字转换成拼音
			String pinyin = PinyinUtils.getPingYin(aDate);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setLetters(sortString.toUpperCase());
			} else {
				sortModel.setLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}
}
