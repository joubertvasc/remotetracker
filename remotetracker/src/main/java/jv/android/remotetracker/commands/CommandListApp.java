package jv.android.remotetracker.commands;

import java.util.ArrayList;

import android.content.Context;

import jv.android.utils.SystemUtils;

public class CommandListApp {

    public static String processCommand(Context context) {
        StringBuilder result = new StringBuilder();
        SystemUtils su = new SystemUtils(context);
        ArrayList<jv.android.utils.SystemUtils.PInfo> packages = su.getPackages(false);

        for (int i = 0; i < packages.size(); i++) {
            result.append(packages.get(i).getAppname()).append(" (").append(packages.get(i).getPname()).append(")\n");
        }

        return result.toString();
    }
}
