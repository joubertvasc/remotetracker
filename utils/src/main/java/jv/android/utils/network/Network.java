package jv.android.utils.network;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.net.SocketException;
import java.util.List;

import jv.android.utils.Logs;

public class Network {

    public static final String MOBILE_2G = "2G";
    public static final String MOBILE_3G = "3G";
    public static final String MOBILE_4G = "4G";
    public static final String MOBILE_DESCONHECIDO = "Desconhecido";

    public static boolean isNetworkAvailable(Context context) {
        boolean HaveConnectedWifi = false;
        boolean HaveConnectedMobile = false;

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();

            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected()) {
                        HaveConnectedWifi = true;
                        break;
                    }

                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected()) {
                        HaveConnectedMobile = true;
                        break;
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HaveConnectedWifi || HaveConnectedMobile;
    }

    public static boolean isMobileNetworkAvailable(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();

            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.getState() == NetworkInfo.State.CONNECTED)
                        return true;
            }
        } else {
            try {
                TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");

                if (null != getMobileDataEnabledMethod) {
                    boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);

                    return mobileDataEnabled;
                } else {
                    return false;
                }
            } catch (Exception ex) {
                return false;
            }
        }

        return false;
    }

    public static boolean isWifiNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.getState() == NetworkInfo.State.CONNECTED)
                    return true;
        }

        return false;
    }

    public static void openNetworkPanel(Context context) {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);

        if (intent != null) {
            context.startActivity(intent);
        }
    }

    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT <= 24) {
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public static boolean setWifiDataEnabled(Context context, boolean enabled) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(enabled);

            return true;
        } catch (Exception e) {
            Logs.errorLog("Network.setWifiDataEnabled error", e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean setMobileDataEnabled(Context context, boolean enabled) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                @SuppressWarnings("rawtypes")
                final Class conmanClass = Class.forName(conman.getClass().getName());
                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                iConnectivityManagerField.setAccessible(true);
                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                @SuppressWarnings("rawtypes")
                final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);

                setMobileDataEnabledMethod.setAccessible(true);

                setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);

                return true;
            } catch (Exception e) {
                Logs.errorLog("Network.setMobileDataEnabled error", e);
                return false;
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager dataManager;
            dataManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method dataMtd = null;
            try {
                dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            } catch (NoSuchMethodException e) {
                Logs.errorLog("Network.setMobileDataEnabled error", e);
                return false;
            }

            dataMtd.setAccessible(true);
            try {
                dataMtd.invoke(dataManager, enabled);
                return true;
            } catch (IllegalArgumentException e) {
                Logs.errorLog("Network.setMobileDataEnabled error", e);
                return false;
            } catch (IllegalAccessException e) {
                Logs.errorLog("Network.setMobileDataEnabled error", e);
                return false;
            } catch (InvocationTargetException e) {
                Logs.errorLog("Network.setMobileDataEnabled error", e);
                return false;
            }
        } else {
            try {
                TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

                if (null != setMobileDataEnabledMethod) {
                    setMobileDataEnabledMethod.invoke(telephonyService, enabled);
                    return true;
                } else {
                    return false;
                }
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public static void turnNetworkOn(Context context, boolean mobile, boolean wifi) {
        if (mobile) {
            Logs.infoLog("Enabling Mobile Network");
            Network.setMobileDataEnabled(context, true);
        }

        if (wifi) {
            Logs.infoLog("Enabling WiFi Network");
            Network.setWifiDataEnabled(context, true);
        }

        if (mobile || wifi) {
            try {
                Logs.infoLog("Sleeping to give time to networks wakeup");
                Thread.sleep(10000);
            } catch (Exception e) {
                // Ignores.
            }

            if (Network.isNetworkAvailable(context))
                Logs.infoLog("Network is now enabled");
            else
                Logs.warningLog("Failed to enable Network");
        }
    }

    public static String getAndroidIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Logs.errorLog("externalip: " + ex.toString());
        }

        return null;
    }

    public static boolean isSystemInFlighMode(Context context) {
        try {
            if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.Global.getString(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON).equals("1");
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean setFlightMode(Context context, boolean turnOn) {
        try {
            Settings.Global.putString(context.getContentResolver(), "airplane_mode_on", (turnOn ? "1" : "0"));

            return true;
        } catch (Exception e) {
            Logs.errorLog("Network.setFlightMode error", e);
            return false;
        }
    }

    public static String getAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());

                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions

        return "";
    }

    public static String getWifiMacAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();

            return wInfo.getMacAddress();
        } catch (Exception e) {
            Logs.errorLog("Network.getWifiMacAddress error", e);
            return "";
        }
    }

    public static boolean executePing(String address) {
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 " + address);
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue " + mExitValue);

            return (mExitValue == 0);
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
        }

        return false;
    }

    public static String getNetworkClass(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return MOBILE_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return MOBILE_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return MOBILE_4G;
            default:
                return MOBILE_DESCONHECIDO;
        }
    }
}
