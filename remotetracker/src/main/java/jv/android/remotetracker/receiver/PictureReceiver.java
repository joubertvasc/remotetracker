package jv.android.remotetracker.receiver;

import java.io.File;

import jv.android.remotetracker.services.SendPictureService;
import jv.android.remotetracker.utils.Preferences;
import jv.android.utils.CameraUtil;
import jv.android.utils.Logs;
import jv.android.utils.SystemUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PictureReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Preferences preferences = new Preferences(context);

		if (preferences.getDebug())
			Logs.startLog(context,"RemoteTrackerPRO", "remotetrackerpro.txt");

		if (!SystemUtils.appIsRunning(context, "jv.android.atm.PictureReceiver")) {		
			if (intent.getAction().toLowerCase().contains("picture")) {
				if (!preferences.isProVersion()) {
					Logs.warningLog("pictureReceiver.onReceive: feature only available in PRO version.");
				} else if (!preferences.isInterceptPics()) {
					Logs.infoLog("pictureReceiver.onReceive: no command to intercept camera was sent. Nothing to do.");
				} else if (preferences.getDefaultEMailAddress().equals("")) {
					Logs.warningLog("pictureReceiver.onReceive: no default email address was defined. Nothing to do."); 
				} else {
					Logs.infoLog("PictureReceiver.onReceive: iniciando SENDPICTURESERVICE.");
					Intent sendPictureService = new Intent(context, SendPictureService.class);
					
					File file = new File(CameraUtil.imagePath(context, intent.getData()));
					
				    sendPictureService.putExtra("fileName", file.getAbsolutePath());
					context.startService(sendPictureService);
				}
			}
		}	
	}
}
