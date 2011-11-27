package edu.berkeley.cs160.teamk;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import android.net.Uri;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class DBAdapter {
	
	public static final String URL_BASE = 
			"https://secure.ocf.berkeley.edu/~goodfrie/";
	public static final String URL_ACT1 = "getRandomActivity.php";
	public static final String URL_ACT3 = "getRandomActivities.php";
	public static final String URL_UPDATE = "updateActivity.php";
	public static final String URL_ADD = "addActivity.php";
	public static final String URL_GET = "getActivityByID.php";
	public static final String URL_PHOTO_GET = "getPhotoByID.php";
	public static final String URL_ADD_PHOTO = "addPhoto.php";
	
	public Task[] tasks;
	
	public DBAdapter() {
		Log.d("DBA", "Initializing empty tasks.");
		initEmptyTasks(3);
		setAllRandomActivities();
	}
	
	public DBAdapter(int id1, int id2, int id3) {
		tasks = new Task[3];
		tasks[0] = getActivityByID(id1);
		tasks[1] = getActivityByID(id2);
		tasks[2] = getActivityByID(id3);
	}
	
	public Task getActivityByID(int id) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("id", String.valueOf(id)));
		
		String result = getDatabaseOutput(URL_BASE + URL_GET, pairs);
		Task[] task = parseJSONData(result);
		return task[0];
	}	
	
	public String[] getPhotoByID(int id) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("actid", String.valueOf(id)));
		
		String result = getDatabaseOutput(URL_BASE + URL_PHOTO_GET, pairs);
		String[] photoids = {""};
		try {
			JSONArray photos = new JSONArray(result);
			photoids = new String[photos.length()];
			for(int i = 0; i<photos.length(); i++){
				JSONObject obj = photos.getJSONObject(i);
				photoids[i] = obj.optString("photo_id");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return photoids;
	}
	
	private Task setNewRandomActivity(int index) {
		String result = getDatabaseOutput(
				URL_BASE + URL_ACT1, findIDsReplace());
		Task[] new_task = parseJSONData(result);
		tasks[index] = new_task[0];
		return new_task[0];
	}
	
	public void setAllRandomActivities() {
		Log.d("DBA", "setAllRandomActivities()");
		String result = getDatabaseOutput(
				URL_BASE + URL_ACT3, findIDsReplace());
		Task[] new_tasks = parseJSONData(result);
		for (int i = 0 ; i < tasks.length ; ++i) {
			tasks[i] = new_tasks[i];
		}
	}
	
	public Task declineActivity(int index) {
		tasks[index].timesDeclined++;
		updateActivity(index);
		return setNewRandomActivity(index);
	}
	
	public Task acceptActivity(int index) {
		tasks[index].timesAccepted++;
		updateActivity(index);
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
			Log.e("DBA", "Error Adding: " + result);
		}
	}
	
	public void addPhoto(String activityid, String photoid){
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(
				"actid", activityid));
		pairs.add(new BasicNameValuePair(
				"photoid", photoid));
		String result = getDatabaseOutput(URL_BASE + URL_ADD_PHOTO, pairs);
		if(!result.equals("SUCCESS")) {
			Log.d("log_tag", "Error Adding: " + result);
		}
	}
	
	public int getID(int index) {
		return tasks[index].id;
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
		Log.d("DBA", "findIDsReplace()");
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		
		for (int i = 0 ; i < tasks.length ; ++i) {
			pairs.add(new BasicNameValuePair(
					"r" + String.valueOf(i + 1),
					String.valueOf(tasks[i].id)));
			Log.d("DBA", "	index: " + String.valueOf(i) +
					", name: " + pairs.get(i).getName() +
					", value: " + pairs.get(i).getValue());
		}
		
		return pairs;
	}
	
	private String getDatabaseOutput(
			String url, ArrayList<NameValuePair> nameValuePairs) {
		Log.d("DBA", "getDatabaseOutput(" + url + ")");
		String result = "";
		
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
		
		// convert response to string
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
		String url_comb = url + "?";
		for (int i = 0 ; i < nameValuePairs.size() ; ++i) {
			String name = nameValuePairs.get(i).getName();
			String value = nameValuePairs.get(i).getValue();
			url_comb += name + "=" + Uri.encode(value);
			if (i + 1 < nameValuePairs.size()) {
				url_comb += "&";
			}
		}
		Log.d("DBA", "URL is: " + url_comb);
		return url_comb;
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
			Log.e("DBA", "Error parsing data: " + e.toString());
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
		Log.d("DBA", "result is: " + result);
		if (!result.equals("SUCCESS")) {
			Log.e("DBA", "Error Updating: " + result);
		}	
	}
	
	private void initEmptyTasks(int size) {
		tasks = new Task[size];
		for (int i = 0 ; i < size ; ++i) {
			tasks[i] = new Task();
		}
	}
	
}