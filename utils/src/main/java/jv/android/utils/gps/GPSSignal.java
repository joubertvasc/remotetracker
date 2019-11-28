package jv.android.utils.gps;

public class GPSSignal {

	private int satelliteID;
	private float strength;
	
	private boolean usedInFix;
	
	public int getSatelliteID() {
		return satelliteID;
	}
	public void setSatelliteID(int satelliteID) {
		this.satelliteID = satelliteID;
	}
	public float getStrength() {
		return strength;
	}
	public void setStrength(float strength) {
		this.strength = strength;
	}
	public boolean isUsedInFix() {
		return usedInFix;
	}
	public void setUsedInFix(boolean usedInFix) {
		this.usedInFix = usedInFix;
	}
	
	
}
