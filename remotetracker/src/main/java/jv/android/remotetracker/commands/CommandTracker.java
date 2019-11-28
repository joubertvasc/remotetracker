package jv.android.remotetracker.commands;

import android.content.Context;
import android.content.Intent;
import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.remotetracker.receiver.SendPositionSchedReceiver;
import jv.android.remotetracker.services.SendPositionService;
import jv.android.utils.Schedule;

public class CommandTracker {

	public final static int alarmId = 785268;
	
	public static String processCommand(Context context, CommandStructure cs, boolean start, int interval) {
		Preferences p = new Preferences(context);
		
		if (start) {
			p.setTrackerInterval(interval);
			
			if (interval > 0) {
				Intent intent = new Intent(context, SendPositionService.class);
				intent.putExtra("fake", false);
				intent.putExtra("commandstructure", cs);
				context.startService(intent);
				
				return context.getString(R.string.avTrackerOn);
			}
		} else {
			p.setTrackerInterval(0);
			Schedule.cancelAlarmId(context, SendPositionSchedReceiver.class, alarmId);
		}

		return context.getString(R.string.avTrackerOff);
	}
}
