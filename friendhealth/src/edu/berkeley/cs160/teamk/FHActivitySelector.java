package edu.berkeley.cs160.teamk;


import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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
	
	
    public static final String APP_ID = "177765768977545";

    String FILENAME = "AndroidSSO_data";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click_sound);
        final MediaPlayer rj = MediaPlayer.create(this, R.raw.reject_sound);
        
        Log.d("friendHealthFHASA", "Starting Activity Selector");
        
		Utility.facebook = new Facebook(APP_ID);
		/*
         * Get existing access_token if any
         */
        Utility.mPrefs = getPreferences(MODE_PRIVATE);
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

            Utility.facebook.authorize(this, new String[] { "user_photos", "read_stream", "publish_stream" }, new DialogListener() {
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
        }

        
        Log.d("friendHealthFHAS", "Logged in");
        
        //---Find Activity Buttons---
        Log.d("friendHealthFHAS", "Init button fH_Act");
        act1_button = (Button) findViewById(R.id.btn_activity1);
        act2_button = (Button) findViewById(R.id.btn_activity2);
        act3_button = (Button) findViewById(R.id.btn_activity3);
        rejectT1 = (ImageButton) findViewById(R.id.reject1);
        rejectT2 = (ImageButton) findViewById(R.id.reject2);
        rejectT3 = (ImageButton) findViewById(R.id.reject3);
        
        Log.d("friendHealthFHAS", "Initializing newTask Button");
        newTask = (Button) findViewById(R.id.newTask);
        
        
        Log.d("friendHealthFHAS", "Creating Database");
        final Database data = new Database();
        Log.d("friendHealthFHAS", "Database created");
        Task act1 = data.getTask();
        Task act2 = data.getTask();
        Task act3 = data.getTask();
        Log.d("friendHealthFHAS", "Database created");
        
        final String name1 = act1.name;
        final String name2 = act2.name;
        final String name3 = act3.name;
        
        final int score1 = act1.points;
        final int score2 = act2.points;
        final int score3 = act3.points;
        
        act1_button.setText(name1 + " (" + score1 + "pts)");
        act2_button.setText(name2 + " (" + score2 + "pts)");
        act3_button.setText(name3 + " (" + score3 + "pts)");
        
        Log.d("friendHealthFHAS", "Set up variables, and setting listeners");
        
        /*
        newTask.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		clickSound.start();
        		//Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivitySelector");
        		//Bundle extras = new Bundle();
        		//extras.putString("name", name1);
        		//extras.putInt("score", score1);
        		//i.putExtras(extras);
        		//tartActivity(i);
        	}	
        });*/
        
        
        act1_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		mp.start();
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name1);
        		extras.putInt("score", score1);
        		i.putExtras(extras);
        		startActivity(i);
        	}	
        });
        
        Log.d("friendHealthFHAS", "act1");
        
        act2_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		mp.start();
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name2);
        		extras.putInt("score", score2);
        		i.putExtras(extras);
        		startActivity(i);
        	}	
        });
        
        act3_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		mp.start();
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name3);
        		extras.putInt("score", score3);
        		i.putExtras(extras);
        		startActivity(i);
        	}	
        });
        
        
        rejectT1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        rj.start();
				Task act1 = data.getTask();
		        final String name1 = act1.name;
		        final int score1 = act1.points;
		        act1_button.setText(name1 + " (" + score1 + "pts)");
			}
		});
        
        rejectT2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        rj.start();
				Task act2 = data.getTask();
		        final String name2 = act2.name;
		        final int score2 = act2.points;
		        act1_button.setText(name2 + " (" + score2 + "pts)");
			}
		});
        
        rejectT3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        rj.start();
				Task act3 = data.getTask();
		        final String name3 = act3.name;
		        final int score3 = act3.points;
		        act1_button.setText(name3 + " (" + score3 + "pts)");
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
        
		
        Log.d("friendHealthFHASA", "buttons connected");       
        */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utility.facebook.authorizeCallback(requestCode, resultCode, data);
        
    }

}


