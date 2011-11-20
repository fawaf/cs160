package edu.berkeley.cs160.teamk;

import android.content.SharedPreferences;
import com.facebook.android.Facebook;

public class Utility {
	public static final String app_name = "friendHealth";
	public static final String APP_ID = "177765768977545";
	public static final int RC_ACTIVITY = 1001;
	public static final int RC_NEWTASK = 1002;
	
	public static Facebook facebook;
	public static SharedPreferences mPrefs;
	
	public static DBAdapter dbAdapter;
}