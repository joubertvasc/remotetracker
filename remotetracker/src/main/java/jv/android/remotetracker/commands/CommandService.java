// Based on FindMyPhone sourcecode: http://sourceforge.net/projects/findmyphone/
package jv.android.remotetracker.commands;

import jv.android.remotetracker.utils.Preferences;
import jv.android.utils.gps.GPSUtils;
import jv.android.utils.Logs;
import jv.android.utils.network.Network;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class CommandService extends Service {

	private Context context = null;
	private CommandGPListener cl = null;
	private CommandStructure cs = null;
	private boolean fake = false;

	boolean mobileNetworkAvailable = false;
	boolean wifiNetworkAvailable = false;
	boolean gpsAvailable = false;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Logs.infoLog("CommandService: onCreate");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Logs.infoLog("CommandService: onStart");

		context = this.getApplicationContext();
		Logs.infoLog("CommandService: Context is null? " + (context == null ? "Y" : "N"));

		if (intent != null) {
			Bundle b = intent.getExtras();
			fake = b.getBoolean("fake");

			cs= (CommandStructure)b.getSerializable("commandstructure"); 

			Logs.infoLog("CommandService: Fake " + (fake ? "Y" : "N"));
			Logs.infoLog("CommandService: cs is null? " + (cs == null ? "Y" : "N"));

			mobileNetworkAvailable = Network.isMobileNetworkAvailable(context);
			wifiNetworkAvailable = Network.isWifiNetworkAvailable(context);
			gpsAvailable = GPSUtils.isGPSActive(context);

			// Test if GPS is enabled or if RT can enable it
			if (GPSUtils.isGPSActive(context)) 
				Logs.infoLog("GPS is already enabled");
			else {
				if (GPSUtils.canToggleGPS(context)) {
					Logs.infoLog("Enabling GPS");
					GPSUtils.turnGPSOn(context);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// Ignores.
					}

					if (GPSUtils.isGPSActive(context))  
						Logs.infoLog("GPS is now enabled");
					else
						Logs.warningLog("Failed to enable GPS");
				} else {
					Logs.warningLog("GPS cannot be enabled remotelly");
				}
			}

			// Test if Network is enabled or if RT can enable it
			if (!Network.isNetworkAvailable(context))
				Network.turnNetworkOn(context, true, true);

			Logs.infoLog("CommandService: before processCommand");
			cl = new CommandGPListener(context, intent, cs, fake);
			cl.processCommand();
			Logs.infoLog("CommandService: after processCommand");

			final Preferences p = new Preferences(context);

			Thread sleepThread = new Thread() {
				public void run() {
					Logs.infoLog("CommandService: Thread.run");
					try {
						Logs.infoLog("CommandService: Thread.sleeping " + String.valueOf(p.getGPSTimeout()) + " seconds.");
						sleep(p.getGPSTimeout() * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Logs.infoLog("CommandService: Thread.end");
					stopService();
				};
			};
			
			sleepThread.start();
		} else {
			Logs.infoLog("CommandService: Force stop service. Intent = null");
			stopService();
		}
	}

	private void stopService() {
		Logs.infoLog("CommandService: stopService");
		stopSelf();
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logs.infoLog("CommandService: onDestroy");

		if (!mobileNetworkAvailable && Network.isMobileNetworkAvailable(context)) {
			Network.setMobileDataEnabled(context, false);
		}

		if (!wifiNetworkAvailable && Network.isWifiNetworkAvailable(context)) {
			Network.setWifiDataEnabled(context, false);
		}

		if (!gpsAvailable && GPSUtils.isGPSActive(context) && GPSUtils.canToggleGPS(context)) {
			GPSUtils.turnGPSOff(context);
		}
	}
}
