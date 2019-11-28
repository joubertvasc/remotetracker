package jv.android.utils.gps;

import jv.android.utils.Convertions;
import jv.android.utils.latlonconvert.Decimal2DMS;
import jv.android.utils.latlonconvert.LatLonUTM;
import jv.android.utils.latlonconvert.UTM;

public class CoordinateFormat {

	private double lat;
	private double lon;
	private boolean useMinus;
	private int format;
	private LatLonUTM latLonUTM;
	private UTM utm;
	
	private String south = "S";
	private String north = "N";
	private String west = "W";
	private String east = "E";

	public static final int DECIMALDEGREE = 1;
	public static final int DEGREEMINUTESSECONDS = 2;
	public static final int DEGREEDECIMALMINUTES = 3;
	public static final int UTM = 4;

	public CoordinateFormat(double latitude, double longitude, boolean showMinus, int format) {
		this.lat = latitude;
		this.lon = longitude;
		this.useMinus = showMinus;
		this.format = format;

		latLonUTM = new LatLonUTM();
		utm = latLonUTM.decToUTM(lat, lon);
	}

	public String latitude () {
    	String result = "";
    	
	    if (format == DECIMALDEGREE) {
	    	if (lat < 0) 
	    		result += (useMinus ? "-" : south);  // South
	    	else if (!useMinus)
	    		result += north; // North	    		

	    	result += Convertions.doubleToString(Math.abs(this.lat)); 
	    } else if (format == DEGREEMINUTESSECONDS) {
	    	Decimal2DMS decimal2DMS = Decimal2DMS.fromDdToDms(lat);
	    	
	    	result += (lat < 0 && useMinus ? "-" : "") + 
	    			  String.valueOf(Math.abs(decimal2DMS.degree)) + "º " +
		    		  String.valueOf(Math.abs(decimal2DMS.minute)) + "' " +
		    	      String.valueOf(Math.abs(decimal2DMS.second)) + "\" ";
	    	
	    	if (!useMinus)
	    		result += (lat >= 0 ? north : south);
	    } else if (format == DEGREEDECIMALMINUTES) {
	    	result = Convertions.doubleToString(this.lat);
	    } else { 
	    	result = utm.longZone + utm.latZone + " " + utm.northing + (lat >= 0 ? north : south);
	    }

	    return result; 
	}

	public String longitude () {
    	String result = "";
    	
	    if (format == DECIMALDEGREE) {
	    	if (lon < 0) 
	    		result += (useMinus ? "-" : west);
	    	else if (!useMinus)
	    		result += east;	    		

	    	result += Convertions.doubleToString(Math.abs(this.lon)); 
	    } else if (format == DEGREEMINUTESSECONDS) {
	    	Decimal2DMS decimal2DMS = Decimal2DMS.fromDdToDms(lon);

	    	result += (lon < 0 && useMinus ? "-" : "") + 
	    			  String.valueOf(Math.abs(decimal2DMS.degree)) + "º " +
		    		  String.valueOf(Math.abs(decimal2DMS.minute)) + "' " +
		    	      String.valueOf(Math.abs(decimal2DMS.second)) + "\" ";
	    	
	    	if (!useMinus)
	    		result += (lon >= 0 ? east : west);
	    } else if (format == DEGREEDECIMALMINUTES) {
	    	result = Convertions.doubleToString(this.lon);
	    } else { 
	    	result = utm.longZone + utm.latZone + " " + utm.easting + (lon >= 0 ? east : west);
	    }
	    
	    return result;
	}

	public String getSouth() {
		return south;
	}

	public void setSouth(String south) {
		this.south = south;
	}

	public String getNorth() {
		return north;
	}

	public void setNorth(String north) {
		this.north = north;
	}

	public String getWest() {
		return west;
	}

	public void setWest(String west) {
		this.west = west;
	}

	public String getEast() {
		return east;
	}

	public void setEast(String east) {
		this.east = east;
	}

}
