package ecl.Overlays;

import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.Overlay;

import ecl.Datacontainer.DrawObjectList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class FhflVectorOverlay extends Overlay 
{
	private Paint paint;
	private DrawObjectList drawList;
	private Projection projection;
	
	public FhflVectorOverlay(ResourceProxy pResourceProxy) 
	{
		super(pResourceProxy);
		paint = new Paint();
		paint.setStrokeWidth(4f);
		paint.setAntiAlias(true);
	}

	@Override
	protected void draw(Canvas arg0, MapView arg1, boolean arg2) 
	{
		Log.v(null, "FhflOverlay:draw():");

        projection = arg1.getProjection();
        Paint backup = paint;

        if(drawList == null)
		{
			Log.e(null, "No Data List found! Use: FhflVectorOverlay.SetDataList()");
			return;
		}
        
		if(!arg2) // arg2 == true --> draw shadow
		{
			Log.v(null, "   drawList.getLength(): "+drawList.getLength());
			for(int i = 0; i < drawList.getLength(); i++)
			{
				Point p1 = new Point();
				Point p2 = new Point();
				p1 = projection.toPixels(drawList.getGeoPointStart(i), p1);
				p2 = projection.toPixels(drawList.getGeoPointEnd(i), p2);
				paint.setColor(drawList.getColor(i)); // für alle gleich
				//Log.d(null, "   i: "+ i +"  Type: "+drawList.getType(i));
				switch(drawList.getType(i))
				{
				case 1: // Punkt
					arg0.drawCircle(p1.x, p1.y, 8f, paint);
					break;
				case 2: // Linie
		            arg0.drawLine( p1.x, p1.y, p2.x, p2.y, paint);
					break;
				case 4: // Pfeil mit Kreis im ursprung (Hack: kein Break, damit der normale Pfeil auch gezeichnet wird)
					arg0.drawCircle(p1.x, p1.y, 8f, paint);
				case 3: // Pfeil
					//Pfeilkopf
					double sx = p2.x + ( ( 20f * ( (p1.x - p2.x)-(p1.y-p2.y) ) )  /  ( Math.sqrt(2f) * Math.sqrt( ((p1.x-p2.x)*(p1.x-p2.x)) + ((p1.y-p2.y)*(p1.y-p2.y)) ) )  );
					double sy = p2.y + ( ( 20f * ( (p1.x - p2.x)+(p1.y-p2.y) ) )  /  ( Math.sqrt(2f) * Math.sqrt( ((p1.x-p2.x)*(p1.x-p2.x)) + ((p1.y-p2.y)*(p1.y-p2.y)) ) )  );
					double tx = p2.x + ( ( 20f * ( (p1.x - p2.x)+(p1.y-p2.y) ) )  /  ( Math.sqrt(2f) * Math.sqrt( ((p1.x-p2.x)*(p1.x-p2.x)) + ((p1.y-p2.y)*(p1.y-p2.y)) ) )  );
					double ty = p2.y - ( ( 20f * ( (p1.x - p2.x)-(p1.y-p2.y) ) )  /  ( Math.sqrt(2f) * Math.sqrt( ((p1.x-p2.x)*(p1.x-p2.x)) + ((p1.y-p2.y)*(p1.y-p2.y)) ) )  );
					arg0.drawLine( p2.x, p2.y, (float)sx, (float)sy, paint);
					arg0.drawLine( p2.x, p2.y, (float)tx, (float)ty, paint);
					// Pfeilstrich
					arg0.drawLine( p1.x, p1.y, p2.x, p2.y, paint);
					break;
				case 5: // nur Text (Wird nach dem Switch sowieso gemacht, da für alle gleich)
					break;
				default:
					Log.d(null, "   i: "+ i +"  Type: Error");
				}
				// Text für alle gleich
				if(drawList.getText(i) != null)
				{
					Point p = new Point();
					p.x = (p1.x+p2.x)/2; // 1 Pixel möglicher Fehler
					p.y = (p1.y+p2.y)/2;
					paint.setColor(drawList.getTextColor(i));
					paint.setTextSize(drawList.getTextSize(i));
					arg0.drawText(drawList.getText(i), p.x, p.y, paint);
				}
			}
		}
		else
		{
			Log.v(null, "   shadow");
		}
		
		paint = backup;
	}
	
	public void setDataList(DrawObjectList drawList)
	{
		this.drawList = drawList;
	}

}
