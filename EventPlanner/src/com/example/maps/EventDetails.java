package com.example.maps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class EventDetails extends ActionBarActivity {

	private static final String LOG_TAG = "Event Details Activity";

	eventMarker temp;
	ArrayOfEvents events;
	Spinner mspin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		temp = eventMarker.getInstance(this);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		events = ArrayOfEvents.getInstance(this);
		EditText tv = (EditText) findViewById(R.id.editText1);
		tv.setText(temp.Title);
		DatePicker dp = (DatePicker) findViewById(R.id.datePicker1);
		dp.setMinDate(System.currentTimeMillis() - 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_details, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_event_details,
					container, false);
			return rootView;
		}
	}
	
	public void makeEvent(View V){
		EditText et1 = (EditText) findViewById(R.id.editText1);
		temp.Title = et1.getText().toString();
		EditText et2 = (EditText) findViewById(R.id.editText2);
		temp.Description = et2.getText().toString();
		DatePicker dp = (DatePicker) findViewById(R.id.datePicker1);
		Log.i(LOG_TAG, Integer.toString(dp.getYear()));
		TimePicker tp = (TimePicker) findViewById(R.id.timePicker1);
		temp.deadline.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute());
		Log.i(LOG_TAG, temp.deadline.toString());		
		events.eventsArray.add(temp.copy());
		onBackPressed();
	}
	
	public void cancel(View V){
		onBackPressed();
	}

}
