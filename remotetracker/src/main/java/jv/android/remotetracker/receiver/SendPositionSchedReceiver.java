package jv.android.remotetracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import jv.android.remotetracker.services.SendPositionService;

public class SendPositionSchedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, SendPositionService.class);
		context.startService(i);
	}

}
