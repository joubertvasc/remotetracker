package jv.android.remotetracker.services;

import java.io.File;

import jv.android.remotetracker.R;
import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.utils.SendEMailOrFTP;
import jv.android.utils.Logs;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class SendPictureService extends IntentService {

	public Preferences preferences;

	public SendPictureService () {
		super("SendPictureService");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Logs.infoLog("SendPictureService.onCreate");

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Logs.infoLog("SendPictureService.onDestroy");

		super.onDestroy();
	}

	protected void sendPicture(Context context, File file) {
		if (SendEMailOrFTP.send(context, file, context.getString(R.string.msgSubject), context.getString(R.string.msgPictureSent), preferences.isInterceptPicsFTP()))
			Logs.infoLog("pictureReceiver: email sent.");
		else 
			Logs.infoLog("pictureReceiver: email not sent.");
	}
	
	@Override
	public void onHandleIntent(Intent intent) {
		Context context = this.getApplicationContext();
		Logs.infoLog("SendPictureService.onHandleIntent. Servi�o iniciado.");

		Bundle params = intent.getExtras();
		if (params != null) {
			String fileName = params.getString("fileName");
			boolean delete = params.getBoolean("deleteAfterSend", false);
			
			File file = new File(fileName);

			if (file.exists()) {
				Logs.infoLog("pictureReceiver File " + file.getAbsolutePath() + " exists! Sending e-mail...");

				preferences = new Preferences(context);

				sendPicture(context, file);
				
				if (delete) {
					Logs.infoLog("SendPictureService.onHandleIntent: deleting file after send: " + fileName);
					file.delete();
				}
			}
			else
				Logs.warningLog("pictureReceiver file does not exists: " + file.getAbsolutePath());
		}

		Logs.infoLog("SendPictureService.onHandleIntent. Parando servi�o.");
		stopSelf();
	}

}
