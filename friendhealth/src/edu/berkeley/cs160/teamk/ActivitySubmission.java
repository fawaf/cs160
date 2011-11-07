package edu.berkeley.cs160.teamk;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.Facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.util.Log;


public class ActivitySubmission extends Activity {
	String act_name = "";
	String img_filename = "";
	int score = 0;
	Button fH_button;
	String origVal = "";
	EditText edt_Caption;
	Bundle bundle = new Bundle();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitysubmission);
		
		Log.d("friendHealthAS", "ActivitySubmission drawn");
		
		//---display the image taken---
		ImageView imageView = (ImageView) findViewById(R.id.img_submit);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			act_name = extras.getString("name");
			img_filename = extras.getString("filename");
			score = extras.getInt("score");
			
			Log.d("friendHealthAS", "Full name " + img_filename);
			String shortname = img_filename.substring(11);
			Log.d("friendHealthAS", "Display " + shortname);
			
			Bitmap myBitmap 
					= BitmapFactory.decodeFile(shortname);
			Log.d("friendHealthAS", "Displaying image");
			imageView.setImageBitmap(myBitmap);
			Log.d("friendHealthAS", "Image displayed");
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			myBitmap.compress(CompressFormat.JPEG, 50, bos);
			bundle.putByteArray("picture", bos.toByteArray());
			SharedPreferences mPref = getSharedPreferences("LoginActivity", MODE_PRIVATE);
			bundle.putString(Facebook.TOKEN, mPref.getString("access_token", null));
			Log.d("friendHealthAS", "access_token: " + mPref.getString("access_token", null));
			
			origVal = "Photo for " + act_name;
			
			edt_Caption = (EditText) findViewById(R.id.edt_Caption);
			edt_Caption.setText(origVal);
			edt_Caption.setOnClickListener(new View.OnClickListener() {
				String message = edt_Caption.getText().toString();

				@Override
				public void onClick(View v) {
						if(message.equals(origVal)) {
							edt_Caption.setText("");
						}
					}
				});
			
			//---get the Submit button---
			fH_button = (Button) findViewById(R.id.btn_Submit);
	        
	        //---event handler for the Submit button---
	        fH_button.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view) {
	        		try {
	        			edt_Caption = (EditText) findViewById(R.id.edt_Caption);
	        			String caption = edt_Caption.getText().toString();
	        			if (caption.equals("")) {
	        				bundle.putString("message", "Photo for " + act_name);
	        			} else {
	        				bundle.putString("message", caption);
	        			}
	        			String response = Utility.facebook.request("me/photos", bundle, "POST");
	        			
	        			if (response.indexOf("OAuthException") > -1) {
	        				response = "Submission Failed";
	        			} else {
	        				response = "Submission Successful";
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
}
