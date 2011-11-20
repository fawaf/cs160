package edu.berkeley.cs160.teamk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
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
    	MenuItem mnu3 = menu.add(0, 2, 2, "Profile");
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
    	Intent i;
    	switch(item.getItemId()) {
    	case 0:
    		i = new Intent("edu.berkeley.cs160.teamk.AddTask");
    		((Activity) context).startActivityForResult(i, Utility.RC_NEWTASK);
    		return true;
    	case 1:
    		Toast.makeText(context, "Add Habit", Toast.LENGTH_SHORT).show();
    		return true;
    	case 2:
    		i = new Intent("edu.berkeley.cs160.teamk.ProfileActivity");
    		((Activity) context).startActivity(i);
    		return true;
    	case 3:
    		i = new Intent("edu.berkeley.cs160.teamk.Settings");
    		((Activity) context).startActivity(i);
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
    	MenuItem mnu4 = menu.add(0, 3, 3, "Activity Selector");
    	{
    		mnu4.setAlphabeticShortcut('d');
    	}
    }
    

    protected static boolean MenuChoice(Context context, MenuItem item) {
    	Intent i;
    	switch(item.getItemId()) {
    	case 0:
    		i = new Intent("edu.berkeley.cs160.teamk.ProfileActivity");
    		((Activity) context).startActivity(i);
    		return true;
    	case 1:
    		i = new Intent("edu.berkeley.cs160.teamk.Settings");
    		((Activity) context).startActivity(i);
    		return true;
    	case 2:
    		Toast.makeText(context, "Tutorials", Toast.LENGTH_SHORT).show();
    		return true;
    	case 3:
    		i = new Intent("edu.berkeley.cs160.teamk.FHActivitySelector");
    		((Activity) context).startActivity(i);
    		return true;
    	}
    	return false;
    }
    
}
