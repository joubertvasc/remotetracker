package jv.android.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import jv.android.utils.log.SqlLog;

public class DebugLog {

	private static List<DebugInfo> debugs;

	public static boolean startLog(Context context, String key, String fileName) {
		if (debugs == null) 
			debugs = new ArrayList<DebugInfo>();

		int index = findKey(key);

		if (index == -1) {
			DebugInfo di = new DebugInfo();
			di.active = true;
			di.fileName = (fileName.contains("/") ? fileName : Environment.getExternalStorageDirectory() + "/" + fileName);
			di.key = key;
			di.file = new File(di.fileName);
			debugs.add(di);

			if (di.file.exists())
				di.file.delete();
			
			writeHeader(context, di.key);
		} else {
			debugs.get(index).active = true;
		}

		// Adaptacao ao log remoto
 		SqlLog.start(context);

		return true;
	}

	public static boolean startLogWithEasterEgg(Context context, String key, String fileName) {
		if (Debugging.isDebugging()) {		
			return startLog(context, key, fileName);
		} else {
			return false;
		}
	}

	public static void stopLog(String key) {
		int index = findKey(key);

		if (index > -1) {
			debugs.get(index).active = false;
		}			
	}

	public static void errorLog (String key, String message) {
		errorLog(key, message, null);		
	}

	public static void errorLog (String key, String message, Throwable e) {
		addLog (key, message, e, "e");

		// Adaptacao ao log remoto
		SqlLog.eLog(key, message, e);
	}

	public static void infoLog (String key, String message) {
		infoLog(key, message, null);		
	}

	public static void infoLog (String key, String message, Throwable e) {
		addLog (key, message, e, "i");

		// Adaptacao ao log remoto
		SqlLog.iLog(key, message, e);
	}

	public static void warningLog (String key, String message) {
		warningLog(key, message, null);		
	}

	public static void warningLog (String key, String message, Throwable e) {
		addLog (key, message, e, "w");

		// Adaptacao ao log remoto
		SqlLog.wLog(key, message, e);
	}

	private static int findKey(String key) {
		if (debugs == null) 
			return -1;
		else
			for (int i = 0; i < debugs.size(); i++) {
				if (debugs.get(i).key.trim().toLowerCase(Locale.getDefault()).equals(key.trim().toLowerCase(Locale.getDefault()))) {
					return i;
				}
			}

		return -1;
	}

	private static void writeHeader(Context context, String key) {
		int index = findKey(key);

		if (context != null && index > -1 && debugs.get(index).active) { 
			try {
				PackageManager pm = context.getPackageManager();
				PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), 0);
				ApplicationInfo ai;

				pinfo = pm.getPackageInfo(context.getPackageName(), 0);

				ai = pm.getApplicationInfo(pinfo.packageName, 0);
				String appName = (String)pm.getApplicationLabel(ai);

				String versionName = pinfo.versionName;
				String versionCode = String.valueOf(pinfo.versionCode);

				infoLog(key, "Application: " + appName);
				infoLog(key, "Package: " + pinfo.packageName);
				infoLog(key, "Version Code: " + versionCode);
				infoLog(key, "Version Name: " + versionName);
				infoLog(key, "Android Version: " + DeviceUtils.androidVersion());
				infoLog(key, "Android Codename: " + DeviceUtils.androidCodename());
				infoLog(key, "Kernel Version: " + DeviceUtils.kernelVersion());
				infoLog(key, "Device Manufacturer: " + DeviceUtils.deviceManufacturer());
				infoLog(key, "Device Model: " + DeviceUtils.deviceModel());
				infoLog(key, "Device Name: " + DeviceUtils.deviceName());
				infoLog(key, "Device Product: " + DeviceUtils.deviceProduct());
				infoLog(key, "Device Brand: " + DeviceUtils.deviceBrand());
			} catch (Exception e) {
				errorLog(key, "Error writing header informations.", e);
			}

		}
	}

	private static void addLog (String key, String message, Throwable e, String type) {
		int index = findKey(key);

		if (index > -1 && debugs.get(index).active) { 
			FileOutputStream _out;

			Date data = new Date();
			data = Calendar.getInstance().getTime();

			String s = Format.formatDateTime(data) + " - ";

			if (type == "e") { 
				s += "error";

				try { Log.e(key, message, e); } catch (Exception ex) {}
			} else if (type == "i") {
				s += "information";
				
				try { Log.i(key, message); } catch (Exception ex) {}
			} else {
				s += "warning";
				
				try { Log.w(key, message); } catch (Exception ex) {}
			}
			
			s += " - " + message;

			try {
				if (e != null && !e.getMessage().equals(""))
					s += " - " + e.getMessage();

				String stackTrace = Log.getStackTraceString(e);

				s += "\n" + stackTrace + "\n";
			} catch (Exception e2) {
				s += " - " + e2.getMessage();
			}

			try	{
				_out = new FileOutputStream(debugs.get(index).file, true);
				try	{
					_out.write (s.getBytes());
				} finally {
					_out.flush();
					_out.close();
				}
			} catch (Exception ex) {
				Log.e(key, "Error writting log", ex);
			}
		}
	}

	public static class DebugInfo {

		public String key;
		public String fileName;
		public File file;
		public boolean active;	
	}
}
