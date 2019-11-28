package jv.android.remotetracker.commands;

import android.content.Context;
import android.content.SharedPreferences;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.utils.Logs;

public class CommandPics {

    public static String processCommand(Context context, boolean enable, boolean ftp) {
        Preferences p = new Preferences(context);
        if (enable) {
            if (ftp && p.getFtpServer().equals("")) {
                Logs.warningLog("CommandPics: No FTP Server");
                return context.getString(R.string.msgNoFTPServer);
            } else if (!ftp && p.getDefaultEMailAddress().equals("")) {
                Logs.warningLog("CommandPics: No EMail address");
                return context.getString(R.string.msgNoDefaultEMail);
            }
        }

        SharedPreferences.Editor prefEditor = p.getSharedPrefs().edit();
        prefEditor.putBoolean("interceptPics", enable);
        prefEditor.putBoolean("interceptPicsFTP", ftp);
        prefEditor.apply();

        Logs.infoLog("CommandPics: configuration done! Capture Picture " + (enable ? "started" : "stopped"));

        if (enable)
            return context.getString(R.string.msgPicsCaptureOn);
        else
            return context.getString(R.string.msgPicsCaptureOff);
    }
}
