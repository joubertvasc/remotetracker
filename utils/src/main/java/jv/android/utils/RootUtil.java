package jv.android.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import jv.android.utils.network.Network;

public class RootUtil {

	public static boolean moveAppToSystemApp(String apkName) {
		return moveAppToSystemApp(apkName, apkName);
	}

	public static void turnOffDevice() {
		try {
			Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot -p" });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean moveAppToSystemApp(String apkName, String appToCopyName) {
	    boolean isKitKat = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT;
	    boolean result = false;
	    
	    File app = new File("/data/app/" + apkName);
	    
	    if (app.exists()) {
	    	result = copyTo(appToCopyName, "/data/app/", (isKitKat ? "/system/priv-app" : "/system/app"));
	    } else if (isKitKat) {
		    File systemApp = new File("/system/app/" + apkName);
		    
		    if (systemApp.exists()) {
		    	result = copyTo(appToCopyName, "/system/app/", "/system/priv-app");
		    }
	    } else {
	    	result = true;
	    }
	    
	    return result;
	}
	
	private static boolean copyTo(String file, String from, String to) {
		try {
		    Logs.infoLog("Root.copyFromUserAppToSystemApp: su.");
		    Process su = Runtime.getRuntime().exec("su");
		    DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

			Logs.infoLog("Root.copyFromUserAppToSystemApp: Remounting system folder with write permition.");
		    outputStream.writeBytes("mount -o remount,rw -t ext4 /emmc@android /system\n");
		    outputStream.flush();

			Logs.infoLog("Root.copyFromUserAppToSystemApp: Copy " + from + "/" + file + " to " + to + " folder.");
		    outputStream.writeBytes("cp " + from + " " + file + " " + to + "\n");
		    outputStream.flush();

			Logs.infoLog("Root.copyFromUserAppToSystemApp: Aplying exec permition to " + file + " in " + to + " folder.");
		    outputStream.writeBytes("chmod 755 " + to + "/" + file + "\n");
		    outputStream.flush();

			Logs.infoLog("Root.copyFromUserAppToSystemApp: Removing " + file + " from " + from + " folder.");
		    outputStream.writeBytes("rm " + from + "/" + file + "\n");
		    outputStream.flush();

			Logs.infoLog("Root.copyFromUserAppToSystemApp: Remounting system folder as readonly.");
		    outputStream.writeBytes("mount -o remount,ro -t ext4 /ennc@android /system\n");
		    outputStream.flush();

		    su.waitFor();
			return true;
		} catch (Exception e) {
			Logs.errorLog("Root.copyFromUserAppToSystemApp: error.", e);
			return false;
		}		
	}
	
	public static boolean reboot() {
		try {
		    Logs.infoLog("Root.reboot: su.");
		    Process su = Runtime.getRuntime().exec("su");
		    DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
	    	
		    Logs.infoLog("Root.reboot: Restarting device.");
	    	outputStream.writeBytes("reboot\n");
	    	outputStream.flush();
		    
	    	su.waitFor();

		    return true;
		} catch (Exception e) {
			Logs.errorLog("Root.reboot: error.", e);
			return false;
		}		
	}

	public static boolean isRooted(Context context) {
		try {
            boolean b = Network.isMobileNetworkAvailable(context);
            if (!Network.setMobileDataEnabled(context, !b)) {
                return false;
            } else {
                Network.setMobileDataEnabled(context, b);

                return true;
            }
		} catch (Exception e) {
			Logs.errorLog("isRooted: error.", e);
			return false;
		}
	}
}
