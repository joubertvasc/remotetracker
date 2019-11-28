package jv.android.remotetracker.commands;

import java.util.ArrayList;
import java.util.Locale;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.utils.Logs;
import jv.android.utils.SystemUtils;

import android.content.Context;

public class CommandRunApp {

    public static String processCommand(Context context, String app) {
        String pac = "";

        if (!app.equals("")) {
            SystemUtils su = new SystemUtils(context);
            ArrayList<jv.android.utils.SystemUtils.PInfo> packages = su.getPackages(true);

            for (int i = 0; i < packages.size(); i++) {
                if (packages.get(i).getAppname().toLowerCase(Locale.getDefault()).trim().equals(app.toLowerCase().trim()) ||
                        packages.get(i).getPname().toLowerCase(Locale.getDefault()).trim().equals(app.toLowerCase().trim())) {
                    pac = packages.get(i).getPname();
                    break;
                }
            }

            if (pac.equals("")) {
                Logs.warningLog("CommandRunApp.processCommand: application '" + app + "' not found.");
                return context.getString(R.string.msgApplicationNotFound);
            } else {
                Logs.infoLog("CommandRunApp.processCommand: application '" + app + "' (" + pac + ") will be invoked.");
                su.startApplication(pac);

                return context.getString(R.string.msgApplicationStarted);
            }
        } else {
            Logs.warningLog("CommandRunApp.processCommand: app is empty");
            return context.getString(R.string.msgApplicationNotDefined);
        }
    }
}
