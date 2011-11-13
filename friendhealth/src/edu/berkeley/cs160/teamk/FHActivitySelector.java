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
	
    public static final String APP_ID = "177765768977545";

    String FILENAME = "AndroidSSO_data";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
        Log.d("friendHealthFHAS", "Initializing newTask Button");
        newTask = (Button) findViewById(R.id.newTask);
        
        
        Log.d("friendHealthFHAS", "Creating Database");
        Database data = new Database();
        Log.d("friendHealthFHAS", "Database created");
        Task act1 = data.getTask();
        Task act2 = data.getTask();
        Task act3 = data.getTask();
        Log.d("friendHealthFHAS", "Database created");
        
        final String name1 = act1.name;
        act1_button.setText(name1);
        final String name2 = act2.name;
        act2_button.setText(name2);
        final String name3 = act3.name;
        act3_button.setText(name3);
        
        final int score1 = act1.points;
        final int score2 = act2.points;
        final int score3 = act3.points;
        
        Log.d("friendHealthFHAS", "Set up variables, and setting listeners");
        
        act1_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name1);
        		extras.putInt("score", score1);
        		i.putExtras(extras);
        		startActivity(i);
        	}	
        });
        
        act2_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
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
        		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivity");
        		Bundle extras = new Bundle();
        		extras.putString("name", name3);
        		extras.putInt("score", score3);
        		i.putExtras(extras);
        		startActivity(i);
        	}	
        });
        
        Log.d("friendHealthFHASA", "buttons connected");       
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utility.facebook.authorizeCallback(requestCode, resultCode, data);
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
    		Toast.makeText(this, "Add Task", Toast.LENGTH_SHORT).show();
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
