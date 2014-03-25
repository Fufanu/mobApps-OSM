package fhfl.android.mobapps_osm;

public class TrackPoint{
	private boolean valid = false;
	private double Lat = 0.0;
	private double Lon = 0.0;
	private double Ele = 0.0;
	private String Date = "empty";
	private String Time = "empty";
	private String TimeFormat = "yyyy-MM-dd hh:mm:ss.SSS";
	
	public TrackPoint(String XML){
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
	
	public double getLat(){
		return Lat;
	}
	
	public double getLon(){
		return Lon;
	}
	
	public double getEle(){
		return Ele;
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


