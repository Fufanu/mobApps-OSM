package fhfl.android.mobapps_osm;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// Konstruktor
@SuppressLint("SimpleDateFormat")
public class fragment_stats extends Fragment implements OnClickListener {
	private DataManager DM; // Dateienhandhabung
	private View view;
	private TrackPointsHandler TPH;
	private GraphicalView mChart; // Diagramm
	private SettingsContainer settings;
	private SimpleDateFormat dateFormat; // Zeitformat
	private MainActivity activity;
	private TextView Meter; // TextView für die Anzeige der ges. Strecke
	private double m = 0.0; // Strecke in Metern

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Elemnte binden
		view = inflater.inflate(R.layout.fragment_stats, container, false);
		Meter = (TextView) view.findViewById(R.id.fStats_meter);
		DM = new DataManager();

		// SettingsContainer holen
		activity = (MainActivity) getActivity();
		settings = activity.settings;

		// Diagramm zeichnen
		OpenChart();

		return view;
	}

	private void OpenChart() {
		// Daten auslesen und im Array speichern
		ArrayList<StatsPoint> data = getNewData();

		double y[] = new double[data.size()];
		double x[] = new double[data.size()];

		double xVal = 0.0;
		double d = 0.0;

		// Werte in die Arrays schreiben
		for (int i = 0; i < data.size(); i++) {
			y[i] = Math.round(data.get(i).getSpeed());
			xVal += data.get(i).getTimeOffset();
			x[i] = xVal / 60;

			d += data.get(i).getSpeed(); // Geschwindigkeit für jeden Punkt
											// aufadieren

		}

		// Erstellen XY Series
		XYSeries xSeries = new XYSeries("aufgezeichnete Geschwindigkeit");
		XYSeries xSeries2 = new XYSeries("durchschnitts Geschwindigkeit");

		// Daten anhängen
		for (int i = 0; i < x.length; i++) {
			xSeries.add(x[i], y[i]);
			xSeries2.add(x[i], d / x.length);

		}

		// Erstelle Dataset für die Series.
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries2);
		dataset.addSeries(xSeries);

		// Erstelle XYSeriesRenderer um XSeries2 zu editieren
		XYSeriesRenderer Xrenderer2 = new XYSeriesRenderer();
		Xrenderer2.setColor(Color.RED);
		Xrenderer2.setPointStyle(PointStyle.POINT);
		Xrenderer2.setPointStrokeWidth(0.1F);
		Xrenderer2.setDisplayChartValues(false);

		Xrenderer2.setLineWidth(4);
		Xrenderer2.setFillPoints(false);

		// Erstelle XYSeriesRenderer um XSeries zu editieren
		XYSeriesRenderer Xrenderer = new XYSeriesRenderer();
		Xrenderer.setColor(Color.BLUE);
		Xrenderer.setPointStyle(PointStyle.DIAMOND);
		Xrenderer.setDisplayChartValues(true);
		Xrenderer.setChartValuesTextSize(25.0F);
		Xrenderer.setLineWidth(6);
		Xrenderer.setFillPoints(true);

		// Erstelle XYMultipleSeriesRenderer um das ganze Chart zu editieren
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.addSeriesRenderer(Xrenderer2);
		mRenderer.addSeriesRenderer(Xrenderer);

		mRenderer.setChartTitle("X Vs Y Chart");
		mRenderer.setXTitle("Zeit in Minuten");
		mRenderer.setYTitle("Geschwindigkeit in km/h");
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setPanEnabled(true, true);
		mRenderer.setClickEnabled(false);
		mRenderer.setYAxisMax(30.0);
		mRenderer.setYAxisMin(0.0);
		mRenderer.setShowGrid(true);
		mRenderer.setAxisTitleTextSize(15.0F);
		mRenderer.setChartTitleTextSize(15.0F);
		mRenderer.setLabelsTextSize(15.0F);
		mRenderer.setLegendTextSize(20.0F);
		mRenderer.setMargins(new int[] { 0, 40, 25, 10 });

		LinearLayout chart_container = (LinearLayout) view
				.findViewById(R.id.fStats_chart);

		// Erstelle intent um das dataset zu plotten
		mChart = (GraphicalView) ChartFactory.getLineChartView(getActivity()
				.getBaseContext(), dataset, mRenderer);

		// Das Objekt dem Chart übergeben
		chart_container.addView(mChart);
	}

	private ArrayList<StatsPoint> getNewData() {

		ArrayList<TrackPoint> TPL = new ArrayList<TrackPoint>();
		if (settings.isGpsTrack()) {
			TPL = settings.getTPList(); // Wenn gerade GPS-Tracking an ist
		} else {
			TPH = new TrackPointsHandler(DM.readGPSLogFile(settings // Wenn aus
																	// Datei
																	// gelesen
																	// werden
																	// soll
					.getCurrentLogFile()));
			TPL = TPH.getPointsList();
		}

		// Meter addieren von jedem Punkt
		TrackPoint tmpPoint = null;
		for (TrackPoint p : TPL) {
			if (tmpPoint != null)
				m += tmpPoint.distanceTo(p);
			tmpPoint = p;
		}

		// Anzeige im TextView
		if (m > 1000)
			Meter.setText("Strecke: " + String.valueOf(m / 1000) + "km");
		else
			Meter.setText("Strecke: " + String.valueOf(m) + "m");

		// Parsen der Daten
		Date parsedDate1 = null;
		Date parsedDate2 = null;
		Timestamp timestamp1 = null;
		Timestamp timestamp2 = null;
		int p1 = 0;
		int p2 = 0;

		ArrayList<StatsPoint> l = new ArrayList<StatsPoint>();

		if (TPL.size() > 0)
			dateFormat = new SimpleDateFormat(TPL.get(0).getTimeFormat());

		for (int i = 1; i < TPL.size() - 1; i++) {

			p2 = i;

			try {
				parsedDate1 = dateFormat.parse(TPL.get(p1).getDate() + " "
						+ TPL.get(p1).getTime());
				parsedDate2 = dateFormat.parse(TPL.get(p2).getDate() + " "
						+ TPL.get(p2).getTime());

				timestamp1 = new Timestamp(parsedDate1.getTime());
				timestamp2 = new Timestamp(parsedDate2.getTime());
			} catch (Exception e) {
				Toast.makeText(activity.getApplicationContext(), "cant parse timestamp", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

			// Vergangene Zeit zwischen zwei Punkten ermitteln ( /100 für
			// Sekunden, sonst Millisekunden)
			double pastTime = (timestamp2.getTime() - timestamp1.getTime()) / 1000.0;
			if (pastTime > 30.0) { // Wenn mindestens 30Sek. vergangen
				double m = TPL.get(p1).distanceTo(TPL.get(p2));

				double kmh = (m / pastTime) * 3.6;
				l.add(new StatsPoint(pastTime, kmh)); // Neuen Punkt zum Zeichen
														// der Liste anhängen
				p1 = p2;
			}
		}
		return l;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
