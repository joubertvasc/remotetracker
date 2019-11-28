package jv.android.utils.gps;

import java.util.List;
import java.util.Locale;

import jv.android.utils.Format;
import jv.android.utils.Logs;
import jv.android.utils.network.Network;

import android.content.Context;
import android.location.Geocoder;
import android.location.Address;

public class CoordinateToAddress {

	private static double lastLatitude;
	private static double lastLongitude;
	private static List<Address> lastAddresses;
	private static String lastAddressString;
	private static boolean processing = false;
	private static String lastErrorMessage;

	public static String getLastErrorMessage() {
		return lastErrorMessage;
	}
	
	private static boolean coordinateChanged(double latitude, double longitude) {
		String lat1 = Format.doubleToString(latitude, 9);
		String lat2 = Format.doubleToString(lastLatitude, 9);
		String lon1 = Format.doubleToString(longitude, 9);
		String lon2 = Format.doubleToString(lastLongitude, 9);

		return !lat1.equals(lat2) || !lon1.equals(lon2);
	}

	private static List<Address> getList(Context context, double latitude, double longitude) {
		if (Network.isNetworkAvailable(context) && !processing) {
			try {
				if (coordinateChanged(latitude, longitude)) {
					processing = true;
					Geocoder geocoder = new Geocoder(context, Locale.getDefault());
					List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

					lastLatitude = latitude;
					lastLongitude = longitude;
					lastAddresses = addresses;
					processing = false;
					lastErrorMessage = "";

					return addresses;
				} else {
					lastErrorMessage = "";
					return lastAddresses;
				}
			} catch (Exception e) {
				lastErrorMessage = e.getMessage();
				return null;
			}
		} else {
			return null;
		}
	}

	public static Address getAddress(Context context, double latitude, double longitude) {
		List<Address> addresses = getList (context, latitude, longitude);

		if (addresses != null && addresses.size() > 0) {
			return addresses.get(0);
		} else {
			return null;
		}
	}

	public static String getAddressString(Context context, double latitude, double longitude) {
		if (coordinateChanged(latitude, longitude)) {
			Address address = getAddress(context, latitude, longitude);

			if (address != null) {
				String result = "";
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++) 
					result += address.getAddressLine(i) + "\n";

				lastAddressString = result;

				Logs.infoLog("CoordinateToAddress.getAddressString: lat=" + String.valueOf(latitude) + ", lon=" + String.valueOf(longitude) + ", endereço="+ result);
				return result;
			} else {
				return lastErrorMessage;
			}
		} else {
			Logs.infoLog("CoordinateToAddress.getAddressString from cache: lat=" + String.valueOf(latitude) + ", lon=" + String.valueOf(longitude) + ", endereço="+ lastAddressString);
			return lastAddressString;
		}
	}

}
