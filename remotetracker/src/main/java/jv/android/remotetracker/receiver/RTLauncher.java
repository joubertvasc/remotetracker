package jv.android.remotetracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import jv.android.remotetracker.services.RTService;

public class RTLauncher extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent commandService = new Intent(context, RTService.class);
		context.startService(commandService);
	}
}
