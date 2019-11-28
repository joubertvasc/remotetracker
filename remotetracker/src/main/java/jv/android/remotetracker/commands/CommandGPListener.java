// Based on FindMyPhone sourcecode: http://sourceforge.net/projects/findmyphone/
package jv.android.remotetracker.commands;

import java.util.Calendar;

import jv.android.remotetracker.utils.Answer;
import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.remotetracker.utils.TimeoutThread;
import jv.android.utils.Format;
import jv.android.utils.Google;
import jv.android.utils.Logs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import jv.android.utils.gps.GPSUtils;

public class CommandGPListener implements LocationListener {

	private LocationManager locationManager;
	private String currentProvider;
	private static final long USE_OLD_FIX_THRESHOLD = 1000 * 60 * 3; // 3 minutes
	private static final int GPS_UPDATE_INTERVAL = 1000 * 20; // 20 seconds
	private boolean inSearch;
	private TimeoutThread timeoutThread;

	private Context context;
	private Intent service;
	private boolean fake;
	private CommandStructure commandStructure;
	private Preferences prefs;

	private final Handler handler = new Handler();
	private final Runnable abortGPSRunnable = new Runnable() {
        public void run() {
        	Logs.infoLog("CommandGPListener.AbortGPSHannable");
            internalAbortGpsSearch();
        }
    };
	private final Runnable abortNetworkRunnable = new Runnable() {
        public void run() {
        	Logs.infoLog("CommandGPListener.AbortNetworkHannable");
            internalAbortNetworkSearch();
        }
    };

	public CommandGPListener(Context context, Intent service, CommandStructure commandStructure, boolean fake) {
		this.context = context;
		this.service = service;
		this.fake = fake;
		this.commandStructure = commandStructure;

		prefs = new Preferences (context);
		prefs.setTrackerGPSProcessed(false);
		prefs.setTrackerGPSLatitude(0);
		prefs.setTrackerGPSLongitude(0);
		prefs.setTrackerGPSAltitude(0);
		prefs.setTrackerGPSSpeed(0);
		prefs.setTrackerGPSType("N");
		prefs.setTrackerGPSMessage("");
	}

	public void processCommand() {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Logs.infoLog("CommandGPListener: ProcessCommand");
		retreiveLocation(false);
	}

	public void abortGpsSearch() {
		Logs.infoLog("CommandGPListener.AbortGPSSearch - posting to main thread");
		handler.post(abortGPSRunnable);
	}

	private void internalAbortGpsSearch() {
		if (inSearch) {
			Logs.infoLog("CommandGPListener.internalAbortGpsSearch. AbortGPSSearch called. Removing listener (Current " + currentProvider + ")");
			inSearch = false;
			locationManager.removeUpdates(this);

			// Try Network fix instead
			Logs.infoLog("CommandGPListener.internalAbortGpsSearch. Trying Network location");
			retreiveLocation(true);
		} else {
			Logs.infoLog("CommandGPListener.internalAbortGpsSearch. AbortGPSSearch not insearch");
		}
	}

	public void abortNetworkSearch() {
		Logs.infoLog("CommandGPListener.AbortNetworkSearch - posting to main thread");
		handler.post(abortNetworkRunnable);
	}

	private void internalAbortNetworkSearch() {
		if (inSearch) {
			Logs.infoLog("CommandGPListener.internalAbortNetworkSearch. AbortNetworkSearch called. Removing listener (Current " + currentProvider + ")");
			inSearch = false;
			locationManager.removeUpdates(this);
			failLocationSearch();
		} else {
			Logs.infoLog("CommandGPListener.internalAbortNetworkSearch. abourtNetworkSearch not insearch");
		}
	}

	@SuppressLint("MissingPermission")
	@SuppressWarnings("unused")
	private void retreiveLocation(boolean networkOk) {
		Logs.infoLog("CommandGPListener: RetrieveLocation");
		currentProvider = null;

		if (inSearch) {
			Logs.infoLog("CommandGPListener: inSearch. Removing updates");
			locationManager.removeUpdates(this);
		} else {
			Logs.infoLog("CommandGPListener: NOT searching yet");
		}

		// Check if we have an old location that will do
		Logs.infoLog("CommandGPListener: Checking for an old location");
		Location location = null; //locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		long threshold = Calendar.getInstance().getTimeInMillis() - USE_OLD_FIX_THRESHOLD;

		if (location.getTime() > threshold ) {
			Logs.infoLog("CommandGPListener: Found useful old location from GPS");
			processLocation(location, LocationManager.GPS_PROVIDER);
		} else if (networkOk) {
			Logs.infoLog("CommandGPListener: Checking for an old location from network");

			if (!GPSUtils.providerExists(LocationManager.NETWORK_PROVIDER, locationManager)) {
				failLocationSearch();
			} else {
				Logs.infoLog("CommandGPListener: Reading location from network");

				location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (location != null && location.getTime() > threshold) {
					Logs.infoLog("CommandGPListener: Found useful old location from network");

					processLocation(location, LocationManager.NETWORK_PROVIDER); // Found an OK Network location
				} else {
					Logs.infoLog("CommandGPListener: No Old Location found. Requesting from network");
					inSearch = true;
					currentProvider = LocationManager.NETWORK_PROVIDER;
					timeoutThread = new TimeoutThread(this);
					timeoutThread.timeoutNetwork(prefs.getGPSTimeout());
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
				}
			}
		} else { // Try to get GPS fix
			if (GPSUtils.providerExists(LocationManager.GPS_PROVIDER, locationManager)) {
				Logs.infoLog("CommandGPListener: Trying to get GPS fix");
				inSearch = true;
				currentProvider = LocationManager.GPS_PROVIDER;
				timeoutThread = new TimeoutThread(this);
				timeoutThread.timeoutGps(prefs.getGPSTimeout());
				Logs.infoLog("requestLocationUpdates");
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_UPDATE_INTERVAL, 0, this);
				Logs.infoLog("CommandGPListener: Waiting GPS");
			} else if (!networkOk) { // Try network
				retreiveLocation(true);
			}
		}

