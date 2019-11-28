package jv.android.utils;

import android.location.Location;

public class Distance {

	private double radius;  

	// R = earth's radius (mean radius = 6,371km or 3,960 miles)  
	// Constructor  
/*	public Distance(double R) {
		radius = R;  
	} /**/

	public Distance (boolean useMiles) {
		if (useMiles)
			radius = 3960;
		else
			radius = 6371;
	}

/*	public double calculationByDistance(GeoPoint startP, GeoPoint endP) {
		double lat1 = startP.getLatitudeE6()/1E6;
		double lat2 = endP.getLatitudeE6()/1E6;  
		double lon1 = startP.getLongitudeE6()/1E6;  
		double lon2 = endP.getLongitudeE6()/1E6;  
		double dLat = Math.toRadians(lat2-lat1);  
		double dLon = Math.toRadians(lon2-lon1);  
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +  
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *  
				Math.sin(dLon/2) * Math.sin(dLon/2);  
		double c = 2 * Math.asin(Math.sqrt(a));  

		return radius * c;
	}  

	public double angle(GeoPoint startP, GeoPoint endP) {
		double dlon = endP.getLongitudeE6() - startP.getLongitudeE6();
		double dlat = endP.getLatitudeE6() - startP.getLatitudeE6();

		return Math.atan2(dlat, dlon);
	}

 public double distanceTo (double latFrom, double lonFrom, double latTo, double lonTo) {
		int latE6_1 = (int) (latFrom * 1e6);
		int lonE6_1 = (int) (lonFrom * 1e6);
		int latE6_2 = (int) (latTo * 1e6);
		int lonE6_2 = (int) (lonTo * 1e6);

		GeoPoint g1 = new GeoPoint(latE6_1, lonE6_1);
		GeoPoint g2 = new GeoPoint(latE6_2, lonE6_2);    			  

		return calculationByDistance(g1, g2);
	} /**/

	public double distanceTo(double latFrom, double lonFrom, double latTo, double lonTo) {
        Location startPoint = new Location("locationA");
        startPoint.setLatitude(latFrom);
        startPoint.setLongitude(lonFrom);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(latTo);
        endPoint.setLongitude(lonTo);

        double distance= (int) startPoint.distanceTo(endPoint);
        return distance;
    }
}
