package ecl.Datacontainer;

import java.io.IOException;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;

import android.graphics.Color;
import android.util.Log;

// TrackpointList Klasse
// selbsterklärend
// ToDo:
public class TrackPointList implements Serializable
{
	private static final long serialVersionUID = 1938523954627884989L;
	
	private int length = 0;
	private ArrayList<TrackPointList.Trackpoint> trkList;
	private int defaultColor = Color.BLUE;
	
	// Hilfsklasse
	public class Trackpoint
	{
		GeoPoint gpTrackPoint = null;
		int color = Color.BLUE;
		int textColor = Color.BLACK;
		float textSize;
		String text = null;
	}
	
	// Konstruktor
	public TrackPointList() 
	{
		trkList = new ArrayList<TrackPointList.Trackpoint>();
		defaultColor = Color.BLUE;
	}
	
	public int getLength() 
	{
		return length;
	}
	
	public int getColor(int index) 
	{
		if(index < length)
			return trkList.get(index).color;
		else
			return -1;
	}

	public int getTextColor(int index) 
	{
		if(index < length)
			return trkList.get(index).textColor;
		else
			return -1;
	}

	public GeoPoint getGeoPoint(int index) 
	{
		if(index < length)
			return trkList.get(index).gpTrackPoint;
		else
			return null;
	}
	
	public String getText(int index) 
	{
		if(index < length)
			return trkList.get(index).text;
		else
			return null;
	}
	
	public float getTextSize(int index)
	{
		if(index < length && index >= 0)
		{
			return trkList.get(index).textSize;
		}
		else
			return 0f;
	}
	
	
	public int add(GeoPoint gpTrackPoint)
	{
		return this.addExt(gpTrackPoint, this.defaultColor, null, Color.BLACK, 0);
	}
	
	public int addExt(GeoPoint gpTrackPoint, int color, String text, int textColor, float textSize) 
	{
		Trackpoint temp = new Trackpoint();
		temp.color = color;
		temp.textColor = textColor;
		temp.gpTrackPoint = gpTrackPoint;
		temp.text = text;
		temp.textSize = textSize;
		trkList.add(temp);
		length++;
		return length -1;
	}
	
	public void remove(int index)
	{
		if(index < length && index >= 0)
		{
			trkList.remove(index);
			length--;
		}
	}
	
	public void clear() {
		trkList.clear();
		length = 0;
	}
	
	public void setDefaultColor(int color)
	{
		this.defaultColor = color;
	}
	
	// Serialisierung
	private void writeObject(java.io.ObjectOutputStream out)
	{
		try {
			out.writeObject(trkList);
			out.writeInt(length);
			out.writeInt(defaultColor);
		} catch (IOException e) {
			Log.d(null, "TrackPointList: Aerger beim serialisieren!");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in)
	{
		try {
			this.trkList = (ArrayList<TrackPointList.Trackpoint>)in.readObject();
			this.length = in.readInt();
			this.defaultColor = in.readInt();
		} catch (OptionalDataException e) {
			Log.d(null, "TrackPointList: Aerger beim deserialisieren! -1-");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.d(null, "TrackPointList: Aerger beim deserialisieren! -2-");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(null, "TrackPointList: Aerger beim deserialisieren! -3-");
			e.printStackTrace();
		}
	}

}