package edu.berkeley.cs160.teamk;

import android.content.SharedPreferences;
import com.facebook.android.Facebook;

public class Utility {
	
	public static final String app_name = "friendHealth";
	public static final String APP_ID = "177765768977545";
	public static final int RC_ACTIVITY = 1001;
	public static final int RC_NEWTASK = 1002;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int RC_ACTIVITYSUBMISSION = 101;
	public static final int RC_INVITE = 102;
	
	public static Facebook facebook;
	public static SharedPreferences mPrefs;
	
	public static DBAdapter dbAdapter;
	
}