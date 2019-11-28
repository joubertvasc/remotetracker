package jv.android.remotetracker.utils;

import java.io.File;

import jv.android.remotetracker.R;
import jv.android.utils.Email.Email;
import jv.android.utils.Logs;
import jv.android.utils.network.Network;
import android.content.Context;

public class SendPhotoEMail {

	public static boolean send(Context context, File file) {
		boolean netOk = true;
		boolean result = false;
		
		if (file.exists()) {
			Logs.infoLog("SendPhotoEMail.send: File " + file.getAbsolutePath() + " exists! Sending e-mail...");
			
			Preferences preferences = new Preferences(context);
			
			// if the command requires a network and it's not available, try to turn 3G and WiFi on.
			if (!Network.isNetworkAvailable(context)) {
				Logs.infoLog("SendPhotoEMail.send: network is off, trying to turn it on.");
				netOk = false;
				Network.turnNetworkOn(context, true, true);
			}
									
			Email email = new Email(preferences.getEmailUser(), preferences.getEmailPassword(), preferences.getEmailUserName(), preferences.getEmailAddress());

			try {
				try {
		    		email.addAttachment(file.getAbsolutePath());
					Logs.infoLog("SendPhotoEMail.send: Picture attached.");
		    		
		    		email.setTo(new String[] { preferences.getDefaultEMailAddress()});
				    email.setSubject(context.getString(R.string.msgSubject));
				    email.setBody(context.getString(R.string.msgPictureSent));
				    email.setSMTP(preferences.getEmailServer());
				    email.setPort(preferences.getEmailPort());
				    email.setAuth(preferences.isEmailSMTPAuth());

				    try {
						Logs.infoLog("SendPhotoEMail.send: Sending e-mail to: " + preferences.getDefaultEMailAddress());
						
						if (email.send()) {
							Logs.infoLog("SendPhotoEMail.send: E-Mail sent!");
							result = true;
						}
						else {
							Logs.warningLog("SendPhotoEMail.send: E-Mail not sent!");
						}
				    } catch (Exception e) {
						Logs.errorLog("SendPhotoEMail.send: Error sending e-mail", e);
				    }
		    	} catch (Exception e) {
		    		Logs.errorLog("SendPhotoEMail.send: attaching error", e);
		    	}
			} finally {
				if (!netOk) {
					Logs.infoLog("SendPhotoEMail.send: network was off, trying to turn it off again.");

					Network.setMobileDataEnabled(context, false);
					Network.setWifiDataEnabled(context, false);
				}
			}
		}
		else 
		{
			Logs.warningLog("SendPhotoEMail.send: file does not exists.");
		}
		
		return result;
	}
}
