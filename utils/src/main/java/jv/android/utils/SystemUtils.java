package jv.android.utils;

import android.content.Context;
import android.content.Intent;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.util.ArrayList;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;

import java.util.Iterator;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;

public class SystemUtils {

	private Context context;

	public SystemUtils(Context context) {
		this.context = context;
	}

	public class PInfo {
		private String appname = "";
		private String pname = "";
		private String versionName = "";
		private int versionCode = 0;
		private Drawable icon;
		private ApplicationInfo applicationInfo;

		public String getAppname() {
			return appname;
		}
		public void setAppname(String appname) {
			this.appname = appname;
		}
		public String getPname() {
			return pname;
		}
		public void setPname(String pname) {
			this.pname = pname;
		}
		public String getVersionName() {
			return versionName;
		}
		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}
		public int getVersionCode() {
			return versionCode;
		}
		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}
		public Drawable getIcon() {
			return icon;
		}
		public void setIcon(Drawable icon) {
			this.icon = icon;
		}

		public ApplicationInfo getApplicationInfo() {
			return this.applicationInfo;
		}

		public void setApplicationInfo (ApplicationInfo applicationInfo) {
			this.applicationInfo = applicationInfo;
		}
	}

	public String getAppLabel(String pathToApk) {
		String result = null;

		if (pathToApk != null && !pathToApk.trim().equals("")) {
			PackageManager pm = context.getPackageManager();

			if (pm != null) {
				PackageInfo pi = pm.getPackageArchiveInfo(pathToApk, 0);

				if (pi != null) {
					// the secret are these two lines....
					pi.applicationInfo.sourceDir       = pathToApk;
					pi.applicationInfo.publicSourceDir = pathToApk;
					//

					result = (String)pi.applicationInfo.loadLabel(pm);
				}
			}
		}

		return result;
	}

	public PInfo findApplication (String appToFind) {
		boolean podeSair = false;
		int tentativas = 0;
		PInfo app = null;

		do {
			try {
				List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);

				for (int i=0;i<packs.size();i++) {
					PackageInfo p = packs.get(i);

					String appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
					String packName = p.packageName;

					if (appName.trim().equalsIgnoreCase(appToFind.trim()) || packName.trim().equalsIgnoreCase(appToFind.trim())) {
						app = new PInfo();
						app.appname = appName;
						app.pname = packName;
						app.versionName = p.versionName;
						app.versionCode = p.versionCode;
						app.icon = p.applicationInfo.loadIcon(context.getPackageManager());
						app.applicationInfo = p.applicationInfo;

						break;
					}
				}

				podeSair = true;
			} catch (OutOfMemoryError ex) {
				tentativas++;
				Logs.errorLog("AtmLauncherDataHelper.getControle: erro de falta de memória. Tentativa " + String.valueOf(tentativas), ex);
				podeSair = false;
				System.gc();
				ThreadUtils.wait(1);
			}
		} while (!podeSair && tentativas < 3);

		return app;
	}

	public ArrayList<PInfo> getPackages(boolean includeSystemPackages) {
		ArrayList<PInfo> apps = getInstalledApps(includeSystemPackages);

		return apps;
	}

	public boolean isPackageInstalled(String pkg) {
		List<PInfo> apps = getInstalledApps(true);

		for (int i = 0; i < apps.size(); i++) {
			if (apps.get(i).getPname().trim().equalsIgnoreCase(pkg.trim())) {
				return true;
			}
		}

		return false;
	}

	private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
		ArrayList<PInfo> res = new ArrayList<PInfo>();        
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
		for(int i=0;i<packs.size();i++) {
			PackageInfo p = packs.get(i);

			if ((!getSysPackages) && (isSystemPackage (p))) {
				continue ;
			}

			PInfo newInfo = new PInfo();
			newInfo.appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
			newInfo.pname = p.packageName;
			newInfo.versionName = p.versionName;
			newInfo.versionCode = p.versionCode;
			newInfo.icon = p.applicationInfo.loadIcon(context.getPackageManager());
			newInfo.applicationInfo = p.applicationInfo;
			res.add(newInfo);
		}

		return res; 
	}

	public boolean isSystemPackage(PackageInfo pi) {
		return ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
	}

	public void startApplication(String application) {
		startApplication(application, "");
	}

	public void startApplication(String application, String params) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(application);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		if (params != null && !params.equals("")) {
			String[] par = params.split(";");

			if (par.length > 0) {
				for (int i = 0; i < par.length; i++) {
					String[] p = par[i].split("=");

					if (p.length == 2) {
						intent.putExtra(p[0], p[1]);
					}
				}				
			}			
		}		

		context.startActivity(intent);
	}

	public void masterReset() {
		try {
			Context foreignContext = context.createPackageContext("com.android.settings", Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);

			Class<?> yourClass = foreignContext.getClassLoader().loadClass("com.android.settings.MasterClear");
			Intent intent = new Intent(foreignContext, yourClass);
			context.startActivity(intent);
		} catch (Exception e) {
			Logs.errorLog("SystemUtils.masterReset error", e);
		}
	}

	// Only works on rooted devices.
	public void reset() {
		try {
			Runtime.getRuntime().exec("su");
			Runtime.getRuntime().exec("reboot"); }
		catch (Exception e) { 
			Logs.errorLog("SystemUtils.reset error", e);
		}
	}

	public static List<ActivityManager.RunningAppProcessInfo> getRunningProcess(Context context) {
		// Get currently running application processes
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();

		return l;
	}

	public static boolean appIsRunning (Context context, String app) {
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

		if (manager != null) {
			for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
				if (service != null && app != null && app.toLowerCase().trim().equals(service.service.getClassName().toLowerCase().trim())) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean killProcess (Context context, String app) {
		List<ActivityManager.RunningAppProcessInfo> l = SystemUtils.getRunningProcess(context);

		Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
		PackageManager pm = context.getPackageManager();

		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
			try {
				CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));

				if (c.toString().toLowerCase().trim().equals(app.toLowerCase().trim())) {
					android.os.Process.killProcess(info.pid);
					return true;
				}
			} catch (Exception e) {
				Logs.errorLog("KillProcesso error.", e);			    
			}
		}

		return false;
	}

	public static boolean killBackgroundProcess (Context context, String packageToKill) {
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		am.killBackgroundProcesses(packageToKill);

		return true;
	}

	@SuppressWarnings("deprecation")
	public static float externalMemoryAvailable() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
		return bytesAvailable / (1024.f * 1024.f);
	}	 

	@SuppressWarnings("deprecation")
	public static float externalMemoryTotal() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getBlockCount();
		return bytesAvailable / (1024.f * 1024.f);
	}	 

	@SuppressWarnings("deprecation")
	public static float internalMemoryAvailable() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
		return bytesAvailable / (1024.f * 1024.f);
	}	 

	@SuppressWarnings("deprecation")
	public static float internalMemoryTotal() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getBlockCount();
		return bytesAvailable / (1024.f * 1024.f);
	}	 

	public static float externalMemoryUsedInPercent() {
		float total = externalMemoryTotal();
		float free = externalMemoryAvailable();
		float used = total - free;

		return used / total * 100;
	}

	public static float internalMemoryUsedInPercent() {
		float total = internalMemoryTotal();
		float free = internalMemoryAvailable();
		float used = total - free;

		return used / total * 100;
	}

	public static boolean isAndroid3() {
		return Build.VERSION.SDK_INT > 11;
	}

	public static boolean isAndroid4() {
		return Build.VERSION.SDK_INT > 14;
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isTablet3(Context context) {
		return isTablet(context) && isAndroid3();
	}

	public static boolean isTablet4(Context context) {
		return isTablet(context) && isAndroid4();
	}	

	public static String getVersao(Context context) {
		String versao = "";
		PackageManager pm = context.getPackageManager();
		PackageInfo pinfo;
		try {
			pinfo = pm.getPackageInfo(context.getPackageName(), 0);
			versao = pinfo.versionName;
		} catch (NameNotFoundException e1) {
		}

		return versao;
	}

	public static void addShortcut(final Context context, Class<?> activityClass, String name, String shortcut, int drawableIcon) {
	    //Adding shortcut for MainActivity 
	    //on Home screen
	    Intent shortcutIntent = new Intent(context, activityClass);
	    
	    shortcutIntent.setAction(Intent.ACTION_MAIN);

	    Intent addIntent = new Intent();
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, drawableIcon));

	    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	    addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
	    context.sendBroadcast(addIntent);
	}
	
	public static void createWebShortcut(Context context, String urlStr, String linkName, int drawableIcon) {
//	    String urlStr = String.format(context.getString(R.string.homescreen_shortcut_search_url), context.getString(R.string.app_id));
	    Intent shortcutIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr));
	    // shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	    Intent intent = new Intent();
	    intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	    // Sets the custom shortcut's title
	    intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, linkName);
	    // Set the custom shortcut icon
	    intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, drawableIcon));
	    intent.putExtra("duplicate", false);

	    // add the shortcut
	    intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	    context.sendBroadcast(intent);
	}

	public static boolean clearApplicationDataOrCache(String packageName, boolean onlyCache) {
		try{
			Logs.infoLog("SystemUtils.clearApplicationDataOrCache: Tentando acesso a superusuário.");
			Process su = Runtime.getRuntime().exec("su");
			DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            String path = "/data/data/" + packageName + (onlyCache ? "/cache" : "");

			Logs.infoLog("SystemUtils.clearApplicationDataOrCache: Acessando diretório da aplicação: " + path);
            outputStream.writeBytes("if [ -d \"" + path + "\" ]; then\n");
            outputStream.writeBytes("cd " + path + "\n");
            outputStream.writeBytes("rm -f -R " + (onlyCache ? "*" : "!(lib)") + "\n");
            outputStream.writeBytes("fi\n");
			outputStream.flush();

            Logs.infoLog("SystemUtils.clearApplicationDataOrCache: waiting for SU");
			su.waitFor();

            Logs.infoLog("SystemUtils.clearApplicationDataOrCache: OK!");
			return true;
		} catch (IOException e) {
			Logs.errorLog("SystemUtils.clearApplicationDataOrCache error.", e);
			return false;
		} catch (InterruptedException e){
			Logs.errorLog("SystemUtils.clearApplicationDataOrCache error.", e);
			return false;
		}
	}

	public static boolean isAppRunningAsSystem(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pinfo;
        try {
            pinfo = pm.getPackageInfo(context.getPackageName(), 0);

            SystemUtils su = new SystemUtils(context);
            return su.isSystemPackage(pinfo);
        } catch (NameNotFoundException e1) {
            return false;
        }
	}
}