package com.example.camapp;

import android.app.Application;

public class CamApp extends Application{
	private static CamApp mApp;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;
	}
	
	public static CamApp getApp() {
		return mApp;
	}
}
