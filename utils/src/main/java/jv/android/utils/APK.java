package jv.android.utils;

import java.net.URL;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.net.Uri;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

public class APK {

	public static boolean downloadFile (Context context, String address, String filename) {
		return downloadAPK (context, address, filename, false);
	}
	
	public static boolean downloadAPK (Context context, String address, String filename, boolean install) {
		try {
			Logs.infoLog("APK.downloadAPK started. Trying to download from " + address + " to " + filename);
			
            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            
            URL url = new URL(address);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            Logs.infoLog("Connected. Starting download.");
            
            File outputFile = new File(file, filename);
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            Logs.infoLog("Downloaded. Starting saving file.");
            
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }

            fos.close();
            is.close();

            Logs.infoLog("Saved.");
            
            if (install) {
            	// Try to install the downloaded file (APK?).
            	return installAPK (context, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + filename)));
            } else {
                return true;
            }
        } catch (IOException e) {
            Logs.errorLog("downloadAPK error.", e);

            return false;
        }	
	}
	
	public static boolean installAPK (Context context, Uri filename) {
		try {
			Logs.infoLog("APK.installAPK started. Trying to install from " + filename);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(filename, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);  
            
            Logs.infoLog(filename + " successfully installed.");
            
			return true;
		} catch (Exception e) {
            Logs.errorLog("installAPK error.", e);

            return false;
		}
	}

	public static String getAppName(Context context, String packageName) {
		try {
			PackageManager pm = context.getPackageManager();
			CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
			
			return c.toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			
			return null;
		}	
	}
	
	public static Drawable getIcon(Context context, String packageName) {
		try {
			return context.getPackageManager().getApplicationIcon(packageName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			
			return null;
		}	
	}
}
