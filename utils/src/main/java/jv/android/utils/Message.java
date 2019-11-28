package jv.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Message {

	//	private static boolean result = false;

	public static void showMessage (Context context, String title, String msg) {
		Message.showMessage (context, title, msg, -1);
	}

	public static void showMessage(Context context, String title, String msg, int iconResource) {
		try {
			AlertDialog.Builder alert = new AlertDialog.Builder(context);            

			if (iconResource > -1) {
				alert.setIcon(iconResource);
			}

			alert.setTitle(title);            
			alert.setMessage(msg);            
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {                
				public void onClick(DialogInterface dialog, int whichButton) {
					try {
						dialog.dismiss();
					} catch (Exception ex) {

					}
				}            
			});            

			alert.create();            
			alert.show();
		} catch (Exception e) {
			return;
		}
	}
}
