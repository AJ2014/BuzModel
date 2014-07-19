package com.example.buzmodel;

import com.example.buzmodel.view.BuzImageView;
import com.example.buzmodel.view.BuzTextView;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	BuzTextView mTv;
	BuzImageView mImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mTv = (BuzTextView) findViewById(R.id.tv_buz);
		mImg = (BuzImageView) findViewById(R.id.img_buz);
		
		mTv.setText("Hello World");
	}
	
	
}
