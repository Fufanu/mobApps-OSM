package fhfl.android.mobapps_osm;

import org.osmdroid.util.GeoPoint;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
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
			String message = String.format(
					"New Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude());
			Log.d("GPS", message);

			if (settings.isGpsOnControl()) {
				if(settings.isFollowing()){
					position = location;
					settings.setCenter(new GeoPoint((int)position.getLatitude() / 1E6, (int)position.getLongitude() / 1E6));
					variableChanged.setVariable(position);
				}
				
				if(settings.isGpsTrack()){
					settings.addtoTPL(new TrackPoint(location.getLatitude(), location.getLongitude())); // <-- Punkt der Liste anhängen	
					Log.d("Main", "New GPS Data");
				}
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

}
