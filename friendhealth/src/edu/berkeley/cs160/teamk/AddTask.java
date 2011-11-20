package edu.berkeley.cs160.teamk;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTask extends Activity {
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtask);
		
		//---get the Add Task button---
		Button btn_addTask = (Button) findViewById(R.id.btn_addTask);
		
		//---event handler for the Add Task button---
		btn_addTask.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent data = new Intent();
				Bundle extras = new Bundle();
				
				//---get the Task name---
				EditText txt_taskName =
						(EditText) findViewById(R.id.taskName);
				//---set the data to pass back---
				extras.putString("name",
						txt_taskName.getText().toString());
				
				// Get Tags.
				EditText txt_taskTags =
						(EditText) findViewById(R.id.taskTags);
				// Put tags into the Bundle.
				extras.putString("tags",
						txt_taskTags.getText().toString());
				
				data.putExtras(extras);
				setResult(RESULT_OK, data);
				
				// Close the activity.
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