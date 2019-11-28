package jv.android.remotetracker.commands;

import android.content.Context;
import jv.android.remotetracker.*;
import jv.android.remotetracker.utils.Preferences;

public class CommandLostpass {

	public static String processCommand(Context context, String answer) {
		Preferences p = new Preferences (context);
		
		if (p.getSecretAnswer().trim().equals("")) {
			return context.getString(R.string.msgNoSecretAnswer);
		}
		else if (p.getSecretAnswer().toLowerCase().trim().equals(answer.toLowerCase().trim()))
		{
			return p.getPassword();
		}
		else
		{
			return context.getString(R.string.msgWrongSecretAnswer);
		}
	}
}
