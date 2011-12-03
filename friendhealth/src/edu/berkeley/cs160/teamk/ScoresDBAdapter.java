package edu.berkeley.cs160.teamk;

import java.util.ArrayList;
import java.util.HashMap;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScoresDBAdapter extends BaseDBAdapter {
	
	private static final String URL_BASE = 
			"https://secure.ocf.berkeley.edu/~goodfrie/";
	private static final String URL_SCORES_SORTEDLEADERBOARD =
			"getSortedLeaderboard.php";
	private static final String URL_SCORES_USERCHECK =
			"checkUserScore.php";
	private static final String URL_SCORES_USERCALCULATE =
			"calculateUserScore.php";
	public static final String URL_SCORES_BASE_GET = "getBaseScore.php";
	public static final String URL_PHOTO_ID_GET = "getPhotoByFBID.php";
	
	
	public ArrayList< HashMap<String, String> > scores;
	public int points;
	public int rank;
	
	
	public ScoresDBAdapter() {
		scores = new ArrayList< HashMap<String, String> >();
	}
	
	
	public void getLeaderboard() {
		String result = getDatabaseOutput(
				URL_BASE + URL_SCORES_SORTEDLEADERBOARD, emptyPair());
		scores = parseScoresJSONData(result);
	}
	
	
	public void checkUserScore(String fb_user_id, String name) {
		Log.d("DBA", "checkUserScore(" + fb_user_id + ", " + name + ")");
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(
				"fb_id", fb_user_id));
		pairs.add(new BasicNameValuePair(
				"name", name));
		
		String result = getDatabaseOutput(
				URL_BASE + URL_SCORES_USERCHECK, pairs);
		parseUserScoreJSONData(result);
		Log.d("DBA", "Points: " + points + " Rank: " + rank);
	}
	
	public String getBaseScore(String photo_id) {
		Log.d("DBA", "getBaseScore(" + photo_id + ")");
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(
				"photo_id", photo_id));
		
		String result = getDatabaseOutput(
				URL_BASE + URL_SCORES_BASE_GET, pairs);
		Log.d("DBA", "Base Score: " + result);
		return result;
	}
	
	public String[] getPhotoID(String fb_user_id) {
		Log.d("DBA", "getPhotoID(" + fb_user_id + ")");
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(
				"fb_user_id", fb_user_id));
		
		String result = getDatabaseOutput(
				URL_BASE + URL_PHOTO_ID_GET, pairs);
		Log.d("DBA", "Photo ID: " + result);
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
	
	public void calculateUserScore(String fb_user_id) {
		Log.d("DBA", "calculateTotalScore(" + fb_user_id + ")");
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(
				"fb_user_id", fb_user_id));

		Scores.calculateFacebookScore(fb_user_id);
		
		String result = getDatabaseOutput(
				URL_BASE + URL_SCORES_USERCALCULATE, pairs);
		parseUserScoreJSONData(result);
		Log.d("DBA", "Points: " + points + " Rank: " + rank);
	}
	
	
	private ArrayList< HashMap<String, String> > parseScoresJSONData(
			String result) {
		ArrayList< HashMap<String, String> > leaderboard = 
				new ArrayList< HashMap<String, String> >();
		try {
			JSONArray jArray = new JSONArray(result);
			
			for (int i = 0 ; i < jArray.length() ; ++i) {
				JSONObject json_data = jArray.getJSONObject(i);
				HashMap<String, String> player = new HashMap<String, String>();
				player.put("Standing", String.valueOf(json_data.getInt("rank")));
				player.put("Score", String.valueOf(json_data.getInt("points")));
				player.put("ID", String.valueOf(json_data.getInt("fb_user_id")));
				player.put("Player", json_data.getString("fb_name"));
				leaderboard.add(player);
			}
		}
		catch (JSONException e) {
			Log.e("DBA", "Error parsing data: " + e.toString());
		}
		
		return leaderboard;
	}
	
	
	private void parseUserScoreJSONData(String result) {
		try {
			JSONArray jArray = new JSONArray(result);
			
			JSONObject json_data = jArray.getJSONObject(0);
			
			points = json_data.getInt("points");
			rank = json_data.getInt("rank");
		}
		catch (JSONException e) {
			Log.e("DBA", "Error parsing data (pUSJD): " + e.toString());
		}
	}
	
}
