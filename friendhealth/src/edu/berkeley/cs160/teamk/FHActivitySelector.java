package edu.berkeley.cs160.teamk;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
	
	public static final int RC_ACTIVITY = 1001;
	public static final int RC_NEWTASK = 1002;

    String FILENAME = "AndroidSSO_data";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
        	Log.d("friendHealthFHASA", "Session Valid");
			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(Utility.facebook);
			mAsyncRunner.logout(getBaseContext(), new LogoutRequestListener());
			Log.d("friendHealthFHASA", "Access Token: " + Utility.facebook.getAccessToken());
			Log.d("friendHealthFHASA", "Access Expires: " + Utility.facebook.getAccessExpires());
			Utility.facebook = new Facebook(Utility.APP_ID);
			Utility.facebook.setAccessToken(null);
			Utility.facebook.setAccessExpires(0);
			Utility.mPrefs = getSharedPreferences("NOTHING", MODE_PRIVATE);
			SharedPreferences.Editor editor = Utility.mPrefs.edit();
			editor.clear();
			editor.remove("access_token");
			editor.remove("access_expires");
            editor.putString("access_token", "NONE");
            editor.putLong("access_expires", 0);
            boolean result = editor.commit();
            Log.d("friendHealthFHASA", "result is: " + result);
        }

        Log.d("friendHealthFHAS", "After Facebook Login: " + Utility.mPrefs.getString("access_token", "NO TOKEN"));
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
        
        name1 = act1.name;
        name2 = act2.name;
        name3 = act3.name;
        
        score1 = act1.points;
        score2 = act2.points;
        score3 = act3.points;
        
        act1_button.setText(name1 + " (" + score1 + "pts)");
        act2_button.setText(name2 + " (" + score2 + "pts)");
        act3_button.setText(name3 + " (" + score3 + "pts)");
        
        Log.d("friendHealthFHAS", "Set up variables, and setting listeners");
        
        act1_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		mp.start();
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name1);
        		extras.putInt("score", score1);
        		i.putExtras(extras);
        		startActivityForResult(i, RC_ACTIVITY);
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
        		startActivityForResult(i, RC_ACTIVITY);
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
        		startActivityForResult(i, RC_ACTIVITY);
        	}	
        });
        
        
        rejectT1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        rj.start();
				Task act1 = data.getTask();
		        name1 = act1.name;
		        score1 = act1.points;
		        act1_button.setText(name1 + " (" + score1 + "pts)");
			}
		});
        
        
        rejectT2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        rj.start();
				Task act2 = data.getTask();
		        name2 = act2.name;
		        score2 = act2.points;
		        act2_button.setText(name2 + " (" + score2 + "pts)");
			}
		});
        
        rejectT3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        rj.start();
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
        Log.d("friendHealthFHASA", "buttons connected");       
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
        case RC_ACTIVITY:
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
        case RC_NEWTASK:
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
        	Log.d("friendHealth", "Default Activity " + requestCode);
        	Utility.facebook.authorizeCallback(requestCode, resultCode, data);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	CreateMenu(menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return MenuChoice(item);
    }

    private class LogoutRequestListener implements RequestListener {
		 
		@Override
		public void onComplete(String response, Object state) {
			Log.d("friendHealthBA", "LOGGED OUT");
		}
 
		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
 
		}
 
	}

    private void CreateMenu(Menu menu) {
    	MenuItem mnu1 = menu.add(0, 0, 0, "Add Task");
    	{
    		mnu1.setAlphabeticShortcut('a');
    	}
    	MenuItem mnu2 = menu.add(0, 1, 1, "Add Habit");
    	{
    		mnu2.setAlphabeticShortcut('b');
    	}
    	MenuItem mnu3 = menu.add(0, 2, 2, "Log Out");
    	{
    		mnu3.setAlphabeticShortcut('c');
    	}
    	MenuItem mnu4 = menu.add(0, 3, 3, "Settings");
    	{
    		mnu4.setAlphabeticShortcut('d');
    	}
    	MenuItem mnu5 = menu.add(0, 4, 4, "Tutorials");
    	{
    		mnu5.setAlphabeticShortcut('e');
    	}
    }
    
    private boolean MenuChoice(MenuItem item) {
    	switch(item.getItemId()) {
    	case 0:
    		Intent i = new Intent("edu.berkeley.cs160.teamk.AddTask");
    		startActivityForResult(i, RC_NEWTASK);
    		return true;
    	case 1:
    		Toast.makeText(this, "Add Habit", Toast.LENGTH_SHORT).show();
    		return true;
    	case 2:
    		Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
    		return true;
    	case 3:
    		Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
    		return true;
    	case 4:
    		Toast.makeText(this, "Tutorials", Toast.LENGTH_SHORT).show();
    		return true;
    	}
    	return false;
    }
}


