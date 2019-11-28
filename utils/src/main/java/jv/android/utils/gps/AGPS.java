package jv.android.utils.gps;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

public class AGPS {

	// Need permision: android.permission.ACCESS_LOCATION_EXTRA_COMMANDS
	public static void resetAGPS(Context context, LocationManager locationManager) {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data", null);
		Bundle bundle = new Bundle();
		locationManager.sendExtraCommand("gps", "force_xtra_injection", bundle);
		locationManager.sendExtraCommand("gps", "force_time_injection", bundle);
	}

}
