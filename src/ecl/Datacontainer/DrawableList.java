package ecl.Datacontainer;

//DrawableList Klasse
//selbsterklärend
//ToDo: 
import java.io.IOException;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * @author ecl
 */
public class DrawableList implements Serializable
{
	private static final long serialVersionUID = 8532784409168964123L;
	
	private int length = 0;
	private ArrayList<DrawableList.DrawableItem> list;
	
	// Hilfsklasse
	public class DrawableItem
	{
		Drawable icon;
		GeoPoint gpPoint;
		int width;
		int height;
		int tintColor = Color.WHITE;
	}
	
	// Konstruktor
		public DrawableList () 
		{
			list = new ArrayList<DrawableList.DrawableItem>();
		}
		
		public int getLength() 
		{
			return length;
		}
		
		public int getTintColor(int index) 
		{
			if(index < length && index >= 0)
				return list.get(index).tintColor;
			else
				return -1;
		}
		
		public GeoPoint getGeoPoint(int index) 
		{
			if(index < length && index >= 0)
				return list.get(index).gpPoint;
			else
				return null;
		}
		
		public int getWidth(int index)
		{
			if(index < length && index >= 0)
				return list.get(index).width;
			else
				return 0;
		}
		
		public int getHeight(int index)
		{
			if(index < length && index >= 0)
				return list.get(index).height;
			else
				return 0;
		}
		
		public Drawable getIcon(int index)
		{
			if(index < length && index >= 0)
				return list.get(index).icon;
			else
				return null;
		}
		
		public int add(Drawable icon, GeoPoint gpPoint, int width, int height, int tintColor)
		{
			DrawableItem temp = new DrawableItem();
			temp.icon = icon;
			temp.gpPoint = gpPoint;
			temp.width = width;
			temp.height = height;
			temp.tintColor = tintColor;
			list.add(temp);
			length++;
			return length -1;
		}
		
		public void set(int index, Drawable icon, GeoPoint gpPoint, int width, int height, int tintColor)
		{
			if(index < length && index >= 0)
			{
				list.get(index).icon = icon;
				list.get(index).gpPoint = gpPoint;
				list.get(index).width = width;
				list.get(index).height = height;
				list.get(index).tintColor = tintColor;
			}
		}
		
		public void move(int index, GeoPoint gpToPoint)
		{
			if(index < length && index >= 0)
			{
				list.get(index).gpPoint = gpToPoint;
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
				this.list = (ArrayList<DrawableList.DrawableItem>)in.readObject();
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
