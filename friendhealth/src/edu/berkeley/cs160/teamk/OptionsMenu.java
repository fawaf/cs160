package edu.berkeley.cs160.teamk;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
//import android.util.Log;


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
    		Toast.makeText(context, "Log Out", Toast.LENGTH_SHORT).show();
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
    
    protected static void BACreateMenu(Menu menu) {
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
    
    protected static boolean BAMenuChoice(Context context, MenuItem item) {
    	switch(item.getItemId()) {
    	case 0:
    		Intent i = new Intent("edu.berkeley.cs160.teamk.AddTask");
    		((Activity) context).startActivityForResult(i, Utility.RC_NEWTASK);
    		return true;
    	case 1:
    		Toast.makeText(context, "Add Habit", Toast.LENGTH_SHORT).show();
    		return true;
    	case 2:
    		Toast.makeText(context, "Log Out", Toast.LENGTH_SHORT).show();
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
}