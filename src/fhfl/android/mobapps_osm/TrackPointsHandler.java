package fhfl.android.mobapps_osm;

import java.util.ArrayList;

public class TrackPointsHandler {
	ArrayList<TrackPoint> TPL;
	
	private int length = 0;
	
	 public TrackPointsHandler(String XML){
		 TPL = new ArrayList<TrackPoint>();
		 passXML(XML);
	 }
	 
	 private void passXML(String XML){
		 String s = XML;
		 int start = 0;
		 int stop = 0;

		 DataManager DM = new DataManager();
		 //Trackpoints suchen
		 while(start >= 0 && stop >= 0){
			 //Trackpoint finden
			 start = s.indexOf("<trkpt");
			 stop = s.indexOf("</trkpt>");
			 
			 if(start >= 0 && stop >= 0){
				 // Punkt in Liste einf�gen
				 TPL.add(new TrackPoint(s.substring(start, stop+8)));
				 
				 // Punkt aus text l�schen
				 s = s.substring(stop+8);
			 }
		 }
	 }

	 public ArrayList<TrackPoint> getPointsList(){
		 return TPL;
	 }
	 
	public int getLength() 
	{
		return length;
	}
}
