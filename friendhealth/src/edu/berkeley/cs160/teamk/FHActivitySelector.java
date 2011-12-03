package edu.berkeley.cs160.teamk;

import java.io.IOException;
import java.net.MalformedURLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.content.DialogInterface;
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
import android.text.Html;
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
	int act_id = 0;
	String submission_act_name = "";
	int submission_score = 0;
	String submission_img_filename = "";
	int submission_act_id = 0;
	int submission_index = -1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fhactivityselector);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click_sound);
        final MediaPlayer rj = MediaPlayer.create(this, R.raw.reject_sound);
        final MediaPlayer hp = MediaPlayer.create(this, R.raw.help_button);
        
        Log.d("friendHealthFHASA", "Starting Activity Selector");
        
        Utility.scoresDBAdapter = new ScoresDBAdapter();
        FacebookLogin();
        
        Log.d("friendHealthFHASA", "After Facebook Login: " + Utility.mPrefs.getString("access_token", "NO TOKEN"));
        
        //---Find Activity Buttons---
        Log.d("friendHealthFHASA", "Init button fH_Act");
        act1_button = (Button) findViewById(R.id.btn_activity1);
        act2_button = (Button) findViewById(R.id.btn_activity2);
        act3_button = (Button) findViewById(R.id.btn_activity3);
        rejectT1 = (ImageButton) findViewById(R.id.reject1);
        rejectT2 = (ImageButton) findViewById(R.id.reject2);
        rejectT3 = (ImageButton) findViewById(R.id.reject3);
        help = (Button) findViewById(R.id.as_help);
        img_filename = Utility.mPrefs.getString("act_img_filename", "");
        submission_act_name = Utility.mPrefs.getString("activity_name", "");
        submission_score = Utility.mPrefs.getInt("activity_score", -1);
        submission_act_id = Utility.mPrefs.getInt("activity_id", 0);
        submission_index = Utility.mPrefs.getInt("activity_index", -1);
       
        
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
        
        act1_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(0) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(0) + " Points" + "</font>"));
        act2_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(1) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(1) + " Points" + "</font>"));
        act3_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(2) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(2) + " Points" + "</font>"));
        
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
        		int act_id = Utility.dbAdapter.getID(0);
        		extras.putInt("id", act_id);
        		extras.putInt("index", 0);
        		i.putExtras(extras);

        		Log.d("friendHealthFHASA", "Score ID is: " + act_id);
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
        		extras.putInt("id", Utility.dbAdapter.getID(1));
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
        		extras.putInt("id", Utility.dbAdapter.getID(2));
        		extras.putInt("index", 2);
        		i.putExtras(extras);
        		startActivityForResult(i, Utility.RC_ACTIVITY);
        		Log.d("friendHealthFHASA", "act3");
        	}	
        });
        calendar.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view){
        		String link = null;
                try {
        			String jsonAlbums = Utility.facebook.request("me/albums");
        			JSONObject obj = Util.parseJson(jsonAlbums);
        			JSONArray albums = obj.getJSONArray("data");
        			Boolean found = false;
        			int i = 0;
        			while (!found){
        				JSONObject album = albums.getJSONObject(i);
        				String name = album.optString("name");
        				if(name.equals("friendHealth Photos")){
        					link = album.optString("link");
        					found = true;
        				}
        				i++;	
        			}
        			
        		} catch (MalformedURLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (JSONException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (FacebookError e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		Intent i = new Intent(android.content.Intent.ACTION_VIEW, 
        				Uri.parse(link));
        		startActivity(i);
        	}
        });
        
        final Toast help1 = Toast.makeText(this, Html.fromHtml("<font color='white'><big>+++  </big></font><font color='red'><big>Welcome </big></font>" +
        		"<big><font color = 'yellow'>to the </font><font color = 'green'>Help </font>" +
        		"<font color = 'blue'>Panel! </font><font color = 'purple'> +++</big></font>"), Toast.LENGTH_LONG);
        //help1.setGravity(Gravity.CENTER, 0, 0);
        
        final Toast help2 = Toast.makeText(this, Html.fromHtml("<font color='white'><big>This is friendHealth's main page: </big></font><font color='yellow'><big>Activity Selector</big></font></big>"), Toast.LENGTH_LONG);
        //help2.setGravity(Gravity.CENTER, 0, 0);
        
        final Toast help3 = Toast.makeText(this, Html.fromHtml("<font color='white'><big>Now let's go through some basic functions in this page</big></font>"), Toast.LENGTH_LONG);
        //help3.setGravity(Gravity.CENTER, 0, 0);
		
        final Toast toast = Toast.makeText(this, Html.fromHtml("<font color='blue'><big>TASK</big></font><br/><font color='white'>" +
				"<big>These three </big></font><font color = 'yellow'><big>yellow buttons </big></font><font color = 'white'><big>display the tasks' names.</big></font>"), Toast.LENGTH_LONG);
		//toast.setGravity(Gravity.CENTER, 0, 0);
		
		final Toast task2 = Toast.makeText(this, Html.fromHtml("<font color='blue'><big><bold>TASK</bold></big></font><br/><font color='white'>" +
		"<big>Simply tap one of the buttons if you want to perform a task.</big></font>"), Toast.LENGTH_LONG);
		//task2.setGravity(Gravity.CENTER, 0, 0);
		
		final Toast task3 = Toast.makeText(this, Html.fromHtml("<font color='green'><big><bold>POINTS</bold></big></font><br/><font color='white'>" +
		"<big>Each task has a point under it indicating how many points you can gain for completing it.</big></font>"), Toast.LENGTH_LONG);
		//task3.setGravity(Gravity.CENTER, 0, 0);
		
		final Toast task4 = Toast.makeText(this, Html.fromHtml("<font color='green'><big>POINTS</big></font><br/><font color='white'>" +
		"<big>For example, You can gain </big></font><big><font color = 'green'>" + Utility.dbAdapter.getPoints(0) + " points </font>" + "<font color = 'white'>by completing </font>" + 
		"</font><font color= 'red'>" + Utility.dbAdapter.getName(0) + "</font></big>"), Toast.LENGTH_LONG);
		//task4.setGravity(Gravity.CENTER, 0, 0);
		
		final Toast reject = Toast.makeText(this, Html.fromHtml("<font color='red'><big>REJECT</big></font><br/><font color='white'>" +
		"<big>Reject buttons allow you to decline a task.</big></font>"), Toast.LENGTH_LONG);
		//reject.setGravity(Gravity.CENTER, 0, 0);
		
		final Toast reject2 = Toast.makeText(this, Html.fromHtml("<font color='red'><big>REJECT</big></font><br/><font color='white'>" +
		"<big>A new task will be generated to replace the rejected task</big></font>"), Toast.LENGTH_LONG);
		//reject2.setGravity(Gravity.CENTER, 0, 0);
	    
		final Toast cameraT1 = Toast.makeText(this, Html.fromHtml("<big><font color='green'>CAMERA </font><br/>" +
				"<font color='white'>Take a Photo will launch the camera directly</font></big>"), Toast.LENGTH_LONG);
		
		final Toast cameraT2 = Toast.makeText(this, Html.fromHtml("<big><font color='green'>CAMERA </font><br/><font color = 'white'>" +
				"This is a shortcut for completing the task. However we do not encourage new users to use it</font><big>"), Toast.LENGTH_LONG);
		
		final Toast newTasksT = Toast.makeText(this, Html.fromHtml("<big><font color='#ffa500'>NEW TASKS </font>will clear " +
				"<font color = 'white'>all current tasks and generate 3 new tasks</font></big>"), Toast.LENGTH_LONG);
		
		final Toast calendarT = Toast.makeText(this, Html.fromHtml("<big><font color='blue'>CALENDAR </font><font color = 'white'>keeps track of " +
		"all the tasks you have completed</font></big>"), Toast.LENGTH_LONG);
		
		final Toast scoresT = Toast.makeText(this, Html.fromHtml("<big><font color='green'>SCORES </font><font color = 'white'>help you track " +
		"your progress as well as your friends and other users all over the world</font></big>"), Toast.LENGTH_LONG);
		
		final Toast helpT = Toast.makeText(this, Html.fromHtml("<big><font color='#ffc0cb'>HELP </font><font color = 'white'>will show " +
		"this demo again. You are welcome to watch it again if you still don't understand.</font></big>"), Toast.LENGTH_LONG);
		
		final Toast finishT = Toast.makeText(this, Html.fromHtml("<big><font color='white'>Otherwise " +
		"please enjoy this innovative health app right on your hand!</font></big>"), Toast.LENGTH_LONG);
		
	    final Animation animation = new AlphaAnimation(1, 0);
	    final Animation animation2 = new AlphaAnimation(1, 0);
	    animation.setDuration(500);
	    animation2.setDuration(200);
	    animation.setInterpolator(new LinearInterpolator());
	    animation2.setInterpolator(new LinearInterpolator());
	    animation.setRepeatCount(6);
	    animation2.setRepeatCount(6);
	    animation.setRepeatMode(Animation.REVERSE);
	    animation2.setRepeatMode(Animation.REVERSE);
	    
	    
	    
        help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
			if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
				hp.start();
			}
			
			help1.show();
			
			new CountDownTimer(6000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	help2.show();
		        }
		        public void onFinish() 
		        {
		        	help2.show();
		        }
		    }.start();
			
			new CountDownTimer(11500, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	help3.show();
		        }
		        public void onFinish() 
		        {
		        	help3.show();
		        }
		    }.start();
			
		    
		    new CountDownTimer(18000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	toast.show();
		        }
		        public void onFinish() 
		        {
		        	toast.show();
		        	act1_button.startAnimation(animation);
		        	act2_button.startAnimation(animation);
		        	act3_button.startAnimation(animation);
		        }
		    }.start();
		    
		    
		    new CountDownTimer(24000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	task2.show();
		        }
		        public void onFinish() 
		        {
		        	task2.show();
		        	act1_button.startAnimation(animation);
		        }
		    }.start();
		    
		    
		    new CountDownTimer(30000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	task3.show();
		        }
		        public void onFinish() 
		        {
		        	task3.show();
		        	act1_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(0) +"</big></font><br/><font color='red'>" + "+" + Utility.dbAdapter.getPoints(0) + " Points" + "</font>"));
		        	act2_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(1) +"</big></font><br/><font color='red'>" + "+" + Utility.dbAdapter.getPoints(1) + " Points" + "</font>"));
		        	act3_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(2) +"</big></font><br/><font color='red'>" + "+" + Utility.dbAdapter.getPoints(2) + " Points" + "</font>"));
		        }
		    }.start();
		    
		    
		    new CountDownTimer(36000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	task4.show();
		        }
		        public void onFinish() 
		        {
		        	task4.show();
		        	act1_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(0) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(0) + " Points" + "</font>"));
		        	act2_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(1) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(1) + " Points" + "</font>"));
		        	act3_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(2) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(2) + " Points" + "</font>"));
		        	act1_button.startAnimation(animation);
		        }
		    }.start();
		    
		    new CountDownTimer(42000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	reject.show();
		        }
		        public void onFinish() 
		        {
		        	reject.show();
		        	rejectT1.startAnimation(animation);
		        	rejectT2.startAnimation(animation);
		        	rejectT3.startAnimation(animation);
		        }
		    }.start();
		    
		    new CountDownTimer(48000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	reject2.show();
		        }
		        public void onFinish() 
		        {
		        	reject2.show();
		        	rejectT1.startAnimation(animation2);
		        	act1_button.startAnimation(animation);
		        	if (Utility.mPrefs.getBoolean("toggle_sound", true)) 
		        	{
						rj.start();
					}
					Utility.dbAdapter.declineActivity(0);
					act1_button.setText(Html.fromHtml("<font color='red'><big>"+ Utility.dbAdapter.getName(0) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(0) + " Points" + "</font>"));

		        }
		    }.start();
		    
		    new CountDownTimer(54000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	cameraT1.show();
		        }
		        public void onFinish() 
		        {
		        	cameraT1.show();
		        	camera1.startAnimation(animation);
		        	camera2.startAnimation(animation);
		        	camera3.startAnimation(animation);
		        }
		    }.start();
		    
		    new CountDownTimer(60000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	cameraT2.show();
		        }
		        public void onFinish() 
		        {
		        	cameraT2.show();
		        }
		    }.start();
		    
		    new CountDownTimer(66000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	newTasksT.show();
		        }
		        public void onFinish() 
		        {
		        	newTasksT.show();
		        	newTask.startAnimation(animation);
		        }
		    }.start();
		    
		    new CountDownTimer(72000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	calendarT.show();
		        }
		        public void onFinish() 
		        {
		        	calendarT.show();
		        	calendar.startAnimation(animation);
		        }
		    }.start();
		    
		    new CountDownTimer(78000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	scoresT.show();
		        }
		        public void onFinish() 
		        {
		        	scoresT.show();
		        	scores.startAnimation(animation);
		        }
		    }.start();
		    
		    new CountDownTimer(84000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	helpT.show();
		        }
		        public void onFinish() 
		        {
		        	helpT.show();
		        	help.startAnimation(animation);
		        }
		    }.start();
		    
		    new CountDownTimer(90000, 1000)
		    {

		        public void onTick(long millisUntilFinished) 
		        {
		        	finishT.show();
		        }
		        public void onFinish() 
		        {
		        	finishT.show();
		        }
		    }.start();
		    
		    
		    
		    
        }
        });
        
        newTask.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					mp.start();
				}
				Utility.dbAdapter.setAllRandomActivities();
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putInt("event_created0", 0);
				editor.putInt("taskID_1", Utility.dbAdapter.getID(0));
				editor.putInt("event_created1", 0);
				editor.putInt("taskID_2", Utility.dbAdapter.getID(1));
				editor.putInt("event_created2", 0);
				editor.putInt("taskID_3", Utility.dbAdapter.getID(2));
				editor.commit();
				act1_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(0) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(0) + " Points" + "</font>"));
				act2_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(1) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(1) + " Points" + "</font>"));
				act3_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(2) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(2) + " Points" + "</font>"));
			}
		});
        
        scores.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent i = new Intent("edu.berkeley.cs160.teamk.Leaderboard");
        		startActivity(i);
        	}
        });
        
        
        
		final CharSequence[] items = {"Too Hard", "Inappropriate", "Don't want to do it"};

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		

        
        rejectT1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					rj.start();
				}
				builder.setTitle("What is the reason you reject the task: " + Utility.dbAdapter.getName(0) + " ?");
				builder.setItems(items, new DialogInterface.OnClickListener() 
				{
				    public void onClick(DialogInterface dialog, int item) 
				    {
				    	if (item == 0)
				    		Utility.dbAdapter.rejectDifficultActivity(0);
				    	else if (item == 1)
				    		Utility.dbAdapter.flagActivity(0);
				    	else
				    		Utility.dbAdapter.declineActivity(0);
				    	Toast.makeText(getApplicationContext(), "Task rejected. Thanks for your feedback.", Toast.LENGTH_SHORT);
				    	SharedPreferences.Editor editor = Utility.mPrefs.edit();
						editor.putInt("event_created0", 0);
						editor.putInt("taskID_1", Utility.dbAdapter.getID(0));
						editor.commit();
				    	act1_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(0) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(0) + " Points" + "</font>"));
				    }
				    
				});
				final AlertDialog alert = builder.create();
				alert.show();
			}
		});
        
        
        rejectT2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					rj.start();
				}
				builder.setTitle("What is the reason you reject the task: " + Utility.dbAdapter.getName(1) + " ?");
				builder.setItems(items, new DialogInterface.OnClickListener() 
				{
				    public void onClick(DialogInterface dialog, int item) 
				    {
				    	if (item == 0)
				    		Utility.dbAdapter.rejectDifficultActivity(1);
				    	else if (item == 1)
				    		Utility.dbAdapter.flagActivity(1);
				    	else
				    		Utility.dbAdapter.declineActivity(1);
				    	Toast.makeText(getApplicationContext(), "Task rejected. Thanks for your feedback.", Toast.LENGTH_SHORT);
				    	SharedPreferences.Editor editor = Utility.mPrefs.edit();
						editor.putInt("event_created1", 0);
						editor.putInt("taskID_2", Utility.dbAdapter.getID(1));
						editor.commit();
				    	act2_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(1) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(1) + " Points" + "</font>"));
				    }
				    
				});
				final AlertDialog alert = builder.create();
				alert.show();
			}
		});
        
        rejectT3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Utility.mPrefs.getBoolean("toggle_sound", true)) {
					rj.start();
				}
				builder.setTitle("What is the reason you reject the task: " + Utility.dbAdapter.getName(2) + " ?");
				builder.setItems(items, new DialogInterface.OnClickListener() 
				{
				    public void onClick(DialogInterface dialog, int item) 
				    {
				    	if (item == 0)
				    		Utility.dbAdapter.rejectDifficultActivity(2);
				    	else if (item == 1)
				    		Utility.dbAdapter.flagActivity(2);
				    	else
				    		Utility.dbAdapter.declineActivity(2);
				    	Toast.makeText(getApplicationContext(), "Task rejected. Thanks for your feedback.", Toast.LENGTH_SHORT);
				    	SharedPreferences.Editor editor = Utility.mPrefs.edit();
						editor.putInt("event_created2", 0);
						editor.putInt("taskID_3", Utility.dbAdapter.getID(2));
						editor.commit();
				    	act3_button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(2) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(2) + " Points" + "</font>"));
				    }
				    
				});
				final AlertDialog alert = builder.create();
				alert.show();
			}
		});
        
        
        
		//---btn_picture---
        camera1 = (ImageButton) findViewById(R.id.camera1);
		// Handle click of button.
		camera1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				act_name = Utility.dbAdapter.getName(0);
				act_id = Utility.dbAdapter.getID(0);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri fileUri = Camera.getOutputMediaFileUri(getBaseContext(), Utility.MEDIA_TYPE_IMAGE, act_name);
				img_filename = fileUri.toString();
				submission_img_filename = img_filename;
				submission_score = Utility.dbAdapter.getPoints(0);
				submission_act_name = act_name;
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putString("act_img_filename", img_filename);
				editor.putString("activity_name", act_name);
				editor.putInt("activity_id", act_id);
				editor.putInt("activity_score", submission_score);
				editor.putInt("activity_index", 0);
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
				act_id = Utility.dbAdapter.getID(1);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri fileUri = Camera.getOutputMediaFileUri(getBaseContext(), Utility.MEDIA_TYPE_IMAGE, act_name);
				img_filename = fileUri.toString();
				submission_img_filename = img_filename;
				submission_score = Utility.dbAdapter.getPoints(1);
				submission_act_name = act_name;
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putString("act_img_filename", img_filename);
				editor.putString("activity_name", act_name);
				editor.putInt("activity_id", act_id);
				editor.putInt("activity_score", submission_score);
				editor.putInt("activity_index", 1);
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
				act_id = Utility.dbAdapter.getID(2);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Log.d("friendHealthFHA", "Before calling camera on button3");
				Uri fileUri = Camera.getOutputMediaFileUri(getBaseContext(), Utility.MEDIA_TYPE_IMAGE, act_name);
				img_filename = fileUri.toString();
				Log.d("friendHealthFHA", "Inside camera3 button");
				Log.d("friendHealthFHA", "img_filename is: "+img_filename);
				submission_img_filename = img_filename;
				submission_score = Utility.dbAdapter.getPoints(2);
				submission_act_name = act_name;
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putString("act_img_filename", img_filename);
				editor.putString("activity_name", act_name);
				editor.putInt("activity_id", act_id);
				editor.putInt("activity_score", submission_score);
				editor.putInt("activity_index", 2);
				editor.commit();
				
				
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
				Log.d("friendHealthAS", "img_filename in onactivityresult: " + img_filename);
				
				Intent intent = new Intent(
						"edu.berkeley.cs160.teamk.ActivitySubmission");
				Bundle extras = new Bundle();
				extras.putString("name", submission_act_name);
				Log.d("friendHealthFHA", "score: " + submission_score);
				extras.putInt("score", submission_score);
				Log.d("friendHealthFHA", "img_filename: " + img_filename);
				extras.putString("filename", img_filename);
				extras.putInt("index", submission_index);
				extras.putInt("id", submission_act_id);
				extras.putString("result", "completed");
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
        				Utility.dbAdapter.acceptActivity(index);
        				Button button;
        				if (index == 0) {
        					button = act1_button;
        				} else if (index == 1) {
        					button = act2_button;
        				} else {
        					button = act3_button;
        				}
        				SharedPreferences.Editor editor = Utility.mPrefs.edit();
        				editor.putInt("taskID_"+(index+1), Utility.dbAdapter.getID(index));
        				editor.commit();
        				button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(index) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(index) + " Points" + "</font>"));
        			}
        			else if (result.equals("rejected")) {
        				final CharSequence[] items = {"Too Hard", "Inappropriate", "Don't want to do it"};
        				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        				
        				builder.setTitle("What is the reason you reject the task: " + Utility.dbAdapter.getName(index) + " ?");
        				builder.setItems(items, new DialogInterface.OnClickListener() 
        				{
        				    public void onClick(DialogInterface dialog, int item) 
        				    {
        				    	if (item == 0)
        				    		Utility.dbAdapter.rejectDifficultActivity(index);
        				    	else if (item == 1)
        				    		Utility.dbAdapter.flagActivity(index);
        				    	else
        				    		Utility.dbAdapter.declineActivity(index);
        				    	
        				    	Toast.makeText(getApplicationContext(), "Task rejected. Thanks for your feedback.", Toast.LENGTH_SHORT);
                				Button button;
                				
                				if (index == 0) {
                					button = act1_button;
                				} 
                				else if (index == 1) {
                					button = act2_button;
                				} 
                				else {
                					button = act3_button;
                				}
                				SharedPreferences.Editor editor = Utility.mPrefs.edit();
                				editor.putInt("taskID_"+(index+1), Utility.dbAdapter.getID(index));
                				editor.commit();
        				    	button.setText(Html.fromHtml("<font color='black'><big>"+ Utility.dbAdapter.getName(index) +"</big></font><br/><font color='green'>" + "+" + Utility.dbAdapter.getPoints(index) + " Points" + "</font>"));
        				    }
        				    
        				});
        				final AlertDialog alert = builder.create();
        				alert.show();
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
    
    /*@Override
    public void onResume() {
    	super.onResume();
    	FacebookLogin();
    }*/
    
    private void FacebookLogin() {
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

            Utility.facebook.authorize(this, new String[] { "user_photos", "friends_photos", "read_stream",
            		"publish_stream", "publish_actions", "create_event", "rsvp_event", "user_events",
            		"friends_events" }, new DialogListener() {
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = Utility.mPrefs.edit();
                    editor.putString("access_token", Utility.facebook.getAccessToken());
                    editor.putLong("access_expires", Utility.facebook.getAccessExpires());
                    editor.commit();
                    //-------Getting Facebook Name, then setting text view//
                    try {
            			String jsonUser = Utility.facebook.request("me");
            			JSONObject obj;
            			obj = Util.parseJson(jsonUser);
            			
            			TextView user_name = (TextView) findViewById(R.id.textView1);
            			String facebookName = obj.optString("name");
            			user_name.setText(facebookName);
            			
                    	String facebookId = obj.optString("id");
                    	Log.d("friendHealthFHASA", "Facebook UID is: " + facebookId);
                    	editor.putString("facebookUID", facebookId);
                    	editor.commit();
                    	
                    	Utility.scoresDBAdapter.checkUserScore(facebookId, facebookName);
                    	TextView score_txt = (TextView) findViewById(R.id.scoreView1);
                    	String score_str = "Score: "
                    			+ Utility.scoresDBAdapter.points
                    			+ " ("
                    			+ Utility.scoresDBAdapter.rank
                    			+ ")";
                    	score_txt.setText(score_str);
                    	Log.d("friendHealthFHASA", "Score: " + score_str);
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

                }
    
                public void onFacebookError(FacebookError error) {
                	Log.d("friendHealthFHASA", "onFacebookError(): " +
                			error.toString());
                }
    
                public void onError(DialogError e) {
                	Log.d("friendHealthFHASA", "onError(): " + e.toString());
                }
    
                public void onCancel() {
                	Log.d("friendHealthFHASA", "DialogListener.onCancel()");
                }
            });
        }
        else {
        	Log.d("friendHealthFHASA", "Logged in");
            try {
            	String jsonUser = Utility.facebook.request("me");
            	JSONObject obj = Util.parseJson(jsonUser);
            	
            	String facebookId = obj.optString("id");
            	SharedPreferences.Editor editor = Utility.mPrefs.edit();
            	editor.putString("facebookUID", facebookId);
            	editor.commit();
            	
            	Log.d("friendHealthFHASA", "Facebook UID is: " + facebookId);
            	
            	TextView user_name = (TextView) findViewById(R.id.textView1);
    			String facebookName = obj.optString("name");
    			user_name.setText(facebookName);
            	
            	Utility.scoresDBAdapter.checkUserScore(facebookId, facebookName);
            	TextView score_txt = (TextView) findViewById(R.id.scoreView1);
            	String score_str = "Score: "
            			+ Utility.scoresDBAdapter.points
            			+ " ("
            			+ Utility.scoresDBAdapter.rank
            			+ ")";
            	score_txt.setText(score_str);
            	Log.d("friendHealthFHASA", "Score: " + score_str);
            }
            catch (FacebookError e) {
            	Log.e("friendHealthFHASA", "FacebookError (sDBA): " + e.toString());
            	e.printStackTrace();
            }
            catch (Exception e) {
            	Log.e("friendHealthFHASA", "Exception (sDBA): " + e.toString());
            	e.printStackTrace();
            }
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
