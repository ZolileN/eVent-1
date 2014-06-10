package com.example.maps;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

public class ViewTaskActivity extends ActionBarActivity {

	eventMarker temp;
	ArrayOfEvents events;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	protected void onResume(){
		super.onResume();
		temp = eventMarker.getInstance(this);
		events = ArrayOfEvents.getInstance(this);
		
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(temp.Title);
		tv = (TextView) findViewById(R.id.textView2);
		tv.setText(temp.Description);
	    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
		tv = (TextView) findViewById(R.id.textView4);
		tv.setText(sdf.format(temp.deadline.getTime()));
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_view_task,
					container, false);
			return rootView;
		}
	}

	public void delete(View V){
		for(int i = 0; i < events.eventsArray.size(); i++){
			if(events.eventsArray.get(i).Loc.equals(temp.Loc) && events.eventsArray.get(i).Title.equals(temp.Title)){
				events.eventsArray.remove(i);
				break;
			}
		}
		
		Gson gson = new Gson();
		
		SharedPreferences settings = getSharedPreferences(MainActivity.MYPREFS, 0);
		Editor editor = settings.edit();
		editor.putString(MainActivity.PREF_STRING_1, gson.toJson(events, ArrayOfEvents.class));
		editor.commit();
		
		onBackPressed();
	}
	
	public void edit(View V){
		temp = eventMarker.getInstance(this);
     	
    	Intent intent = new Intent(this, EditEvent.class);
		startActivity(intent);
	}
}
