package jv.android.remotetracker.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class BuyProVersion {

	public static void goPlay(Context context) {
    	Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Links.getPro()));
    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	
    	context.startActivity(i);
	}
	
	public static void askBuyProVersion(Context context) {
		final Context c = context;
		
		AlertDialog.Builder alert = new AlertDialog.Builder(context);            
    	alert.setIcon(jv.android.remotetracker.R.drawable.exclamation);
		alert.setTitle(context.getString(jv.android.remotetracker.R.string.msgNotAvailable));
		alert.setMessage(context.getString(jv.android.remotetracker.R.string.msgOnlyInProVersion));
		alert.setPositiveButton(context.getString(jv.android.remotetracker.R.string.msgBuyProVersion), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				goPlay(c);
			}            
		});            
		
		alert.setNegativeButton(context.getString(jv.android.remotetracker.R.string.btCancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {                    
				dialog.dismiss();                
			}            
		});            

		alert.create();            
		alert.show();
	}
}
