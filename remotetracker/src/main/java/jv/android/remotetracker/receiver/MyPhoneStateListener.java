package jv.android.remotetracker.receiver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.remotetracker.utils.SendEMailOrFTP;
import jv.android.utils.camera.GBCameraUtil;
import jv.android.utils.camera.GBTakePictureNoPreview;
import jv.android.utils.camera.IGBPictureTaken;
import jv.android.utils.Logs;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.content.Context;

public class MyPhoneStateListener extends PhoneStateListener implements IGBPictureTaken {

	private Context context;

	public MyPhoneStateListener (Context context) {
		this.context = context;
	}

	public void onCallStateChanged(int state, String incomingNumber){
		switch(state)
		{
		case TelephonyManager.CALL_STATE_IDLE:
			//              Log.d("DEBUG", "IDLE");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			phoneIsOffHeek(); 

			break;
		case TelephonyManager.CALL_STATE_RINGING:
			//        	   Log.d("DEBUG", "RINGING");
			break;
		}
	}	

	private void phoneIsOffHeek() {
//		android.os.Debug.waitForDebugger();
		Logs.infoLog("MyPhoneStateListener started");

		Preferences p = new Preferences(context);

		if (!p.isProVersion()) {
			Logs.warningLog("MyPhoneStateListener: feature only available in PRO version.");
		}
		else if (!p.isTakePhotoOnCalls()) {
			Logs.infoLog("MyPhoneStateListener: no command to take photos on call was sent. Nothing to do.");
		}
		else if (p.getDefaultEMailAddress().equals("")) {
			Logs.warningLog("MyPhoneStateListener: no default email address was defined. Nothing to do."); 
		} 
		else 
		{
			Logs.infoLog("MyPhoneStateListener: Received new call.");

			if (GBCameraUtil.findFrontFacingCamera() == -1) {
				Logs.warningLog("MyPhoneStateListener: there is no front camera.");
			} else {			
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				String date = dateFormat.format(new Date());
				final String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + date + ".jpg";	
				Logs.infoLog("MyPhoneStateListener: " + fileName);

				Logs.infoLog("MyPhoneStateListener: taking picture");
				GBTakePictureNoPreview c = new GBTakePictureNoPreview(context, this);

				c.setUseFrontCamera(true);

				c.setFileName(fileName);

				if (c.cameraIsOk()) {
					try {
						c.takePicture();
					} catch (Exception e) {
						Logs.errorLog("MyPhoneStateListener: error", e);
					}
				}

/*				Thread emailThread = new Thread() {
					public void run() {
						Logs.infoLog("MyPhoneStateListener: Thread.run");
						try {
							Logs.infoLog("MyPhoneStateListener: sleeping...");
							sleep(5000);

							File file = new File(fileName);

							if (file.exists()) {
								Logs.infoLog("MyPhoneStateListener File " + file.getAbsolutePath() + " exists! Sending e-mail...");

								Preferences preferences = new Preferences(context);

								if (SendEMailOrFTP.send(context, file, context.getString(R.string.msgSubject), context.getString(R.string.msgPictureCaptured), preferences.isTakePhotoOnCallsFTP())) {
									Logs.infoLog("MyPhoneStateListener: email sent.");
								}
								else 
								{
									Logs.infoLog("MyPhoneStateListener: email not sent.");
								}

								file.delete();
							} else
								Logs.warningLog("MyPhoneStateListener: file does not exists: " + fileName);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						Logs.infoLog("MyPhoneStateListener: Thread.end");
					};
				};

				emailThread.start(); /**/		
			}
		}		
		Logs.infoLog("MyPhoneStateListener: ended");
	}

	@Override
	public void onPictureTaken(String fileName) {
		File file = new File(fileName);

		if (file.exists()) {
			Logs.infoLog("MyPhoneStateListener File " + file.getAbsolutePath() + " exists! Sending e-mail...");

			Preferences preferences = new Preferences(context);

			if (SendEMailOrFTP.send(context, file, context.getString(R.string.msgSubject), context.getString(R.string.msgPictureCaptured), preferences.isTakePhotoOnCallsFTP())) {
				Logs.infoLog("MyPhoneStateListener: email sent.");
			}
			else 
			{
				Logs.infoLog("MyPhoneStateListener: email not sent.");
			}

			file.delete();
		} else
			Logs.warningLog("MyPhoneStateListener: file does not exists: " + fileName);
	}

	@Override
	public void onPictureError(String message) {
		Logs.errorLog("MyPhoneStateListener: onPictureError: " + message);
	}

}