package jv.android.remotetracker.commands;

import jv.android.remotetracker.R;
import jv.android.utils.Logs;
import jv.android.utils.PhoneUtils;

import android.content.Context;

public class CommandLogOn {

    public static String processCommand(Context context) {
        PhoneUtils pu = new PhoneUtils(context);

        Logs.startLog(context, "REMOTETRACKERLOG", pu.getIMEI() + "debugmodelog.txt");
        return context.getString(R.string.msgLogStarted);
    }
}
