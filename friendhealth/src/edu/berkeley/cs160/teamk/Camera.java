package edu.berkeley.cs160.teamk;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


public final class Camera {
	
	protected static final Uri getOutputMediaFileUri(Context c, int type, String name) {
		
		File mediaStorageDir;
		
		String ext_state = Environment.getExternalStorageState();
		// If no external memory, this is bad.
		if (!Environment.MEDIA_MOUNTED.equals(ext_state)) {
			Toast.makeText(c, "No External Memory!",
					Toast.LENGTH_LONG).show();
			Log.d("friendHealthFHA", "No External Memory");
			mediaStorageDir = new File(
					Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_PICTURES), Utility.app_name);
		}
		else {
			mediaStorageDir = new File(
					Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_PICTURES), Utility.app_name);
		}
		// This location works best if you want the created images to be
		// shared between applications and persist after your app has been
		// uninstalled.
		
		// Create the storage directory if it does not exist.
		if (!mediaStorageDir.exists()) {
			boolean success = mediaStorageDir.mkdirs();
			if (!success) {
				Log.d("friendHealthFHA", "failed to create directory");
				return null;
			}
			else {
				Log.d("friendHealthFHA", "directory created");
			}
		}
		
		// Create a media file name.
		String timeStamp 
			= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String fileName = name.replace(" ", "_") + "_" + timeStamp;
		
		File mediaFile;
		if (type == Utility.MEDIA_TYPE_IMAGE) {
			mediaFile = new File(
					mediaStorageDir.getPath()
					+ File.separator
					+ fileName
					+ ".jpg");
		}
		else if (type == Utility.MEDIA_TYPE_VIDEO) {
			mediaFile = new File(
					mediaStorageDir.getPath()
					+ File.separator
					+ fileName
					+ ".mp4");
		}
		else {
			return null;
		}
		
		
		Uri file = Uri.fromFile(mediaFile);
		Log.d("friendHealthFHA", "File name: " + file.toString());
		
		return file;
	}
	
}
