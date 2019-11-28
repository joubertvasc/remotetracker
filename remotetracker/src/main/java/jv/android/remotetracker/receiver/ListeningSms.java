package jv.android.remotetracker.receiver;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.commands.CommandProcessor;
import jv.android.utils.gps.GPSUtils;
import jv.android.utils.Logs;
import jv.android.utils.network.Network;
import jv.android.utils.Sms;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class ListeningSms extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Preferences preferences = new Preferences(context);

		if (preferences.getDebug())
			Logs.startLog(context,"RemoteTracker", (preferences.isProVersion() ? "remotetrackerpro.txt" :  "remotetracker.txt"));

		// Verify the need of load preferences backup
		if (!preferences.getTosAccepted() && preferences.backupExists())
			preferences.importPreferences();

		// If user really didn't accept the ToS, exit.
		if (!preferences.getTosAccepted()) {
			Logs.warningLog("Tos was not accepted.");

			this.clearAbortBroadcast();
		}
		else {
			Logs.infoLog("Action: " + intent.getAction());

			if (intent.getAction().toLowerCase().contains("sms")) {			
				Sms sms = new Sms();

				//L� a mensagem
				SmsMessage msg = sms.receberMensagem(intent);
				String celular = msg.getDisplayOriginatingAddress();
				String mensagem = msg.getDisplayMessageBody();

				Logs.infoLog("Cellular: " + celular + " Message: " + mensagem);

				// Verifica se � para abortar a mensagem e apresentar um toast
				if (mensagem.toLowerCase().trim().contains("rt#")) {
					Logs.infoLog("Message is a RT command: " + mensagem);

					CommandProcessor commands = new CommandProcessor(context, celular, "", mensagem.trim());
					Logs.infoLog("Message was parsed. Command: " + commands.getCommandStructure().getCommand());
					Logs.infoLog("Message was parsed. Extra info: " + commands.getCommandStructure().getExtraParameter());
					Logs.infoLog("Message was parsed. E-Mail: " + commands.getCommandStructure().getReturnEMail());
					Logs.infoLog("Message was parsed. Number: " + commands.getCommandStructure().getReturnNumber());
					Logs.infoLog("Message was parsed. Password: " + commands.getCommandStructure().getPassword());

					boolean mobileNetworkAvailable = Network.isMobileNetworkAvailable(context);
					boolean wifiNetworkAvailable = Network.isWifiNetworkAvailable(context);
					boolean gpsAvailable = GPSUtils.isGPSActive(context);

					try {
						this.abortBroadcast();

						commands.processCommands(context);
						Logs.infoLog("Command was processed");
					} finally {
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
				else {
					Logs.infoLog("Message is NOT a RT command");

					// continue the normal process of sms and will get alert and reaches inbox
					this.clearAbortBroadcast();
				}
			}
		}
	}
}
