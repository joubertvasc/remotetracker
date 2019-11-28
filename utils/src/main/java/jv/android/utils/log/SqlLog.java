package jv.android.utils.log;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import jv.android.utils.Debugging;
import jv.android.utils.DeviceUtils;
import jv.android.utils.PhoneUtils;

/**
 * Created by joubertvasc on 07/02/2018.
 */

public class SqlLog {

    private static Context _context;
    private static boolean _logging = false;


    public static void start(Context context) {
        _context = context;

        if (!_logging) {
            _logging = true;

            writeHeader("START");
        }
    }

    public static void stop() {
        _logging = false;
    }

    private static boolean isLogging() {
        return _logging || Debugging.isDebugging();
    }

    public static Log eLog(String key, String message) {
        return eLog(key, message, null);
    }

    public static Log eLog(String key, String message, Throwable e) {
        return addLog(key, message, e, "e");
    }

    public static Log iLog(String key, String message) {
        return iLog(key, message, null);
    }

    public static Log iLog(String key, String message, Throwable e) {
        return addLog(key, message, e, "i");
    }

    public static Log wLog(String key, String message) {
        return wLog(key, message, null);
    }

    public static Log wLog(String key, String message, Throwable e) {
        return addLog(key, message, e, "w");
    }

    private static void writeHeader(String key) {
        if (_context != null) {
            try {
                PackageManager pm = _context.getPackageManager();
                PackageInfo pinfo = pm.getPackageInfo(_context.getPackageName(), 0);
                ApplicationInfo ai;

                pinfo = pm.getPackageInfo(_context.getPackageName(), 0);

                ai = pm.getApplicationInfo(pinfo.packageName, 0);
                String appName = (String) pm.getApplicationLabel(ai);

                String versionName = pinfo.versionName;
                String versionCode = String.valueOf(pinfo.versionCode);

                iLog(key, "Application: " + appName);
                iLog(key, "Package: " + pinfo.packageName);
                iLog(key, "Version Code: " + versionCode);
                iLog(key, "Version Name: " + versionName);
                iLog(key, "Android Version: " + DeviceUtils.androidVersion());
                iLog(key, "Android Codename: " + DeviceUtils.androidCodename());
                iLog(key, "Kernel Version: " + DeviceUtils.kernelVersion());
                iLog(key, "Device Manufacturer: " + DeviceUtils.deviceManufacturer());
                iLog(key, "Device Model: " + DeviceUtils.deviceModel());
                iLog(key, "Device Name: " + DeviceUtils.deviceName());
                iLog(key, "Device Product: " + DeviceUtils.deviceProduct());
                iLog(key, "Device Brand: " + DeviceUtils.deviceBrand());
            } catch (Exception e) {
                eLog(key, "Error writing header informations.", e);
            }
        }
    }

    private static Log addLog(String key, String message, Throwable e, String type) {
        Log log = new Log();

        if (isLogging() && _context != null) {
            PhoneUtils pu = new PhoneUtils(_context);

            log.setKey(key);
            log.setExtrakey(pu.getIMEI());
            log.setText(message);
            log.setType(type);
            log.setException(e == null ? "" : e.getMessage());

            return addLog(log);
        } else {
            return log;
        }
    }

    public static Log addLog(Log log) {
        if (isLogging() && _context != null) {
            if (log.getExtrakey().trim().equals("")) {
                PhoneUtils pu = new PhoneUtils(_context);
                log.setExtrakey(pu.getIMEI());
            }

            LogDataHelper dh = new LogDataHelper(_context);
            int id = dh.insertLog(log);

            if (id > -1) {
                log.setId(id);
            }
        }

        return log;
    }
}
