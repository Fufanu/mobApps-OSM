package fhfl.android.mobapps_osm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;
import ecl.Datacontainer.DrawObjectList;
import ecl.Datacontainer.DrawableList;
import ecl.Datacontainer.TrackPointList;

public class SettingsContainer extends Observable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MapView mapView;
	private static String[] test = {"http://toolserver.org/tiles/hikebike/"};
	private static final ITileSource hikeBike = new XYTileSource("OpenStreetMap Hikebikemap.de", null, 9, 15, 256, ".png", test);
	private ITileSource tileSource = hikeBike;
	private GeoPoint center = new GeoPoint(54.775139, 9.452691); //vorläufig für Standort bis GPS läuft
	
	private static final DataManager DM = new DataManager();
	private TrackPointsHandler TPH;
	private ArrayList<TrackPoint> TPL;
	private TrackPointList mapTrkpList = new TrackPointList();
	private DrawObjectList mapTrackDrawList = new DrawObjectList();
	private DrawObjectList mapDistanceDrawList = new DrawObjectList();
	
	private int zoom = 13;
	
	private GeoPoint mapDistancePoint;
	private Toast distanceToast;
	private int toastDurationt = Toast.LENGTH_LONG;
	
	private boolean internetConnection = false;
	private boolean mapFollowing = false;
	private boolean gpsTrack = false;
	private boolean measure = false;
	
	public SettingsContainer()
	{
		reloadTrackPointHandler();
		loadOverlayLists();
	}
	
	
	/**
	 * @return the mapView
	 */
	public MapView getMapView() {
		return mapView;
	}
	/**
	 * @param mapView the mapView to set
	 */
	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}
	/**
	 * @return the internetConnection
	 */
	public boolean isInternetConnection() {
		return internetConnection;
	}
	/**
	 * @param internetConnection the internetConnection to set
	 */
	public void setInternetConnection(boolean internetConnection) {
		this.internetConnection = internetConnection;
		mapView.setUseDataConnection(internetConnection);
	}
	/**
	 * @return the mapFollowing
	 */
	public boolean isFollowing() {
		return mapFollowing;
	}
	/**
	 * @param following the mapFollowing to set
	 */
	public void setFollowing(boolean following) {
		this.mapFollowing = following;
	}
	/**
	 * @return the gpsTrack
	 */
	public boolean isGpsTrack() {
		return gpsTrack;
	}
	/**
	 * @param gpsTrack the gpsTrack to set
	 */
	public void setGpsTrack(boolean gpsTrack) {
		this.gpsTrack = gpsTrack;
	}
	/**
	 * @return the zoom
	 */
	public int getZoom() {
		return zoom;
	}
	/**
	 * @param zoom the zoom to set
	 */
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	/**
	 * @return the center
	 */
	public GeoPoint getCenter() {
		return center;
	}
	/**
	 * @param center the center to set
	 */
	public void setCenter(GeoPoint center) {
		this.center = center;
	}
	/**
	 * @return the tileSource
	 */
	public ITileSource getTileSource() {
		return tileSource;
	}
	/**
	 * @return the tPL
	 */
	public ArrayList<TrackPoint> getTPList() {
		return TPL;
	}
	/**
	 * @return the mapIconList
	 */
	public DrawObjectList getMapDistanceDrawList() {
		return mapDistanceDrawList;
	}
	/**
	 * @return the mapTrkpIconList
	 */
	public TrackPointList getMapTrkpList() {
		return mapTrkpList;
	}
	
	public void reloadTrackPointHandler()
	{
		TPH = new TrackPointsHandler(DM.readGPSLogFile(DM.readSettingsFile())); 
		TPL = TPH.getPointsList();
	}
	
	public void loadOverlayLists()
	{
		mapTrkpList.clear();
		mapTrackDrawList.clear();
		for(TrackPoint tp : TPL)
		{
			mapTrkpList.add(new GeoPoint(tp.getLat(), tp.getLon()));
			Log.d("GEO", String.valueOf(tp.getLat()) + " , " + String.valueOf(tp.getLon()));
		}
		mapTrackDrawList.addPoint(mapTrkpList.getGeoPoint(0), Color.GREEN, "Start", Color.BLACK, 24f);
		mapTrackDrawList.addPoint(mapTrkpList.getGeoPoint(mapTrkpList.getLength()-1), Color.RED, "Stop", Color.BLACK, 24f);
		Log.i("MAP","Start: " + mapTrkpList.getGeoPoint(0) + ", Stop: " + mapTrkpList.getGeoPoint(mapTrkpList.getLength()-1));
	}
	/**
	 * @return the measure
	 */
	public boolean isMeasure() {
		return measure;
	}
	/**
	 * @param measure the measure to set
	 */
	public void setMeasure(boolean measure) {
		this.measure = measure;
	}
	/**
	 * @return the mapDistancePoint
	 */
	public GeoPoint getMapDistancePoint() {
		return mapDistancePoint;
	}
	/**
	 * @param mapDistancePoint the mapDistancePoint to set
	 */
	public void setMapDistancePoint(GeoPoint mapDistancePoint) {
		this.mapDistancePoint = mapDistancePoint;
	}
	/**
	 * @return the mapTrackDrawList
	 */
	public DrawObjectList getMapTrackDrawList() {
		return mapTrackDrawList;
	}


	/**
	 * @return the distanceToast
	 */
	public Toast getDistanceToast() {
		return distanceToast;
	}


	/**
	 * @param distanceToast the distanceToast to set
	 */
	public void setDistanceToast(Toast distanceToast) {
		this.distanceToast = distanceToast;
	}


	/**
	 * @return the toastDurationt
	 */
	public int getToastDurationt() {
		return toastDurationt;
	}


	/**
	 * @param toastDurationt the toastDurationt to set
	 */
	public void setToastDurationt(int toastDurationt) {
		this.toastDurationt = toastDurationt;
	}
}
