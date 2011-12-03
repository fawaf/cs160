package edu.berkeley.cs160.teamk;

// Copied from https://gist.github.com/1256137
import android.app.Activity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Leaderboard extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leaderboard);
		
		ListView leaderboard = (ListView) findViewById(R.id.leaderboard);
		
		String fb_user_id = Utility.mPrefs.getString("facebookUID", "");
		Utility.scoresDBAdapter.calculateUserScore(fb_user_id);
		Utility.scoresDBAdapter.getLeaderboard();
		SimpleAdapter adapter = new SimpleAdapter(
				this,
				Utility.scoresDBAdapter.scores,
				R.layout.leaderboard_item,
				new String[] {"Standing", "Score", "Player"},
				new int[] {R.id.standingText, R.id.scoreText, R.id.userText});
		leaderboard.setItemChecked(0, false);
		leaderboard.setItemChecked(1, true);
		leaderboard.setAdapter(adapter);
	}
	
	/*
	private void populateList() {
		HashMap<String, String> temp1 = new HashMap<String, String>();
		temp1.put("Standing", "1");
		temp1.put("Score", "100");
		temp1.put("Player", "Gong Cheng");
		scores.add(temp1);
		
		HashMap<String, String> temp2 = new HashMap<String, String>();
		temp2.put("Standing", "2");
		temp2.put("Score", "85");
		temp2.put("Player", "Manduo Dong");
		scores.add(temp2);
		
		HashMap<String, String> temp3 = new HashMap<String, String>();
		temp3.put("Standing", "3");
		temp3.put("Score", "45");
		temp3.put("Player", "Scott Goodfriend");
		scores.add(temp3);
	}
	*/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	OptionsMenu om = new OptionsMenu();
    	om.CreateMenu(menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	OptionsMenu om = new OptionsMenu();
    	return om.MenuChoice(this, item);
    }
}