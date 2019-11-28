package jv.android.remotetracker.receiver;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.commands.CommandProcessor;
import jv.android.utils.gps.GPSUtils;
import jv.android.utils.Logs;
import jv.android.utils.network.Network;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ListeningEMail extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Preferences preferences = new Preferences(context);
		
		if (preferences.getDebug())
			Logs.startLog(context,"RemoteTracker", (preferences.isProVersion() ? "remotetrackerpro.txt" : "remotetracker.txt"));
		
		// Verify the need of load preferences backup
		if (!preferences.getTosAccepted() && preferences.backupExists())
			preferences.importPreferences();
		
		// If user didn't accept the ToS, exit.
		if (!preferences.getTosAccepted()) {
			Logs.warningLog("Tos was not accepted.");
			
            this.clearAbortBroadcast();
		}
		else {
			Logs.infoLog("Action: " + intent.getAction());
			
			if (intent.getAction().toLowerCase().contains("email")) {			
				Logs.infoLog("E-Mail received.");
				Bundle bundle = intent.getExtras();

				if (bundle != null && bundle.containsKey("com.fsck.k9.intent.extra.SUBJECT")) {
					String subject = bundle.getString("com.fsck.k9.intent.extra.SUBJECT");
					String from = bundle.getString("com.fsck.k9.intent.extra.FROM");
					
					if (subject.toLowerCase().contains("rt#")) {
						Logs.infoLog("Subject contains RT key: " + subject);

						CommandProcessor commands = new CommandProcessor(context, "", from, subject.trim());
						Logs.infoLog("Message was parsed. Command: " + commands.getCommandStructure().getCommand());
						Logs.infoLog("Message was parsed. Extra info: " + commands.getCommandStructure().getExtraParameter());
						Logs.infoLog("Message was parsed. E-Mail: " + commands.getCommandStructure().getReturnEMail());
						Logs.infoLog("Message was parsed. Number: " + commands.getCommandStructure().getReturnNumber());
						Logs.infoLog("Message was parsed. Password: " + commands.getCommandStructure().getPassword());

						if (commands.getCommandStructure().getReturnEMail().equals("") &&
							    commands.getCommandStructure().getReturnNumber().equals("")) {
							Logs.infoLog("No Cellular number or E-Mail was send. Nothing to do.");

							//continue the normal process of sms and will get alert and reaches inbox
				            this.clearAbortBroadcast();
						}								
						else {							
							boolean mobileNetworkAvailable = Network.isMobileNetworkAvailable(context);
							boolean wifiNetworkAvailable = Network.isWifiNetworkAvailable(context);
							boolean gpsAvailable = GPSUtils.isGPSActive(context);

							try {
					       		commands.processCommands(context);
								Logs.infoLog("Command was processed");
							} finally {
					       		this.abortBroadcast();

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
					}
			        else {
						Logs.infoLog("E-Mail is NOT a RT command");

						//continue the normal process of sms and will get alert and reaches inbox
			            this.clearAbortBroadcast();
			        }
				}
				else {
					Logs.infoLog("Broadcast received something different from SMS or EMAIL");

					//continue the normal process of sms and will get alert and reaches inbox
		            this.clearAbortBroadcast();
				}
			}
			else {
				Logs.infoLog("Broadcast received something different from SMS or EMAIL");

				//continue the normal process of sms and will get alert and reaches inbox
	            this.clearAbortBroadcast();
			}
		}
	}
}
