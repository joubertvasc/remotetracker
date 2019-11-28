package jv.android.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;

import android.content.Context;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.provider.CallLog;
import android.database.Cursor;

import java.util.Date;
import java.util.Locale;

public class PhoneUtils {
	Context mContext; 
	TelephonyManager telephonyManager;
	
	public PhoneUtils (Context mContext){       
		this.mContext = mContext;  
        telephonyManager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public String getIMEI(){
		try {
			return telephonyManager.getDeviceId();
		} catch (Exception e) {
			return "000000000000000";
		}
	}
	
	public String getIMSI() {
		return telephonyManager.getSubscriberId();
	}
	
	public String getICCID() {
		return telephonyManager.getSimSerialNumber();
	}
	
	public GsmCellLocation getCellLocation() {
		return (GsmCellLocation)telephonyManager.getCellLocation();           
	}
	
	public CellID getCurrentCellID() {
		try
		{
			GsmCellLocation gcl = this.getCellLocation();

			CellID c = new CellID();

			c.setCellID(gcl.getCid());
			c.setLac(gcl.getLac());

			String networkOperator = telephonyManager.getNetworkOperator();    

			if (networkOperator != null) {        
				c.setMcc(Integer.parseInt(networkOperator.substring(0, 3)));        
				c.setMnc(Integer.parseInt(networkOperator.substring(3)));    
			}

			return c;
		} catch (Exception e)
		{
			return null;
		}
	}

	public Coordinates getCoordinatesFromCellId()
	{
		GsmCellLocation gcl = this.getCellLocation();

		return RqsLocation (gcl.getCid(), gcl.getLac());
	}
	
	private Coordinates RqsLocation(int cid, int lac){
		Coordinates c = new Coordinates();
		c.setLatitude(0);
		c.setLongitude(0);
		
		String urlString = "http://www.google.com/glm/mmap";            
	    
        //---open a connection to Google Maps API---
		try
		{
			URL url = new URL(urlString); 
			URLConnection conn = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) conn;        
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true); 
			httpConn.setDoInput(true);
			httpConn.connect(); 

			//---write some custom data to Google Maps API---
			OutputStream outputStream = httpConn.getOutputStream();
			WriteData(outputStream, cid, lac);       

			//---get the response---
			InputStream inputStream = httpConn.getInputStream();  
			DataInputStream dataInputStream = new DataInputStream(inputStream);

			//---interpret the response obtained---
			dataInputStream.readShort();
			dataInputStream.readByte();
			int code = dataInputStream.readInt();
			if (code == 0) {
				c.setLatitude((double) dataInputStream.readInt() / 1000000D);
				c.setLongitude((double) dataInputStream.readInt() / 1000000D);
				
				dataInputStream.readInt();
				dataInputStream.readInt();
				dataInputStream.readUTF();
				
			}
		} catch (IOException e) {
		
		}
		
		return c;
	}   
	
	private void WriteData(OutputStream out, int cid, int lac)  throws IOException  {          
		DataOutputStream dataOutputStream = new DataOutputStream(out);      
		dataOutputStream.writeShort(21);      
		dataOutputStream.writeLong(0);      
		dataOutputStream.writeUTF("en");      
		dataOutputStream.writeUTF("Android");      
		dataOutputStream.writeUTF("1.0");      
		dataOutputStream.writeUTF("Web");      
		dataOutputStream.writeByte(27);      
		dataOutputStream.writeInt(0);      
		dataOutputStream.writeInt(0);      
		dataOutputStream.writeInt(3);      
		dataOutputStream.writeUTF("");       
		dataOutputStream.writeInt(cid);      
		dataOutputStream.writeInt(lac);          
		dataOutputStream.writeInt(0);      
		dataOutputStream.writeInt(0);      
		dataOutputStream.writeInt(0);      
		dataOutputStream.writeInt(0);      
		dataOutputStream.flush();      
	}
	
	@SuppressWarnings("deprecation")
	public static Calls[] getCallLog(Context context, String incoming, String outgoing, String missed) {
		Logs.infoLog("PhoneUtils.getCallLog started");
		
		String[] strFields = {
				android.provider.CallLog.Calls.DATE,
		        android.provider.CallLog.Calls.NUMBER, 
		        android.provider.CallLog.Calls.DURATION,
		        android.provider.CallLog.Calls.TYPE,
		        android.provider.CallLog.Calls.CACHED_NAME,
		        android.provider.CallLog.Calls.CACHED_NUMBER_TYPE,
		        };
		String strOrder = android.provider.CallLog.Calls.DATE + " DESC"; 
		 
		Logs.infoLog("PhoneUtils.getCallLog reading log");
		Cursor c = context.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, strFields, null, null, strOrder);		
		
        Calls[] result = null;

        if (c != null) {        
    		Logs.infoLog("PhoneUtils.getCallLog size= " + String.valueOf(c.getCount()));
            result = new Calls[c.getCount()];
            int count = 0;
            
            while (c.moveToNext())
            {
                long dialed=c.getLong(c.getColumnIndex(CallLog.Calls.DATE));                 
                Calls call = new Calls();
                
				DateFormat format = DateFormat.getDateInstance();
				Date date = new Date(dialed);
				
                call.setNumber(c.getString(1));
				call.setDate(format.format(date));
                call.setDuration(c.getString(2));
                call.setType((c.getString(3).equals("1") ? incoming : (c.getString(3).equals("2") ? outgoing : missed)));          
                
                result[count] = call;
                count++;
            }
        }
        else {
        	Logs.warningLog("PhoneUtils.getCallLog c = null");
        }
		Logs.infoLog("PhoneUtils.getCallLog ended");
        return result;
    }

	public static String getMccFromSim(Context context) {
//		int c = context.getResources().getConfiguration().mcc;

//		String mcc = "000" + String.valueOf(c);
//		Logs.infoLog("PhoneUtils.getMccFromSim: " + mcc);
//		return mcc.substring(mcc.length()-3);
		
		String mccmnc = getMccMnc(context);
		Logs.infoLog("PhoneUtils.getMccFromSim: " + mccmnc);
		if (mccmnc.length() >= 3) {
			return mccmnc.substring(0, 3);
		} else {
			return "";
		}
	}

	public static String getMncFromSim(Context context) {
//		int m = context.getResources().getConfiguration().mnc;

//		String mnc = "00" + String.valueOf(m);
//		Logs.infoLog("PhoneUtils.getMncFromSim: " + mnc);
//		return mnc.substring(mnc.length()-2);

		String mccmnc = getMccMnc(context);
		Logs.infoLog("PhoneUtils.getMccFromSim: " + mccmnc);
		if (mccmnc.length() >= 5) {
			return mccmnc.substring(mccmnc.length()-2);
		} else {
			return "";
		}
	}


	public static String getMccMnc(final Context context) {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final int configMcc = context.getResources().getConfiguration().mcc;
		final int configMnc = context.getResources().getConfiguration().mnc;
		
		if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
			return tm.getSimOperator();
		} else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
			return tm.getNetworkOperator();
		} else if (configMcc != 0 && configMnc != 0) {
			return String.format(Locale.getDefault(), "%03d%d",
					configMcc,
					configMnc == Configuration.MNC_ZERO ? 0 : configMnc);
		} else {
			return null;
		}
	}
}
