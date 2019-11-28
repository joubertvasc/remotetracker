package jv.android.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.content.Context;

import jv.android.utils.log.SqlLog;

public class Logs {

	private static String fileName;
	private static String CATEG;
	private static boolean on = false;

	public static void stop() {
		infoLog("Log stopped");

		on = false;
	}

	private static void startLog(String categ, String name) {
		Log.i(CATEG, "Log started: " + categ + "| " + name);

		fileName = name;
		CATEG = categ;
		on = true;
	}

	public static void startLogWithEasterEgg(Context context, String categ, String name) {
		if (Debugging.isDebugging()) {
			startLog (categ, name);

			try {
				PackageManager pm = context.getPackageManager();
				PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), 0);
				ApplicationInfo ai;

				pinfo = pm.getPackageInfo(context.getPackageName(), 0);

				ai = pm.getApplicationInfo(pinfo.packageName, 0);
				String appName = (String)pm.getApplicationLabel(ai);

				String versionName = pinfo.versionName;
				String versionCode = String.valueOf(pinfo.versionCode);

				infoLog("Application: " + appName);
				infoLog("Package: " + pinfo.packageName);
				infoLog("Version Code: " + versionCode);
				infoLog("Version Name: " + versionName);
				infoLog("Android Version: " + DeviceUtils.androidVersion());
				infoLog("Android Codename: " + DeviceUtils.androidCodename());
				infoLog("Kernel Version: " + DeviceUtils.kernelVersion());
				infoLog("Device Manufacturer: " + DeviceUtils.deviceManufacturer());
				infoLog("Device Model: " + DeviceUtils.deviceModel());
				infoLog("Device Name: " + DeviceUtils.deviceName());
				infoLog("Device Product: " + DeviceUtils.deviceProduct());
				infoLog("Device Brand: " + DeviceUtils.deviceBrand());

				// Adaptacao ao log remoto
				SqlLog.start(context);
			} catch (Exception e) {
				// Who cares?
			}
		}
	}

	public static void startLog(Context context, String categ, String name) {
		startLog (categ, name);

		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), 0);
			ApplicationInfo ai;

			pinfo = pm.getPackageInfo(context.getPackageName(), 0);

			ai = pm.getApplicationInfo(pinfo.packageName, 0);
			String appName = (String)pm.getApplicationLabel(ai);

			String versionName = pinfo.versionName;
			String versionCode = String.valueOf(pinfo.versionCode);

			infoLog("Application: " + appName);
			infoLog("Package: " + pinfo.packageName);
			infoLog("Version Code: " + versionCode);
			infoLog("Version Name: " + versionName);
			infoLog("Android Version: " + DeviceUtils.androidVersion());
			infoLog("Android Codename: " + DeviceUtils.androidCodename());
			infoLog("Kernel Version: " + DeviceUtils.kernelVersion());
			infoLog("Device Manufacturer: " + DeviceUtils.deviceManufacturer());
			infoLog("Device Model: " + DeviceUtils.deviceModel());
			infoLog("Device Name: " + DeviceUtils.deviceName());
			infoLog("Device Product: " + DeviceUtils.deviceProduct());
			infoLog("Device Brand: " + DeviceUtils.deviceBrand());

			// Adaptacao ao log remoto
			SqlLog.start(context);
		} catch (Exception e) {
			// Who cares?
		}
	}

	public static void errorLog (String message) {
		errorLog(message, null);		
	}

	public static void errorLog (String message, Throwable e) {
		addLog (message, e, "e");

		if (on) {
			try {
				Log.e(CATEG, message, e);
			} catch (Exception ex) {

			}
		}

		// Adaptacao ao log remoto
		SqlLog.eLog(CATEG, message, e);
	}

	public static void infoLog (String message) {
		infoLog(message, null);		
	}

	public static void infoLog (String message, Throwable e) {
		addLog (message, e, "i");

		if (on) {
			try {
				Log.i(CATEG, message, e);
			} catch (Exception ex) {

			}
		}

		// Adaptacao ao log remoto
		SqlLog.iLog(CATEG, message, e);
	}

	public static void warningLog (String message) {
		warningLog(message, null);		
	}

	public static void warningLog (String message, Throwable e) {
		addLog (message, e, "w");

		if (on) {
			try {
				Log.w(CATEG, message, e);
			} catch (Exception ex) {

			}
		}

		// Adaptacao ao log remoto
		SqlLog.wLog(CATEG, message, e);
	}

	private static void addLog (String message, Throwable e, String type) {
		if (on) {
			File _root;
			FileOutputStream _out;

			Date data = new Date();
			data = Calendar.getInstance().getTime();

			String s = Format.formatDateTime(data) + " - ";

			if (type == "e") 
				s += "error";
			else if (type == "i")
				s += "information";
			else
				s += "warning";

			s += " - " + CATEG + " - " + message;

			try {
				if (e != null && !e.getMessage().equals(""))
					s += " - " + e.getMessage();

				String stackTrace = Log.getStackTraceString(e);

				s += "\n" + stackTrace + "\n";
			} catch (Exception e2) {
				s += " - " + e2.getMessage();
			}

			_root = Environment.getExternalStorageDirectory();
			File file = new File (_root, fileName);

			try
			{
				_out = new FileOutputStream(file, true);
				try 
				{
					_out.write (s.getBytes());
				}
				finally {
					_out.flush();
					_out.close();
				}
			} catch (Exception ex) {
				Log.e(CATEG, "Error writting log", ex);
			}
		}
	}
}
