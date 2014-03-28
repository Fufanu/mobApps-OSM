package ecl.Overlays;

import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.Overlay;

import ecl.Datacontainer.TrackPointList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class FhflTrackOverlay extends Overlay {
	private Paint paint;
	private Paint point;
	private Projection projection;
	private TrackPointList trkList = null;

	public FhflTrackOverlay(ResourceProxy pResourceProxy) {
		super(pResourceProxy);
		paint = new Paint();
		paint.setStrokeWidth(3f);
		paint.setAntiAlias(true);
		point = new Paint();
		point.setStrokeWidth(3f);
		point.setAntiAlias(true);
	}

	@Override
	protected void draw(Canvas arg0, MapView arg1, boolean arg2) {
		Log.v(null, "FhflTrackOverlay:draw():");
		if (trkList == null) {
			Log.e(null,
					"No Data List found! Use: FhflTrackOverlay.SetDataList()");
			return;
		}

		projection = arg1.getProjection();

		Paint backup = paint;

		if (!arg2) // arg2 == true --> draw shadow
		{
			Log.v(null, "   trkList.getLength(): " + trkList.getLength());
			if (trkList.getLength() > 1) { // nur wenn mehr als ein Trackpoint
											// vorhanden
				for (int i = 1; i < trkList.getLength(); i++) {
					Point p1 = new Point();
					Point p2 = new Point();
					p1 = projection.toPixels(trkList.getGeoPoint(i - 1), p1);
					p2 = projection.toPixels(trkList.getGeoPoint(i), p2);
					paint.setColor(trkList.getColor(i - 1));
					arg0.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
					// Falls ein Text zu einem Trackpunkt existiert
					if (trkList.getText(i - 1) != null) {
						paint.setColor(trkList.getTextColor(i - 1));
						paint.setTextSize(trkList.getTextSize(i - 1));
						arg0.drawText(trkList.getText(i - 1), p1.x, p1.y, paint);
					}
					// Zeichnen von Start- und Endpunkt
					if (i == 1) {
						point.setColor(Color.GREEN);
						arg0.drawCircle(p1.x, p1.y, 6, point);
					} else if (i == trkList.getLength() - 1) {
						point.setColor(Color.RED);
						arg0.drawCircle(p1.x, p1.y, 6, point);
					}
				}
			}
		} else
			Log.v(null, "   shadow");

		paint = backup;
	}

	public void setDataList(TrackPointList trkList) {
		this.trkList = trkList;
	}
}
