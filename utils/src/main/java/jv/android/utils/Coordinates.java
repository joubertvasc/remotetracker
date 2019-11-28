package jv.android.utils;

public class Coordinates {
	private double latitude = 0;
	private double longitude = 0;
	private double altitude = 0;
	private double speed = 0;
	private String name = "";
	private String pin = Google.YELLOW_PUSHPIN;

	private String picture = "";
	private String extraDescription = "";

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSpeed(){
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getAltitude() {
		return altitude;
	}	
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public static double getDistanceTo(double latitude, double longitude, double latitudePto, double longitudePto){  
        double dlon, dlat, a, distancia;  
        dlon = longitudePto - longitude;  
        dlat = latitudePto - latitude;  
        a = Math.pow(Math.sin(dlat/2),2) + Math.cos(latitude) * Math.cos(latitudePto) * Math.pow(Math.sin(dlon/2),2);  
        distancia = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));  
        return 6378140 * distancia; /* 6378140 is the radius of the Earth in meters*/  
    }
	public String getExtraDescription() {
		return extraDescription;
	}
	public void setExtraDescription(String extraDescription) {
		this.extraDescription = extraDescription;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}


}
