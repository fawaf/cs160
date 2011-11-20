package edu.berkeley.cs160.teamk;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DBAdapter {
	public static final String URL_BASE = 
			"https://secure.ocf.berkeley.edu/~goodfrie/";
	public static final String URL_ACT1 = "getRandomActivity.php";
	public static final String URL_ACT3 = "getRandomActivities.php";
	public static final String URL_UPDATE = "updateActivity.php";
	public static final String URL_ADD = "addActivity.php";
	
	public Task[] tasks;
	
	
	public DBAdapter() {
		setAllRandomActivities();
	}
	
	
	private Task setNewRandomActivity(int index) {
		String result = getDatabaseOutput(
				URL_BASE + URL_ACT1, findIDsReplace());
		Task[] new_task = parseJSONData(result);
		tasks[index] = new_task[0];
		return new_task[0];
	}
	
	
	public void setAllRandomActivities() {
		String result = getDatabaseOutput(
				URL_BASE + URL_ACT3, findIDsReplace());
		Task[] new_tasks = parseJSONData(result);
		for (int i = 0 ; i < tasks.length ; ++i) {
			tasks[i] = new_tasks[i];
		}
	}
	
	
	public Task declineActivity(int index) {
		return setNewRandomActivity(index);
	}
	
	
	public Task rejectDifficultActivity(int index) {
		tasks[index].timesDeclined++;
		updateActivity(index);
		return setNewRandomActivity(index);
	}
	
	
	public Task flagActivity(int index) {
		tasks[index].timesFlagged++;
		updateActivity(index);
		return setNewRandomActivity(index);
	}
	
	
	public void addActivity(Task new_task) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(
				"name", new_task.name));
		pairs.add(new BasicNameValuePair(
				"points", String.valueOf(new_task.points)));
		
		String result = getDatabaseOutput(URL_BASE + URL_ADD, pairs);
		if (!result.equals("SUCCESS")) {
			Log.e("log_tag", "Error Adding: " + result);
		}
	}
	
	
	public String getName(int index) {
		return tasks[index].name;
	}
	
	public int getPoints(int index) {
		return tasks[index].points;
	}
	
	public String toString(int index) {
		return tasks[index].toString();
	}
	
	
	private ArrayList<NameValuePair> findIDsReplace() {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		
		for (int i = 0 ; i < tasks.length ; ++i) {
			pairs.add(new BasicNameValuePair(
					"r" + String.valueOf(i),
					String.valueOf(tasks[i].id)));
		}
		
		return pairs;
	}
	
	
	private String getDatabaseOutput(
			String url, ArrayList<NameValuePair> nameValuePairs) {
		String result = "";
		
		// http post
		InputStream is = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}
		catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		}
		catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		
		return result;
	}
	
	
	private Task[] parseJSONData(String result) {
		Task[] new_tasks = null;
		try {
			JSONArray jArray = new JSONArray(result);

			new_tasks = new Task[jArray.length()];
			for (int i = 0 ; i < jArray.length() ; ++i) {
				JSONObject json_data = jArray.getJSONObject(i);
				new_tasks[i] = new Task(
						json_data.getInt("id"),
						json_data.getString("name"),
						json_data.getInt("points"),
						json_data.getInt("timesFlagged"),
						json_data.getInt("timesDeclined"),
						json_data.getInt("timesAccepted"));
			}
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return new_tasks;
	}
	
	
	private void updateActivity(int index) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		
		pairs.add(new BasicNameValuePair(
				"id", String.valueOf(tasks[index].id)));
		pairs.add(new BasicNameValuePair(
				"name", tasks[index].name));
		pairs.add(new BasicNameValuePair(
				"points", String.valueOf(tasks[index].points)));
		pairs.add(new BasicNameValuePair(
				"tF", String.valueOf(tasks[index].timesFlagged)));
		pairs.add(new BasicNameValuePair(
				"tD", String.valueOf(tasks[index].timesDeclined)));
		pairs.add(new BasicNameValuePair(
				"tA", String.valueOf(tasks[index].timesAccepted)));
		
		String result = getDatabaseOutput(URL_BASE + URL_UPDATE, pairs);
		if (!result.equals("SUCCESS")) {
			Log.e("log_tag", "Error Updating: " + result);
		}	
	}
}