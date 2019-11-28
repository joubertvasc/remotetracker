package jv.android.utils.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import jv.android.utils.FileUtil;
import jv.android.utils.Message;
import jv.android.utils.R;
import jv.android.utils.CameraUtil;

public class CameraView extends Activity implements SurfaceHolder.Callback
{
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private static final String TAG = "CameraTest";
	private Camera mCamera;
	private boolean mPreviewRunning = false;
	
	private Intent intent;
	private String label;
	private String folder = "";
	private String pictureHeader = "";
	private String pictureExtension = "";

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.e(TAG, "onCreate");

    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    	intent = getIntent();
    	String message = "";

        if (intent != null) {
        	Bundle params = intent.getExtras();
        	
        	if (params != null) {
        		message = params.getString("message");
				label = params.getString("label");
        		folder = params.getString("folder");
        		pictureHeader = params.getString("pictureheader");
        		pictureExtension = params.getString("pictureextension");
        	}
        	
        	if (message == null || message.trim().equals(""))
        		message = "OK";
        }		
        
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cameraview);
		
		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		mSurfaceView.setFocusable(true);
		
		//        mSurfaceView.setOnClickListener(this);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		Toast.makeText(this, "Clique na imagem para capturar uma foto", Toast.LENGTH_LONG).show();
	}

	public void doTakePicture(View v) {
		try {
			mCamera.takePicture(null, mPictureCallback, mPictureCallback);
		} catch (Exception e) {
			Message.showMessage(this, getString(R.string.warning), getString(R.string.avErrorTakingPicture));
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if (data != null) {
				mCamera.stopPreview();
				mPreviewRunning = false;
				File tmp = null;

				CameraUtil.shootSound(getApplicationContext());

				FileOutputStream outStream = null;
				try {
					tmp = FileUtil.getTempFile(getApplicationContext(), folder, pictureHeader, pictureExtension);
					
					outStream = new FileOutputStream(tmp);
					outStream.write(data);
					outStream.close();
					Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}

				Log.d(TAG, "onPictureTaken - jpeg");

				if (intent != null) { 
			        intent.putExtra("arquivo", tmp.getAbsolutePath());
                    intent.putExtra("label", label);
					setResult(RESULT_OK, intent);
				}

				finish();
			}       
		}
	};

	protected void onResume() 
	{
		Log.e(TAG, "onResume");
		super.onResume();
	}

	protected void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
	}

	protected void onStop() {
		Log.e(TAG, "onStop");
		super.onStop();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(TAG, "surfaceCreated");
		mCamera = Camera.open();
        
        try {
			mCamera.setPreviewDisplay(holder);
//			setDisplayOrientation(mCamera, 90);
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}		
	}

	protected void setDisplayOrientation(Camera camera, int angle) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, new Object[] { angle });
        } catch (Exception e1) {
        }
    }
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.e(TAG, "surfaceChanged");

		if (mPreviewRunning) {
			mCamera.stopPreview();
		}

		Camera.Parameters parameters = mCamera.getParameters();
        
		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Size optimalSize = CameraUtil.getOptimalPreviewSize(sizes, w, h);
		parameters.setPreviewSize(optimalSize.width, optimalSize.height);

		mCamera.setParameters(parameters);
		mCamera.startPreview();

		mPreviewRunning = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(TAG, "surfaceDestroyed");
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;	
	}
}