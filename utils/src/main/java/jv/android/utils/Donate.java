package jv.android.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Donate {
	
    public static String donateURL()
    {
        return "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=2138678";
    }

    public static void goDonate(Context context)
    {
    	Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(donateURL()));
    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	
    	context.startActivity(i);
    }
}
