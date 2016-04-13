package com.example.posterdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class NetUtils {

	 public static String appendParams(String url, Map<String, String> params)
	    {
	        StringBuilder sb = new StringBuilder();
	        sb.append(url + "&");
	        if (params != null && !params.isEmpty())
	        {
	            for (String key : params.keySet())
	            {
	                sb.append(key).append("=").append(params.get(key)).append("&");
	            }
	        }

	        sb = sb.deleteCharAt(sb.length() - 1);
	        return sb.toString();
	    }


		public static  String convertStreamToString(InputStream is) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();

		}

}
