package edu.berkeley.cs160.teamk;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScoresDBAdapter extends BaseDBAdapter {
	private static final String URL_BASE = 
			"https://secure.ocf.berkeley.edu/~goodfrie/";
	private static final String URL_SCORES_LEADERBOARD = 
			"getLeaderboard.php";
	
	
	public ArrayList< HashMap<String, String> > scores;
	
	
	public ScoresDBAdapter() {
		scores = new ArrayList< HashMap<String, String> >();
	}
	
	
	public void getLeaderboard() {
		String result = getDatabaseOutput(
				URL_BASE + URL_SCORES_LEADERBOARD, emptyPair());
		scores = parseScoresJSONData(result);
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
}
