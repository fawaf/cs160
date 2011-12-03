package edu.berkeley.cs160.teamk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class OptionsMenu extends Activity {	

	static Context ctxt;
	
	protected void FHASCreateMenu(Menu menu) {
    	MenuItem mnu1 = menu.add(0, 0, 0, "Add Task");
    	MenuItem mnu2 = menu.add(0, 1, 1, "Profile");
    	MenuItem mnu3 = menu.add(0, 2, 2, "Settings");
    	MenuItem mnu4 = menu.add(0, 3, 3, "Tutorials");
    	MenuItem mnu5 = menu.add(0, 4, 4, "Feedback");
    	mnu1.setAlphabeticShortcut('a');
    	mnu2.setAlphabeticShortcut('b');
    	mnu3.setAlphabeticShortcut('c');
    	mnu4.setAlphabeticShortcut('d');
    	mnu5.setAlphabeticShortcut('e');
    }
    
    protected boolean FHASMenuChoice(Context context, MenuItem item) {
    	Intent i;
    	ctxt = context;
    	switch(item.getItemId()) {
    	case 0:
    		i = new Intent("edu.berkeley.cs160.teamk.AddTask");
    		((Activity) context).startActivityForResult(i, Utility.RC_NEWTASK);
    		return true;
    	case 1:
    		i = new Intent("edu.berkeley.cs160.teamk.ProfileActivity");
    		((Activity) context).startActivity(i);
    		return true;
    	case 2:
    		i = new Intent("edu.berkeley.cs160.teamk.Settings");
    		((Activity) context).startActivity(i);
    		return true;
    	case 3:
    		Log.d("friendHealthOM", "About to call showDialog");
    		showDialog(0);
    		Log.d("friendHealthOM", "Finished showDialog");
    		return true;
    	case 4:
    		Log.d("friendHealthOM", 
    				"Sending email to " + Utility.fH_email);
    		Intent emailIntent = new Intent(Intent.ACTION_SEND);
    		emailIntent.setData(Uri.parse("mailto:"));
    		String[] to = {Utility.fH_email};
    		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
    		emailIntent.setType("message/rfc822");
    		((Activity) context).startActivity(
    				Intent.createChooser(emailIntent,  "Email"));
    		return true;
    	}
    	return false;
    }
    
    protected void CreateMenu(Menu menu) {
    	MenuItem mnu1 = menu.add(0, 0, 0, "Activity Selector");
    	MenuItem mnu2 = menu.add(0, 1, 1, "Profile");
    	MenuItem mnu3 = menu.add(0, 2, 2, "Settings");
    	MenuItem mnu4 = menu.add(0, 3, 3, "Tutorials");
    	MenuItem mnu5 = menu.add(0, 4, 4, "Feedback");
    	mnu1.setAlphabeticShortcut('a');
    	mnu2.setAlphabeticShortcut('b');
    	mnu3.setAlphabeticShortcut('c');
    	mnu4.setAlphabeticShortcut('d');
    	mnu5.setAlphabeticShortcut('e');
    }
    

    protected boolean MenuChoice(Context context, MenuItem item) {
    	Intent i;
    	ctxt = context;
    	switch(item.getItemId()) {
    	case 0:
    		i = new Intent("edu.berkeley.cs160.teamk.FHActivitySelector");
    		((Activity) context).startActivity(i);
    		return true;
    	case 1:
    		i = new Intent("edu.berkeley.cs160.teamk.ProfileActivity");
    		((Activity) context).startActivity(i);
    		return true;
    	case 2:
    		i = new Intent("edu.berkeley.cs160.teamk.Settings");
    		((Activity) context).startActivity(i);
    		return true;
    	case 3:
    		showDialog(0);
    		return true;
    	case 4:
    		Log.d("friendHealthOM", 
    				"Sending email to " + Utility.fH_email);
    		Intent emailIntent = new Intent(Intent.ACTION_SEND);
    		emailIntent.setData(Uri.parse("mailto:"));
    		String[] to = {Utility.fH_email};
    		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
    		emailIntent.setType("message/rfc822");
    		((Activity) context).startActivity(
    				Intent.createChooser(emailIntent,  "Email"));
    		return true;
    	}
    	return false;
    }
    
    protected Dialog onCreateDialog(int id) {
    	switch (id){
    	case 0:
    		Log.d("friendHealthOM", "In Dialog Builder");
    		AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
    		builder.setMessage("Are you sure you want to go to YouTube to view the tutorial?");
    		builder.setPositiveButton("Yes", new
    				DialogInterface.OnClickListener(){
    				public void onClick(DialogInterface dialog,
    				int whichButton)
    				{
    					String video_path = "http://www.youtube.com/watch?v=ICoyNN9akcc";
    		    		Uri uri = Uri.parse(video_path);

    		    		// With this line the Youtube application, if installed, will launch immediately.
    		    		// Without it you will be prompted with a list of the application to choose.
    		    		uri = Uri.parse("vnd.youtube:"  + uri.getQueryParameter("v"));

    		    		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    		    		((Activity) ctxt).startActivity(intent);
    				}
    		
    				});
    		builder.setNegativeButton("No", new
    				DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog,
    				int whichButton){}
    				
    		});
    		builder.create();
    		builder.show();
    	}
    	return null;
    }
    
}
