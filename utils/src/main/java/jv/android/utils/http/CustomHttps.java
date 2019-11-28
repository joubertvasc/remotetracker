package jv.android.utils.http;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import jv.android.utils.Logs;

/**
 * Created by joubert on 24/10/17.
 */

public class CustomHttps {

    public static String GET = "GET";
    public static String POST = "POST";
    public static int TIMEOUT = 30000;

    public static CustomHttpResult httpsGet(String serverAddress) {
        return httpsGet(serverAddress, TIMEOUT);
    }

    public static CustomHttpResult httpsGet(String serverAddress, int timeout) {
        return sendToServer(serverAddress, null, GET, timeout);
    }

    public static CustomHttpResult httpsPost(String serverAddress) {
        return httpsPost(serverAddress, null, TIMEOUT);
    }

    public static CustomHttpResult httpsPost(String serverAddress, int timeout) {
        return httpsPost(serverAddress, null, timeout);
    }

    public static CustomHttpResult httpsPost(String serverAddress, Map<String, String> params) {
        return httpsPost(serverAddress, params, TIMEOUT);
    }

    public static CustomHttpResult httpsPost(String serverAddress, Map<String, String> params, int timeout) {
        if (params != null && params.size() > 0) {
            return sendToServer(serverAddress, params, POST, timeout);
        } else {
            return sendToServer(serverAddress, null, POST, timeout);
        }
    }

    private static CustomHttpResult sendToServer(String serverAddress, Map<String, String> params, String method, int timeout) {
        CustomHttpResult result = new CustomHttpResult();
        String encodedStr = "";

        if (params != null)
            encodedStr = CustomUtf8.encode(params);

        BufferedReader reader = null;

        try {
            // Converting address String to URL
            URL url = new URL(serverAddress);
            trustAllHosts();
            // Opening the connection (Not setting or using CONNECTION_TIMEOUT)
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            // Post Method
            con.setRequestMethod(method);
            con.setConnectTimeout(timeout);
            con.setReadTimeout(timeout);
            con.setHostnameVerifier(DO_NOT_VERIFY);

            // To enable inputting values using POST method
            // (Basically, after this we can write the dataToSend to the body of POST method)
            if (method.equalsIgnoreCase(POST))
                con.setDoOutput(true);

            if (!encodedStr.equals("")) {
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
        } catch (Exception e) {
            Logs.errorLog("CustomHttp.sendToServer error: ", e);

            result.setException(e.getLocalizedMessage());
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

    public static String sendHttpsJSON(String server, String json) {
        return sendHttpsJSON(server, json, TIMEOUT);
    }

    public static String sendHttpsJSON(String server, String json, int timeout) {
        try {
            URL url = new URL(server);
            trustAllHosts();
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setHostnameVerifier(DO_NOT_VERIFY);

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

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
