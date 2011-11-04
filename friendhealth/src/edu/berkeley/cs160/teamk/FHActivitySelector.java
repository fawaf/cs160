package edu.berkeley.cs160.teamk;


import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import android.util.Log;


public class FHActivitySelector extends Activity {
	Button fH_button;
	Button btn_login;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d("friendHealth", "Starting...");
        
        //---Immediately try to log in.---
        startActivity(new Intent(
				"edu.berkeley.cs160.teamk.LoginActivity"));
        
        Log.d("friendHealth", "Logged in");
        
        //---get the fH Activity button---
        fH_button = (Button) findViewById(R.id.btn_fHActivity);
        
        //---event handler for the fH Activity button---
        fH_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", "Eat an Apple");
        		extras.putInt("score", 10);
        		i.putExtras(extras);
        		startActivity(i);
        	}
        });
    }
}
