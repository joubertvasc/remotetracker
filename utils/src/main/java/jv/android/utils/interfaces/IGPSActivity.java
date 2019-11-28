package jv.android.utils.interfaces;

import jv.android.utils.gps.CoordinateInfo;
import android.location.GpsStatus;

public interface IGPSActivity {
    public void locationChanged(CoordinateInfo coordinateInfo);
    public void locationNotChanged(CoordinateInfo coordinateInfo);
    public void displayGPSSettingsDialog();
    public void onGpsStatusChanged(int Event, GpsStatus gpsStatus);
}