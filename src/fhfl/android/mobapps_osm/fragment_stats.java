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
	//private SettingsContainer settings;
	private SimpleDateFormat dateFormat;
	//private MainActivity activity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_stats, container, false);
		DM = new DataManager();
		
		//activity = (MainActivity) getActivity();
		//settings = activity.settings;
		
		OpenChart();
		/*handler = new Handler();
		handler.postDelayed(runnable, 0);*/

		return view;
	}
	
	private Runnable runnable = new Runnable() {
		   @Override
		   public void run() {
		      /* do what you need to do */
			   OpenChart();
		      /* and here comes the "trick" */
		      handler.postDelayed(this, 60000);
		   }
		};

	private void OpenChart() {
		// Define the number of elements you want in the chart.
	    
	    ArrayList<StatsPoint> data = getNewData();
	    Log.d("Size", String.valueOf(data.size()));
	   
	    double y[] = new double[data.size()];
	    double x[] = new double[data.size()];

	    
	    double xVal = 0.0;
	    
	    for(int i =0; i < data.size(); i++){
	    	y[i] = Math.round(data.get(i).getSpeed());
	    	xVal += data.get(i).getTimeOffset();
	    	x[i] = xVal/60;
	    } 
	    
	    // Create XY Series for X Series.
	    XYSeries xSeries=new XYSeries("X Series");
	   
	    //  Adding data to the X Series.
	    for(int i=0;i<x.length;i++)
	    {
	    xSeries.add(x[i],y[i]);
	    }
	    
	    // Create a Dataset to hold the XSeries.
	    XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
	   
	    // Add X series to the Dataset.  
	    dataset.addSeries(xSeries);
	    
	    // Create XYSeriesRenderer to customize XSeries
	    XYSeriesRenderer Xrenderer=new XYSeriesRenderer();
	    Xrenderer.setColor(Color.BLUE);
	    Xrenderer.setPointStyle(PointStyle.DIAMOND);
	    Xrenderer.setDisplayChartValues(true);
	    Xrenderer.setLineWidth(4);
	    Xrenderer.setFillPoints(true);
	    
	    
	    
	    
	    // Create XYMultipleSeriesRenderer to customize the whole chart
	    XYMultipleSeriesRenderer mRenderer=new XYMultipleSeriesRenderer();
	   
	    mRenderer.setChartTitle("X Vs Y Chart");
	    mRenderer.setXTitle("Zeit in Minuten");
	    mRenderer.setYTitle("Geschwindigkeit in km/h");
	    mRenderer.setZoomButtonsVisible(true);
	    mRenderer.setPanEnabled(true, true);
	    mRenderer.setClickEnabled(false);
	    mRenderer.setYAxisMax(30.0);
	    mRenderer.setYAxisMin(0.0);
	    mRenderer.setShowGrid(true);
	    
	   
	    /*for(int i=0;i<z.length;i++)
	    {
	    mRenderer.addXTextLabel(i, mMonth[i]);
	    }*/
	    
	    // Adding the XSeriesRenderer to the MultipleRenderer. 
	    mRenderer.addSeriesRenderer(Xrenderer);
   
	    LinearLayout chart_container=(LinearLayout)view.findViewById(R.id.fStats_chart);
	    
	    // Creating an intent to plot line chart using dataset and multipleRenderer 
	    mChart=(GraphicalView)ChartFactory.getLineChartView(getActivity().getBaseContext(), dataset, mRenderer);
	    
	    //  Adding click event to the Line Chart.
	   
	    
	    // Add the graphical view mChart object into the Linear layout .
	    chart_container.addView(mChart);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	private double gps2m(double lat_a, double lon_a, double lat_b, double lon_b) {
		
		double R = 6371*1000; // m
		double dLat =  Math.toRadians((lat_b - lat_a));
		double dLon =  Math.toRadians((lon_b - lon_a));
		double lat1 = Math.toRadians(lat_a);
		double lat2 = Math.toRadians(lat_b);
		
		double a = Math.sin(dLat/2) * 
				Math.sin(dLat/2) +
				Math.sin(dLon/2) *
				Math.sin(dLon/2) * Math.cos(lat1)
				* Math.cos(lat2);
		
		double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0-a));
		
		double d = R * c;
		
		return d;
		
	}
	
	private ArrayList<StatsPoint> getNewData(){
		TPH = new TrackPointsHandler(DM.readGPSLogFile("FH-_Hafermarkt.gpx"));
		
		ArrayList<TrackPoint> TPL = TPH.getPointsList();
		Date parsedDate1 = null;
		Date parsedDate2 = null;		
		Timestamp timestamp1 = null;
		Timestamp timestamp2 = null;
		int p1 = 0;
		int p2 = 0;
		ArrayList<StatsPoint> l = new ArrayList<StatsPoint>();
		
		if(TPL.size() > 0)
			dateFormat = new SimpleDateFormat(TPL.get(0).getTimeFormat());
		
		for(int i = 1; i < TPL.size()-1; i++){

			p2 = i;
			
			try {
				parsedDate1 = dateFormat.parse(TPL.get(p1).getDate()+" "+TPL.get(p1).getTime());
				parsedDate2 = dateFormat.parse(TPL.get(p2).getDate()+" "+TPL.get(p2).getTime());
				
				timestamp1 = new Timestamp(parsedDate1.getTime());
				timestamp2 = new Timestamp(parsedDate2.getTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			double pastTime = (timestamp2.getTime() - timestamp1.getTime()) / 1000.0;
			if(pastTime > 60.0){
				double m = gps2m(TPL.get(p1).getLat(), TPL.get(p1).getLon(), TPL.get(p2).getLat(), TPL.get(p2).getLon());
				
				double kmh = (m/pastTime)*3.6;
				l.add(new StatsPoint(pastTime,kmh));
				p1 = p2;
			}
		}
		return l;
	}
}
