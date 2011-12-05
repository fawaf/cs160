package edu.berkeley.cs160.teamk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import android.util.Log;


public final class Scores {
	
	public static final void calculateFacebookScore(String fb_user_id) {
		String[] photoids = Utility.scoresDBAdapter.getPhotoByFBID(fb_user_id);
		for (int i = 0; i < photoids.length; i++) {
			String basescore = Utility.scoresDBAdapter.getBaseScore(photoids[i]);
			Log.d("Scores", "Base Score: " + basescore);
			try {
				String likesresult = Utility.facebook.request(photoids[i] + "/likes");
				Log.d("Scores", "Photo like result: " + likesresult);
				JSONObject lobj = Util.parseJson(likesresult);
				JSONArray likes = new JSONArray(lobj.optString("data"));
				Log.d("Scores", "Photo likes: " + likes);
				int numLikes = likes.length();
				Log.d("Scores", "Photo number of likes: " + numLikes);
				String commentsresult = Utility.facebook.request(photoids[i] + "/comments");
				Log.d("Scores", "Photo comments result: " + commentsresult);
				JSONObject cobj = Util.parseJson(commentsresult);
				JSONArray comments = new JSONArray(cobj.optString("data"));
				Log.d("Scores", "Photo comments: " + comments);
				int numComments = comments.length();
				Log.d("Scores", "Photo number of comments: " + numComments);
				
				int totalFacebookScore = numLikes + numComments;
				basescore = basescore.replaceAll("\\n","");
				int calculatedScore = totalFacebookScore + Integer.parseInt(basescore);
				Utility.scoresDBAdapter.updateCalculatedScore(
						String.valueOf(calculatedScore), photoids[i]);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FacebookError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("Scores", "Base Score: " + basescore);
		}
	}
	
}
