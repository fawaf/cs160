package edu.berkeley.cs160.teamk;


import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.view.Menu;
import android.view.MenuItem;

import android.util.Log;


public class FHActivitySelector extends Activity {
	
	Button act1_button;
	Button act2_button;
	Button act3_button;
	Button btn_login;
	Button newTask;
	Button calendar;
	Button scores;
	Button help;
	ImageButton rejectT1;
	ImageButton rejectT2;
	ImageButton rejectT3;
	ImageButton camera1;
	ImageButton camera2;
	ImageButton camera3;
	String img_filename = "";
	String act_name = "";
	int index = 0;
	String submission_act_name = "";
	int submission_score = 0;
	String submission_img_filename = "";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fhactivityselector);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click_sound);
        final MediaPlayer rj = MediaPlayer.create(this, R.raw.reject_sound);
        
        Log.d("friendHealthFHASA", "Starting Activity Selector");
        
		Utility.facebook = new Facebook(Utility.APP_ID);
		/*
         * Get existing access_token if any
         */
        Utility.mPrefs = getSharedPreferences("FHActivitySelector", MODE_PRIVATE);
        String access_token = Utility.mPrefs.getString("access_token", null);
        Log.d("friendHealthFHASA", "AccessToken: " + access_token);
        long expires = Utility.mPrefs.getLong("access_expires", 0);
        if (access_token != null) {
            Utility.facebook.setAccessToken(access_token);
        }
        if (expires != 0) {
            Utility.facebook.setAccessExpires(expires);
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!Utility.facebook.isSessionValid()) {
        	Log.d("friendHealthFHASA", "Session Not Valid");

            Utility.facebook.authorize(this, new String[] { "user_photos", "friends_photos", "read_stream", "publish_stream", "publish_actions", "create_event", "rsvp_event" }, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = Utility.mPrefs.edit();
                    editor.putString("access_token", Utility.facebook.getAccessToken());
                    editor.putLong("access_expires", Utility.facebook.getAccessExpires());
                    editor.commit();
                }
    
                @Override
                public void onFacebookError(FacebookError error) {}
    
                @Override
                public void onError(DialogError e) {}
    
                @Override
                public void onCancel() {}
            });
        }
        else {
        	Log.d("friendHealthFHASA", "Logged in");
        }
         //-------Getting Facebook Name, then setting text view//
        try {
			String jsonUser = Utility.facebook.request("me");
			JSONObject obj;
			obj = Util.parseJson(jsonUser);
			
			TextView user_name = (TextView) findViewById(R.id.textView1);
			String facebookName = obj.optString("name");
			user_name.setText(facebookName);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.d("friendHealthPA", "MalformedURLException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("friendHealthPA", "IOException");
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("friendHealthPA", "JSONException");
			e.printStackTrace();
		} catch (FacebookError e) {
			// TODO Auto-generated catch block
			Log.d("friendHealthFHASA", "FacebookError: " + e.toString());
			Log.d("friendHealthFHASA", "Access Token: " + Utility.facebook.getAccessToken());
			e.printStackTrace();
		}
		//----End get facebook name//

        Log.d("friendHealthFHASA", "After Facebook Login: " + Utility.mPrefs.getString("access_token", "NO TOKEN"));
        
        //---Find Activity Buttons---
        Log.d("friendHealthFHASA", "Init button fH_Act");
        act1_button = (Button) findViewById(R.id.btn_activity1);
        act2_button = (Button) findViewById(R.id.btn_activity2);
        act3_button = (Button) findViewById(R.id.btn_activity3);
        rejectT1 = (ImageButton) findViewById(R.id.reject1);
        rejectT2 = (ImageButton) findViewById(R.id.reject2);
        rejectT3 = (ImageButton) findViewById(R.id.reject3);
        
        Log.d("friendHealthFHASA", "Initializing newTask Button");
        
        
        //------SET BUTTONS' COLOR-------
        act1_button.getBackground().setColorFilter(Color.rgb(248, 235, 152), PorterDuff.Mode.MULTIPLY);
        
        act2_button.getBackground().setColorFilter(Color.rgb(248, 235, 152), PorterDuff.Mode.MULTIPLY);
        
        act3_button.getBackground().setColorFilter(Color.rgb(248, 235, 152), PorterDuff.Mode.MULTIPLY);
        
        newTask = (Button) findViewById(R.id.newTask);
        newTask.getBackground().setColorFilter(Color.rgb(255, 215, 140), PorterDuff.Mode.MULTIPLY);
        
        calendar = (Button) findViewById(R.id.calendar);
        calendar.getBackground().setColorFilter(Color.rgb(217, 246, 255), PorterDuff.Mode.MULTIPLY);
        
        scores = (Button) findViewById(R.id.leaderboard);
        scores.getBackground().setColorFilter(Color.rgb(198, 235, 152), PorterDuff.Mode.MULTIPLY);
        
        help = (Button) findViewById(R.id.as_help);
        help.getBackground().setColorFilter(Color.rgb(255, 222, 233), PorterDuff.Mode.MULTIPLY);
        
        Log.d("friendHealthFHASA", "Initialzing Activity IDs");
        int id1 = Utility.mPrefs.getInt("taskID_1", 0);
        int id2 = Utility.mPrefs.getInt("taskID_2", 0);
        int id3 = Utility.mPrefs.getInt("taskID_3", 0);
        
        Log.d("friendHealthFHASA", "Saved Activity IDs: "
        		+ id1 + ", " + id2 + ", " + id3);
        if (id1 == 0 || id2 == 0 || id3 == 0) {
            Log.d("friendHealthFHASA", "Initializing DBAdapter");
        	Utility.dbAdapter = new DBAdapter();
        	SharedPreferences.Editor editor =Utility.mPrefs.edit();
        	editor.putInt("taskID_1", Utility.dbAdapter.getID(0));
        	editor.putInt("taskID_2", Utility.dbAdapter.getID(1));
        	editor.putInt("taskID_3", Utility.dbAdapter.getID(2));
        	editor.commit();
        }
        else {
        	Utility.dbAdapter = new DBAdapter(id1, id2, id3);
        }
        
        act1_button.setText(Utility.dbAdapter.toString(0));
        act2_button.setText(Utility.dbAdapter.toString(1));
        act3_button.setText(Utility.dbAdapter.toString(2));
        
        Log.d("friendHealthFHASA", "Set up variables, and setting listeners");
        
        act1_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					mp.start();
				}
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", Utility.dbAdapter.getName(0));
        		extras.putInt("score", Utility.dbAdapter.getPoints(0));
        		extras.putInt("index", 0);
        		i.putExtras(extras);

        		startActivityForResult(i, Utility.RC_ACTIVITY);
        		Log.d("friendHealthFHASA", "act1");
        	}
        });
        
        act2_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					mp.start();
				}
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", Utility.dbAdapter.getName(1));
        		extras.putInt("score", Utility.dbAdapter.getPoints(1));
        		extras.putInt("index", 1);
        		i.putExtras(extras);
        		startActivityForResult(i, Utility.RC_ACTIVITY);
        		Log.d("friendHealthFHASA", "act2");
        	}	
        });
        
        act3_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					mp.start();
				}
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", Utility.dbAdapter.getName(2));
        		extras.putInt("score", Utility.dbAdapter.getPoints(2));
        		extras.putInt("index", 2);
        		i.putExtras(extras);
        		startActivityForResult(i, Utility.RC_ACTIVITY);
        		Log.d("friendHealthFHASA", "act3");
        	}	
        });
        
        newTask.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					mp.start();
				}
				Utility.dbAdapter.setAllRandomActivities();
		        act1_button.setText(Utility.dbAdapter.toString(0));
		        act2_button.setText(Utility.dbAdapter.toString(1));
		        act3_button.setText(Utility.dbAdapter.toString(2));
			}
		});
        
        rejectT1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					rj.start();
				}
				Utility.dbAdapter.declineActivity(0);
				act1_button.setText(Utility.dbAdapter.toString(0));
			}
		});
        
        
        rejectT2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					rj.start();
				}
				Utility.dbAdapter.declineActivity(1);
				act2_button.setText(Utility.dbAdapter.toString(1));
			}
		});
        
        rejectT3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					rj.start();
				}
				Utility.dbAdapter.declineActivity(2);
				act3_button.setText(Utility.dbAdapter.toString(2));
			}
		});
        
        
        
		//---btn_picture---
        camera1 = (ImageButton) findViewById(R.id.camera1);
		// Handle click of button.
		camera1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				act_name = Utility.dbAdapter.getName(0);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri fileUri = Camera.getOutputMediaFileUri(getBaseContext(), Utility.MEDIA_TYPE_IMAGE, act_name);
				img_filename = fileUri.toString();
				submission_img_filename = img_filename;
				submission_score = Utility.dbAdapter.getPoints(0);
				submission_act_name = act_name;
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putString("act_img_filename", img_filename);
				editor.commit();
				
				Log.d("friendHealthFHA", "Image name: " + img_filename);
				
				// start the image capture Intent.
				startActivityForResult(intent, 
						Utility.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		
		//---btn_picture---
        camera2 = (ImageButton) findViewById(R.id.camera2);
		// Handle click of button.
		camera2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				act_name = Utility.dbAdapter.getName(1);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri fileUri = Camera.getOutputMediaFileUri(getBaseContext(), Utility.MEDIA_TYPE_IMAGE, act_name);
				img_filename = fileUri.toString();
				submission_img_filename = img_filename;
				submission_score = Utility.dbAdapter.getPoints(1);
				submission_act_name = act_name;
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putString("act_img_filename", img_filename);
				editor.commit();
				
				Log.d("friendHealthFHA", "Image name: " + img_filename);
				
				// start the image capture Intent.
				startActivityForResult(intent, 
						Utility.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		
		//---btn_picture---
        camera3 = (ImageButton) findViewById(R.id.camera3);
		// Handle click of button.
		camera3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				act_name = Utility.dbAdapter.getName(2);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri fileUri = Camera.getOutputMediaFileUri(getBaseContext(), Utility.MEDIA_TYPE_IMAGE, act_name);
				img_filename = fileUri.toString();
				submission_img_filename = img_filename;
				submission_score = Utility.dbAdapter.getPoints(2);
				submission_act_name = act_name;
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putString("act_img_filename", img_filename);
				editor.commit();
				
				Log.d("friendHealthFHA", "Image name: " + img_filename);
				
				// start the image capture Intent.
				startActivityForResult(intent, 
						Utility.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
        
        Log.d("friendHealthFHASA", "buttons connected");       
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("friendHealthFHASA", "Entered onActivityResult");
        switch(requestCode) {
        case Utility.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
        	if (resultCode == RESULT_OK) {
        		Log.d("friendHealthFHASA", "Entered Utility.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE");
				Log.d("friendHealthFHA", submission_act_name + " image taken.");
				
				Intent intent = new Intent(
						"edu.berkeley.cs160.teamk.ActivitySubmission");
				Bundle extras = new Bundle();
				extras.putString("name", submission_act_name);
				Log.d("friendHealthFHA", "score: " + submission_score);
				extras.putInt("score", submission_score);
				Log.d("friendHealthFHA", "img_filename: " + img_filename);
				extras.putString("filename", img_filename);
				intent.putExtras(extras);
				
				Log.d("friendHealthFHA", "Starting submission activity");
				startActivityForResult(intent, Utility.RC_ACTIVITYSUBMISSION);
			}
        	return;
        case Utility.RC_ACTIVITY:
        	if (resultCode == RESULT_OK) {
        		Log.d("friendHealthFHASA", "Entered Utility.RC_ACTIVITY");
        		Bundle extras = data.getExtras();
        		if (extras != null) {
        			String result = extras.getString("result");
        			index = extras.getInt("index");
        			if (result.equals("completed")) {
        				Toast.makeText(this,
        						"Activity completed",
        						Toast.LENGTH_LONG).show();
        			}
        			else if (result.equals("rejected")) {
        				Log.d("friendHealthFHASA", "index is: " + index);
        				Toast.makeText(this, 
        						"Activity rejected",
        						Toast.LENGTH_LONG).show();
        				Utility.dbAdapter.declineActivity(index);
        				Button button;
        				if (index == 0) {
        					button = act1_button;
        				} else if (index == 1) {
        					button = act2_button;
        				} else {
        					button = act3_button;
        				}
        				button.setText(Utility.dbAdapter.toString(index));
        			}
        			else if (result.equals("rejected_tooHard")) {
        				Toast.makeText(this,
        						"Activity rejected as too difficult",
        						Toast.LENGTH_LONG).show();
        			}
        			else if (result.equals("flagged")) {
        				Toast.makeText(this,
        						"Activity flagged",
        						Toast.LENGTH_LONG).show();
        			}
        			else {
        				Toast.makeText(this,
        						"UNKNOWN RESULT",
        						Toast.LENGTH_LONG).show();
        			}
        		}
        	}
        	return;
        case Utility.RC_NEWTASK:
        	if (resultCode == RESULT_OK) {
        		Log.d("friendHealthFHASA", "Entered Utility.RC_NEWTASK");
        		Bundle extras = data.getExtras();
        		if (extras != null) {
        			Task new_task = new Task();
        			new_task.name = extras.getString("name");
        			new_task.points = 1;
        			Utility.dbAdapter.addActivity(new_task);
        			Toast.makeText(this,
        					"Added activity: " + new_task.name,
        					Toast.LENGTH_LONG).show();
        		}
        	}
        	return;
        default:
        	Log.d("friendHealthFHASA", "Entered default onActivityResult");
        	
        	/*Bundle extras = data.getExtras();
        	if (extras != null) {
        		String response = extras.getString("response");
        		Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        	}*/
        	
        	Utility.facebook.authorizeCallback(requestCode, resultCode, data);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	OptionsMenu om = new OptionsMenu();
    	om.FHASCreateMenu(menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	OptionsMenu om = new OptionsMenu();
    	return om.FHASMenuChoice(this, item);
    }

}
