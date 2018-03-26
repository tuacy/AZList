package com.tuacy.azlist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		findViewById(R.id.button_right_hint).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RightHintActivity.startUp(mContext);
			}
		});

		findViewById(R.id.button_center_hint).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CenterHintActivity.startUp(mContext);
			}
		});
	}


}
