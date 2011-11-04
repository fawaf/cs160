package edu.berkeley.cs160.teamk;
// Core code copied from Beginning Android Application Development
// by Wei-Meng Lee.


import android.app.Activity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
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


public class FHActivity extends Activity{
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	
	Button btn_picture, btn_reject, btn_invite, btn_help;
	
	String act_name = "";
	String img_filename = "";
	String response = "";
	int score = 0;
	
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
		if (extras != null) {
			act_name = extras.getString("name");
			score = extras.getInt("score");
			response = extras.getString("invite_response");
	
			if(response != null){
				Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
			}
			
			TextView txt_ActTitle = (TextView) findViewById(R.id.txt_ActTitle);
			txt_ActTitle.setText(act_name + " (+" + score + " points)");
		}
		
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
				Log.d("friendHealth", "Taking Image from Activity");
				
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, act_name);
				img_filename = fileUri.toString();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				
				Log.d("friendHealth", "Image name: " + img_filename);
				
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
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Log.d("friendHealth", act_name + " image taken.");
				
				Intent intent = new Intent(
						"edu.berkeley.cs160.teamk.ActivitySubmission");
				Bundle extras = new Bundle();
				extras.putString("name", act_name);
				Log.d("friendHealthfh", "score: " + score);
				extras.putInt("score", score);
				Log.d("friendHealthfh", "img_filename: " + img_filename);
				extras.putString("filename", img_filename);
				intent.putExtras(extras);
				
				Log.d("friendHealth", "Starting submission activity");
				startActivity(intent);
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
		String app_name = "friendHealth";
		
		File mediaStorageDir;
		
		String ext_state = Environment.getExternalStorageState();
		// If no external memory, this is bad.
		if (!Environment.MEDIA_MOUNTED.equals(ext_state)) {
			Toast.makeText(this, "No External Memory!",
					Toast.LENGTH_SHORT).show();
			Log.d(app_name, "No External Memory");
			mediaStorageDir = new File(
					Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_PICTURES), app_name);
			/*
			mediaStorageDir = new File(
					Environment.getDataDirectory(), app_name);
					*/
		}
		else {
			mediaStorageDir = new File(
					Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_PICTURES), app_name);
		}
		// This location works best if you want the created images to be
		// shared between applications and persist after your app has been
		// uninstalled.
		
		// Create the storage directory if it does not exist.
		if (!mediaStorageDir.exists()) {
			boolean success = mediaStorageDir.mkdirs();
			if (!success) {
				Log.d(app_name, "failed to create directory");
				return null;
			}
			else {
				Log.d(app_name, "directory created");
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
		Log.d(app_name, "file name " + file.toString());
		
		return file;
	}
}
