package edu.berkeley.cs160.teamk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class OptionsMenu extends Activity {

	protected static void FHASCreateMenu(Menu menu) {
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
    
    protected static boolean FHASMenuChoice(Context context, MenuItem item) {
    	switch(item.getItemId()) {
    	case 0:
    		Intent i = new Intent("edu.berkeley.cs160.teamk.AddTask");
    		((Activity) context).startActivityForResult(i, Utility.RC_NEWTASK);
    		return true;
    	case 1:
    		Toast.makeText(context, "Add Habit", Toast.LENGTH_SHORT).show();
    		return true;
    	case 2:
    		Log.d("friendHealthFHASA", "Logging out of Facebook");
			try {
				Utility.facebook.logout(context);
			} catch (Exception e){
				Log.d("friendHealthFHASA", e.toString());
			}
			Log.d("friendHealthFHASA", "Access Token: " + Utility.facebook.getAccessToken());
			Log.d("friendHealthFHASA", "Access Expires: " + Utility.facebook.getAccessExpires());
			Utility.mPrefs = context.getSharedPreferences("FHActivitySelector", MODE_PRIVATE);
			SharedPreferences.Editor editor = Utility.mPrefs.edit();
			editor.clear();
            boolean result = editor.commit();
            Log.d("friendHealthFHASA", "SharedPreferences ommit result is: " + result);
    		return true;
    	case 3:
    		Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show();
    		return true;
    	case 4:
    		Toast.makeText(context, "Tutorials", Toast.LENGTH_SHORT).show();
    		return true;
    	}
    	return false;
    }
    
    protected static void CreateMenu(Menu menu) {
    	MenuItem mnu1 = menu.add(0, 0, 0, "Profile");
    	{
    		mnu1.setAlphabeticShortcut('a');
    	}
    	MenuItem mnu2 = menu.add(0, 1, 1, "Settings");
    	{
    		mnu2.setAlphabeticShortcut('b');
    	}
    	MenuItem mnu3 = menu.add(0, 2, 2, "Tutorials");
    	{
    		mnu3.setAlphabeticShortcut('c');
    	}
    }
    
    protected static boolean MenuChoice(Context context, MenuItem item) {
    	switch(item.getItemId()) {
    	case 0:
    		Intent i = new Intent("edu.berkeley.cs160.teamk.ProfileActivity");
    		((Activity) context).startActivity(i);
    		return true;
    	case 1:
    		Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show();
    		return true;
    	case 2:
    		Toast.makeText(context, "Tutorials", Toast.LENGTH_SHORT).show();
    		return true;
    	}
    	return false;
    }
    
}