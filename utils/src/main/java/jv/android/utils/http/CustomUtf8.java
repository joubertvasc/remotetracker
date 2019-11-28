package jv.android.utils.http;

import java.net.URLEncoder;
import java.util.Map;

public class CustomUtf8 {
	
	public static String encode(Map<String,String> data) {
		StringBuilder sb = new StringBuilder();
		
        for(String key : data.keySet()) {
            if (data.get(key) != null) {
                String value = null;

                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");

                    if (sb.length() > 0)
                        sb.append("&");

                    sb.append(key + "=" + value);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        
        return sb.toString();
	}

}
