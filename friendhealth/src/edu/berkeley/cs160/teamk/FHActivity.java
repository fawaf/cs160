package edu.berkeley.cs160.teamk;

import android.app.Activity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.net.Uri;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FHActivity extends Activity {
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int RC_ACTIVITYSUBMISSION = 101;
	public static final int RC_INVITE = 102;
	
	Button btn_picture, btn_reject, btn_invite, btn_help;
	String act_name = "";
	String img_filename = "";
	String response = "";
	int score = 0;
	SharedPreferences.Editor editor = Utility.mPrefs.edit();
	
	//---the images to display---
	Integer[] imageIDs = {
			R.drawable.pic1,
			R.drawable.pic2,
			R.drawable.pic3,
			R.drawable.pic4,
			R.drawable.pic5,
			R.drawable.pic6,
			R.drawable.pic7
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fhactivity);
		Bundle extras = getIntent().getExtras();
		act_name = Utility.mPrefs.getString("act_name", "");
		score = Utility.mPrefs.getInt("act_score", 0);
		img_filename = Utility.mPrefs.getString("act_img_filename", "");

		if (extras != null) {
			act_name = extras.getString("name");
			score = extras.getInt("score");
			
			editor.putString("act_name", act_name);
			editor.putInt("act_score", score);
			editor.commit();
			
		}
		TextView txt_ActTitle = (TextView) findViewById(R.id.txt_ActTitle);
		TextView txt_ActPt = (TextView) findViewById(R.id.txt_ActPt);
		txt_ActTitle.setText(act_name);
		txt_ActPt.setText("(+ "+ score + " points)");
			
		
		Gallery gallery = (Gallery) findViewById(R.id.activityGallery);
		
		
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(
					AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(getBaseContext(),
						"pic" + (position + 1) +  " selected",
						Toast.LENGTH_SHORT).show();
				
				//---display the images selected---
				ImageView imageView = (ImageView) findViewById(R.id.img_fhAct);
				imageView.setImageResource(imageIDs[position]);
			}
		});
		
		//---btn_picture---
		btn_picture = (Button) findViewById(R.id.btn_ActTakePicture);
		// Handle click of button.
		btn_picture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Log.d("friendHealthFHA", "Taking Image from Activity");
				
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, act_name);
				img_filename = fileUri.toString();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				
				SharedPreferences.Editor editor = Utility.mPrefs.edit();
				editor.putString("act_img_filename", img_filename);
				editor.commit();
				
				Log.d("friendHealthFHA", "Image name: " + img_filename);
				
				// start the image capture Intent.
				startActivityForResult(intent, 
						CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		
		//---btn_invite---
		btn_invite = (Button) findViewById(R.id.btn_ActInvite);
		// Handle click of button.
		btn_invite.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(
						"edu.berkeley.cs160.teamk.BallyhooActivity");
				intent.putExtras(getIntent().getExtras());
				startActivityForResult(intent, RC_INVITE);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Log.d("friendHealthFHA", act_name + " image taken.");
				
				Intent intent = new Intent(
						"edu.berkeley.cs160.teamk.ActivitySubmission");
				Bundle extras = new Bundle();
				extras.putString("name", act_name);
				Log.d("friendHealthFHA", "score: " + score);
				extras.putInt("score", score);
				Log.d("friendHealthFHA", "img_filename: " + img_filename);
				extras.putString("filename", img_filename);
				intent.putExtras(extras);
				
				Log.d("friendHealthFHA", "Starting submission activity");
				startActivity(intent);
			}
		}
		else if (requestCode == RC_INVITE) {
			if (resultCode == RESULT_OK) {
				Log.d("friendHealthFHA", "Showing toast and setting invite background color");
				response = Utility.mPrefs.getString("act_response", "");
				Bundle extras = data.getExtras();
				
				if (extras != null) {
					response = extras.getString("response");
					editor.putString("act_response", response);
					Toast.makeText(this, response,
							Toast.LENGTH_SHORT).show();
				}
				
				btn_invite.setBackgroundColor(0xFF00FF00);
			}
		}
		else if (requestCode == RC_ACTIVITYSUBMISSION) {
			if (resultCode == RESULT_OK) {
				Intent output = new Intent();
				Bundle extras = new Bundle();
				extras.putString("result", "completed");
				output.putExtras(extras);
				setResult(RESULT_OK, data);
				finish();
			}
		}
	}
	
	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private int itemBackground;
		
		public ImageAdapter(Context c) {
			context = c;
			//---setting the style---
			TypedArray a = obtainStyledAttributes(R.styleable.ActivityGallery);
			itemBackground = a.getResourceId(
					R.styleable.ActivityGallery_android_galleryItemBackground,
					0);
			a.recycle();
		}
		
		//---returns the number of images---
		public int getCount() {
			return imageIDs.length;
		}
		
		//---returns the ID of an item---
		public Object getItem(int position) {
			return position;
		}
		
		//---returns the ID of an time---
		public long getItemId(int position) {
			return position;
		}
		
		//---returns an ImageView view---
		public View getView(
				int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			imageView.setImageResource(imageIDs[position]);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(150, 120));
			imageView.setBackgroundResource(itemBackground);
			return imageView;
		}
	}
	
	private Uri getOutputMediaFileUri(int type, String name) {
		
		File mediaStorageDir;
		
		String ext_state = Environment.getExternalStorageState();
		// If no external memory, this is bad.
		if (!Environment.MEDIA_MOUNTED.equals(ext_state)) {
			Toast.makeText(this, "No External Memory!",
					Toast.LENGTH_SHORT).show();
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
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(
					mediaStorageDir.getPath()
					+ File.separator
					+ fileName
					+ ".jpg");
		}
		else if (type == MEDIA_TYPE_VIDEO) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	OptionsMenu om = new OptionsMenu();
    	om.FHASCreateMenu(menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	OptionsMenu om = new OptionsMenu();
    	return om.FHASMenuChoice(this, item);
    }

}
