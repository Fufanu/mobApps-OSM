package fhfl.android.mobapps_osm;


import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import android.content.Context;
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
import ecl.Overlays.FhflTrackOverlay;

public class fragment_map extends Fragment implements MapEventsReceiver, OnClickListener {
	
	private SettingsContainer settings; // Zentraler DatenContainer
	private Context context;
	private ResourceProxy mapResourceProxy;
	private ToggleButton TB_Center;		// Button f�r Zentrierung
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//Initialisierung des DatenContainers und des Buttons
		context = inflater.getContext();
		MainActivity activity = (MainActivity) getActivity();
		settings = activity.settings;
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		TB_Center = (ToggleButton)view.findViewById(R.id.fMapToggleCenter);
		TB_Center.setOnClickListener(this);
		TB_Center.setChecked(settings.isFollowing());
		
		//Initialisierung der Karte
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
		CompassOverlay mapCompassOverlay = new CompassOverlay(context, mapView);
		mapCompassOverlay.enableCompass();
		mapView.getOverlays().add(mapCompassOverlay);
		
		//Trackpoints laden
		settings.loadOverlayLists();
		Log.i("MAP", String.valueOf(settings.getTrkpList().getLength()) + "Trackpoints in TrackpointList");
		
		//Trackpoint Overlay for Tracking
		FhflTrackOverlay fhflTrackOverlay = new FhflTrackOverlay(mapResourceProxy);
		fhflTrackOverlay.setDataList(settings.getTrkpList());
		mapView.getOverlays().add(fhflTrackOverlay);
		
		//Trackpoint Overlay for Distance
		FhflTrackOverlay fhflTrackDistOverlay = new FhflTrackOverlay(mapResourceProxy);
		fhflTrackDistOverlay.setDataList(settings.getDistanceTrkpList());
		mapView.getOverlays().add(fhflTrackDistOverlay);
		
		//Trackpoint Overlay for Location
		FhflTrackOverlay fhflTrackLocationOverlay = new FhflTrackOverlay(mapResourceProxy);
		fhflTrackLocationOverlay.setDataList(settings.getMapLocationTrkpList());
		mapView.getOverlays().add(fhflTrackLocationOverlay);
		
		// Event Overlay
		MapEventsOverlay mapEventOverlay = new MapEventsOverlay(this.getActivity(), (MapEventsReceiver) this);
		mapView.getOverlays().add(mapEventOverlay);
        
        //einzige brauchbare L�sung um die Scalebar anzuzeigen. Nochmal nach einer sch�neren L�sung suchen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    	    mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        
        Log.d("OUTPUT", mapView.getOverlays().toString());
        
        
        //Listener um die TouchEvents zu handeln
        @SuppressWarnings("unused")
		MapListener mapListener = new MapListener()
		{
			@Override
			public boolean onScroll(ScrollEvent e) {
				if(!settings.isFollowing())										//Setzt den boolean f�r die Zentrierung
					settings.setCenter((TrackPoint) mapView.getMapCenter());	//Speichert den aktuellen Mittelpunkt der Karte zwischen
				return false;
			}

			@Override
			public boolean onZoom(ZoomEvent arg0) {
		    	if(  (byte)mapView.getZoomLevel() == settings.getZoom() )		
		    		return true;
		    	
		    	settings.setZoom( (byte)mapView.getZoomLevel() );				//Speichert den aktuellen Zoomlevel der Karte zwischen
		    	Log.i("MAP", "Zoom-Level: " + settings.getZoom());
		        return true;
			}
		 };
		 
		 
		 mapView.postInvalidate();
	}

	
	//Mit dem longPress werden die Punkte f�r die Distanzrechnung gesetzt
	@Override
	public boolean longPressHelper(IGeoPoint point) {
		try{
			TrackPoint tmpPoint = new TrackPoint(point.getLatitudeE6(), point.getLongitudeE6());
			if(settings.isMeasure())
			{
				//2.Punkt setzen und Distanz ausrechnen
				settings.setMeasure(false);
				settings.getDistanceTrkpList().add(tmpPoint);
				double a = settings.getDistancePoint().distanceTo(tmpPoint);
				String distanz;
				if(a > 1000)
				{
					distanz = String.valueOf(a/1000) + "km";
				}
				else
				{
					distanz = String.valueOf(a) + "m";
				}
				CharSequence text = "Punkt 1 Koordinaten: " + settings.getDistancePoint().toString() + "\n" + "Punkt 2 Koordinaten: " + tmpPoint.toString() + "\n" + "Distanz: " + distanz;
				Toast.makeText(context, text, Toast.LENGTH_LONG).show();
				Log.i("MAP","Distance: " + text.toString());
			}
			else
			{
				//1.Punkt setzen
				settings.getDistanceTrkpList().clear();
				settings.setMeasure(true);
				settings.setDistancePoint(tmpPoint);
			}
			settings.getMapView().postInvalidate();
			return true;
		}
		catch(Exception e)
		{
			Toast.makeText(getActivity().getApplicationContext(), "cant calculate distance", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	//L�schen der Distanzmessung auf der Karte
	@Override
	public boolean singleTapUpHelper(IGeoPoint arg0) {
		if(!settings.isMeasure())
		{
			settings.getDistanceTrkpList().clear();
			settings.getMapView().postInvalidate();
		}
		return true;
	}

	//Toggeln der Zentrierung
	@Override
	public void onClick(View v) {
		if(v.getId() ==  R.id.fMapToggleCenter){
			settings.setFollowing(TB_Center.isChecked());
			settings.getMapView().postInvalidate();
		}
	}
}
