package jv.android.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.os.Build;

public class NotificationPanel {
	
	/*
	   For opening and closing the notification-bar programmatically. Here is an example for closing.
	   <uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />
	   <uses-permission android:name="android.permission.STATUS_BAR"/>
       <uses-permission android:name="android.permission.DISABLE_STATUS_BAR"/> 
	   It might be an unofficial info.
	   http://stackoverflow.com/questions/5029354/how-can-i-programmatically-open-close-notifications-in-android 
	   http://stackoverflow.com/questions/13766789/android-how-to-collapse-status-bar-on-android-4-2
	 */

	public static void disable(Context context) {
		Object service = context.getSystemService ("statusbar");
		try {
			Class <?> statusBarManager = Class.forName ("Android.app.StatusBarManager");
			Method expand = statusBarManager.getMethod ("disable", int.class);
			expand.invoke (service, 0x00000001);
		} catch (Exception e) {
			e.printStackTrace ();
		} 
	}
	
	public static void collapsePanel(Context _context) {
		try {
			Object sbservice = _context.getSystemService("statusbar");
			Class<?> statusbarManager;
			statusbarManager = Class.forName("android.app.StatusBarManager");
			Method showsb;
			if (Build.VERSION.SDK_INT >= 17) {
				showsb = statusbarManager.getMethod("collapsePanels");
			} else {
				showsb = statusbarManager.getMethod("collapse");
			}
			showsb.invoke(sbservice);
		} catch (ClassNotFoundException _e) {
			_e.printStackTrace();
		} catch (NoSuchMethodException _e) {
			_e.printStackTrace();
		} catch (IllegalArgumentException _e) {
			_e.printStackTrace();
		} catch (IllegalAccessException _e) {
			_e.printStackTrace();
		} catch (InvocationTargetException _e) {
			_e.printStackTrace();
		}
	}

	public static void expandPanel(Context _context) {
		try {
			Object sbservice = _context.getSystemService("statusbar");
			Class<?> statusbarManager;
			statusbarManager = Class.forName("android.app.StatusBarManager");
			Method showsb;
			if (Build.VERSION.SDK_INT >= 17) {
				showsb = statusbarManager.getMethod("expandPanels");
			} else {
				showsb = statusbarManager.getMethod("expand");
			}
			showsb.invoke(sbservice);
		} catch (ClassNotFoundException _e) {
			_e.printStackTrace();
		} catch (NoSuchMethodException _e) {
			_e.printStackTrace();
		} catch (IllegalArgumentException _e) {
			_e.printStackTrace();
		} catch (IllegalAccessException _e) {
			_e.printStackTrace();
		} catch (InvocationTargetException _e) {
			_e.printStackTrace();
		}
	}

}
