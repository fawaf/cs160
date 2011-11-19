package edu.berkeley.cs160.teamk;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profileactivity);
		
		String jsonUser;
		try {
			jsonUser = Utility.facebook.request("me");
			JSONObject obj;
			obj = Util.parseJson(jsonUser);
			String facebookId = obj.optString("id");
			Log.d("friendHealthPA", "Facebook UID is: " + facebookId);
			ImageView user_picture;
		    user_picture=(ImageView)findViewById(R.id.profilePic);
		    URL img_value = null;
		    img_value = new URL("http://graph.facebook.com/"+facebookId+"/picture?type=large");
		    Bitmap mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
			user_picture.setImageBitmap(mIcon1);
			
			TextView user_name = (TextView) findViewById(R.id.fbName);
			String facebookName = obj.optString("name");
			user_name.setText(facebookName);
			
			jsonUser = Utility.facebook.request("me/friends");
			obj = Util.parseJson(jsonUser);
			JSONArray friends = obj.getJSONArray("data");
		
			TextView friend_name1 = (TextView) findViewById(R.id.friendName1);
			JSONObject friendObj1 = friends.getJSONObject(0);
			String friendName1 = friendObj1.optString("name");
			friend_name1.setText(friendName1);
			
			TextView friend_name2 = (TextView) findViewById(R.id.friendName2);
			JSONObject friendObj2 = friends.getJSONObject(2);
			String friendName2 = friendObj2.optString("name");
			friend_name2.setText(friendName2);
			
			TextView friend_name3 = (TextView) findViewById(R.id.friendName3);
			JSONObject friendObj3 = friends.getJSONObject(3);
			String friendName3 = friendObj3.optString("name");
			friend_name3.setText(friendName3);
	
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

	}

}
