package jv.android.remotetracker.commands;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import android.content.Context;
import android.content.Intent;
import jv.android.utils.SystemUtils;

public class CommandUnlock {

	public static String processCommand(Context context) {
		Preferences p = new Preferences(context);
		
		p.setMsgLock("");
		
		Intent i = new Intent(context, jv.android.remotetracker.activity.LockActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		i.putExtra("islock", "false");
		i.putExtra("fullscreen", "false");
		i.putExtra("msg", "teste");

		String process = (p.isProVersion() ? "jv.android.remotetrackerpro" : "jv.android.remotetracker");
		
		if (SystemUtils.appIsRunning(context, process)) {
			SystemUtils.killProcess (context, process);
		}
		
    	return context.getString(R.string.msgDeviceUnlocked);
		
	}
}
