package jv.android.remotetracker.commands;

import jv.android.remotetracker.R;
import android.content.Context;
import android.content.Intent;

public class CommandMsg {

	public static String processCommand(Context context, String msg) {
		if (msg == null || msg.equals("")) {
			return context.getString(R.string.msgCantShowEmptyMessage);
		}
		else
		{
			Intent i = new Intent(context, jv.android.remotetracker.activity.LockActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			i.putExtra("islock", "false");
			i.putExtra("fullscreen", "false");
			i.putExtra("msg", msg);
			
	    	context.startActivity(i);
	    	return context.getString(R.string.msgMessageShow);
		}
	}
}
