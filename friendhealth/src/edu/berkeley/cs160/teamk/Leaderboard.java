package edu.berkeley.cs160.teamk;

// Copied from https://gist.github.com/1256137
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
		Utility.scoresDBAdapter.calculateUserTotalScore(fb_user_id);
		Log.d("friendHealthL", "FB User: " + fb_user_id + 
				" (" + Utility.scoresDBAdapter.rank + ")");
		SimpleAdapter adapter = new SimpleAdapter(
				this,
				Utility.scoresDBAdapter.scores,
				R.layout.leaderboard_item,
				new String[] {"Standing", "Score", "Player"},
				new int[] {R.id.standingText, R.id.scoreText, R.id.userText});
		adapter.getView(0, null, null).setBackgroundColor(Color.GREEN);
		leaderboard.setAdapter(adapter);
		
		Log.d("friendHealthL", "leaderboard: " + leaderboard.getChildCount() +
				" out of " + adapter.getCount());
	}
	
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
