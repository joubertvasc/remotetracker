package jv.android.utils.camera;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;

public class GBCameraUtil {

	public static int findBackFacingCamera() {
		int cameraId = -1;

		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);

			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				break;
			}
		}

		return cameraId;
	}

	public static int findFrontFacingCamera() {
		int cameraId = -1;

		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);

			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				break;
			}
		}

		return cameraId;
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

	 public static void setCameraDisplayOrientation(Context context, int cameraId, Camera camera) {
	     CameraInfo info = new CameraInfo();
	     Camera.getCameraInfo(cameraId, info);
	     
	     int rotation = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();
	     int degrees = 0;
	     
	     switch (rotation) { 
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     
	     camera.setDisplayOrientation(result);
	 }	
}
