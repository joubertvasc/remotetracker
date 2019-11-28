package jv.android.utils.AdView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdViewWrapper {

	private static AdRequest prepare (AdView adView) {
		AdRequest adRequest = new AdRequest.Builder().
				addTestDevice(AdRequest.DEVICE_ID_EMULATOR).
				addTestDevice("D4A6F73C7D3922753ED8E29E7B516B4E"). // ASUS ZenFone 2
				addTestDevice("780F41C09A9F61B69B863396BB2E904E"). // S7

				build();
		
		return adRequest;
	}
	
	public static void setAdSize(AdView adView) {
		adView.setAdSize(AdSize.BANNER);
	}
	
	public static void adViewRequest(AdView adView, String id) {
		if (adView != null) { 
			AdRequest adRequest = prepare (adView);
			adView.setAdUnitId(id);
			adView.loadAd(adRequest);
		}
	}
	
	public static void adViewRequest(AdView adView) {
		if (adView != null) { 
			AdRequest adRequest = prepare (adView);
			adView.loadAd(adRequest);
		}
	}
	
	public static void hideAdView(AdView adView) {
		if (adView != null) { 
			adView.setVisibility(com.google.android.gms.ads.AdView.INVISIBLE);
			adView.setVisibility(com.google.android.gms.ads.AdView.GONE);
		}
	}

	public static void showAdView(AdView adView) {
		if (adView != null) 
			adView.setVisibility(com.google.android.gms.ads.AdView.VISIBLE);
	}

}
