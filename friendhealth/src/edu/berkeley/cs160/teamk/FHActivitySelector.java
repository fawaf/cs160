package edu.berkeley.cs160.teamk;


import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

import android.util.Log;


public class FHActivitySelector extends Activity {
	Button fH_button;
	Button act1_button;
	Button act2_button;
	Button act3_button;
	Button btn_login;
	Button newTask;
	
    public static final String APP_ID = "177765768977545";
    private static String app_name = "friendHealth";

    String FILENAME = "AndroidSSO_data";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d("friendHealth", "Starting Activity Selector...");
        
		Utility.facebook = new Facebook(APP_ID);
		/*
         * Get existing access_token if any
         */
        Utility.mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = Utility.mPrefs.getString("access_token", null);
        Log.d("friendHealthFHACTSEL", "AccessToken: " + access_token);
        long expires = Utility.mPrefs.getLong("access_expires", 0);
        if (access_token != null)
        {
            Utility.facebook.setAccessToken(access_token);
        }
        if (expires != 0) 
        {
            Utility.facebook.setAccessExpires(expires);
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!Utility.facebook.isSessionValid()) 
        {

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
        else 
        {
        }

        
        Log.d("friendHealth", "Logged in inited.");
        
        //---get the fH Activity button---
        fH_button = (Button) findViewById(R.id.btn_fHActivity);
        
        Log.d("friendHealth", "Init button fH_Act");
        act1_button = (Button) findViewById(R.id.btn_activity1);
        act2_button = (Button) findViewById(R.id.btn_activity2);
        act3_button = (Button) findViewById(R.id.btn_activity3);
        Log.d("friendHealth", "Initializing newTask Button");
        newTask = (Button) findViewById(R.id.newTask);
        
        
        Log.d("friendHealth", "Creating Database");
        Database data = new Database();
        Log.d("friendHealth", "init data");
        Task act1 = data.getTask();
        Task act2 = data.getTask();
        Task act3 = data.getTask();
        Log.d("friendHealth", "Database created");
        
        final String name1 = act1.name;
        final String name2 = act2.name;
        final String name3 = act3.name;
        
        final int score1 = act1.points;
        final int score2 = act2.points;
        final int score3 = act3.points;
        
        Log.d("friendHealth", "Set up variables, and setting listeners.");
        //---event handler for the fH Activity button---
        fH_button.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View view) {
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", "Eat an Apple");
        		extras.putInt("score", 10);
        		i.putExtras(extras);
        		startActivity(i);
        	}	
        });
        Log.d(app_name, "fH_button connected");
        
        act1_button.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View view) {
        		Log.d(app_name, "Printing name1 and score1");
        		Log.d(app_name, "Activity: " + name1 + " " + score1);
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name1);
        		extras.putInt("score", score1);
        		i.putExtras(extras);
        		startActivity(i);
        	}	
        });
        
        act2_button.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View view) {
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name2);
        		extras.putInt("score", score2);
        		i.putExtras(extras);
        		startActivity(i);
        	}	
        });
        
        act3_button.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View view) {
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name3);
        		extras.putInt("score", score3);
        		i.putExtras(extras);
        		startActivity(i);
        	}	
        });
        
        Log.d(app_name, "buttons connected");
        
        /*
        newTask.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) {
		        Task act1 = data.generateTask();
		        Task act2 = data.generateTask();
		        Task act3 = data.generateTask();
		        
		        final String name1 = act1.name;
		        final String name2 = act2.name;
		        final String name3 = act3.name;
		        
		        final int score1 = act1.points;
		        final int score2 = act2.points;
		        final int score3 = act3.points;
			}
		});
		*/
        
        
        
        
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utility.facebook.authorizeCallback(requestCode, resultCode, data);
        
    }

}
