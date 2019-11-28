package jv.android.remotetracker.receiver;

import jv.android.remotetracker.utils.Preferences;
import jv.android.utils.Logs;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LockRTReceiver extends BroadcastReceiver {

	@SuppressWarnings("unused")
	@Override
	public void onReceive(Context context, Intent intent) {
		Preferences preferences = new Preferences(context);

		if (preferences.getDebug())
			Logs.startLog(context,"RemoteTracker", "LockRTReceiver.txt");
		
		if (!preferences.getMsgLock().equals("")) {
			Logs.infoLog("Message: " + preferences.getMsgLock());
			Intent i = new Intent(context, jv.android.remotetracker.activity.LockActivity.class);
			
			if (i != null) {
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				i.putExtra("islock", "true");
				i.putExtra("fullscreen", "true");
				i.putExtra("msg", preferences.getMsgLock());

				Logs.infoLog("Starting activity");
	        	context.startActivity(i);
			}
			else {
				Logs.warningLog("i = null!");
			}
		}
	}
}
