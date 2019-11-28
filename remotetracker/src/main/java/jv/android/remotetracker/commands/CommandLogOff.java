package jv.android.remotetracker.commands;

import jv.android.remotetracker.R;
import jv.android.utils.Logs;
import android.content.Context;

public class CommandLogOff {

	public static String processCommand(Context context) {
		Logs.stop();
		return context.getString(R.string.msgLogStoped);
	}
}
