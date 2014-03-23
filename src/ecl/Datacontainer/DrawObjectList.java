package ecl.Datacontainer;

//DrawObjectList Klasse
//selbsterklärend
//ToDo: 
import java.io.IOException;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;

import android.graphics.Color;
import android.util.Log;

/**
 * Typen:
 * 1: Punkt
 * 2: Linie
 * 3: Pfeil
 * 4: Pfeil mit Kreis im Ursprung
 * 5: nur Text
 * 
 * Hinweis: DrawObject.color = Color.TRANSPARENT nutzen, um nur den Text darzustellen
 * @author ecl
 */
public class DrawObjectList implements Serializable
{
	private static final long serialVersionUID = -6930533749781780308L;
	private int length = 0;
	private ArrayList<DrawObjectList.DrawObject> list;
	
	public class DrawObject
	{
		int type;
		int color;
		int textColor;
		float textSize;
		GeoPoint gpStart;
		GeoPoint gpEnd;
		String text;
	}
	
	// Konstruktor
		public DrawObjectList () 
		{
			list = new ArrayList<DrawObjectList.DrawObject>();
		}
		
		public int getLength() 
		{
			return length;
		}
		
		public int getType(int index) 
		{
			if(index < length)
				return list.get(index).type;
			else
				return -1;
		}
		
		public int getColor(int index) 
		{
			if(index < length)
				return list.get(index).color;
			else
				return -1;
		}

		public int getTextColor(int index) 
		{
			if(index < length)
				return list.get(index).textColor;
			else
				return -1;
		}
		
		public GeoPoint getGeoPointStart(int index) 
		{
			if(index < length)
				return list.get(index).gpStart;
			else
				return null;
		}
		
		public GeoPoint getGeoPointEnd(int index) 
		{
			if(index < length)
				return list.get(index).gpEnd;
			else
				return null;
		}
		
		public String getText(int index) 
		{
			if(index < length)
				return list.get(index).text;
			else
				return null;
		}
		
		public float getTextSize(int index)
		{
			if(index < length && index >= 0)
			{
				return list.get(index).textSize;
			}
			else
				return 0f;
		}

		public void set(int index, int type, GeoPoint gpStart, GeoPoint gpEnd, int color, String text, int textColor, float textSize)
		{
			if(index < length && index >= 0)
			{
				list.get(index).type = type;
				list.get(index).gpStart = gpStart;
				list.get(index).gpEnd = gpEnd;
				list.get(index).color = color;
				list.get(index).text = text;
				list.get(index).textColor = textColor;
				list.get(index).textSize = textSize;
			}
		}
		
		public void move(int index, GeoPoint gpStart, GeoPoint gpEnd)
		{
			if(index < length && index >= 0)
			{
				list.get(index).gpStart = gpStart;
				list.get(index).gpEnd = gpEnd;
			}
		}
		
		public void remove(int index)
		{
			if(index < length && index >= 0)
			{
				list.remove(index);
				length--;
			}
		}
		
		public void clear() 
		{
			list.clear();
			length = 0;
		}
	
		
		
		/** Add Methoden */
		
		/** 
		 * @param type : 1 - Point, 2 - Line, 3 - Arrow, 4 - Arrow2, 5 - just Text
		 * @return index of Element in List
		 * */
		public int add(int type, GeoPoint gpStart, GeoPoint gpEnd, int color, String text, int textColor, float textSize) 
		{
			DrawObject temp = new DrawObject();
			temp.type = type;
			temp.color = color;
			temp.textColor = textColor;
			temp.gpStart = gpStart;
			temp.gpEnd = gpEnd;
			temp.text = text;
			temp.textSize = textSize;
			list.add(temp);
			length++;
			return length -1;
		}

		public int addPoint(GeoPoint gpPoint, int color, String text, int textColor, float textSize)
		{
			return this.add(1, gpPoint, gpPoint, color, text, textColor, textSize);
		}
		
		public int addLine(GeoPoint gpStart, GeoPoint gpEnd, int color, String text, int textColor, float textSize)
		{
			return this.add(2, gpStart, gpEnd, color, text, textColor, textSize);
		}
		
		public int addArrow(GeoPoint gpStart, GeoPoint gpEnd, int color, String text, int textColor, float textSize)
		{
			return this.add(3, gpStart, gpEnd, color, text, textColor, textSize);
		}
		
		public int addArrow2(GeoPoint gpStart, GeoPoint gpEnd, int color, String text, int textColor, float textSize)
		{
			return this.add(4, gpStart, gpEnd, color, text, textColor, textSize);
		}
		
		public int addText(GeoPoint gpPoint, String text, int textColor, float textSize)
		{
			return this.add(1, gpPoint, gpPoint, Color.TRANSPARENT, text, textColor, textSize);
		}
		
		
		// Serialisierung
		private void writeObject(java.io.ObjectOutputStream out)
		{
			try {
				out.writeObject(list);
				out.writeInt(length);
			} catch (IOException e) {
				Log.d(null, "TrackPointList: Aerger beim serialisieren!");
				e.printStackTrace();
			}
		}
		
		private void readObject(java.io.ObjectInputStream in)
		{
			try {
				this.list = (ArrayList<DrawObjectList.DrawObject>)in.readObject();
				this.length = in.readInt();
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
