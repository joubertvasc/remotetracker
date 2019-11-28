package jv.android.utils;

import java.util.Locale;

import android.os.Build;

public class DeviceUtils {

	public static int androidLevel() {
		return Build.VERSION.SDK_INT;
	}
	
	public static String androidVersion() {
		return Build.VERSION.RELEASE;
	}

	public static String androidCodename() {
		return Build.VERSION.CODENAME;
	}
	
	public static String kernelVersion() {
		return System.getProperty("os.version");
	}
	
	public static String deviceName() {
		return Build.DEVICE;
	}
	
	public static String deviceManufacturer() {
		return Build.MANUFACTURER;
	}
	
	public static String deviceModel() {
		return Build.MODEL;
	}
	
	public static String deviceBrand() {
		return Build.BRAND;
	}
	
	public static String deviceProduct() {
		return Build.PRODUCT;
	}
	
	public static boolean isEmmulator() {
		return Build.FINGERPRINT.toLowerCase(Locale.getDefault()).contains("generic");
	}
}
