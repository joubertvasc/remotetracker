package jv.android.utils.whastapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import jv.android.utils.SystemUtils;

/**
 * Created by joubertvasc on 22/12/2016.
 */

public class Whatsapp {

    public static boolean isInstalled(Context context) {
        SystemUtils su = new SystemUtils(context);

        return su.isPackageInstalled("com.whatsapp");
    }

    public static boolean sendMessageToNumber(Context context, String toNumber, String message) {
        try {
            Uri uri = Uri.parse("smsto:" + toNumber);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.putExtra("sms_body", message);
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setPackage("com.whatsapp");
            context.startActivity(i);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendMessateToContact(Context context, String contact, String subject, String message) {
        try {
/*            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("content://com.android.contacts/data/" + contact));
            i.setType("text/plain");
            i.setPackage("com.whatsapp");           // so that only Whatsapp reacts and not the chooser
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            i.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(i); /**/

            Intent sendIntent = new Intent(Intent.ACTION_SEND, Uri.parse("content://com.android.contacts/data/" + contact));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            context.startActivity(sendIntent);


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
