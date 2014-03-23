package ecl.Overlays;

import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.Overlay;

import ecl.Datacontainer.DrawableList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class FhflDrawableOverlay extends Overlay
{
	private Paint paint;
	private DrawableList iconList;
	private Projection projection;
	
	public FhflDrawableOverlay (ResourceProxy pResourceProxy) 
	{
		super(pResourceProxy);
		paint = new Paint();
		paint.setStrokeWidth(4f);
		paint.setAntiAlias(true);
	}

	@Override
	protected void draw(Canvas arg0, MapView arg1, boolean arg2) 
	{
		Log.v(null, "FhflDrawableOverlay:draw():");
		if(iconList == null)
		{
			Log.e(null, "No Data List found! Use: FhflDrawableOverlay.SetDataList()");
			return;
		}
		
        projection = arg1.getProjection();

        if(!arg2) // arg2 == true --> draw shadow
		{
			Log.v(null, "   iconList.size(): "+iconList.getLength());
			for(int i = 0; i < iconList.getLength(); i++)
			{
				Point p1 = new Point();
				p1 = projection.toPixels(iconList.getGeoPoint(i), p1);
				Drawable icon = iconList.getIcon(i);
				int tintColor = iconList.getTintColor(i);
				int width = iconList.getWidth(i);
				int height = iconList.getHeight(i);
				icon.setColorFilter(tintColor, Mode.MULTIPLY);
				icon.setBounds(p1.x-(width/2), p1.y-height, p1.x+(width/2), p1.y);
				icon.draw(arg0);
			}
		}
        else
        {
        	Log.v(null, "   shadow");
        }
	}
	
	public void setDataList(DrawableList iconList)
	{
		this.iconList = iconList;
	}
}
