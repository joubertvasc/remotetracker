package jv.android.utils.gps;

import java.util.Calendar;
import java.util.Iterator;

import jv.android.utils.Format;
import jv.android.utils.Logs;
import jv.android.utils.interfaces.IGPSActivity;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.os.SystemClock;

public class GPS {

	private IGPSActivity main;

	// Helper for GPS-Position
	private LocationListener mlocListener;
	private LocationManager mlocManager;
	//    private CoordinateInfo coordinateInfo;

	private boolean isRunning;

	// Constantes
	public int GPS_PROVIDER = 0;
	public int NETWORK_PROVIDER = 1;

	// Helper for GPS control
	private int listenerLimit = 0;
	private int gpsTimeout = 120;
	private boolean usesNetwork = true;
	private int listenerCount = 0;
	private boolean isGPSFix = false;
	private Location lastLocation = null;
	private Long mLastLocationMillis;
	private int gpsTimeoutCounter = 0;
	private int satFixed = 0;
	private int satTotal = 0;
	private boolean firstTime;

	public GPS(Context main, int timeout, int interval, boolean usesNetwork, int accuracy, int power) {
		createGPS (main, timeout, interval, usesNetwork, accuracy, power);
	}

	public GPS(Context main, int timeout, int interval, boolean usesNetwork) {
		createGPS (main, timeout, interval, usesNetwork, Criteria.ACCURACY_FINE, Criteria.POWER_LOW);
	}

	private void createGPS(Context main, int timeout, int interval, boolean usesNetwork, int accuracy, int power) {
		Logs.infoLog("GPS: starting. Timeout: " + String.valueOf(timeout) + ". Interval: " + String.valueOf(interval) + ". Use network after timeout: " + (usesNetwork ? "Y" : "N"));

		this.main = (IGPSActivity)main;
		this.listenerLimit = interval;
		this.gpsTimeout = timeout;
		this.usesNetwork = usesNetwork;
		this.listenerCount = 0;
		this.satTotal = 0;
		this.satFixed = 0;
		this.isGPSFix = false;
		this.lastLocation = null;
		this.gpsTimeoutCounter = 0;

		// GPS Position
		mlocManager = (LocationManager) main.getSystemService(Context.LOCATION_SERVICE);
		mlocManager.addGpsStatusListener(onGpsStatusChange) ; 
		mlocListener = new GPSLocationListener();

		Criteria criteria = new Criteria();
		criteria.setAccuracy(accuracy);
		criteria.setPowerRequirement(power);
		criteria.setAltitudeRequired(true);
		criteria.setSpeedRequired(true);
		criteria.setCostAllowed(true);
		criteria.setBearingRequired(true);

		mlocManager.getBestProvider(criteria, true); 
	}

	public void startGPS() {
		Logs.infoLog("GPS.startGPS: requesting location updates");
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		this.isRunning = true;
		this.firstTime = true;
		this.lastLocation = null;
	}

	public void stopGPS() {
		if (isRunning) {
			Logs.infoLog("GPS.stopGPS: removing location updates");
			mlocManager.removeUpdates(mlocListener);
			this.isRunning = false;
		}
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public class GPSLocationListener implements LocationListener {

		private final String tag = GPSLocationListener.class.getSimpleName();

		@Override
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				Logs.infoLog("GPS.GPSLocationListener.onLocationChanged: location is not null");

				CoordinateInfo coordinateInfo = new CoordinateInfo();
				coordinateInfo.setDate(Calendar.getInstance().getTime());
				coordinateInfo.setGpsFixed(isGPSFix);
				coordinateInfo.setSatFixed(satFixed);
				coordinateInfo.setSatTotal(satTotal);
				coordinateInfo.setLatitude(loc.getLatitude());
				coordinateInfo.setLongitude(loc.getLongitude());
				coordinateInfo.setTag(tag);
				coordinateInfo.setType(GPS_PROVIDER);
				coordinateInfo.setGpsStatus(mlocManager.getGpsStatus(null));
				coordinateInfo.setLastAccuracy(loc.hasAccuracy() ? loc.getAccuracy() : 0);
				coordinateInfo.setSpeed(loc.hasSpeed() ? loc.getSpeed() * 3.6 : 0);
				coordinateInfo.setAltitude(loc.hasAltitude() ? loc.getAltitude() : 0);

				mLastLocationMillis = SystemClock.elapsedRealtime();    

				listenerCount++;

				Logs.infoLog("GPS.GPSLocationListener.onLocationChanged: isGPSFix = " + (isGPSFix ? "Y" : "N") + " satFixed = " + String.valueOf(satFixed) + 
						" LimiterOk = " + (listenerCount > listenerLimit || listenerLimit == 0 || firstTime ? "Y" : "N"));
				Logs.infoLog("GPS.GPSLocationListener.onLocationChanged: loc.Speed = " + (loc.hasSpeed() ? Format.doubleToString(loc.getSpeed(), 2) : "N/A") +
						" loc.Altitude = " + (loc.hasAltitude() ? Format.doubleToString(loc.getAltitude(), 2) : "N/A") + 
						" loc.Accuracy = " + (loc.hasAccuracy() ? Format.doubleToString(loc.getAccuracy(), 2) : "N/A") + 
						" loc.Latitude = " + Format.doubleToString(loc.getLatitude(), 5) + 
						" loc.Longitude = " + Format.doubleToString(loc.getLongitude(), 5));

				if (lastLocation == null)
					Logs.infoLog("GPS.GPSLocationListener.onLocationChanged: LastLocation = null");
				else {
					Logs.infoLog("GPS.GPSLocationListener.onLocationChanged: LastLocation.Latitude + " + Format.doubleToString(lastLocation.getLatitude(), 5) + 
							" LastLocation.Longitude + " + Format.doubleToString(lastLocation.getLongitude(), 5));
				}                

				if (isGPSFix && locationChanged(loc) && satFixed > 2 && (listenerCount > listenerLimit || listenerLimit == 0 || firstTime)) {
					lastLocation = loc;

					GPS.this.main.locationChanged(coordinateInfo);

					listenerCount = 0;
					firstTime = false;
				} else {
					Logs.infoLog("GPS.GPSLocationListener.onLocationNotChanged.");
					GPS.this.main.locationNotChanged(coordinateInfo);
				}
			} else {
				Logs.infoLog("GPS.GPSLocationListener.onLocationChanged: location is null");
			}
		}

