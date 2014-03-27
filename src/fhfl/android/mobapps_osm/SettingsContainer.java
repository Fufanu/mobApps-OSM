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
import ecl.Datacontainer.DrawObjectList;
import ecl.Datacontainer.TrackPointList;

public class SettingsContainer extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private MapView mapView = null;
	private static String[] test = {"http://toolserver.org/tiles/hikebike/"};
	private static final ITileSource hikeBike = new XYTileSource("OpenStreetMap Hikebikemap.de", null, 9, 15, 256, ".png", test);
	private ITileSource tileSource = hikeBike;
	private GeoPoint center = new GeoPoint(54.775139, 9.452691); //vorläufig für Standort bis GPS läuft
	
	private static final DataManager DM = new DataManager();
	private TrackPointsHandler TPH;
	private ArrayList<TrackPoint> TPL;
	private TrackPointList mapTrkpList = null;
	private TrackPointList mapDistanceTrkpList = null;
	private DrawObjectList mapTrackDrawList = null;
	private DrawObjectList mapDistanceDrawList = null;
	private Gpsm gpsm;
	private int zoom = 13;
	
	private GeoPoint mapDistancePoint;
	
	private boolean internetConnection = true;
	private boolean mapFollowing = false;
	private boolean gpsTrack = false;
	private boolean measure = false;
	private boolean gpsOnControl = true;
	private String currentLogFile ="";  // <---- Hier die neuen Punkte rein (Hannes)
	
	public SettingsContainer()
	{
		
		mapTrkpList = new TrackPointList();
		mapDistanceTrkpList = new TrackPointList();
		mapTrackDrawList = new DrawObjectList();
		mapDistanceDrawList = new DrawObjectList();
		
		loadSettingsXML();
		reloadTrackPointHandler();
		loadOverlayLists();
		gpsm= new Gpsm();
		
	}
	
	private void loadSettingsXML(){
		String XML = DM.readSettingsFile();
		String CurrentLogFile = "";
		Boolean GpsOnControl = true;
		Boolean InternetConnection = true;
		int Start;
		int Stop;
		boolean CurrentLogFileValid = false;
		boolean GpsOnControlValid = false;
		boolean InternetConnectionValid = false;
		
		if(!XML.contains("ERROR_readStettingsFile")){
			
			//Parse CurrentLogFile
			Start = XML.indexOf("<CurrentLogFile>");
			Stop = XML.indexOf("</CurrentLogFile>");
			if(Start != -1 && Stop != -1){
				CurrentLogFile = XML.substring(Start+16, Stop).trim();
				if(DM.fileExistd(CurrentLogFile)){
					CurrentLogFileValid = true;
				}
			}
			
			//Parse GpsOnControl
			Start = XML.indexOf("<GpsOnControl>");
			Stop = XML.indexOf("</GpsOnControl>");
			if(Start != -1 && Stop != -1){
				try{
					GpsOnControl = Boolean.valueOf(XML.substring(Start+14, Stop));
					GpsOnControlValid = true;
				}
				catch(Exception e)
				{
					
				}
			}

			//Parse InternetConnection
			Start = XML.indexOf("<InternetConnection>");
			Stop = XML.indexOf("</InternetConnection>");
			if(Start != -1 && Stop != -1){
				try{
					InternetConnection = Boolean.valueOf(XML.substring(Start+20, Stop));
					InternetConnectionValid = true;
				}
				catch(Exception e)
				{
					
				}
			}
		}
		
		//Speichern
		if(InternetConnectionValid && CurrentLogFileValid && GpsOnControlValid){
			this.setCurrentLogFile(CurrentLogFile);
			this.setGpsOnControl(GpsOnControl);
			Log.d("fuck", String.valueOf(InternetConnection));
			this.setInternetConnection(InternetConnection);
			Log.d("Settings", "XML korrekt eingelesen");
			
		}
		else{
			Log.d("Settings GPS", String.valueOf(GpsOnControl) + " " + String.valueOf(GpsOnControlValid));
			Log.d("Settings INet", String.valueOf(InternetConnection) + " " + String.valueOf(InternetConnectionValid));
			Log.d("Settings File", CurrentLogFile + " " + String.valueOf(CurrentLogFileValid));
		}
	}
	
	public MapView getMapView() {
		return mapView;
	}
	
	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}
	
	public boolean isInternetConnection() {
		return internetConnection;
	}
	
	public void setInternetConnection(boolean internetConnection) {
		this.internetConnection = internetConnection;
		if(mapView != null)
			mapView.setUseDataConnection(internetConnection);
	}
	
	public boolean isFollowing() {
		return mapFollowing;
	}
	
	public void setFollowing(boolean following) {
		this.mapFollowing = following;
	}
	
	public boolean isGpsTrack() {
		return gpsTrack;
	}
	
	public void setGpsTrack(boolean gpsTrack) {
		this.gpsTrack = gpsTrack;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
	public GeoPoint getCenter() {
		return center;
	}
	
	public void setCenter(GeoPoint center) {
		this.center = center;
	}
	
	public ITileSource getTileSource() {
		return tileSource;
	}
	
	public ArrayList<TrackPoint> getTPList() {
		return TPL;
	}
	
	public DrawObjectList getDistanceDrawList() {
		return mapDistanceDrawList;
	}
	
	public TrackPointList getTrkpList() {
		return mapTrkpList;
	}
	
	public TrackPointList getDistanceTrkpList() {
		return mapDistanceTrkpList;
	}
	
	public GeoPoint getDistancePoint() {
		return mapDistancePoint;
	}
	
	public void setDistancePoint(GeoPoint mapDistancePoint) {
		this.mapDistancePoint = mapDistancePoint;
		mapDistanceTrkpList.add(mapDistancePoint);
		mapDistanceDrawList.addPoint((GeoPoint) mapDistancePoint, Color.GREEN, "Punkt 1", Color.BLACK, 24f);
	}
	
	public DrawObjectList getTrackDrawList() {
		return mapTrackDrawList;
	}
	
	public void reloadTrackPointHandler()
	{
		TPH = new TrackPointsHandler(DM.readGPSLogFile(this.getCurrentLogFile())); 
		TPL = TPH.getPointsList();
	}
	
	public void loadOverlayLists()
	{
		mapTrkpList.clear();
		mapTrackDrawList.clear();
		if(TPL.size() > 0){
			
			for(TrackPoint tp : TPL)
			{
				mapTrkpList.add(tp);
				Log.d("GEO", String.valueOf(tp));
			}
			mapTrackDrawList.addPoint(mapTrkpList.getGeoPoint(0), Color.GREEN, "Start", Color.BLACK, 24f);
			mapTrackDrawList.addPoint(mapTrkpList.getGeoPoint(mapTrkpList.getLength()-1), Color.RED, "Stop", Color.BLACK, 24f);
			Log.i("MAP","Start: " + mapTrkpList.getGeoPoint(0) + ", Stop: " + mapTrkpList.getGeoPoint(mapTrkpList.getLength()-1));
		}
	}
	
	public boolean isMeasure() {
		return measure;
	}
	
	public void setMeasure(boolean measure) {
		this.measure = measure;
	}

	public String getCurrentLogFile() {
		return currentLogFile;
	}

	public void setCurrentLogFile(String currentLogFile) {
		this.currentLogFile = currentLogFile;
	}

	public boolean isGpsOnControl() {
		return gpsOnControl;
	}

	public void setGpsOnControl(boolean gpsOnControl) {
		this.gpsOnControl = gpsOnControl;
	}
	public void addtoTPL(TrackPoint p)
	{
		
		TPL.add(p);
	}
	
}