		Logs.infoLog("CommandGPListener: Exiting Retrieve Location");
	}

	private static String addGoogleLink (double lat, double lon) {
		Google g = new Google();

		return g.googleMapsURL(lat,  lon);
	}

	private void processLocation(Location location, String provider) {
		inSearch = false;

		String message = "";

		if (location != null) {
			Logs.infoLog("CommandGPListener: Processing Location");

			double lat = 0;
			double lon = 0;

			try {
				lat = location.getLatitude();
				prefs.setTrackerGPSLatitude((float)lat);
			} catch (Exception e) {
				Logs.errorLog("CommandGPListener: Error converting latitude", e);
			}

			try {
				lon = location.getLongitude();
				prefs.setTrackerGPSLongitude((float)lon);
			} catch (Exception e) {
				Logs.errorLog("CommandGPListener: Error converting longitude", e);
			}

			try {
				prefs.setTrackerGPSAltitude((float)location.getAltitude());
			} catch (Exception e) {
				Logs.errorLog("CommandGPListener: Error converting altitude", e);
			}

			try {
				prefs.setTrackerGPSSpeed((float)location.getSpeed());
			} catch (Exception e) {
				Logs.errorLog("CommandGPListener: Error converting speed", e);
			}

			if (currentProvider == null)
				Logs.warningLog("CommandGPListener: currentProvider is null!");
			else {
				if (currentProvider.equals(LocationManager.GPS_PROVIDER)) {
					message = context.getString(R.string.msgPositionFromGPS);
					prefs.setTrackerGPSType("G");
				} else {
					message = context.getString(R.string.msgPositionFromGoogle);
					prefs.setTrackerGPSType("N");
				}
			}

			message += " " +
				    context.getString(R.string.msgLat) + " " + Format.doubleToString(lat, 6) + " " +
					context.getString(R.string.msgLon) + " " + Format.doubleToString(lon, 6) + "\n" +
					context.getString(R.string.msgLink) + " " + addGoogleLink (lat, lon);
		} else {
			message = context.getString(R.string.msgNoDataOrGPSAvailable);
		}

		Logs.infoLog("CommandGPListener: Message to send: " + message);
		prefs.setTrackerGPSMessage(message);
		prefs.setTrackerGPSProcessed(true);
		Answer.sendAnswer(context, commandStructure, message, fake);

		if (service != null) {
			Logs.infoLog("CommandGPListener: stopping service");
			context.stopService(service);
		}
	}

	@SuppressLint("MissingPermission")
	private void failLocationSearch() {
		Logs.infoLog("Failed to get location. Taking last known location even though it's old. Considering both GPS and network");
		Location gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location netLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (gpsLoc == null && netLoc == null) {
			Logs.infoLog("No last known location at all!");
			processLocation(null, null);
		} else if (netLoc != null && (gpsLoc == null || netLoc.getTime() > gpsLoc.getTime())) {
			Logs.infoLog("Failback to last known NETWORK");
			processLocation(netLoc, LocationManager.NETWORK_PROVIDER);
		} else {
			Logs.infoLog("Failback to last known GPS");
			processLocation(gpsLoc, LocationManager.GPS_PROVIDER);			
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		Logs.infoLog("Location Changed " + currentProvider);
		locationManager.removeUpdates(this);
		processLocation(location, currentProvider);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Logs.infoLog("Provider " + provider + " disabled!");
		if (LocationManager.GPS_PROVIDER.equals(provider)) {
			internalAbortGpsSearch();
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		Logs.infoLog("Provider enabled " + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Logs.infoLog("Change of status " + status);
		if (inSearch) {
			switch(status) {
			case LocationProvider.OUT_OF_SERVICE:
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Logs.infoLog("Location Not available yet. Trying Network location (Current " + currentProvider + ")");
				inSearch = false;
				locationManager.removeUpdates(this);
				retreiveLocation(true);
				break;
			}
		}
	}
}
