package jv.android.utils;

import android.net.Uri;
import android.content.Context;
import android.provider.MediaStore;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraUtils {
	
	private static Camera camera;
	private static Camera.Parameters camParams;
	private static String fileName = "";

	public static String imagePath (Context context, Uri photoUri) {
	    Cursor photoCursor = null;

	    try {
	        // Attempt to fetch asset filename for image
	        String[] projection = { MediaStore.Images.Media.DATA };
	        photoCursor = context.getContentResolver().query( photoUri, 
	                                                    projection, null, null, null );

	        if ( photoCursor != null && photoCursor.getCount() == 1 ) {
	            photoCursor.moveToFirst();
	            return  photoCursor.getString(photoCursor.getColumnIndex(MediaStore.Images.Media.DATA) );
	        }
	    } finally {
	        if ( photoCursor != null ) {
	            photoCursor.close();
	        }
	    }

	    return "";
	}

	public static Bitmap loadFullImage( Context context, Uri photoUri) {
	    Cursor photoCursor = null;

	    try {
	        // Attempt to fetch asset filename for image
	        String[] projection = { MediaStore.Images.Media.DATA };
	        photoCursor = context.getContentResolver().query( photoUri, 
	                                                    projection, null, null, null );

	        if ( photoCursor != null && photoCursor.getCount() == 1 ) {
	            photoCursor.moveToFirst();
	            String photoFilePath = photoCursor.getString(photoCursor.getColumnIndex(MediaStore.Images.Media.DATA) );

	            // Load image from path
	            return BitmapFactory.decodeFile( photoFilePath, null );
	        }
	    } finally {
	        if ( photoCursor != null ) {
	            photoCursor.close();
	        }
	    }

	    return null;
    }

	public static boolean takePicture (int cam, String name) {
		fileName = name;
				
		try {
			if (camera != null) {
				Logs.infoLog("CameraUtils.takePicture: camera already openned, releasing.");
				camera.release();
			}
			
			Logs.infoLog("CameraUtils.takePicture: openning camera");
			camera = Camera.open();

			Logs.infoLog("CameraUtils.takePicture: getting parameters");
			camParams = camera.getParameters();
			
			Logs.infoLog("CameraUtils.takePicture: definning camera " + String.valueOf(cam));
			camParams.set("camera-id", cam);
			camera.setParameters(camParams);		
			
			camera.startPreview();
			camera.takePicture(null, null, jpegCallback);
			camera.stopPreview();
			 
			camera.release();

			Logs.infoLog("CameraUtils.takePicture: done");
			return true;
		} catch (Exception e) {
			Logs.errorLog("CameraUtils.takePicture error", e);
			return false;
		}		
	}
	
	/** Handles data for jpeg picture */
	private static PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				// write to local sandbox file system
				// outStream =
				// CameraDemo.this.openFileOutput(String.format("%d.jpg",
				// System.currentTimeMillis()), 0);
				// Or write to sdcard

				Logs.infoLog("CameraUtils.takePicture: onPictureTaken");

				if (fileName.equals("")) 
					outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
				else
					outStream = new FileOutputStream(fileName);
				
				outStream.write(data);
				outStream.close();
				
				Logs.infoLog("CameraUtils.takePicture: onPictureTaken - wrote bytes: " + data.length);
			} catch (FileNotFoundException e) {
				Logs.errorLog("CameraUtils.takePicture: onPictureTaken - jpeg FILENOTFOUND", e);
			} catch (IOException e) {
				Logs.errorLog("CameraUtils.takePicture: onPictureTaken - jpeg IOERROR", e);
			} finally {
				
			}
			
			Logs.infoLog("onPictureTaken - jpeg");
		}
	};	
	
}
