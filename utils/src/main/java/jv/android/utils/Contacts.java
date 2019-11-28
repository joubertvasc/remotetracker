// bases on: http://www.vtgroup.com/#ContactsContract
package jv.android.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.app.Activity;

public class Contacts {

	private static String pbHomeEmail = "Home E-Mail";
	private static String pbMobileEmail = "Mobile E-Mail";
	private static String pbWorkEmail = "Work E-Mail";
	private static String pbOtherEmail = "Other E-Mail";
	private static String pbCustomEmail = "Custom E-Mail";
	private static String pbHomePhone = "Home Phone";
	private static String pbMobilePhone = "Mobile Phone";
	private static String pbWorkPhone = "Work Phone";
	private static String pbOtherPhone = "Other Phone";
	private static String pbCustomPhone = "Custom Phone";
	private static String pbHomeFax = "Home FAX";
	private static String pbWorkFax = "Work FAX";
	private static String pbOtherFax = "Other FAX";

	private static void addLines(String subject, List<String> result, List<String> list) {
		if (list != null && list.size() > 0)
			for (int i=0; i < list.size(); i++)
				if (list.get(i) != null && !list.get(i).trim().equals(""))
					result.add(subject + "=" + list.get(i).trim());
	}
	
	public static String[] emails(Context context, String name) {
		ContactDetail[] cd = Contacts.getContacts(context, name);
		List<String> l = new ArrayList<String>();

		if (cd != null) {	
			addLines (pbHomeEmail, l, cd[0].getHomeEmail());
			addLines (pbMobileEmail, l, cd[0].getMobileEmail());
			addLines (pbWorkEmail, l, cd[0].getWorkEmail());
			addLines (pbOtherEmail, l, cd[0].getOtherEmail());
			addLines (pbCustomEmail, l, cd[0].getCustomEmail());

			String line = "";
			
			for (int i = 0; i < l.size(); i++) {
				line += (line.equals("") ? "" : "!") + l.get(i); 
			}
			
			if (!line.equals(""))
				return line.split("!");
		}

		return null;
	}

	public static String[] numbers(Context context, String name) {
		ContactDetail[] cd = Contacts.getContacts(context, name);
		List<String> l = new ArrayList<String>();

		if (cd != null) {			
			addLines(pbHomePhone, l, cd[0].getHomePhone());
			addLines(pbMobilePhone, l, cd[0].getMobilePhone());
			addLines(pbWorkPhone, l, cd[0].getWorkPhone());
			addLines(pbOtherPhone, l, cd[0].getOtherPhone());
			addLines(pbCustomPhone, l, cd[0].getCustomPhone());
			addLines(pbHomeFax, l, cd[0].getHomeFax());
			addLines(pbWorkFax, l, cd[0].getWorkFax());
			addLines(pbOtherFax, l, cd[0].getOtherFax());

			String line = "";
			
			for (int i = 0; i < l.size(); i++) {
				line += (line.equals("") ? "" : "!") + l.get(i); 
			}
			
			if (!line.equals(""))
				return line.split("!");
		}

		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static String[] pickContact(Context context, Intent data) {
		String[] result = new String[4];

		Uri contactData = data.getData();
		Cursor c =  ((Activity)context).managedQuery(contactData, null, null, null, null);
		if (c.moveToFirst()) {
			// ID
			result[0] = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
			// Name
			result[1] = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));        	
			String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			// PhoneNumber
			result[2] = "";
			// EMailAddress
			result[3] = "";

			if (hasPhone.equalsIgnoreCase("1"))
				hasPhone = "true";
			else
				hasPhone = "false" ;

			if (Boolean.parseBoolean(hasPhone)) 
			{
				Cursor phones = context.getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID +" = "+ result[0], null, null);
				while (phones.moveToNext()) 
				{
					result[2] = phones.getString(phones.getColumnIndex(Phone.NUMBER));
				}

				phones.close();
			}

			// Find Email Addresses
			Cursor emails = context.getContentResolver().query(Email.CONTENT_URI,null, Email.CONTACT_ID + " = " + result[0], null, null);
			while (emails.moveToNext()) 
			{
				result[3] = emails.getString(emails.getColumnIndex(Email.DATA));
			}

