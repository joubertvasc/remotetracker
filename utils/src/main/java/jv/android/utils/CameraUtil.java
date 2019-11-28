package jv.android.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraUtil implements SurfaceHolder.Callback {

	// open back facing camera by default
	private Camera myCamera = null;
	private boolean mPreviewRunning = false;

	public void takePictureNoPreview(Context context, boolean forceLandscape) {
		takePicture (context, forceLandscape);
	}

	public void takePictureNoPreview(Context context)
	{
		takePicture (context, false);
	}

	@SuppressWarnings("deprecation")
	private void takePicture (Context context, boolean forceLandscape) {
		// do we have a camera?
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			if (myCamera == null)
				myCamera = Camera.open();

			if (myCamera != null)
			{
				try
				{
					//set camera parameters if you want to
					//...
					// here, the unused surface view and holder
					SurfaceView surfaceView = new SurfaceView(context);
					surfaceView.setFocusable(true);

					SurfaceHolder holder = surfaceView.getHolder();
					holder.addCallback(this);
					holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

					try {
						Camera.Parameters parameters = myCamera.getParameters();

						List<Size> sizes = parameters.getSupportedPreviewSizes();
						Size optimalSize = CameraUtil.getOptimalPreviewSize(sizes, 0, 0);

						int sdkBuildVersion = Integer.parseInt( android.os.Build.VERSION.SDK );

						if (sdkBuildVersion < 5 || forceLandscape)
						{
							// Picture size should be landscape
							if (optimalSize.width < optimalSize.height || forceLandscape)
								parameters.setPictureSize( optimalSize.height, optimalSize.width );
							else
								parameters.setPictureSize( optimalSize.width, optimalSize.height );
						}
						else
						{
							parameters.setPictureSize( optimalSize.width, optimalSize.height );
						}

						myCamera.setParameters(parameters);

						myCamera.setPreviewDisplay(holder);
						myCamera.startPreview();

						myCamera.takePicture(null, null, getJpegCallback());

						try {
							Thread.sleep(1000);
						} catch (Exception e) {

						}
					} catch (Exception e) {

					}
				} finally {
					//				myCamera.release();
				}
			} else {
				// nao existe camera traseira
			}
		}
	}

	private PictureCallback getJpegCallback(){
		PictureCallback jpeg=new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				myCamera.stopPreview();
				mPreviewRunning = false;

				if (data != null) {
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.jpeg");
						fos.write(data);
						fos.close();
					}  catch (IOException e) {
						//do something about it
					}
				}

				myCamera.release();
				myCamera = null;
			}
		};

		return jpeg;
	}

	public void surfaceCreated(SurfaceHolder holder)
	{
		myCamera = Camera.open();

		try {
			if (myCamera != null)
				myCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			myCamera.release();
			myCamera = null;
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (mPreviewRunning)
		{
			myCamera.stopPreview();
		}

		Camera.Parameters parameters = myCamera.getParameters();

		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Size optimalSize = CameraUtil.getOptimalPreviewSize(sizes, w, h);
		parameters.setPreviewSize(optimalSize.width, optimalSize.height);

		myCamera.setParameters(parameters);
		myCamera.startPreview();

		mPreviewRunning = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (myCamera != null) {
			myCamera.stopPreview();
			myCamera.release();
			myCamera = null;
		}
	}

	public static Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null) return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	public static void shootSound(Context context)
	{
		AudioManager meng = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

		MediaPlayer _shootMP = null;

		if (volume != 0)
		{
			if (_shootMP == null)
				_shootMP = MediaPlayer.create(context.getApplicationContext(), Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
			if (_shootMP != null)
				_shootMP.start();
		}
	}

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

}