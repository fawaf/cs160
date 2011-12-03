package edu.berkeley.cs160.teamk;

import java.io.InputStream;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import android.net.Uri;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class BaseDBAdapter {
	
	public BaseDBAdapter() {
		
	}
	
	protected ArrayList<NameValuePair> emptyPair() {
		return new ArrayList<NameValuePair>();
	}
	
	protected String getDatabaseOutput(
			String url, ArrayList<NameValuePair> nameValuePairs) {
		String result = "";
		
		Log.d("DBA", "getDatabaseOutput(" + url + ")");
		// HTTP post
		InputStream is = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(formatURL(url, nameValuePairs));
			Log.d("DBA", "HTTP POST: " + httpGet.getURI().toString());
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}
		catch (Exception e) {
			Log.e("DBA", "Error in HTTP connection: " + e.toString());
		}
		
		// convert response to string.
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";
			while ((line = reader.readLine()) != null) {
				Log.d("DBA", "Convert to String: " + line);
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		}
		catch (Exception e) {
			Log.e("DBA", "Error converting result: " + e.toString());
		}
		
		Log.d("DBA", "gDO Output: " + result);
		return result;
	}
	
	
	private String formatURL(
			String url, ArrayList<NameValuePair> nameValuePairs) {
		String url_cat = url + "?";
		for (int i = 0 ; i < nameValuePairs.size(); ++i) {
			String name = nameValuePairs.get(i).getName();
			String value = nameValuePairs.get(i).getValue();
			url_cat += name + "=" + Uri.encode(value);
			if (i + 1 < nameValuePairs.size()) {
				url_cat += "&";
			}
		}
		Log.d("DBA", "URL is: " + url_cat);
		return url_cat;
	}
	
}