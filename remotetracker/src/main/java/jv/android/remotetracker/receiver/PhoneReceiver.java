package jv.android.remotetracker.receiver;

import jv.android.remotetracker.utils.Preferences;
import jv.android.utils.Logs;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneReceiver  extends BroadcastReceiver {

	@SuppressLint("UnsafeProtectedBroadcastReceiver")
	@Override
	public void onReceive(Context context, Intent intent) {
		Preferences preferences = new Preferences(context);

		if (preferences.getDebug())
			Logs.startLog(context,"RemoteTrackerPRO", "remotetrackerpro.txt");

		Logs.infoLog("PhoneReceiver started. Defining PhoneStateListener.");
		MyPhoneStateListener phoneListener=new MyPhoneStateListener(context);
		
		Logs.infoLog("PhoneReceiver: getting TELEPHONY_SERVICE");
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        
		Logs.infoLog("PhoneReceiver: starting LISTEN_CALL_STATE");
		if (telephony != null) {
			telephony.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
}

