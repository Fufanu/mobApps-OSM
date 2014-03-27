package fhfl.android.mobapps_osm;

import org.osmdroid.util.GeoPoint;

public class TrackPoint extends GeoPoint{
	private boolean valid = false;
	private String Date = "empty";
	private String Time = "empty";
	private String TimeFormat = "yyyy-MM-dd hh:mm:ss.SSS";
	
	public TrackPoint(String XML){
		
		super(0,0);	
		passXML(XML);
		
	}
	
	private void passXML(String XML){
		
		
		
		if(XML.contains("lat=") && XML.contains("lon=")){
			String s = XML;
			int start = 0;
			int stop = 0;
			
			// Lat extrahieren
			start = s.indexOf("lat")+5;
			stop = s.indexOf("lon")-2;
			this.setLatitudeE6((int)(Double.parseDouble(s.substring(start, stop)) * 1E6));
			
			// Lon extrahieren
			start = s.indexOf("lon")+5;
			stop = s.indexOf(">")-1;
			this.setLongitudeE6((int)(Double.parseDouble(s.substring(start, stop)) * 1E6));
			
			// Ele extrahieren <--- pro trkpt
			if(s.contains("<ele>")){
				start = s.indexOf("<ele>")+5;
				stop = s.indexOf("</ele>");
				this.setAltitude((int)(Double.parseDouble(s.substring(start, stop))));
			}
			
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