		private boolean locationChanged(Location l) {
			if (lastLocation == null || l == null)
				return true;

			String lat1 = Format.doubleToString(l.getLatitude(), 5);
			String lat2 = Format.doubleToString(lastLocation.getLatitude(), 5);
			String lon1 = Format.doubleToString(l.getLongitude(), 5);
			String lon2 = Format.doubleToString(lastLocation.getLongitude(), 5);

			if (!lat1.equals(lat2) || !lon1.equals(lon2)) 
				return true;

			return false;    
		}    

		@Override
		public void onProviderDisabled(String provider) {
			Logs.infoLog("GPS.GPSLocationListener.onProviderDisabled"); 
			GPS.this.main.displayGPSSettingsDialog();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Logs.infoLog("GPS.GPSLocationListener.onProviderEnabled: " + provider); 
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Logs.infoLog("GPS.GPSLocationListener.onStatusChanged: " + provider + ", status: " + String.valueOf(status)); 
		}

	}

	private final Listener onGpsStatusChange=new GpsStatus.Listener() 
	{ 
		public void onGpsStatusChanged(int event)
		{
			GpsStatus gpsStatus = null;

			switch( event ) 
			{
			case GpsStatus.GPS_EVENT_STARTED: 
				// Started...
				break ; 
			case GpsStatus.GPS_EVENT_STOPPED: 
				// Stopped... 
				break ;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:                
				if (lastLocation != null)                    
					isGPSFix = (SystemClock.elapsedRealtime() - mLastLocationMillis) < 3000;                

				Logs.infoLog("GPS.onGpsStatusChange.onGpsStatusChanged. fixed? " + (isGPSFix ? "Y" : "N")); 

				gpsStatus = mlocManager.getGpsStatus(null);
				Iterable<GpsSatellite> iSatellites = gpsStatus.getSatellites(); 
				Iterator<GpsSatellite> it = iSatellites.iterator();

				int s = 0;
				int f = 0;

				while (it.hasNext() ) 
				{
					s++;
					GpsSatellite oSat = it.next();

					if (oSat.usedInFix())
						f++;
				}

				satFixed = f;
				satTotal = s;
				Logs.infoLog("GPS.onGpsStatusChange.onGpsStatusChanged. satellites: (" + String.valueOf(f) + "/" + String.valueOf(s) + ")"); 

				gpsTimeoutCounter++;
				CoordinateInfo coordinateInfo = null;

				if (gpsTimeout > 0 && gpsTimeoutCounter >= gpsTimeout) {
					gpsTimeoutCounter = 0;

					if (usesNetwork) {
						Logs.infoLog("GPS.onGpsStatusChange.onGpsStatusChanged. timeout! trying to get position from network"); 
						coordinateInfo = getPositionFromNetwork();
						GPS.this.main.locationChanged(coordinateInfo);
					} else {
						Logs.infoLog("GPS.onGpsStatusChange.onGpsStatusChanged. timeout! returning null"); 
						GPS.this.main.locationChanged(null);
					}
				}

				break;            
			case GpsStatus.GPS_EVENT_FIRST_FIX:                
				// Do something.                
				isGPSFix = true;                

				break;//                 
			} 

			GPS.this.main.onGpsStatusChanged(event, gpsStatus);
		} 
	} ; 

	public CoordinateInfo getPositionFromNetwork() {
		CoordinateInfo ci = new CoordinateInfo();

		ci.setGpsFixed(false);
		ci.setSatTotal(0);
		ci.setSatFixed(0);
		ci.setAltitude(0);
		ci.setSpeed(0);
		ci.setDate(Calendar.getInstance().getTime());
		ci.setTag("");
		ci.setType(NETWORK_PROVIDER);
		ci.setGpsStatus(null);

		Location location = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			double lat = 0;
			double lon = 0;

			try {
				lat = location.getLatitude();
			} catch (Exception e) {
				Logs.errorLog("CommandGPListener: Error converting latitude", e);
			}

			try {
				lon = location.getLongitude();
			} catch (Exception e) {
				Logs.errorLog("CommandGPListener: Error converting longitude", e);
			}

			if (lat != 0 && lon != 0) {
				ci.setLatitude(lat);
				ci.setLongitude(lon);
				ci.setLastAccuracy(location.getAccuracy());

				Logs.infoLog("GPS.doGetPositionFromNetwork. Returning position from network: lat=" + String.valueOf(lat) + " lon=" + String.valueOf(lon)); 
				return ci;
			} else {
				Logs.infoLog("GPS.doGetPositionFromNetwork. Lat or Lon is null. Nothing to do and returning null"); 
				return null;
			}
		} else {
			Logs.infoLog("GPS.doGetPositionFromNetwork. Location is null. Is network position available?"); 
			return null;
		}
	}
}
