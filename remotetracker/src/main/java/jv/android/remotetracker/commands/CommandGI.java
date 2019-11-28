package jv.android.remotetracker.commands;

import jv.android.utils.PhoneUtils;
import android.content.Context;

public class CommandGI {
	
	public static String processCommand(Context context) {
		
		PhoneUtils p = new PhoneUtils(context);

		try
		{
			return "IMSI: " + p.getIMSI() + "\n" +
                    "IMEI: " + p.getIMEI() + "\n" + 
                    "ICCID: " + p.getICCID() + "\n";
		}
		catch (Exception e) {
			return "";
		}
	}

}
