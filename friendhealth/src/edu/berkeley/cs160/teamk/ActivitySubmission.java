package edu.berkeley.cs160.teamk;


import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.Facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Log;


public class ActivitySubmission extends Activity {
	String act_name = "";
	String img_filename = "";
	int score = 0;
	Button fH_button;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitysubmission);
		
		Log.d("friendHealth", "ActivitySubmission drawn");
		
		//---display the image taken---
		ImageView imageView = (ImageView) findViewById(R.id.img_submit);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			act_name = extras.getString("name");
			img_filename = extras.getString("filename");
			score = extras.getInt("score");
			
			Log.d("friendHealth", "Full name " + img_filename);
			String shortname = img_filename.substring(11);
			Log.d("friendHealth", "Display " + shortname);
			
			Bitmap myBitmap 
					= BitmapFactory.decodeFile(shortname);
			Log.d("friendHealth", "Displaying image");
			imageView.setImageBitmap(myBitmap);
		
			//---get the Submit button---
			fH_button = (Button) findViewById(R.id.btn_Submit);
	        
	        //---event handler for the Submit button---
	        fH_button.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view) {
	        		try {
	        			SharedPreferences mPref = getSharedPreferences("LoginActivity", MODE_PRIVATE);
	        			Bundle bundle = new Bundle();
	        			bundle.putString("message", "Photo for " + act_name);
	        			bundle.putString(Facebook.TOKEN, mPref.getString("access_token", null));
	        			bundle.putString("image", img_filename);
	        			String response = Utility.facebook.request("me/photos", bundle, "POST");
	        			
	        			if(response.indexOf("OAuthException") > -1){
	        				response = "Submission Failed";
	        			}else{
	        				response = "Submission Successful";
	        			}
	        			
	        			Intent intent = new Intent("edu.berkeley.cs160.teamk.FHActivity");
	        			Bundle extras = getIntent().getExtras();
	        			extras.putString("submit_response", response);
	        			intent.putExtras(extras);
	        			startActivity(intent);
	        		} catch (MalformedURLException e) {
	        		} catch (IOException e) {
	        		}
	        	}
	        });
		}
	}
}
