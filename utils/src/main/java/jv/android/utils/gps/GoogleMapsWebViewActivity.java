package jv.android.utils.gps;

import jv.android.utils.R;
import jv.android.utils.Format;
import jv.android.utils.gps.CoordinateInfo;
import jv.android.utils.gps.GPS;
import jv.android.utils.interfaces.IGPSActivity;
import android.app.Activity;
import android.content.Intent;
import android.location.GpsStatus;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GoogleMapsWebViewActivity extends Activity implements IGPSActivity {

	private WebView mapView;
	private String centerURL;;
	private GPS gps;
	private boolean isLoaded;
	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_maps_web_view);
		mapView = (WebView) findViewById(R.id.mapView);
		mapView.getSettings().setJavaScriptEnabled(true);
		
		intent = getIntent();
		int timeout = 120;
		int interval = 0;
		
		if (intent != null) {
        	Bundle params = intent.getExtras();
        	
        	if (params != null) {
        		timeout = params.getInt("timeout");
        		interval = params.getInt("interval");
        	}
		}
		
		isLoaded = false;
		gps = new GPS(this, timeout, interval, true);
		setupWebView(0, 0);
	}
	
	/** Sets up the WebView object and loads the URL of the page **/
	private void setupWebView(double lat, double lon){
		if (lat == 0 && lon == 0) {
			CoordinateInfo ci = gps.getPositionFromNetwork();
			lat = ci.getLatitude();
			lon = ci.getLongitude();
		}
		
		centerURL = "javascript:centerAt(" + Format.commaToPoint(Format.format(lat)) + "," + Format.commaToPoint(Format.format(lon)) + ")";

		//Wait for the page to load then send the location information
		if (lat != 0 && lon != 0) {
			mapView.setWebViewClient(new WebViewClient(){ 
				@Override
				public void onPageFinished(WebView view, String url) 
				{
					mapView.loadUrl(centerURL);
					isLoaded = true;
				} 
			});
		}        

		if (!isLoaded) {
			String MAP_URL = "https://jvsoftware.sslblindado.com/jvtrackingfleet/ws/map.html";
			mapView.loadUrl(MAP_URL); 
		} else {
			mapView.loadUrl(centerURL);
		}
	}

	@Override
	protected void onResume() { 
		if (!gps.isRunning()) 
			gps.startGPS();

		super.onResume();
	}

	@Override
	protected void onStop() {
		if (gps.isRunning())
			gps.stopGPS();

		super.onStop();
	}

	@Override
	public void locationChanged(CoordinateInfo coordinateInfo) {
		if (coordinateInfo != null) {
			setupWebView(coordinateInfo.getLatitude(), coordinateInfo.getLongitude());
		}
	}

	@Override
	public void displayGPSSettingsDialog() {

	}

	@Override
	public void onGpsStatusChanged(int Event, GpsStatus gpsStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void locationNotChanged(CoordinateInfo coordinateInfo) {
		// TODO Auto-generated method stub
		
	}
}

