package jv.android.utils.gps;

import java.util.Date;

import android.location.GpsStatus;

public class CoordinateInfo {

	private double latitude;
	private double longitude;
	private double speed;
	private double altitude;
	private int satTotal;
	private int satFixed;
	private boolean gpsFixed;
	private int type;
	private Date date;
	private String tag;
	private GpsStatus gpsStatus;
	private float lastAccuracy;
	private String metaData;
	private String name;
	
	public static final int GPS = 0;
	public static final int NETWORK = 1;
	
	public CoordinateInfo() {
		latitude = 0;
		longitude = 0;
		speed = 0;
		altitude = 0;
		satTotal = 0;
		satFixed = 0;
		gpsFixed = false;
		type = NETWORK;
		date = null;
		gpsStatus = null;
		lastAccuracy = 0;
		metaData = "";
		name = "";
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
	public double getSpeed() {
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
	public boolean isGpsFixed() {
		return gpsFixed;
	}
	public void setGpsFixed(boolean gpsFixed) {
		this.gpsFixed = gpsFixed;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public GpsStatus getGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(GpsStatus gpsStatus) {
		this.gpsStatus = gpsStatus;
	}

	public int getSatTotal() {
		return satTotal;
	}

	public void setSatTotal(int satTotal) {
		this.satTotal = satTotal;
	}

	public int getSatFixed() {
		return satFixed;
	}

	public void setSatFixed(int satFixed) {
		this.satFixed = satFixed;
	}

	public float getLastAccuracy() {
		return lastAccuracy;
	}

	public void setLastAccuracy(float lastAccuracy) {
		this.lastAccuracy = lastAccuracy;
	}

	public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