			emails.close();                
		}            	

		return result;
	}

	public static ContactDetail[] getContacts(Context context, String name) {
		int count = 0;
		Logs.infoLog("Contacts.getContacts started");
		
		//
	    //  Find contact based on name.
	    //

		ContactDetail[] result = null;
		Logs.infoLog("Contacts.getContacts getContentResolver");
		ContentResolver cr = context.getContentResolver();

		if (cr != null) {
			Logs.infoLog("Contacts.getContacts cursor");
			Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, (name.equals("") ? "" : "DISPLAY_NAME = '" + name + "'"), null, null);
			
			if (cursor != null) {
				Logs.infoLog("Contacts.getContacts count= " + String.valueOf(cursor.getCount()));
				result = new ContactDetail[cursor.getCount()];	    
			    
			    //if (cursor.moveToFirst()) {
				while (cursor.moveToNext()) {
			    	ContactDetail cd = new ContactDetail();
			    	
			        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			        String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			        
			        cd.setId(contactId);
			        cd.setName(contactName);
			        
			        //
			        //  Get all phone numbers.
			        //
			        Cursor phones = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + contactId, null, null);
			        
			        if (phones != null) {
				        while (phones.moveToNext()) {
				            String number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
				            
				            int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
				            switch (type) {
				                case Phone.TYPE_HOME:
				                	cd.addHomePhone(number);
//				                	cd.setHomePhone(number);
				                    break;
				                case Phone.TYPE_MOBILE:
				                	cd.addMobilePhone(number);
//				                	cd.setMobilePhone(number);
				                    break;
				                case Phone.TYPE_WORK:
				                	cd.addWorkPhone(number);
//				                	cd.setWorkPhone(number);
				                    break; 
				                case Phone.TYPE_OTHER:
				                	cd.addOtherPhone(number);
//				                	cd.setOtherPhone(number);
				                	break;
				                case Phone.TYPE_FAX_HOME:
				                	cd.addHomeFax(number);
//				                	cd.setHomeFax(number);
				                	break;
				                case Phone.TYPE_FAX_WORK:
				                    cd.addWorkFax(number);
//				                    cd.setWorkFax(number);
				                    break;
				                case Phone.TYPE_OTHER_FAX:
				                	cd.addOtherFax(number);
//				                	cd.setOtherFax(number);
				                	break;
				                case Phone.TYPE_CUSTOM:
				                	cd.addCustomPhone(number);
//				                	cd.setCustomPhone(number);
				                	break;
				            }
				        }
				        phones.close();
			        }
			        else {
						Logs.warningLog("Contacts.getContacts phones is null");
			        }

			        //
			        //  Get all email addresses.
			        //
			        Cursor emails = cr.query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + contactId, null, null);
			        
			        if (emails != null) {
				        while (emails.moveToNext()) {
				            String email = emails.getString(emails.getColumnIndex(Email.DATA));
				            
				            int type = emails.getInt(emails.getColumnIndex(Phone.TYPE));
				            switch (type) {
				                case Email.TYPE_HOME:
				                    cd.addHomeEmail(email);
//				                    cd.setHomeEmail(email);
				                    break;
				                case Email.TYPE_WORK:
				                    cd.addWorkEmail(email);
//				                    cd.setWorkEmail(email);
				                    break; 
				                case Email.TYPE_MOBILE:
				                    cd.addMobileEmail(email);
//				                    cd.setMobileEmail(email);
				                    break;
				                case Email.TYPE_OTHER:
				                	cd.addOtherEmail(email);
//				                	cd.setOtherEmail(email);
				                	break;
				                case Email.TYPE_CUSTOM:
				                	cd.addCustomEmail(email);
//				                	cd.setCustomEmail(email);
				                	break;
				            }
				        }
				        
				        emails.close();
			        }
			        else {
						Logs.warningLog("Contacts.getContacts emails is null");
			        }
			        
			        result[count] = cd;
			        count++;
			    }
			    
			    cursor.close();
			}
			else
			{
				Logs.warningLog("Contacts.getContacts cursor is null");
			}
		} else
		{
			Logs.warningLog("Contacts.getContacts cr is null");
		}

		Logs.infoLog("Contacts.getContacts ended");
	    return result;
	}

	public static String getPbHomeEmail() {
		return pbHomeEmail;
	}

	public static void setPbHomeEmail(String pbHomeEmail) {
		Contacts.pbHomeEmail = pbHomeEmail;
	}

	public static String getPbMobileEmail() {
		return pbMobileEmail;
	}

	public static void setPbMobileEmail(String pbMobileEmail) {
		Contacts.pbMobileEmail = pbMobileEmail;
	}

	public static String getPbWorkEmail() {
		return pbWorkEmail;
	}

	public static void setPbWorkEmail(String pbWorkEmail) {
		Contacts.pbWorkEmail = pbWorkEmail;
	}

	public static String getPbOtherEmail() {
		return pbOtherEmail;
	}

	public static void setPbOtherEmail(String pbOtherEmail) {
		Contacts.pbOtherEmail = pbOtherEmail;
	}

	public static String getPbCustomEmail() {
		return pbCustomEmail;
	}

	public static void setPbCustomEmail(String pbCustomEmail) {
		Contacts.pbCustomEmail = pbCustomEmail;
	}

	public static String getPbHomePhone() {
		return pbHomePhone;
	}

	public static void setPbHomePhone(String pbHomePhone) {
		Contacts.pbHomePhone = pbHomePhone;
	}

	public static String getPbMobilePhone() {
		return pbMobilePhone;
	}

	public static void setPbMobilePhone(String pbMobilePhone) {
		Contacts.pbMobilePhone = pbMobilePhone;
	}

	public static String getPbWorkPhone() {
		return pbWorkPhone;
	}

	public static void setPbWorkPhone(String pbWorkPhone) {
		Contacts.pbWorkPhone = pbWorkPhone;
	}

	public static String getPbOtherPhone() {
		return pbOtherPhone;
	}

	public static void setPbOtherPhone(String pbOtherPhone) {
		Contacts.pbOtherPhone = pbOtherPhone;
	}

	public static String getPbCustomPhone() {
		return pbCustomPhone;
	}

	public static void setPbCustomPhone(String pbCustomPhone) {
		Contacts.pbCustomPhone = pbCustomPhone;
	}

	public static String getPbHomeFax() {
		return pbHomeFax;
	}

	public static void setPbHomeFax(String pbHomeFax) {
		Contacts.pbHomeFax = pbHomeFax;
	}

	public static String getPbWorkFax() {
		return pbWorkFax;
	}

	public static void setPbWorkFax(String pbWorkFax) {
		Contacts.pbWorkFax = pbWorkFax;
	}

	public static String getPbOtherFax() {
		return pbOtherFax;
	}

	public static void setPbOtherFax(String pbOtherFax) {
		Contacts.pbOtherFax = pbOtherFax;
	}	
}
