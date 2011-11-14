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
import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.view.Menu;
import android.view.MenuItem;

import android.util.Log;


public class FHActivitySelector extends Activity {
	Button fH_button;
	Button act1_button;
	Button act2_button;
	Button act3_button;
	Button btn_login;
	Button newTask;
	
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
            editor.clear();
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
        Log.d("friendHealthFHAS", "Initializing newTask Button");
        newTask = (Button) findViewById(R.id.newTask);
        
        
        Log.d("friendHealthFHAS", "Creating Database");
        Database data = new Database();
        Log.d("friendHealthFHAS", "Database created");
        Task act1 = data.getTask();
        Task act2 = data.getTask();
        Task act3 = data.getTask();
        Log.d("friendHealthFHAS", "Database created");
        
        name1 = act1.name;
        act1_button.setText(name1);
        name2 = act2.name;
        act2_button.setText(name2);
        name3 = act3.name;
        act3_button.setText(name3);
        
        score1 = act1.points;
        score2 = act2.points;
        score3 = act3.points;
        
        Log.d("friendHealthFHAS", "Set up variables, and setting listeners");
    
        act1_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name1);
        		extras.putInt("score", score1);
        		i.putExtras(extras);
        		startActivityForResult(i, RC_ACTIVITY);
        	}	
        });
        
        act2_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
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
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name3);
        		extras.putInt("score", score3);
        		i.putExtras(extras);
        		startActivityForResult(i, RC_ACTIVITY);
        	}	
        });
        
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
        	Utility.facebook.authorizeCallback(requestCode, resultCode, data);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	OptionsMenu.FHASCreateMenu(menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return OptionsMenu.FHASMenuChoice(this, item);
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
}