package edu.berkeley.cs160.teamk;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.util.Log;


public class ActivitySubmission extends Activity {
	
	String act_name = "";
	String img_filename = "";
	int score = 0;
	Button submit_button;
	Button rotate_button;
	String origVal = "";
	EditText edt_Caption;
	Bundle bundle = new Bundle();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitysubmission);
		
		Log.d("friendHealthAS", "ActivitySubmission drawn");
		
		//---display the image taken---
		final ImageView imageView = (ImageView) findViewById(R.id.img_submit);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			act_name = extras.getString("name");
			img_filename = extras.getString("filename");
			score = extras.getInt("score");
			
			Log.d("friendHealthAS", "Full name: " + img_filename);
			String shortname = img_filename.substring(11);
			Log.d("friendHealthAS", "Display name: " + shortname);
			

			Bitmap myBitmap = null;
			myBitmap = BitmapFactory.decodeFile(shortname);
			/*try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inDither = false;
				options.inPurgeable = true;
				options.inInputShareable = true;
				options.inTempStorage = new byte[32 * 1024];
				myBitmap = BitmapFactory.decodeFile(shortname, options);
			}
			catch (OutOfMemoryError e) {
				Log.d("friendHealthAS", e.toString());
				Toast.makeText(getBaseContext(),
						"OutOfMemoryError: " + e.toString(),
						Toast.LENGTH_LONG).show();
				return;
			}*/
			
			Log.d("friendHealthAS", "Displaying image");
			imageView.setImageBitmap(myBitmap);
			Log.d("friendHealthAS", "Image displayed");
			int orientation = getResources().getConfiguration().orientation;
			Log.d("Orientation: ", "" + orientation);
			if (orientation == 1)
			{
		        //super.onCreate(savedInstanceState);
		        //setContentView(R.layout.activitysubmission);
		        Matrix mtx = new Matrix();
		        mtx.postRotate(270);
		        Bitmap rotatedBMP = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), mtx, true);
		        BitmapDrawable bmd = new BitmapDrawable(rotatedBMP);
		        imageView.setImageDrawable(bmd);
		        myBitmap = rotatedBMP;
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			myBitmap.compress(CompressFormat.JPEG, 75, bos);
			bundle.putByteArray("picture", bos.toByteArray());
			Utility.mPrefs = getSharedPreferences("FHActivitySelector", MODE_PRIVATE);
			bundle.putString(Facebook.TOKEN, Utility.mPrefs.getString("access_token", null));
			Log.d("friendHealthAS", "access_token: " + Utility.mPrefs.getString("access_token", null));
			
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
			submit_button = (Button) findViewById(R.id.btn_Submit);
			rotate_button = (Button) findViewById(R.id.btn_Rotate);
			
			Log.d("check", "0");
			
			rotate_button.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view) {
	        		Log.d("check", "1");
	        		Matrix mtx = new Matrix();
	        		mtx.reset();
	        		mtx.postRotate(90);
	        		imageView.buildDrawingCache();
	        		Bitmap bmap = imageView.getDrawingCache();
	        		
			        Bitmap finalmap2 = Bitmap.createBitmap(bmap, 0, 0, bmap.getWidth(), bmap.getHeight(), mtx, true);
			        BitmapDrawable bmd = new BitmapDrawable(finalmap2);
			        Log.d("check", "" + finalmap2);
			        imageView.setImageDrawable(bmd);
	        	}
	        	});

	        //---event handler for the Submit button---
	        submit_button.setOnClickListener(new View.OnClickListener() {
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
	        				Log.d("friendHealthAS", "Response: " + response);
	        				response = "Submission Failed";
	        			} else {
	        				Log.d("friendHealthAS", "Response: " + response);
	        				response = "Submission Successful";
	        			}
	        			
	        			
	        			
	        			setResult(RESULT_OK);
	    				finish();
	        		} catch (MalformedURLException e) {
	        		} catch (IOException e) {
	        		
					}
	        	}
	        });
		}
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
