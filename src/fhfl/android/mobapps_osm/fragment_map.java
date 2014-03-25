package fhfl.android.mobapps_osm;

import java.util.ArrayList;

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
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class fragment_map extends Fragment implements MapEventsReceiver {
	
	private SettingsContainer settings;
	private Context context;
	private ResourceProxy mapResourceProxy;
	private ScaleBarOverlay mapScaleBarOverlay;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//Initialisierung
		context = inflater.getContext();
		MainActivity activity = (MainActivity) getActivity();
		settings = activity.settings;
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		
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
		mapScaleBarOverlay = new ScaleBarOverlay(context);
		mapScaleBarOverlay.setLineWidth(2);
		mapScaleBarOverlay.setTextSize(20);
		mapView.getOverlays().add(mapScaleBarOverlay);
		
		//Compass Overlay
		//CompassOverlay mapCompassOverlay = new CompassOverlay(context, mapView);
		//mapCompassOverlay.enableCompass();
		//mapView.getOverlays().add(mapCompassOverlay);
		
		//Trackpoints laden
		settings.loadOverlayLists();
		Log.i("MAP", String.valueOf(settings.getMapTrkpList().getLength()) + "Trackpoints in TrackpointList");
		
		//Trackpoint Overlay
		FhflTrackOverlay fhflTrackOverlay = new FhflTrackOverlay(mapResourceProxy);
		fhflTrackOverlay.setDataList(settings.getMapTrkpList());
		mapView.getOverlays().add(fhflTrackOverlay);
		
		// Vector Overlay for Distance
		FhflVectorOverlay fhflVectorTrackOverlay = new FhflVectorOverlay(mapResourceProxy);
		fhflVectorTrackOverlay.setDataList(settings.getMapTrackDrawList());
		mapView.getOverlays().add(fhflVectorTrackOverlay);
		
		// Vector Overlay for Distance
		FhflVectorOverlay fhflVectorDistOverlay = new FhflVectorOverlay(mapResourceProxy);
		fhflVectorDistOverlay.setDataList(settings.getMapDistanceDrawList());
		mapView.getOverlays().add(fhflVectorDistOverlay);
		
		// Event Overlay
		MapEventsOverlay mapEventOverlay = new MapEventsOverlay(this.getActivity(), (MapEventsReceiver) this);
		mapView.getOverlays().add(mapEventOverlay);
        
        //einzige brauchbare Lösung um die Scalebar anzuzeigen. Nochmal nach einer schöneren Lösung suchen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    	    mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        
        Log.d("OUTPUT", mapView.getOverlays().toString());
        
        MapListener mapListener = new MapListener()
		{
			@Override
			public boolean onScroll(ScrollEvent e) {
				// TODO Auto-generated method stub
		        //Log.i("zoom", e.toString());
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
		if(settings.isMeasure())
		{
			settings.setMeasure(false);
			settings.getMapDistanceDrawList().addPoint((GeoPoint) point, Color.RED, "Punkt 2", Color.BLACK, 24f);
			Log.i("MAP", "Punkt1: " + settings.getMapDistancePoint().toString() + ", Punkt2: " + point.toString());
			// TODO Rechnung und vielleicht Verbindungslinie
			int a = settings.getMapDistancePoint().distanceTo(point);
			CharSequence text = "Punkt 1 Koordinaten: " + settings.getMapDistancePoint().toString() + "\n" + "Punkt 2 Koordinaten: " + point.toString() + "\n" + "Distanz: " + String.valueOf(a) + "m";
			settings.setDistanceToast(Toast.makeText(context, text, settings.getToastDurationt()));
			settings.getDistanceToast().show();
			Log.d("DISTANCE",text.toString());
		}
		else
		{
			settings.getMapDistanceDrawList().clear();
			settings.setMeasure(true);
			settings.getMapDistanceDrawList().addPoint((GeoPoint) point, Color.GREEN, "Punkt 1", Color.BLACK, 24f);
			settings.setMapDistancePoint((GeoPoint) point);
		}
		settings.getMapView().postInvalidate();
		return true;
	}

	@Override
	public boolean singleTapUpHelper(IGeoPoint arg0) {
		Log.d("DEBUG","Kurz gedrückt");
		if(!settings.isMeasure())
		{
			settings.getMapDistanceDrawList().clear();
			settings.getMapView().postInvalidate();
		}
		return true;
	}
}
