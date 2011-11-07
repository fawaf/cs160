package edu.berkeley.cs160.teamk;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.android.*;

public class BallyhooActivity extends Activity {
	Button fH_button;
	String name = "";
	int score = 0;
	String origVal = "";
	EditText edt_Message;
	String message;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ballyhooactivity);
        
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			name = extras.getString("name");
			score = extras.getInt("score");
			
			TextView txt_ActivityName = (TextView) findViewById(R.id.txt_ActivityName);
			txt_ActivityName.setText(name + " (+" + score + " points)");
		}
		
		origVal = "I am going to perform the activity: " + name;
		
		edt_Message = (EditText) findViewById(R.id.edt_Message);
		edt_Message.setText(origVal);
		edt_Message.setOnClickListener(new View.OnClickListener() {
			String message = edt_Message.getText().toString();

			@Override
			public void onClick(View v) {
					if(message.equals(origVal)) {
						edt_Message.setText("");
					}
				}
			});

		//---get the Invite button---
		fH_button = (Button) findViewById(R.id.btn_Invite);
        
        //---event handler for the Invite button---
        fH_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		try {
        			EditText edt_Message = (EditText) findViewById(R.id.edt_Message);
        			message = edt_Message.getText().toString();
        			if (message.equals("")) {
        				message = origVal;
        			}
        			
        			Log.d("friendHealthBA", "Edit message: " + message);
        			
        			Utility.mPrefs = getSharedPreferences("LoginActivity", MODE_PRIVATE);
        			Bundle bundle = new Bundle();
        			bundle.putString("message", message);
        			bundle.putString(Facebook.TOKEN, Utility.mPrefs.getString("access_token", null));
        			Log.d("friendHealthBA", "Access_token: " + Utility.mPrefs.getString("access_token", null));
        			String response = Utility.facebook.request("me/feed", bundle, "POST");
        			
        			if(response.indexOf("OAuthException") > -1){
        				Log.d("friendHealthBA", "Response: " + response);
        				response = "Invitation Failed";
        			}else{
        				response = "Invitation Successful";
        				setResult(RESULT_OK);
        			}
        			
        			Intent intent = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        			Bundle extras = getIntent().getExtras();
        			extras.putString("response", response);
        			intent.putExtras(extras);
        			startActivity(intent);
        		} catch (MalformedURLException e) {
        		} catch (IOException e) {
        		}
        	}
        });
	}
	}
