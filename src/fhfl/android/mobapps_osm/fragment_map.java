package fhfl.android.mobapps_osm;

import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import ecl.Overlays.FhflTrackOverlay;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragment_map extends Fragment {
	
	private DataManager dm;
	private DataContainer dc;
	private Context context;
	private ResourceProxy mapResourceProxy;
	private ScaleBarOverlay mapScaleBarOverlay;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Initialisierung
		context = inflater.getContext();
		dm = new DataManager();
		dc = new DataContainer();
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		
		mapViewInit(view);
		
		return view;
	}
	
	private void mapViewInit(View view)
	{
		// OSM Map anlegen
        final MapView mapView = (MapView) view.findViewById(R.id.mapView);
        dc.setMapView(mapView);
        mapResourceProxy = new ResourceProxyImpl(context.getApplicationContext());
		
		//Settings
        mapView.setUseDataConnection(dc.isInternetConnection());
        mapView.setTileSource(dc.getTileSource());
		mapView.getController().setZoom(dc.getZoom());
		mapView.getController().setCenter(dc.getCenter()); //Koordinaten der Mensa     
		mapView.setUseSafeCanvas(true);
		mapView.setClickable(false);
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);
		
		// ScaleBar Overlay
		mapScaleBarOverlay = new ScaleBarOverlay(context);
		mapScaleBarOverlay.setLineWidth(2);
		mapScaleBarOverlay.setTextSize(20);
		
		//Trackpoint Overlay
		FhflTrackOverlay fhflTrackOverlay = new FhflTrackOverlay(mapResourceProxy);
		fhflTrackOverlay.setDataList(dc.getTrkList());

        //Overlays zur Karte hinzufŸgen
        mapView.getOverlays().add(mapScaleBarOverlay);
		mapView.getOverlays().add(fhflTrackOverlay);
        
        //einzige brauchbare Lšsung um die Scalebar anzuzeigen. Nochmal nach einer schšneren Lšsung suchen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    	    mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        
        Log.d("OUTPUT", mapView.getOverlays().toString());
	}
}
