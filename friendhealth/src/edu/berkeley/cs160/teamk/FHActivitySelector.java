package edu.berkeley.cs160.teamk;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class FHActivitySelector extends Activity {
	Button fH_button;
	Button btn_login;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
      //---get the fH Activity button---
        fH_button = (Button) findViewById(R.id.btn_fHActivity);
        
        //---event handler for the fH Activity button---
        fH_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		startActivity(new Intent(
        				"edu.berkeley.cs160.teamk.FHActivity"));
        	}
        });
        
        //---get the Login button---
        btn_login = (Button) findViewById(R.id.btn_login);
        
        //---event handler for the fH Activity button---
        btn_login.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		startActivity(new Intent(
        				"edu.berkeley.cs160.teamk.LoginActivity"));
        	}
        });
    }
}
