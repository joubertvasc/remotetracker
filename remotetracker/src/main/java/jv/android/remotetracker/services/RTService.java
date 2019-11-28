package jv.android.remotetracker.services;

import java.text.MessageFormat;

import jv.android.remotetracker.R;
import jv.android.remotetracker.activity.LockActivity;
import jv.android.remotetracker.utils.Preferences;
import jv.android.utils.PhoneUtils;
import jv.android.utils.Sms;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class RTService extends IntentService {

	private Context context = null;
	private Preferences preferences;

	public RTService () {
		super("RTService");
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onHandleIntent(Intent intent) {
		context = this.getApplicationContext();

		preferences = new Preferences (context);

		if (preferences.getDebug()) {
			// Sleeping to give time to mount the cards
			try {
				Thread.sleep(60000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Verify the need of load preferences backup
		if (!preferences.getTosAccepted() && preferences.backupExists())
			preferences.importPreferences();

		// Only works if ToS was accepted
		if (preferences.getTosAccepted()) {
			PhoneUtils pu = new PhoneUtils(context);
			
			String imsi = pu.getIMSI();
			
			if (imsi != null && !imsi.equals("")) {
				if (!imsi.equals(preferences.getImsi1()) &&
					!imsi.equals(preferences.getImsi2()) &&
					!imsi.equals(preferences.getImsi3()) &&
					!imsi.equals(preferences.getImsi4())) {
					if (!preferences.getCel1().equals(""))
						sms(preferences.getCel1());
					if (!preferences.getCel2().equals(""))
						sms(preferences.getCel2());
					if (!preferences.getCel3().equals(""))
						sms(preferences.getCel3());
					if (!preferences.getCel4().equals(""))
						sms(preferences.getCel4());
				}			
			}
			
			// Verify if device is locked. if so, open the Lock Activity
			if (!preferences.getMsgLock().equals("")) {
				Intent i = new Intent(context, LockActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				i.putExtra("islock", "true");
				i.putExtra("fullscreen", "true");
				i.putExtra("msg", preferences.getMsgLock());
				
	        	context.startActivity(i);			
			}			
		}

		stopSelf();
	}

	private void sms(String number) {
		Sms sms = new Sms();
		
		MessageFormat form = new MessageFormat(context.getString(R.string.msgSIMCardChanged));
		Object[] args = {preferences.getOwnerName()};
		
		try {
			sms.sendSms(context, number, form.format(args));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
	
