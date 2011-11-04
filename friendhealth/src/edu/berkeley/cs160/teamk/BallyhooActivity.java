package edu.berkeley.cs160.teamk;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class BallyhooActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.ballyhooactivity);
        
        String name = "";
		int score = 0;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			name = extras.getString("name");
			score = extras.getInt("score");
			
			TextView txt_ActivityName = (TextView) findViewById(R.id.txt_ActivityName);
			txt_ActivityName.setText(name + " (+" + score + " points)");
		}
		setContentView(R.layout.ballyhooactivity);
		
		EditText edt_Message = (EditText) findViewById(R.id.edt_Message);
		String message = edt_Message.getText().toString();
		SharedPreferences mPref = getSharedPreferences("LoginActivity", MODE_PRIVATE);
		
		try {
			Bundle bundle = new Bundle();
			bundle.putString(message, "test update");
			bundle.putString(Facebook.TOKEN, mPref.getString("access_token", null));
			String response = Utility.facebook.request("me/feed", bundle, "POST");
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
	}
}
