package edu.berkeley.cs160.teamk;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddTaskActivity extends Activity {
	
	Intent data;
	Bundle extras;
	String taskName = "";
	String taskTags = "";
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtask);
		
		//---get the Add Task button---
		Button btn_addTask = (Button) findViewById(R.id.btn_addTask);
		
		//---event handler for the Add Task button---
		btn_addTask.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				data = new Intent();
				extras = new Bundle();
				
				//---get the Task name---
				EditText txt_taskName =
						(EditText) findViewById(R.id.taskName);
				taskName = txt_taskName.getText().toString();
				Log.d("friendHealthATA", "Task name: " + taskName);
				// Get Tags.
				EditText txt_taskTags =
						(EditText) findViewById(R.id.taskTags);
				taskTags = txt_taskTags.getText().toString(); 
				Log.d("friendHealthATA", "Task tag: " + taskTags);
						
				
				//---set the data to pass back---
				extras.putString("name", taskName);
				// Put tags into the Bundle.
				extras.putString("tags", taskTags);
				// Only adding task rather taking picture.
				extras.putString("result", "only_add");
				
				data.putExtras(extras);
				setResult(RESULT_OK, data);
				
				// Close the activity.
				finish();
			}
		});
		
		//---get the Add Task button---
		Button btn_takePicture= (Button) findViewById(R.id.btn_takePicture);
		
		//---event handler for the Add Task button---
		btn_takePicture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				data = new Intent();
				extras = new Bundle();
				
				//---get the Task name---
				EditText txt_taskName =
						(EditText) findViewById(R.id.taskName);
				taskName = txt_taskName.getText().toString();
				Log.d("friendHealthATA", "Task name: " + taskName);
				// Get Tags.
				EditText txt_taskTags =
						(EditText) findViewById(R.id.taskTags);
				taskTags = txt_taskTags.getText().toString(); 
				Log.d("friendHealthATA", "Task tag: " + taskTags);
				
				extras.putString("result", "take_photo");
				extras.putString("name", taskName);
				data.putExtras(extras);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	OptionsMenu om = new OptionsMenu();
    	om.CreateMenu(menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	OptionsMenu om = new OptionsMenu();
    	return om.MenuChoice(this, item);
    }

}