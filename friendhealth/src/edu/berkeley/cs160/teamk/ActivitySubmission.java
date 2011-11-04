package edu.berkeley.cs160.teamk;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.widget.ImageView;
import android.util.Log;


public class ActivitySubmission extends Activity {
	String act_name = "";
	String img_filename = "";
	int score = 0;
	
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
		}
	}
}
