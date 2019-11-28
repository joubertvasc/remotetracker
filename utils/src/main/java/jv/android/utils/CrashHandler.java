package jv.android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class CrashHandler {

	private static String _debugKey = "";

    public static void configureExceptionHandler(Context context) {
        configureExceptionHandler(context, null, "", true);
    }

    public static void configureExceptionHandler(Context context, final Intent intent) {
        configureExceptionHandler(context, intent, "", true);
	}

    public static void configureExceptionHandler(Context context, String debugKey) {
        configureExceptionHandler(context, null, debugKey, true);
    }

    public static void configureExceptionHandler(Context context, boolean forceExit) {
        configureExceptionHandler(context, null, "", forceExit);
    }

    public static void configureExceptionHandler(Context context, Intent intent, boolean forceExit) {
        configureExceptionHandler(context, intent, "", forceExit);
    }

    public static void configureExceptionHandler(Context context, String debugKey, boolean forceExit) {
        configureExceptionHandler(context, null, debugKey, forceExit);
    }

    public static void configureExceptionHandler(final Context context, final Intent intent, final String debugKey, final boolean forceExit) {
        _debugKey = debugKey;

        if (context != null) {
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                public void uncaughtException(Thread thread, Throwable ex) {
                    if (!debugKey.equals("")) {
                        DebugLog.errorLog(debugKey, Log.getStackTraceString(ex));
                    }

                    saveCrash(context, Log.getStackTraceString(ex));

                    if (intent != null) {
                        PendingIntent vIntent = PendingIntent.getActivity(context, 0, new Intent(intent), intent.getFlags());

                        AlarmManager vAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        vAlarmMgr.set(AlarmManager.RTC,	System.currentTimeMillis() + 2000, vIntent);
                    }

                    if (forceExit) {
                        System.exit(2);
                    }
                }
            });
        }
    }

    private static void saveCrash(Context context, String crash) {
		if (context != null) {
			PhoneUtils pu = new PhoneUtils(context);
			String imei = pu.getIMEI();
			String dateTime = DateTime.currentDateTime();

			File report = new File(Environment.getExternalStorageDirectory() + "/" + imei + "-" + dateTime.replace("/", "-").replace(":", "-") + "_crashreport.txt");

			if (report.exists()) 
				report.delete();

			FileOutputStream _out;
			try {
				_out = new FileOutputStream(report, true);
				try	{
					_out.write (crash.getBytes());
				} finally {
					_out.flush();
					_out.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
