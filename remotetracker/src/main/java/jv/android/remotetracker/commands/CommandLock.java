package jv.android.remotetracker.commands;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import android.content.Context;
import android.content.Intent;

public class CommandLock {

	public static String processCommand(Context context, String msg) {
		if (msg == null || msg.equals("")) {
			msg = context.getString(R.string.msgLock);
		}
		
		Preferences p = new Preferences(context);
		
		p.setMsgLock(msg);
		
		Intent i = new Intent(context, jv.android.remotetracker.activity.LockActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		i.putExtra("islock", "true");
		i.putExtra("fullscreen", "true");
		i.putExtra("msg", msg);
		
    	context.startActivity(i);
    	return context.getString(R.string.msgDeviceLocked);
	}
}
