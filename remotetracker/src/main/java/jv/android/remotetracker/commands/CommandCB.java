package jv.android.remotetracker.commands;

import jv.android.utils.Logs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;

public class CommandCB {

	@SuppressLint("MissingPermission")
	public static void processCommand(Context context, String phoneNumber) {
		if (phoneNumber == null || phoneNumber.equals("")) {
			Logs.warningLog("CommandCB.ProcessoCommand: No PhoneNumber.");
		}
		else {
			Uri uri = Uri.fromParts("tel", phoneNumber, null);

			Intent callIntent = new Intent(Intent.ACTION_CALL, uri);
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			context.startActivity(callIntent);
		}
	}
}
