package fhfl.android.mobapps_osm;
import org.osmdroid.util.GeoPoint;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Gpsm extends MainActivity implements LocationListener{
	
	private SettingsContainer settings;
	private LocationManager manager;
	private LocationListener mlocListener;
	private double lati;
	private double longi;
	
	public Gpsm(SettingsContainer s){
		settings = s;
		Log.d("Gpsm", "created");
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new LocationListener(){
			public void onLocationChanged(Location location) {
			      // Called when a new location is found by the network location provider.
			      makeUseOfNewLocation(location);
			    }

			    public void onStatusChanged(String provider, int status, Bundle extras) {}

			    public void onProviderEnabled(String provider) {}

			    public void onProviderDisabled(String provider) {}

		};

	}
	

	
	
	public void makeUseOfNewLocation(Location location) {
		
		Log.d("Gpsm", "new Data");
		//gps on
		if (settings.isGpsOnControl())
		{

			//gps listener on ...implements LocationListener... 
			mlocListener = Gpsm.this;
			//location updates on
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, mlocListener);//5000=5sec, antitoastspam
			//Toast.makeText(activity.getApplication(), "GPS gestartet.", Toast.LENGTH_LONG).show();
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
			//Toast.makeText(activity.getApplication(), "GPS beendet.", Toast.LENGTH_LONG).show();
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

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.d("Gpsm", "falsche Methode");
		
	}




	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Gpsm", "falsche Methode");
		
	}




	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Gpsm", "falsche Methode");
		
	}




	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
