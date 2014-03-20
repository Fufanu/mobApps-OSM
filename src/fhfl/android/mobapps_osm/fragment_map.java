package fhfl.android.mobapps_osm;

import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.util.constants.MapViewConstants;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragment_map extends Fragment implements MapViewConstants {
	
	private MapView mapView;
	private ResourceProxy mapResourceProxy;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Initialisierung
		
		// OSM einbinden
		//mapView = new MapView(getActivity(), 256);
		//mapView = (MapView) mapView.findViewById(R.id.mapView);
		
		mapResourceProxy = new ResourceProxyImpl(inflater.getContext().getApplicationContext());
        mapView = new MapView(inflater.getContext(), 256, mapResourceProxy);
		
		mapView.setUseDataConnection(false);
		mapView.getController().setZoom(14);
		mapView.setMultiTouchControls(true);
		mapView.getController().setCenter(new GeoPoint(54.775235, 9.452794));
		
		return mapView;
		//return inflater.inflate(R.layout.fragment_map, container, false);
	}
}
