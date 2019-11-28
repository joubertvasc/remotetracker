package jv.android.remotetracker.commands;

import java.io.File;

import jv.android.remotetracker.R;
import jv.android.utils.PhoneUtils;
import android.content.Context;
import android.os.Environment;

public class CommandClearLog {

	public static String processCommand(Context context) {
		PhoneUtils pu = new PhoneUtils(context);

		String fileName = pu.getIMEI() + "debugmodelog.txt";
		File file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
		
		if (!file.exists()) {
			return context.getString(R.string.msgLogDoesNotExist);
		} else {
			file.delete();

			return context.getString(R.string.msgLogClear);
		}
	}
}
