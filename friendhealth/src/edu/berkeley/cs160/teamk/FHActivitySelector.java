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
import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
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
	ImageButton rejectT1;
	ImageButton rejectT2;
	ImageButton rejectT3;
	ImageButton camera1;
	
	
	String name1, name2, name3;
	int score1, score2, score3;

    String FILENAME = "AndroidSSO_data";
	
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
        Log.d("friendHealthFHASAA", "AccessToken: " + access_token);
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
        	Log.d("friendHealthFHASAA", "Session Not Valid");

            Utility.facebook.authorize(this, new String[] { "user_photos", "read_stream", "publish_stream"}, new DialogListener() {
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
        newTask = (Button) findViewById(R.id.newTask);
        
        
        Log.d("friendHealthFHASA", "Creating Database");
        final Database data = new Database();
        Log.d("friendHealthFHASA", "Database created");
        Task act1 = data.getTask();
        Task act2 = data.getTask();
        Task act3 = data.getTask();
        
        name1 = act1.name;
        name2 = act2.name;
        name3 = act3.name;
        
        score1 = act1.points;
        score2 = act2.points;
        score3 = act3.points;
        
        act1_button.setText(name1 + " (" + score1 + "pts)");
        act2_button.setText(name2 + " (" + score2 + "pts)");
        act3_button.setText(name3 + " (" + score3 + "pts)");
        
        Log.d("friendHealthFHASA", "Set up variables, and setting listeners");
        
        act1_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					mp.start();
				}
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name1);
        		extras.putInt("score", score1);
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
        		extras.putString("name", name2);
        		extras.putInt("score", score2);
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
        		extras.putString("name", name3);
        		extras.putInt("score", score3);
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
				Task act1 = data.getTask();
		        name1 = act1.name;
		        score1 = act1.points;
		        act1_button.setText(name1 + " (" + score1 + "pts)");
				Task act2 = data.getTask();
		        name2 = act2.name;
		        score2 = act2.points;
		        act2_button.setText(name2 + " (" + score2 + "pts)");
				Task act3 = data.getTask();
		        name3 = act3.name;
		        score3 = act3.points;
		        act3_button.setText(name3 + " (" + score3 + "pts)");
			}
		});
        
        rejectT1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					rj.start();
				}
				Task act1 = data.getTask();
		        name1 = act1.name;
		        score1 = act1.points;
		        act1_button.setText(name1 + " (" + score1 + "pts)");
			}
		});
        
        
        rejectT2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					rj.start();
				}
				Task act2 = data.getTask();
		        name2 = act2.name;
		        score2 = act2.points;
		        act2_button.setText(name2 + " (" + score2 + "pts)");
			}
		});
        
        rejectT3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					rj.start();
				}
				Task act3 = data.getTask();
		        name3 = act3.name;
		        score3 = act3.points;
		        act3_button.setText(name3 + " (" + score3 + "pts)");
			}
		});
        
        
        
		//---btn_picture---
		/*
        camera1 = (ImageButton) findViewById(R.id.camera1);
		// Handle click of button.
		camera1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				final int MEDIA_TYPE_IMAGE = 1;
				final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
				String img_filename = "";
				
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, name1);
				//img_filename = fileUri.toString();
				//intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putString("act_img_filename", img_filename);
				editor.commit();
				
				Log.d("friendHealthFHA", "Image name: " + img_filename);
				
				// start the image capture Intent.
				startActivityForResult(intent, 
						CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
        
		*/
        Log.d("friendHealthFHASAA", "buttons connected");       
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
        case Utility.RC_ACTIVITY:
        	if (resultCode == RESULT_OK) {
        		Bundle extras = data.getExtras();
        		if (extras != null) {
        			String result = extras.getString("result");
        			if (result.equals("completed")) {
        				Toast.makeText(this,
        						"Activity completed",
        						Toast.LENGTH_SHORT).show();
        			}
        			else if (result.equals("rejected")) {
        				Toast.makeText(this, 
        						"Activity rejected",
        						Toast.LENGTH_SHORT).show();
        			}
        			else if (result.equals("rejected_tooHard")) {
        				Toast.makeText(this,
        						"Activity rejected as too difficult",
        						Toast.LENGTH_SHORT).show();
        			}
        			else if (result.equals("flagged")) {
        				Toast.makeText(this,
        						"Activity flagged",
        						Toast.LENGTH_SHORT).show();
        			}
        			else {
        				Toast.makeText(this,
        						"UNKNOWN RESULT",
        						Toast.LENGTH_SHORT).show();
        			}
        		}
        	}
        	return;
        case Utility.RC_NEWTASK:
        	if (resultCode == RESULT_OK) {
        		Bundle extras = data.getExtras();
        		if (extras != null) {
        			String taskname = extras.getString("name");
        			Toast.makeText(this,
        					"Add activity: " + taskname,
        					Toast.LENGTH_SHORT).show();
        		}
        	}
        	return;
        default:
        	Log.d("friendHealthFHASAA", "Default Activity " + requestCode);
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
