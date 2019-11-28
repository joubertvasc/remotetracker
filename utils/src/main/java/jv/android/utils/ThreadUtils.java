package jv.android.utils;

public class ThreadUtils {

	public static void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			
		}
	}

	public static void waitms(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (Exception e) {
			
		}
	}
}
