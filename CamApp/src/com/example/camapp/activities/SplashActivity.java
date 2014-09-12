package com.example.camapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.camapp.R;

public class SplashActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 2000);
	}
}
