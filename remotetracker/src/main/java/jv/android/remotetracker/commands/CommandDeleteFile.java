package jv.android.remotetracker.commands;

import java.io.DataOutputStream;
import java.io.File;

import jv.android.remotetracker.R;
import jv.android.utils.Logs;
import android.content.Context;

public class CommandDeleteFile {

	public static String processCommand(Context context, String fileName) {
		if (!fileName.equals("")) {
			try {
				fileName = fileName.replace('_', '/');
				File file = new File(fileName);
				
				if (!file.exists()) {
					return context.getString(R.string.msgFileDoesNotExist);
				} else {
					if (fileName.toLowerCase().startsWith("/system")) {
						Logs.infoLog("CommandRunApp.processCommand: Trying SU access.");
					    Process su = Runtime.getRuntime().exec("su");
					    DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

						Logs.infoLog("CommandRunApp.processCommand: mounting /system as not readonly.");
					    outputStream.writeBytes("mount -o remount,rw -t ext4 /emmc@android /system\n");
					    outputStream.flush();

						Logs.infoLog("CommandRunApp.processCommand: erasing the file.");
					    outputStream.writeBytes("rm " + fileName + "\n");
					    outputStream.flush();

						Logs.infoLog("CommandRunApp.processCommand: mounting /system as readonly.");
					    outputStream.writeBytes("mount -o remount,ro -t ext4 /ennc@android /system\n");
					    outputStream.flush();
					    
					    su.waitFor();
					} else {
						Logs.infoLog("CommandRunApp.processCommand: Trying delete simple file.");
						file.delete();
					}
					
					return context.getString(R.string.msgTheFileWasDeleted);
				}
			} catch (Exception e) {
				Logs.errorLog("CommandDeleteFile.processCommand error.", e);
				return context.getString(R.string.msgCouldNotDeleteTheFile);
			}
		} else {
			Logs.warningLog("CommandRunApp.processCommand: fileName is empty");
			return context.getString(R.string.msgFileNotDefined);
		}
	}
}
