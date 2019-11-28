package jv.android.remotetracker.commands;

import jv.android.utils.camera.GBCameraUtil;
import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.utils.Logs;

import android.content.Context;
import android.content.SharedPreferences;

public class CommandPCalls {

    public static String processCommand(Context context, boolean enable, boolean ftp) {
        Preferences p = new Preferences(context);
        if (enable) {
            if (ftp && p.getFtpServer().equals("")) {
                return context.getString(R.string.msgNoFTPServer);
            } else if (!ftp && p.getDefaultEMailAddress().equals("")) {
                return context.getString(R.string.msgNoDefaultEMail);
            }
        }

        if (GBCameraUtil.findFrontFacingCamera() == -1) {
            Logs.warningLog("phoneIsOffHeek: there is no front camera.");
            return context.getString(R.string.msgTakePhotosNoFrontCamera);
        } else {

            SharedPreferences.Editor prefEditor = p.getSharedPrefs().edit();
            prefEditor.putBoolean("takePhotoOnCalls", enable);
            prefEditor.putBoolean("takePhotoOnCallsFTP", ftp);
            prefEditor.apply();

            Logs.infoLog("CommandPCalls: configuration done! Take Photos on Calls " + (enable ? "started" : "stopped"));

            if (enable)
                return context.getString(R.string.msgTakePhotosOnCallOn);
            else
                return context.getString(R.string.msgTakePhotosOnCallOff);
        }
    }
}
