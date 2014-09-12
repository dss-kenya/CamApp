package com.example.camapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.camapp.R;

/**
 * http://fc02.deviantart.net/fs71/i/2013/026/e/6/capturing_memories_by_eugeniea-d5sszs2.jpg
 * @author user
 *
 */
public class MainActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public boolean hasCamera() {
		return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}
	
	public void captureClick(View v) {
		if(hasCamera()) {
			Intent intent = new Intent(getApplicationContext(),TakePictureActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
