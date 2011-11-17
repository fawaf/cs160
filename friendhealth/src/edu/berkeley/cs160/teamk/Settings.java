package edu.berkeley.cs160.teamk;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import android.content.SharedPreferences;

public class Settings extends Activity {
	ToggleButton toggle_sound;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		// ToggleButton.
		ToggleButton toggle_sound =
				(ToggleButton) findViewById(R.id.toggleSound);
		toggle_sound.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Utility.mPrefs = getSharedPreferences(
						"FHActivitySelector", MODE_PRIVATE);
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putBoolean("toggle_sound",
						((ToggleButton) v).isChecked());
				Toast.makeText(getBaseContext(),
						((ToggleButton) v).isChecked()
						? "Sound Toggled ON"
						: "Sound Toggled OFF",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}