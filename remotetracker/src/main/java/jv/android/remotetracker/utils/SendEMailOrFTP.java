package jv.android.remotetracker.utils;

import java.io.File;

import jv.android.utils.Email.Email;
import jv.android.utils.FTP;
import jv.android.utils.Logs;
import jv.android.utils.network.Network;
import android.content.Context;

public class SendEMailOrFTP {

	public static boolean send(Context context, File file, String subject, String body, boolean ftp) {
		boolean netOk = true;
		boolean result = false;
		
		if (file.exists()) {
			Logs.infoLog("SendPhotoEMailOrFTP.send: File " + file.getAbsolutePath() + " exists! Sending e-mail...");
			
			Preferences preferences = new Preferences(context);
			
			// if the command requires a network and it's not available, try to turn 3G and WiFi on.
			if (!Network.isNetworkAvailable(context)) {
				Logs.infoLog("SendPhotoEMailOrFTP.send: network is off, trying to turn it on.");
				netOk = false;
				Network.turnNetworkOn(context, true, true);
			}
				
			try {
				if (ftp) {
					Logs.infoLog("SendPhotoEMailOrFTP.send: Sending file to FTPServer: " + preferences.getFtpServer() + " - " + preferences.getFtpUserName());
					if (FTP.sendFile(context, preferences.getFtpServer(), preferences.getFtpUserName(), preferences.getFtpPassword(), preferences.getFtpRemotePath(), file.getPath())) {
						Logs.infoLog("SendPhotoEMailOrFTP.send. File was sent to FTP server.");
					} else {
						Logs.warningLog("SendPhotoEMailOrFTP.send. Could not send file to FTP server. Is configuration correct?");
					}
				} else {
					Email email = new Email(preferences.getEmailUser(), preferences.getEmailPassword(), preferences.getEmailUserName(), preferences.getEmailAddress());

					try {
			    		email.addAttachment(file.getAbsolutePath());
						Logs.infoLog("SendPhotoEMailOrFTP.send: Picture attached.");
			    		
			    		email.setTo(new String[] { preferences.getDefaultEMailAddress()});
					    email.setSubject(subject);
					    email.setBody(body);
					    email.setSMTP(preferences.getEmailServer());
					    email.setPort(preferences.getEmailPort());
					    email.setAuth(preferences.isEmailSMTPAuth());

					    Logs.infoLog("SendPhotoEMailOrFTP.send: Sending e-mail to: " + preferences.getDefaultEMailAddress());

					    if (email.send()) {
					    	Logs.infoLog("SendPhotoEMailOrFTP.send: E-Mail sent!");
					    	result = true;
					    }
					    else {
					    	Logs.warningLog("SendPhotoEMailOrFTP.send: E-Mail not sent!");
					    }
			    	} catch (Exception e) {
			    		Logs.errorLog("SendPhotoEMailOrFTP.send: attaching error", e);
			    	}
				}
			} finally {
				if (!netOk) {
					Logs.infoLog("SendPhotoEMailOrFTP.send: network was off, trying to turn it off again.");

					Network.setMobileDataEnabled(context, false);
					Network.setWifiDataEnabled(context, false);
				}
			}
		}
		else 
		{
			Logs.warningLog("SendPhotoEMailOrFTP.send: file does not exists.");
		}
		
		return result;
	}
}
