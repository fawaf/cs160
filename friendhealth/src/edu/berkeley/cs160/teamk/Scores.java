package edu.berkeley.cs160.teamk;

import android.util.Log;

public final class Scores {
	
	String fb_user_id = Utility.mPrefs.getString("facebookUID", null);
	
	public static final void calculateFacebookScore(String fb_user_id) {
		String[] photoids = Utility.scoresDBAdapter.getPhotoID(fb_user_id);
		for (int i = 0; i < photoids.length; i++) {
			String basescore = Utility.scoresDBAdapter.getBaseScore(photoids[i]);
			Log.d("Scores", "Base Score: " + basescore);
		}
	}
	
}
