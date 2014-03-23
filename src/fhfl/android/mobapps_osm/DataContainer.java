package fhfl.android.mobapps_osm;

import java.io.Serializable;
import java.util.Observable;

import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.location.LocationManager;
import ecl.Datacontainer.TrackPointList;

public class DataContainer extends Observable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MapView mapView;
	private TrackPointList trkList;
	private LocationManager mapLocationManager;
	private static String[] test = {"http://toolserver.org/tiles/hikebike/"};
	private static final ITileSource hikeBike = new XYTileSource("OpenStreetMap Hikebikemap.de", null, 9, 15, 256, ".png", test);
	private ITileSource tileSource = hikeBike;
	private GeoPoint center = new GeoPoint(54.775139, 9.452691); //vorläufig für Standort bis GPS läuft
	
	private int zoom = 12;
	
	private boolean internetConnection = false;
	private boolean mapFollowing = false;
	private boolean gpsTrack = false;
	
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
	 * @return the trkList
	 */
	public TrackPointList getTrkList() {
		return trkList;
	}
	/**
	 * @param trkList the trkList to set
	 */
	public void setTrkList(TrackPointList trkList) {
		this.trkList = trkList;
	}
	/**
	 * @return the tileSource
	 */
	public ITileSource getTileSource() {
		return tileSource;
	}
	

}
