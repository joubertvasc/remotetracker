package jv.android.remotetracker.commands;

import jv.android.remotetracker.R;
import jv.android.utils.Format;
import jv.android.utils.Logs;
import jv.android.utils.Sms;
import android.content.Context;

public class CommandSms {

	public static String processCommand (Context context, String msg, String number) {
		if (msg == null || msg.equals("")) 
			return context.getString(R.string.msgCantSendEmptyMessage);
		
		if (number == null || number.equals("")) 
			return context.getString(R.string.msgEmptyNumber);

		try {
			Sms sms = new jv.android.utils.Sms();
			
			sms.sendSms(context, number, Format.removeAccents(msg));
			return context.getString(R.string.msgMessageSent);
		} catch (Exception e) {
			Logs.errorLog("CommandSMS.processCommand error", e);

			return context.getString(R.string.msgMessageNotSent);
		}		
	}
}
