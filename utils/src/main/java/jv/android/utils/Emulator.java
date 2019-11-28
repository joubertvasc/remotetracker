package jv.android.utils;

import android.os.Build;

public class Emulator {

	public static boolean isEmulator() {
		return Build.PRODUCT.equals("sdk") || Build.PRODUCT.equals("google_sdk");
	}
}
