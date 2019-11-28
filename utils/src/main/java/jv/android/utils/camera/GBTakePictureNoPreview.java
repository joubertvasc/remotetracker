// Thanks to: 

// http://www.vogella.com/articles/AndroidCamera/article.html
// http://developer.android.com/reference/android/hardware/Camera.html#setDisplayOrientation(int)

package jv.android.utils.camera;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import jv.android.utils.Logs;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("deprecation")
public class GBTakePictureNoPreview implements SurfaceHolder.Callback {

	// open back facing camera by default
	private Camera myCamera = null;
	private int camId = GBCameraUtil.findBackFacingCamera();

	// Tell me if the preview is running or stopped
	private boolean mPreviewRunning = false;

	// Parameters
	private Context context;
	private IGBPictureTaken iGBPictureTaken = null;
	private String fileName = "";
	private boolean usingLandscape = false;

	public GBTakePictureNoPreview (Context context, IGBPictureTaken iGBPictureTaken) {
		this.context = context;
		this.iGBPictureTaken = iGBPictureTaken;
	}

	public boolean setUseFrontCamera(boolean useFrontCamera) {
		int c = GBCameraUtil.findFrontFacingCamera();

		if (c != -1) {
			camId = c;		

			return true;
		} else {
			camId = GBCameraUtil.findBackFacingCamera();

			return false;
		}
	}

	public void setFileName (String fileName) {
		this.fileName = fileName;
	}

	public void setLandscape () {
		this.usingLandscape = true;
	}

	public void setPortrait () {
		this.usingLandscape = false;
	}

	public boolean cameraIsOk() {
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) && camId > -1;
	}

	public void takePicture () {
		// do we have a camera?
		if (cameraIsOk()) {
//			ThreadUtils.wait(2);
			try {
				Logs.infoLog("takePicture: waiting to take picture");
				Thread.sleep(500);				
			} catch (Exception e) {
				Logs.infoLog("takePicture: sleep error", e);
			}

			if (myCamera == null) { 
				Logs.infoLog("takePicture: openning camera: " + String.valueOf(camId));
				myCamera = Camera.open(camId);
			}

			if (myCamera != null)
			{
				Logs.infoLog("takePicture: camera opened. Creating surface");
				SurfaceView surfaceView = new SurfaceView(context);
				surfaceView.setFocusable(true);
				surfaceView.setSoundEffectsEnabled(false);

				Logs.infoLog("takePicture: creating holder.");
				SurfaceHolder holder = surfaceView.getHolder();
//				holder.addCallback(this);
				holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

				try {
					Logs.infoLog("takePicture: setting parameters.");
					Camera.Parameters parameters = myCamera.getParameters();

					List<Size> sizes = parameters.getSupportedPreviewSizes();
					Size optimalSize = GBCameraUtil.getOptimalPreviewSize(sizes, 1024, 768);

					int sdkBuildVersion = Integer.parseInt( android.os.Build.VERSION.SDK );

					if (sdkBuildVersion < 5 || usingLandscape) 
					{
						// Picture size should be landscape
						if (optimalSize.width < optimalSize.height || usingLandscape)
							parameters.setPictureSize( optimalSize.height, optimalSize.width );
						else
							parameters.setPictureSize( optimalSize.width, optimalSize.height );
					}
					else
					{
						// If the device is in portraint and width > height, 
						// or if the device is in landscape and height > height, so we need to rotate them.
						switch (context.getResources().getConfiguration().orientation) {
						case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
							if (optimalSize.height > optimalSize.width ) {
								parameters.setRotation(camId == GBCameraUtil.findFrontFacingCamera() ? 270 : 90);
							}

							break;								 
						case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :						
							if (optimalSize.width > optimalSize.height) {
								parameters.setRotation(camId == GBCameraUtil.findFrontFacingCamera() ? 270 : 90);
							}

							break;								 
						}  

						parameters.setPictureSize (optimalSize.width, optimalSize.height);
					}
					parameters.setPictureFormat(ImageFormat.JPEG);

					myCamera.setParameters(parameters);	

					Logs.infoLog("takePicture: starting preview.");
					myCamera.setPreviewDisplay(holder);    
					myCamera.startPreview(); 

					Logs.infoLog("takePicture: taking picture.");

					myCamera.takePicture(shuttercallback, null, getJpegCallback());

//					ThreadUtils.wait(1);
					try {
						Logs.infoLog("takePicture: waiting the camera.");
						Thread.sleep(3000);				
					} catch (Exception e) {
						Logs.errorLog("takePicture: sleep error", e);
					}

					Logs.infoLog("takePicture: finishing.");
				} catch (Exception e) {
					// Sorry, nothing to do
					Logs.errorLog("takePicture: error", e);
				}
			} else {
				Logs.warningLog("takePicture: could not open camera.");
			}
		}
	}

	private Camera.ShutterCallback shuttercallback = new Camera.ShutterCallback() {

	    @Override
	    public void onShutter() {
	        Logs.infoLog("Camera.ShutterCallback");
	    }
	};
	
	private Camera.PictureCallback getJpegCallback(){
		Camera.PictureCallback jpeg=new Camera.PictureCallback() {   
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				Logs.infoLog("onPictureTaken: stopping preview.");
				myCamera.stopPreview();
				mPreviewRunning = false;

				if (data != null) {
					FileOutputStream fos;
					try {
						if (fileName.equals("")) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
							String date = dateFormat.format(new Date());
							fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + date + ".jpg";							
						}

						Logs.infoLog("takePicture: saving picture: " + fileName);
						fos = new FileOutputStream(fileName);
						fos.write(data);
						fos.close();
						Logs.infoLog("takePicture: saved");
						
						iGBPictureTaken.onPictureTaken(fileName);
					}  catch (IOException e) {
						Logs.errorLog("takePicture: Error saving picture", e);
						iGBPictureTaken.onPictureError(e.getMessage());
					}
				} else {
					Logs.warningLog("onPictureTaken: data = null.");
					iGBPictureTaken.onPictureError("Data = null");
				}

				Logs.infoLog("takePicture: releasing camera");
				myCamera.release();
				myCamera = null;	
			}
		};

		return jpeg;
	}

	public void surfaceCreated(SurfaceHolder holder) 
	{
		Logs.infoLog("surfaceCreated: opening camera: " + String.valueOf(camId));
		myCamera = Camera.open(camId);

		try {
			if (myCamera != null) {
				Logs.infoLog("surfaceCreated: surface created");
				myCamera.setPreviewDisplay(holder);
			}
		} catch (IOException exception) {
			Logs.errorLog("surfaceCreated: error opening camera", exception);
			myCamera.release();
			myCamera = null;
		}		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Logs.infoLog("surfaceChanged: setting parameters");

		if (mPreviewRunning) 
		{
			myCamera.stopPreview();
		}

		Camera.Parameters parameters = myCamera.getParameters();

		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Size optimalSize = GBCameraUtil.getOptimalPreviewSize(sizes, w, h);
		parameters.setPreviewSize(optimalSize.width, optimalSize.height);

		myCamera.setParameters(parameters);
		myCamera.startPreview();

		Logs.infoLog("surfaceChanged: end");
		mPreviewRunning = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Logs.infoLog("surfaceDestroyed: enter");

		if (myCamera != null) {
			Logs.infoLog("surfaceDestroyed: stop preview and releasing camera");
			myCamera.stopPreview();
			myCamera.release();
			myCamera = null;	
		}

		Logs.infoLog("surfaceDestroyed: end");
	}

}
