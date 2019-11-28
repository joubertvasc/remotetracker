package jv.android.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;


/**
 * Created by joubert on 11/03/2017.
 */

public class SettingsScreen {

    protected static void _showSettingScreen(Context context, String intentStr) {
        try {
            Intent intent = new Intent(intentStr);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showHomeScreen(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void showSettingScreen(Context context) {
        _showSettingScreen(context, "android.settings.SETTINGS");
    }

    public static void showAPNScreen(Context context) {
        _showSettingScreen(context, "android.settings.APN_SETTINGS");
    }

    public static void showLocationScreen(Context context) {
        _showSettingScreen(context, "android.settings.LOCATION_SOURCE_SETTINGS");
    }

    public static void showSecurityScreen(Context context) {
        _showSettingScreen(context, "android.settings.SECURITY_SETTINGS");
    }

    public static void showWifiScreen(Context context) {
        _showSettingScreen(context, "android.settings.WIFI_SETTINGS");
    }

    public static void showBluetoothScreen(Context context) {
        _showSettingScreen(context, "android.settings.BLUETOOTH_SETTINGS");
    }

    public static void showDateScreen(Context context) {
        _showSettingScreen(context, "android.settings.DATE_SETTINGS");
    }

    public static void showSoundScreen(Context context) {
        _showSettingScreen(context, "android.settings.SOUND_SETTINGS");
    }

    public static void showDisplayScreen(Context context) {
        _showSettingScreen(context, "android.settings.DISPLAY_SETTINGS");
    }

    public static void showApplicationScreen(Context context) {
        _showSettingScreen(context, "android.settings.APPLICATION_SETTINGS");
    }

    public static void showNetworkSettingScreen(Context context) {
        showDataRoamingScreen(context);
    }

    public static void showNetworkOperatorScreen(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            _showSettingScreen(context, "android.settings.NETWORK_OPERATOR_SETTINGS");
        } else {
            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void showSettingsScreen(Context context) {
        _showSettingScreen(context, android.provider.Settings.ACTION_SETTINGS);
    }

    public static void showDataRoamingScreen(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            _showSettingScreen(context, "android.settings.DATA_ROAMING_SETTINGS");
        } else {
            Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
            ComponentName cName = new ComponentName("com.android.phone", "com.android.phone.Settings");
            intent.setComponent(cName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void showDataMobileScreen(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);//android.provider.Settings.ACTION_SETTINGS //Intent.ACTION_MAIN
            intent.setClassName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            showDataRoamingScreen(context);
        }
    }

    public static void showNotificationScreen(Context context) {
        _showSettingScreen(context, "android.settings.NOTIFICATION_SETTINGS");
    }

    public static void showBatterySaverScreen(Context context) {
        _showSettingScreen(context, "android.settings.BATTERY_SAVER_SETTINGS");
    }

    public static void showNfcScreen(Context context) {
        _showSettingScreen(context, "android.settings.NFC_SETTINGS");
    }

    public static void showInternalStorageScreen(Context context) {
        _showSettingScreen(context, "android.settings.INTERNAL_STORAGE_SETTINGS");
    }

    public static void showDictionarySettingScreen(Context context) {
        _showSettingScreen(context, "android.settings.USER_DICTIONARY_SETTINGS");
    }

    public static void showManageApplicationsScreen(Context context) {
        _showSettingScreen(context, "android.settings.MANAGE_APPLICATIONS_SETTINGS");
    }

    public static void showManageAllApplicationsScreen(Context context) {
        _showSettingScreen(context, "android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS");
    }

    public static void showMemoryCardScreen(Context context) {
        _showSettingScreen(context, "android.settings.MEMORY_CARD_SETTINGS");
    }

    public static void showAirPlaneScreen(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (DeviceUtils.deviceBrand().equalsIgnoreCase("Lenovo")) {
                showSettingScreen(context);
            } else {
                _showSettingScreen(context, "android.settings.WIRELESS_SETTINGS");
            }
        } else {
            _showSettingScreen(context, "android.settings.AIRPLANE_MODE_SETTINGS");
        }
    }

    public static void showWirelessScreen(Context context) {
        _showSettingScreen(context, "android.settings.WIRELESS_SETTINGS");
    }

    public static void showWifiScreenSafe(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
