package fhfl.android.mobapps_osm;


import org.osmdroid.util.GeoPoint;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

	SettingsContainer settings = new SettingsContainer();
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in
																		// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in
																	// Milliseconds
	protected LocationManager locationManager;
	private VariableChanged variableChanged = new VariableChanged(null);
	private Location position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment_place, new fragment_map());
		transaction.addToBackStack(null);
		transaction.commit();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
		variableChanged.setVariableChangeListener(settings);
	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			//wird geprüft ob GPS aktiv ist
			if (settings.isGpsOnControl()) {
				position = location;
				settings.getMapLocationTrkpList().clear();
				settings.getMapLocationTrkpList().add(new TrackPoint((int)(location.getLatitude() * 1000000),(int)(location.getLongitude() * 1000000)));
				if(settings.isGpsTrack()){
					settings.addtoTPL(new TrackPoint((int)(location.getLatitude() * 1000000),(int)(location.getLongitude() * 1000000))); // <-- Punkt der Liste anhängen	
					Log.i("Main", "New GPS Data");
				}
				
				if(settings.isFollowing()){
					settings.setCenter(new TrackPoint((int)(location.getLatitude() * 1000000),(int)(location.getLongitude() * 1000000)));
					Log.i("MainAct GPS: ", String.valueOf((int)(location.getLatitude() * 1000000)) + " " + String.valueOf((int)(location.getLongitude() * 1000000)));
				}
				variableChanged.setVariable(position);
			}
		}

		public void onStatusChanged(String s, int i, Bundle b) {

		}

		public void onProviderDisabled(String s) {

		}

		public void onProviderEnabled(String s) {

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		try{
			Fragment newFragment;
	
			if (item.getItemId() == R.id.Menue_SwitchButton_Map) {
				newFragment = new fragment_map();
			} else if (item.getItemId() == R.id.Menue_SwitchButton_Settings) {
				newFragment = new fragment_settings();
			} else if (item.getItemId() == R.id.Menue_SwitchButton_Stats) {
				newFragment = new fragment_stats();
			} else {
				newFragment = new fragment_map();
			}
	
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_place, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
	
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}
