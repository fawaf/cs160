package edu.berkeley.cs160.teamk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.os.Bundle;
import android.util.Log;

public final class Scores {
	
	String fb_user_id = Utility.mPrefs.getString("facebookUID", null);
	
	public static final void calculateFacebookScore(String fb_user_id) {
		String[] photoids = Utility.scoresDBAdapter.getPhotoID(fb_user_id);
		for (int i = 0; i < photoids.length; i++) {
			String basescore = Utility.scoresDBAdapter.getBaseScore(photoids[i]);
			Bundle bundle = new Bundle();
			bundle.putString("wejlkg", "jksgad");
			try {
				String result = Utility.facebook.request(photoids[i]);
				Log.d("Scores", "Photo result: " + result);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("Scores", "Base Score: " + basescore);
		}
	}
	
}
