package fhfl.android.mobapps_osm;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import fhfl.android.mobapps_osm.VariableChanged.VariableChangedListener;
import android.R.string;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class fragment_stats extends Fragment implements OnClickListener {
	private DataManager DM;
	private View view;
	private TrackPointsHandler TPH;
	private Handler handler;
	private GraphicalView mChart;
	private ProgressDialog progress;
	private SettingsContainer settings;
	private SimpleDateFormat dateFormat;
	private MainActivity activity;
	private TextView Meter; 
	private double m = 0.0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_stats, container, false);
		Meter = (TextView)view.findViewById(R.id.fStats_meter);
		DM = new DataManager();

		activity = (MainActivity) getActivity();
		settings = activity.settings;

		OpenChart();

		return view;
	}

	private void OpenChart() {
		// Define the number of elements you want in the chart.
		Log.d("Ststs", "bis hier");

		ArrayList<StatsPoint> data = getNewData();

		Log.d("Size", String.valueOf(data.size()));

		double y[] = new double[data.size()];
		double x[] = new double[data.size()];

		double xVal = 0.0;
		double d = 0.0;
		

		for (int i = 0; i < data.size(); i++) {
			y[i] = Math.round(data.get(i).getSpeed());
			xVal += data.get(i).getTimeOffset();
			x[i] = xVal / 60;
			Log.d("Point " + i, "X:" + x[i] + " Y:" + y[i]);
			d += data.get(i).getSpeed();
			
		}

		// Create XY Series for X Series.
		XYSeries xSeries = new XYSeries("aufgezeichnete Geschwindigkeit");
		XYSeries xSeries2 = new XYSeries("durchschnitts Geschwindigkeit"); 

		// Adding data to the X Series.
		for (int i = 0; i < x.length; i++) {
			xSeries.add(x[i], y[i]);
			xSeries2.add(x[i], d/x.length);
			
		}

		// Create a Dataset to hold the XSeries.
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries2);
		dataset.addSeries(xSeries);
		
		// Create XYSeriesRenderer to customize XSeries2
				XYSeriesRenderer Xrenderer2 = new XYSeriesRenderer();
				Xrenderer2.setColor(Color.RED);
				Xrenderer2.setPointStyle(PointStyle.POINT);
				Xrenderer2.setPointStrokeWidth(0.1F);
				Xrenderer2.setDisplayChartValues(false);
				
				Xrenderer2.setLineWidth(4);
				Xrenderer2.setFillPoints(false);
				
		
				// Create XYSeriesRenderer to customize XSeries
		XYSeriesRenderer Xrenderer = new XYSeriesRenderer();
		Xrenderer.setColor(Color.BLUE);
		Xrenderer.setPointStyle(PointStyle.DIAMOND);
		Xrenderer.setDisplayChartValues(true);
		Xrenderer.setChartValuesTextSize(25.0F);
		Xrenderer.setLineWidth(6);
		Xrenderer.setFillPoints(true);
				

		// Create XYMultipleSeriesRenderer to customize the whole chart
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

		// Creating an intent to plot line chart using dataset and
		// multipleRenderer
		mChart = (GraphicalView) ChartFactory.getLineChartView(getActivity()
				.getBaseContext(), dataset, mRenderer);

		// Adding click event to the Line Chart.

		// Add the graphical view mChart object into the Linear layout .
		chart_container.addView(mChart);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private ArrayList<StatsPoint> getNewData() {

		ArrayList<TrackPoint> TPL = new ArrayList<TrackPoint>();
		if (settings.isGpsTrack()) {
			TPL = settings.getTPList();
		} else {
			TPH = new TrackPointsHandler(DM.readGPSLogFile(settings
					.getCurrentLogFile()));
			TPL = TPH.getPointsList();
		}
		
		//Meter addieren
		TrackPoint tmpPoint = null;
		for(TrackPoint p : TPL){
			if(tmpPoint != null)
				m += tmpPoint.distanceTo(p);
			tmpPoint = p;
		}
		
		if(m > 1000)
			Meter.setText("Strecke: "+String.valueOf(m/1000) + "km");
		else
			Meter.setText("Strecke: "+String.valueOf(m)+ "m");
		

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			double pastTime = (timestamp2.getTime() - timestamp1.getTime()) / 1000.0;
			if (pastTime > 30.0) {
				double m = TPL.get(p1).distanceTo(TPL.get(p2));

				double kmh = (m / pastTime) * 3.6;
				l.add(new StatsPoint(pastTime, kmh));
				p1 = p2;
			}
		}
		return l;
	}
}
