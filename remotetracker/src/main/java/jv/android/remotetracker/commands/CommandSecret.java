package jv.android.remotetracker.commands;

import android.content.Context;
import jv.android.remotetracker.*;
import jv.android.remotetracker.utils.Preferences;

public class CommandSecret {

	public static String processCommand(Context context) {
		Preferences p = new Preferences(context);
		
		if (p.getSecretQuestion().trim().equals("")) {
			return context.getString(R.string.msgNoSecretQuestion);
		}
		else
		{
			return p.getSecretQuestion().trim();
		}
	}
}
