package jv.android.utils.http;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import jv.android.utils.Logs;
import jv.android.utils.http.CustomHttp;

public class CustomHttp {
	
	public static String GET = "GET";
	public static String POST = "POST";
	public static int TIMEOUT = 30000;
	
	public static CustomHttpResult httpGet(String serverAddress) {
		return httpGet(serverAddress, TIMEOUT); 
	}
	
	public static CustomHttpResult httpGet(String serverAddress, int timeout) {
		return sendToServer(serverAddress, null, GET, timeout); 
	}
	
	public static CustomHttpResult httpPost(String serverAddress) {
		return httpPost(serverAddress, null, TIMEOUT);
	}
	
	public static CustomHttpResult httpPost(String serverAddress, int timeout) {
		return httpPost(serverAddress, null, timeout);
	}
	
	public static CustomHttpResult httpPost(String serverAddress, Map<String,String> params) {		
		return httpPost(serverAddress, params, TIMEOUT);
	}	
	
	public static CustomHttpResult httpPost(String serverAddress, Map<String,String> params, int timeout) {		
		if (params != null && params.size() > 0) {
			return sendToServer(serverAddress, params, POST, timeout); 
		} else {
			return sendToServer(serverAddress, null, POST, timeout); 
		}
	}	
	
	private static CustomHttpResult sendToServer(String serverAddress, Map<String,String> params, String method, int timeout) {
		CustomHttpResult result = new CustomHttpResult();
	    String encodedStr = "";
	    
	    if (params != null)
	    	encodedStr = CustomUtf8.encode(params);
	    
	    BufferedReader reader = null;

		try {
			// Converting address String to URL
			URL url = new URL(serverAddress);
			// Opening the connection (Not setting or using CONNECTION_TIMEOUT)
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			try {
				// Post Method
				con.setRequestMethod(method);
				con.setConnectTimeout(timeout);
				con.setReadTimeout(timeout);
//                con.setRequestProperty("Acceptcharset", "en-us");
//                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//                con.setRequestProperty("charset", "EN-US");
//                con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//                con.setRequestProperty("Content-type", "application/json; charset=UTF-8");

				// To enable inputting values using POST method
				// (Basically, after this we can write the dataToSend to the body of POST method)
				if (method.equalsIgnoreCase(POST)) {
                    con.setDoOutput(true);
//                    con.setDoInput(true);
				}

				if (encodedStr != null && !encodedStr.equals("")) {
					InterruptThread it = new InterruptThread(con, timeout);
					it.run();

					OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

					it.setOk(true);
					// Writing dataToSend to outputstreamwriter
					writer.write(encodedStr);
					// Sending the data to the server - This much is enough to send data to server
					// But to read the response of the server, you will have to implement the procedure below
					writer.flush();
				}

				// Data Read Procedure - Basically reading the data comming line by line
				StringBuilder sb = new StringBuilder();
				reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

				String line;
				while ((line = reader.readLine()) != null) { // Read till there is something available
					sb.append(line + "\n");     // Reading and saving line by line - not all at once
				}
				line = sb.toString();           // Saving complete data received in string, you can do it differently

				// Fill the result class
				result.setServerRequestCode(con.getResponseCode());
				result.setSuccess(true);
				result.setHttpResult(line);

				// Just check to the values received in Logcat
				Logs.infoLog("CustomHttp.sendToServer (code=" + String.valueOf(result.getServerRequestCode()) + ") : " + line);
			} catch (FileNotFoundException e){
				Logs.errorLog("CustomHttp.sendToServer filenotfound exception: ", e);

				result.setServerRequestCode(con.getResponseCode());
				result.setException(e.getMessage());
				result.setSuccess(false);
		    }
	    } catch (Exception e) {
	        Logs.errorLog("CustomHttp.sendToServer error: ", e);

			result.setServerRequestCode(-1);
	        result.setException(e.getMessage());
	        result.setSuccess(false);
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();     //Closing the 
	            } catch (IOException e) {
	    	        Logs.errorLog("CustomHttp.sendToServer error: ", e);

	    	        result.setException(e.getLocalizedMessage());
	    	        result.setSuccess(false);
	            }
	        }
	    }
	    
	    return result;
	}

	public static String sendJSON(String server, String json) {
		return sendJSON(server, json, TIMEOUT);
	}

	public static String sendJSON(String server, String json, int timeout) {
		try {
			URL url = new URL(server);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);

            InterruptThread it = new InterruptThread(conn, timeout);
            it.run();

			OutputStream os = conn.getOutputStream();

			final String jsonUTF8 = new String(json.getBytes(), "UTF-8");

            it.setOk(true);
			os.write(jsonUTF8.getBytes());
			os.flush();
            os.close();

            try {
                Logs.infoLog("CustomHttp.sendJSON request body: " + jsonUTF8.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

			if (conn.getResponseCode() != 200) {
				return String.valueOf(conn.getResponseCode()) + " - " + conn.getResponseMessage();
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String result = "";
				String output;
				while ((output = br.readLine()) != null) {
					result = result + output;
				}
				
				return result;
			}
		} catch (MalformedURLException e) {			
			return e.getLocalizedMessage();
		} catch (IOException e) {
			return e.getLocalizedMessage();
		}		
		
	}
	
}