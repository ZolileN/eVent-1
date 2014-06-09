package com.example.maps;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection; //projections are used to get points on screen
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//used to make custom marker images

public class MakeEventActivity extends FragmentActivity implements OnMapClickListener, OnMarkerClickListener, OnInfoWindowClickListener{

	eventMarker temp;
	ArrayOfEvents events;
		
	private GoogleMap googleMap;
	Projection projection;
	DisplayMetrics metrics;
	
	static int screen_height;
	static int screen_width;
	
	private LocationManager locationManager;
	
	private static final String LOG_TAG = "Make Event Activity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_event);
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		relativeLayout.setVisibility(View.GONE);
        try {
            // Loading map
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	// Define a listener that responds to location updates
	LocationListener networkLocationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	      // Calls your function that uses the location.
	    	if (googleMap != null){
		        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
		    	locationManager.removeUpdates(this);
				RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
				relativeLayout.setVisibility(View.VISIBLE);
	    	}
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}
	    public void onProviderEnabled(String provider) {}
	    public void onProviderDisabled(String provider) {}
	  };
	  
	// Define a listener that responds to location updates
	LocationListener gpsLocationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {}
	    public void onStatusChanged(String provider, int status, Bundle extras) {}
	    public void onProviderEnabled(String provider) {}
	    public void onProviderDisabled(String provider) {}
	  };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.make_event, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_make_event,
					container, false);
			return rootView;
		}
	}
	
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            else
            {
            	projection = googleMap.getProjection();
    			googleMap.setOnMapClickListener(this);
    			googleMap.setOnMarkerClickListener(this);
    			googleMap.setOnInfoWindowClickListener(this);
    			googleMap.setMyLocationEnabled(true);
    			getFragmentManager().findFragmentById(R.id.map).setRetainInstance(true);
            }
            
    		//googleMap.setOnMarkerClickListener(googleMap);
    		
    		metrics = new DisplayMetrics();
    		getWindowManager().getDefaultDisplay().getMetrics(metrics);
    		screen_width = metrics.widthPixels;
    		screen_height = metrics.heightPixels;
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
    	events = ArrayOfEvents.getInstance(this);
    	Log.i(LOG_TAG, events.toString());
        initilizeMap();
        temp = eventMarker.getInstance(this);
        googleMap.clear();
        for(int i = 0; i < events.eventsArray.size(); i++)
    		googleMap.addMarker(new MarkerOptions().position(events.eventsArray.get(i).Loc).title(events.eventsArray.get(i).Title));
    }	
    
    @Override
    protected void onDestroy(){
    	locationManager.removeUpdates(gpsLocationListener);
    	super.onDestroy();
    }
    
    @Override
    public void onMapClick (LatLng point)
    {
    	temp = eventMarker.getInstance(this);
    	temp.Loc = point;
     	
    	Intent intent = new Intent(this, EventDetails.class);
		startActivity(intent);
    }
    
    /*
    public void clickPlaceEvent(View v){
		projection = googleMap.getProjection();
		LatLng tempLoc = projection.fromScreenLocation(new Point((int) (screen_width / 2), (int) (screen_height/2)));
		/*SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
	    editor.putString(CreateEvent.PREF_LAT, Double.toString(tempLoc.latitude));
	    editor.putString(CreateEvent.PREF_LONG, Double.toString(tempLoc.longitude));
	    editor.commit();

		Intent intent = new Intent(this, CreateEvent.class);
		startActivity(intent);
		temp = eventMarker.getInstance(this);
		temp.Loc = tempLoc;
		EditText tempText = (EditText)findViewById(R.id.editText1);
		temp.Title = tempText.getText().toString();
		/*.snippet("Kiel is cool")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		tempText.setText("New Event Title");
		
		Intent intent = new Intent(this, EventDetails.class);
		startActivity(intent);
	}*/
    
	public void onClickTitleEditText(View v)
	{
		EditText tempText = (EditText)findViewById(R.id.editText1);
		tempText.getText().clear();
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}
	
    @Override
    public void onInfoWindowClick(Marker marker) {
    	for(int i = 0; i < events.eventsArray.size(); i++){
    		if(events.eventsArray.get(i).Loc.equals(marker.getPosition()) && events.eventsArray.get(i).Title.equals(marker.getTitle())){
    			temp.Title = events.eventsArray.get(i).Title;
    			temp.Description = events.eventsArray.get(i).Description;
    			temp.Loc = events.eventsArray.get(i).Loc;
    			temp.deadline = events.eventsArray.get(i).deadline;
    			break;
    		}
    	}
    	Intent intent = new Intent(this, ViewTaskActivity.class);
    	startActivity(intent);
    }

}
