package fhfl.android.mobapps_osm;


import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import ecl.Overlays.FhflTrackOverlay;
import ecl.Overlays.FhflVectorOverlay;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

public class fragment_map extends Fragment implements MapEventsReceiver, OnClickListener {
	
	private SettingsContainer settings;
	private Context context;
	private ResourceProxy mapResourceProxy;
	private ToggleButton TB_Center;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//Initialisierung
		context = inflater.getContext();
		MainActivity activity = (MainActivity) getActivity();
		settings = activity.settings;
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		TB_Center = (ToggleButton)view.findViewById(R.id.fMapToggleCenter);
		TB_Center.setOnClickListener(this);
		TB_Center.setChecked(settings.isFollowing());
		
		mapViewInit(view);
		
		return view;
	}
	
	private void mapViewInit(View view)
	{
		// OSM anlegen
        final MapView mapView = (MapView) view.findViewById(R.id.mapView);
        settings.setMapView(mapView);
        mapResourceProxy = new ResourceProxyImpl(context.getApplicationContext());
		
		//Settings
        mapView.setUseDataConnection(settings.isInternetConnection());
        mapView.setTileSource(settings.getTileSource());
		mapView.getController().setZoom(settings.getZoom());
		mapView.getController().setCenter(settings.getCenter()); //Koordinaten der Mensa     
		mapView.setUseSafeCanvas(true);
		mapView.setClickable(false);
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);
		
		// ScaleBar Overlay
		ScaleBarOverlay mapScaleBarOverlay = new ScaleBarOverlay(context);
		mapScaleBarOverlay.setLineWidth(2);
		mapScaleBarOverlay.setTextSize(20);
		mapView.getOverlays().add(mapScaleBarOverlay);
		
		//Compass Overlay
		//CompassOverlay mapCompassOverlay = new CompassOverlay(context, mapView);
		//mapCompassOverlay.enableCompass();
		//mapView.getOverlays().add(mapCompassOverlay);
		
		//Trackpoints laden
		settings.loadOverlayLists();
		Log.i("MAP", String.valueOf(settings.getTrkpList().getLength()) + "Trackpoints in TrackpointList");
		
		//Trackpoint Overlay
		FhflTrackOverlay fhflTrackOverlay = new FhflTrackOverlay(mapResourceProxy);
		fhflTrackOverlay.setDataList(settings.getTrkpList());
		mapView.getOverlays().add(fhflTrackOverlay);
		
		//Trackpoint Overlay
		FhflTrackOverlay fhflTrackDistOverlay = new FhflTrackOverlay(mapResourceProxy);
		fhflTrackDistOverlay.setDataList(settings.getDistanceTrkpList());
		mapView.getOverlays().add(fhflTrackDistOverlay);
		
		// Vector Overlay for Distance
		FhflVectorOverlay fhflVectorDistOverlay = new FhflVectorOverlay(mapResourceProxy);
		fhflVectorDistOverlay.setDataList(settings.getDistanceDrawList());
		mapView.getOverlays().add(fhflVectorDistOverlay);
		
		// Event Overlay
		MapEventsOverlay mapEventOverlay = new MapEventsOverlay(this.getActivity(), (MapEventsReceiver) this);
		mapView.getOverlays().add(mapEventOverlay);
        
        //einzige brauchbare Lšsung um die Scalebar anzuzeigen. Nochmal nach einer schšneren Lšsung suchen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    	    mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        
        Log.d("OUTPUT", mapView.getOverlays().toString());
        
        MapListener mapListener = new MapListener()
		{
			@Override
			public boolean onScroll(ScrollEvent e) {
				if(!settings.isFollowing())
					settings.setCenter((TrackPoint) mapView.getMapCenter());
				return false;
			}

			@Override
			public boolean onZoom(ZoomEvent arg0) {
				//do something
		    	if(  (byte)mapView.getZoomLevel() == settings.getZoom() )
		    		return true;
		    	
		    	settings.setZoom( (byte)mapView.getZoomLevel() );
		    	Log.d("MAP", "Zoom-Level: " + settings.getZoom());
		        return true;
			}
		 };
		 
		 mapView.setMapListener(new DelayedMapListener(mapListener, 1000 ));
		 
		 mapView.postInvalidate();
	}

	@Override
	public boolean longPressHelper(IGeoPoint point) {
		TrackPoint tmpPoint = new TrackPoint(point.getLatitude(), point.getLongitude());
		if(settings.isMeasure())
		{
			settings.setMeasure(false);
			settings.getDistanceDrawList().addPoint(tmpPoint, Color.RED, "  Punkt 2", Color.BLACK, 24f);
			settings.getDistanceTrkpList().add(tmpPoint);
			int a = settings.getDistancePoint().distanceTo(tmpPoint);
			CharSequence text = "Punkt 1 Koordinaten: " + settings.getDistancePoint().toString() + "\n" + "Punkt 2 Koordinaten: " + tmpPoint.toString() + "\n" + "Distanz: " + String.valueOf(a) + "m";
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
			Log.i("MAP","Distance: " + text.toString());
		}
		else
		{
			settings.getDistanceTrkpList().clear();
			settings.getDistanceDrawList().clear();
			settings.setMeasure(true);
			settings.setDistancePoint(tmpPoint);
		}
		settings.getMapView().postInvalidate();
		return true;
	}

	@Override
	public boolean singleTapUpHelper(IGeoPoint arg0) {
		if(!settings.isMeasure())
		{
			settings.getDistanceTrkpList().clear();
			settings.getDistanceDrawList().clear();
			settings.getMapView().postInvalidate();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() ==  R.id.fMapToggleCenter){
			settings.setFollowing(TB_Center.isChecked());
		}
		
	}
}
