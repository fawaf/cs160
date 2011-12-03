package edu.berkeley.cs160.teamk;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.content.SharedPreferences;


public class Settings extends Activity {
	
	ToggleButton toggle_sound;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		// Toggle sound
		ToggleButton toggle_sound =
				(ToggleButton) findViewById(R.id.toggleSound);
		toggle_sound.setChecked(
				Utility.mPrefs.getBoolean("toggle_sound", true));
		toggle_sound.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putBoolean("toggle_sound",
						((ToggleButton) v).isChecked());
				Toast.makeText(getBaseContext(),
						((ToggleButton) v).isChecked()
						? "Sound Toggled ON"
						: "Sound Toggled OFF",
						Toast.LENGTH_SHORT).show();
				editor.commit();
			}
		});
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