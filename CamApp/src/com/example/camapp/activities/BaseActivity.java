package com.example.camapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;

public class BaseActivity extends ActionBarActivity{
	private DisplayMetrics mOutMetrics;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mOutMetrics = new DisplayMetrics();
	}
	
	public int getWidth() {
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(mOutMetrics);
		return mOutMetrics.widthPixels;
	}
	
	public int getHeight() {
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(mOutMetrics);
		return mOutMetrics.heightPixels;
	}
	
}
