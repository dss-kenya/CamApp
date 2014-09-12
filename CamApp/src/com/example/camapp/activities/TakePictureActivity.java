package com.example.camapp.activities;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.example.camapp.R;
import com.example.camapp.activities.utilities.ImageUtilities;

/**
 * Read: http://stackoverflow.com/questions/8543244/custom-camera-android 
 * to capture the image and save it well
 * @author user
 *
 */
public class TakePictureActivity extends BaseActivity implements Callback, OnClickListener{
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Camera mCamera;
	private boolean mPreviewing;
	private LayoutInflater mInflater;
	private Button mBtnCancel;
	private Button mBtnCapture;
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_pic);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		getWindow().setFormat(PixelFormat.UNKNOWN);
		mSurfaceView = (SurfaceView)findViewById(R.id.surfaceView);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mInflater = LayoutInflater.from(getBaseContext());
		View viewControl = mInflater.inflate(R.layout.overlay, null);
		LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		layoutParamsControl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.addContentView(viewControl, layoutParamsControl);

		mBtnCancel = (Button)viewControl.findViewById(R.id.btnCancel);
		mBtnCapture = (Button)viewControl.findViewById(R.id.btnCapture);

		mBtnCancel.setOnClickListener(this);
		mBtnCapture.setOnClickListener(this);
		mSurfaceHolder.setFixedSize(getWidth(),getHeight());
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if(mPreviewing){
			mCamera.stopPreview();
			mPreviewing = false;
		}

		if (mCamera != null){
			try {
				Camera.Parameters parameters = mCamera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height,parameters);
				
				if (size != null) {
					parameters.setPreviewSize(size.width, size.height);
					mCamera.setParameters(parameters);
				}
				
				if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			        mCamera.setDisplayOrientation(90);
			    } else {
			    	mCamera.setDisplayOrientation(0);
			    }
				
				mCamera.setPreviewDisplay(mSurfaceHolder);
				mCamera.startPreview();
				mPreviewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(mSurfaceHolder);
		} catch (Throwable t) {
		Log.e("dhara",
				"Exception in setPreviewDisplay()", t);
			Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG)
			.show();
		}
}

@Override
public void surfaceDestroyed(SurfaceHolder holder) {

}

@Override
public void onResume() {
	super.onResume();
	mCamera = Camera.open();
}

@Override
public void onPause() {
	if (mPreviewing) {
		mCamera.stopPreview();
	}

	mCamera.release();
	mCamera = null;
	mPreviewing = false;
	super.onPause();
}

@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.btnCancel:
		finish();
		break;

	case R.id.btnCapture:
		mCamera.takePicture(null, null, photoCallback);
		mPreviewing = false;
		break;

	default:
		break;
	}
}

Camera.PictureCallback photoCallback = new Camera.PictureCallback() {
	public void onPictureTaken(final byte[] data, final Camera camera) {
		mDialog = ProgressDialog.show(TakePictureActivity.this, "", "Saving photo");
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (Exception ex) {}
				onPictureTake(data, camera);
			}
		}.start();
	}
};

public void onPictureTake(byte[] data, Camera camera) {
	Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
	Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
	ImageUtilities.savePhoto(mutableBitmap);
	mDialog.dismiss();
}

private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
	Camera.Size result = null;
	for (Camera.Size size: parameters.getSupportedPreviewSizes()) {
		if (size.width <= width && size.height <= height) {
			if (result == null) {
				result = size;
			} else {
				int resultArea = result.width * result.height;
				int newArea = size.width * size.height;
				if (newArea > resultArea) {
					result = size;
				}
			}
		}
	}
	return (result);
}
}
