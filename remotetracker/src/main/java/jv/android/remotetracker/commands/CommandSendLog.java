package jv.android.remotetracker.commands;

import java.io.File;

import jv.android.remotetracker.R;
import jv.android.utils.FTP;
import jv.android.utils.PhoneUtils;
import jv.android.remotetracker.utils.Preferences;

import android.content.Context;
import android.os.Environment;

public class CommandSendLog {

    public static String processCommand(Context context) {
        PhoneUtils pu = new PhoneUtils(context);

        String fileName = pu.getIMEI() + "debugmodelog.txt";
        File file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);

        if (!file.exists()) {
            return context.getString(R.string.msgLogDoesNotExist);
        } else {
            Preferences preferences = new Preferences(context);

            if (FTP.sendFile(context, preferences.getFtpServer(), preferences.getFtpUserName(), preferences.getFtpPassword(), preferences.getFtpRemotePath(), fileName)) {
                return context.getString(R.string.msgLogSent);
            } else {
                return context.getString(R.string.msgLogErrorSendingLog);
            }
        }
    }
}
