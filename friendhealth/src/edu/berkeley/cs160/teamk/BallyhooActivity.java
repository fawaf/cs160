package edu.berkeley.cs160.teamk;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.android.*;

public class BallyhooActivity extends Activity {
	Button fH_button;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ballyhooactivity);
        
        String name = "";
		int score = 0;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			name = extras.getString("name");
			score = extras.getInt("score");
			
			TextView txt_ActivityName = (TextView) findViewById(R.id.txt_ActivityName);
			txt_ActivityName.setText(name + " (+" + score + " points)");
		}
		//setContentView(R.layout.ballyhooactivity);
		
		//---get the fH Activity button---
		fH_button = (Button) findViewById(R.id.btn_Invite);
        
        //---event handler for the fH Activity button---
        fH_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		try {
        			EditText edt_Message = (EditText) findViewById(R.id.edt_Message);
        			String message = edt_Message.getText().toString();
        			SharedPreferences mPref = getSharedPreferences("LoginActivity", MODE_PRIVATE);
        			Bundle bundle = new Bundle();
        			bundle.putString("message", message);
        			bundle.putString(Facebook.TOKEN, mPref.getString("access_token", "aelkgjalwekgj"));
        			Log.d("fh",""+mPref.getString("access_token", "aelkgjalwekgj"));
        			/*String response =*/ Utility.facebook.request("me/feed", bundle, "POST");
        		} catch (MalformedURLException e) {
        		} catch (IOException e) {
        		}
        	}
        });
	}
}
