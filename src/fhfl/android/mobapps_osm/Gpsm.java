package fhfl.android.mobapps_osm;
import org.osmdroid.util.GeoPoint;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class Gpsm extends Fragment implements LocationListener{
	
	private SettingsContainer settings;
	private LocationManager manager;
	private LocationListener mlocListener;
	private double lati;
	private double longi;
	private MainActivity activity = (MainActivity) getActivity();
	
	//standard locationlistenerfunctions
	public void onProviderDisabled(String provider) 			
	{
		Toast.makeText(activity.getApplication(), provider, Toast.LENGTH_LONG).show();
	}
	public void onProviderEnabled(String provider) 
	{
		Toast.makeText(activity.getApplication(), provider, Toast.LENGTH_LONG).show();
	}	
	public void onStatusChanged(String provider, int status, Bundle arg2) 
	{
		Toast.makeText(activity.getApplication(), provider + status, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onLocationChanged(Location location) {
		MainActivity activity = (MainActivity) getActivity();
		settings = activity.settings;
		//gps on
		if (settings.isGpsOnControl())
		{
			manager = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
			//gps listener on ...implements LocationListener... 
			mlocListener = Gpsm.this;
			//location updates on
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, mlocListener);//5000=5sec, antitoastspam
			Toast.makeText(activity.getApplication(), "GPS gestartet.", Toast.LENGTH_LONG).show();
			lati = location.getLatitude();
			longi = location.getLongitude(); 
			settings.setCenter(new GeoPoint(lati, longi));
			settings.addtoTPL(new TrackPoint(lati, longi));
		}
		if (!(settings.isGpsOnControl())) 
		{
			//location updates off
			manager.removeUpdates(mlocListener);
			//gps listener off
			mlocListener = null;
			manager = null;
			Toast.makeText(activity.getApplication(), "GPS beendet.", Toast.LENGTH_LONG).show();
		}
	}
	public double getlongi()
	{
		return  longi;
	}
	public double getlati()
	{
		return lati;
	}
}
