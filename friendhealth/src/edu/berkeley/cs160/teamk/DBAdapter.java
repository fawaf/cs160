package edu.berkeley.cs160.teamk;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DBAdapter extends BaseDBAdapter {
	
	public static final String URL_BASE = 
			"https://secure.ocf.berkeley.edu/~goodfrie/";
	public static final String URL_ACTIVITY_GET = "getRandomActivity.php";
	public static final String URL_ACTIVITY_GET_RANDOM = "getRandomActivities.php";
	public static final String URL_ACTIVITY_UPDATE = "updateActivity.php";
	public static final String URL_ACTIVITY_ADD = "addActivity.php";
	public static final String URL_ACTIVITY_GET_ID = "getActivityByID.php";
	public static final String URL_PHOTO_GET = "getPhotoByID.php";
	public static final String URL_INFO_ADD = "addUserInfo.php";
	public static final String URL_SCORE_GET = "getScoreByID.php";
	public static final String URL_SCORE_ADD = "addScore.php";
	public static final String URL_ACTIVITY_ACCEPT = "acceptActivity.php";
	public static final String URL_ACTIVITY_REJECT = "rejectActivity.php";
	public static final String URL_ACTIVITY_FLAG = "flagActivity.php";
	
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
		String result = getDatabaseOutput(
				URL_BASE + URL_ACTIVITY_GET_ID, getBoundPair(id));
		Task[] task = parseJSONData(result);
		return task[0];
	}
	
	private ArrayList<NameValuePair> getBoundPair(int id) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("id", String.valueOf(id)));
		return pairs;
	}	
 
	public Task declineActivity(int index) {
		return setNewRandomActivity(index);
	}
	
	public Task acceptActivity(int index) {
		getDatabaseOutput(
				URL_BASE + URL_ACTIVITY_ACCEPT, getBoundPair(getID(index)));
		return setNewRandomActivity(index);
	}
	
	public Task rejectDifficultActivity(int index) {
		getDatabaseOutput(
				URL_BASE + URL_ACTIVITY_REJECT, getBoundPair(getID(index)));
		return setNewRandomActivity(index);
	}
	
	public Task flagActivity(int index) {
		getDatabaseOutput(
				URL_BASE + URL_ACTIVITY_FLAG, getBoundPair(getID(index)));
		return setNewRandomActivity(index);
	}	
	
	public String[] getPhotoByID(int id) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("act_id", String.valueOf(id)));
		
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
			e.printStackTrace();
		}
		return photoids;
	}
	
	private Task setNewRandomActivity(int index) {
		String result = getDatabaseOutput(
				URL_BASE + URL_ACTIVITY_GET, findIDsReplace());
		Task[] new_task = parseJSONData(result);
		tasks[index] = new_task[0];
		return new_task[0];
	}
	
	public void setAllRandomActivities() {
		Log.d("DBA", "setAllRandomActivities()");
		String result = getDatabaseOutput(
				URL_BASE + URL_ACTIVITY_GET_RANDOM, findIDsReplace());
		Task[] new_tasks = parseJSONData(result);
		for (int i = 0 ; i < tasks.length ; i++) {
			tasks[i] = new_tasks[i];
		}
	}
	
	public void addActivity(Task new_task) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(
				"name", new_task.name));
		pairs.add(new BasicNameValuePair(
				"points", String.valueOf(new_task.points)));
		
		String result = getDatabaseOutput(URL_BASE + URL_ACTIVITY_ADD, pairs);
		if (!result.equals("SUCCESS")) {
			Log.e("DBA", "Error Adding: " + result);
		}
	}
	
	public void addUserInfo(String activityid, String photoid, int base_score, String user_id){
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(
				"act_id", activityid));
		pairs.add(new BasicNameValuePair(
				"photo_id", photoid));
		pairs.add(new BasicNameValuePair(
				"base_score", base_score+""));
		pairs.add(new BasicNameValuePair(
				"fb_user_id", user_id));
		String result = getDatabaseOutput(URL_BASE + URL_INFO_ADD, pairs);
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
						json_data.getInt("timesRejected"),
						json_data.getInt("timesAccepted"));
			}
		}
		catch (JSONException e) {
			Log.e("DBA", "Error parsing data: " + e.toString());
		}
		return new_tasks;
	}
	
	private void initEmptyTasks(int size) {
		tasks = new Task[size];
		for (int i = 0 ; i < size ; ++i) {
			tasks[i] = new Task();
		}
	}
	
}