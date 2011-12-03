package edu.berkeley.cs160.teamk;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class ProfileActivity extends Activity {
	
	Button logout_button;
	TextView total_score;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profileactivity);
		
		try {
			String jsonUser = Utility.facebook.request("me");
			JSONObject obj = Util.parseJson(jsonUser);
			String facebookId = Utility.mPrefs.getString("facebookUID", "");
			Log.d("friendHealthPA", "Facebook UID is: " + facebookId);
			ImageView user_picture = (ImageView) findViewById(R.id.profilePic);
		    URL img_value = new URL("http://graph.facebook.com/" + facebookId + "/picture?type=large");
		    Bitmap mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
			user_picture.setImageBitmap(mIcon1);
			
			TextView user_name = (TextView) findViewById(R.id.fbName);
			String facebookName = obj.optString("name");
			user_name.setText(facebookName);
			
			jsonUser = Utility.facebook.request("me/friends");
			obj = Util.parseJson(jsonUser);
			JSONArray friends = obj.getJSONArray("data");
			
			ListView s = (ListView) findViewById(R.id.spinner);
			String[] friends_array = new String[friends.length()];
			for (int i = 0; i < friends.length(); i++) {
				JSONObject friendObj = friends.getJSONObject(i);
				friends_array[i] = friendObj.optString("name");
			}
			Arrays.sort(friends_array, String.CASE_INSENSITIVE_ORDER);
			s.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, friends_array));
	
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
			Log.d("friendHealthPA", "FacebookError: " + e.toString());
			Log.d("friendHealthPA", "Access Token: " + Utility.facebook.getAccessToken());
			e.printStackTrace();
		}
		
		//---get TextView for total score---
		total_score = (TextView) findViewById(R.id.fHScore);
		
		//---event handler for total score TextView---
		total_score.setText(String.valueOf(Utility.scoresDBAdapter.points));
		
		//---get the Log Out button---
		logout_button = (Button) findViewById(R.id.btn_logout);
	    
	    //---event handler for the Log Out button---
	    logout_button.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
	    		Log.d("friendHealthFHASA", "Logging out of Facebook");
				try {
					Utility.facebook.logout(getBaseContext());
				} catch (Exception e){
					Log.d("friendHealthFHASA", e.toString());
				}
				Log.d("friendHealthFHASA", "Access Token: " + Utility.facebook.getAccessToken());
				Log.d("friendHealthFHASA", "Access Expires: " + Utility.facebook.getAccessExpires());
				Utility.mPrefs = getBaseContext().getSharedPreferences("FHActivitySelector", MODE_PRIVATE);
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.clear();
	            boolean result = editor.commit();
	            Log.d("friendHealthFHASA", "SharedPreferences ommit result is: " + result);
	    		Intent i = new Intent("edu.berkeley.cs160.teamk.FHActivitySelector");
	    		startActivity(i);
	    	}
	    });
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
