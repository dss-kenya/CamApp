package com.example.camapp.activities.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import com.example.camapp.CamApp;

import android.graphics.Bitmap;
import android.os.Environment;

public class ImageUtilities {
	private static File mImgFileName;
	private static final String SAVED_FILE_FOLDER = "SavedImages";
	private static final String MODIFIED_FILE_FOLDER="SuperImposedImages";
	
	public static void savePhoto(Bitmap mutableBitmap) {
		File imageFileFolder = new File(Environment.getExternalStorageDirectory(), 
				CamApp.getApp().getPackageName() + 
				"/" +
				SAVED_FILE_FOLDER);
        imageFileFolder.mkdirs();
        FileOutputStream out = null;
        Calendar c = Calendar.getInstance();
        
        String date = fromInt(c.get(Calendar.MONTH)) + 
        		fromInt(c.get(Calendar.DAY_OF_MONTH)) + 
        		fromInt(c.get(Calendar.YEAR)) + 
        		fromInt(c.get(Calendar.HOUR_OF_DAY)) + 
        		fromInt(c.get(Calendar.MINUTE)) + 
        		fromInt(c.get(Calendar.SECOND));
        
        mImgFileName = new File(imageFileFolder, date.toString() + ".png");
        
        if(!mImgFileName.exists()) {
        	mImgFileName.mkdirs();
        }
        
        try {
            out = new FileOutputStream(mImgFileName);
            mutableBitmap.compress(Bitmap.CompressFormat.PNG,100, out);
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static String fromInt(int val) {
        return String.valueOf(val);
    }
}
