package fhfl.android.mobapps_osm;

import org.osmdroid.util.GeoPoint;

public class TrackPoint{
	private boolean valid = false;
	private GeoPoint point = null;
	private String Date = "empty";
	private String Time = "empty";
	private String TimeFormat = "yyyy-MM-dd hh:mm:ss.SSS";
	
	public TrackPoint(String XML){
		passXML(XML);
	}
	
	private void passXML(String XML){
		
		double Lat = 0.0;
		double Lon = 0.0;
		double Ele = 0.0;
		
		if(XML.contains("lat=") && XML.contains("lon=")){
			String s = XML;
			int start = 0;
			int stop = 0;
			
			// Lat extrahieren
			start = s.indexOf("lat")+5;
			stop = s.indexOf("lon")-2;
			Lat = Double.parseDouble(s.substring(start, stop));
			
			// Lon extrahieren
			start = s.indexOf("lon")+5;
			stop = s.indexOf(">")-1;
			Lon = Double.parseDouble(s.substring(start, stop));
			
			// Ele extrahieren <--- pro trkpt
			if(s.contains("<ele>")){
				start = s.indexOf("<ele>")+5;
				stop = s.indexOf("</ele>");
				Ele = Double.parseDouble(s.substring(start, stop));
			}
			
			point = new GeoPoint(Lat, Lon, Ele);
			
			// Date extrahieren
			start = s.indexOf("<time>")+6;
			stop = s.indexOf("<time>")+16;
			Date = s.substring(start, stop);
			
			// Time extrahieren
			start = s.indexOf("<time>")+17;
			stop = s.indexOf("</time>")-1;
			Time = s.substring(start, stop);
			
			if(Time.length() == 8)
				TimeFormat = "yyyy-MM-dd hh:mm:ss";
			
			//Set valid
			valid = true;
						
		}
	}
	
	public GeoPoint getPoint(){
		return point;
	}
	
	public double getLat(){
		return point.getLatitudeE6();
	}
	
	public double getLon(){
		return point.getLongitudeE6();
	}
	
	public double getEle(){
		return point.getAltitude();
	}
	
	public String getDate(){
		return Date;
	}
	
	public String getTime(){
		return Time;
	}
	
	public boolean getValid(){
		return valid;
	}
	
	public String getTimeFormat(){
		return TimeFormat;
	}
}


