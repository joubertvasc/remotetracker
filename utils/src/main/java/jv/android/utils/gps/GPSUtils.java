package jv.android.utils.gps;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import com.google.android.gms.maps.model.LatLng;

import jv.android.utils.Logs;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

public class GPSUtils {

	public static boolean isMockSettingsON(Context context) {
		// returns true if mock location enabled, false if not enabled.
		if (Settings.Secure.getString(context.getContentResolver(),	Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
			return false;
		else
			return true;
	}	

	public static Address getAddressFromGeocode(Context context, double latitude, double longitude) {
		Geocoder geocoder;
		List<Address> addresses;

		Address address = null;
		geocoder = new Geocoder(context, Locale.getDefault());
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);

			address = addresses.get(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return address;
	}

	public static LatLng centerPolygon(List<LatLng> points) {
		if (points != null) {
			double[] centroid = { 0.0, 0.0 };

			for (int i = 0; i < points.size(); i++) {
				centroid[0] += points.get(i).latitude;
				centroid[1] += points.get(i).longitude;
			}

			int totalPoints = points.size();
			centroid[0] = centroid[0] / totalPoints;
			centroid[1] = centroid[1] / totalPoints;

			return new LatLng(centroid[0], centroid[1]);
		} else {
			return null;
		}
	}


	public static boolean isGPSActive(Context context) {
		final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );

		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			return false;
		}
		else
		{
			return true;
		}
	}

	public static void openGPSPanel(Context context) {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		context.startActivity(intent);
	}

	public static boolean providerExists(String provider, LocationManager lm) {
		List<String> plist = lm.getAllProviders();
		boolean result = false;
		for (Iterator<String> iterator = plist.iterator(); iterator.hasNext();) {
			String p = iterator.next();
			if(provider.equals(p)) {
				result  = true;
				break;
			}
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	public static boolean turnGPSOn(Context context){
		try {  
			Settings.Secure.putString (context.getContentResolver(),  
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED, String.format ("%s,%s",  
							Settings.Secure.getString (context.getContentResolver(),  
									Settings.Secure.LOCATION_PROVIDERS_ALLOWED), LocationManager.GPS_PROVIDER));  
		} catch(Exception e) {

		}

		try {
			if (android.os.Build.VERSION.SDK_INT > 11) {
				Logs.infoLog("GPS.turnGPSOff, trying to turn on.");
				final Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
				intent.putExtra("enabled", true);
				context.sendBroadcast(intent);

				return true;
			} else {
				String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

				if (!provider.contains("gps")){ //if gps is disabled
					final Intent poke = new Intent();
					poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
					poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
					poke.setData(Uri.parse("3")); 
					context.sendBroadcast(poke);

					return true;
				}
			}

			return false;			
		} catch (Exception e) {
			Logs.errorLog("GPS.turnGPSOff error", e);
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean turnGPSOff(Context context){
		try {
			if (android.os.Build.VERSION.SDK_INT > 11) {
				final Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
				intent.putExtra("enabled", false);
				context.sendBroadcast(intent);

				return true;
			} else {
				String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

				if(provider.contains("gps")){ //if gps is enabled
					final Intent poke = new Intent();
					poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
					poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
					poke.setData(Uri.parse("3")); 
					context.sendBroadcast(poke);

					return true;
				}
			}

			return false;
		} catch (Exception e) {
			Logs.errorLog("GPS.turnGPSOff error", e);
			return false;
		}
	}

	public static boolean canToggleGPS(Context context) {
		PackageManager pacman = context.getPackageManager();
		PackageInfo pacInfo = null;

		try {
			pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
		} catch (NameNotFoundException e) {
			return false; //package not found
		}

		if (pacInfo != null){
			for (ActivityInfo actInfo : pacInfo.receivers){
				// test if recevier is exported. if so, we can toggle GPS.
				if (actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported){
					return true;
				}
			}
		}

		return false; //default
	}

}
