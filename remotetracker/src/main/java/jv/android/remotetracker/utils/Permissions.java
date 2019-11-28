package jv.android.remotetracker.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by joubertvasc on 18/11/2016.
 */

public class Permissions {

    public static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
    };

    public static final String[] INITIAL_PERMS_CONTROLLER = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int INITIAL_REQUEST = 1337;
    public static final int INITIAL_REQUEST_CONTROLLER = 1338;

    public static boolean canReadExternalStorage(Context context) {
        return(hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    public static boolean canWriteExternalStorage(Context context) {
        return(hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    public static boolean canReadPhoneState(Context context) {
        return(hasPermission(context, Manifest.permission.READ_PHONE_STATE));
    }

    public static boolean canAccessFineLocation(Context context) {
        return(hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION));
    }

    public static boolean canAccessCoarseLocation(Context context) {
        return(hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    public static boolean canAccessCamera(Context context) {
        return(hasPermission(context, Manifest.permission.CAMERA));
    }

    public static boolean canReadContacts(Context context) {
        return(hasPermission(context, Manifest.permission.READ_CONTACTS));
    }

    public static boolean canWriteContacts(Context context) {
        return(hasPermission(context, Manifest.permission.WRITE_CONTACTS));
    }

    public static boolean canReadSMS(Context context) {
        return(hasPermission(context, Manifest.permission.READ_SMS));
    }

    public static boolean canSendSMS(Context context) {
        return(hasPermission(context, Manifest.permission.SEND_SMS));
    }

    public static boolean canReceiveSMS(Context context) {
        return(hasPermission(context, Manifest.permission.RECEIVE_SMS));
    }

    public static boolean hasPermission(Context context, String perm) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
            return (PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(perm));
        else
            return true;
    }
}
