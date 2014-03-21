package fhfl.android.mobapps_osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.R.string;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	private Button btn_deleteFile;
	private Spinner DropDown;
	private DataManager DM;
	private TextView DisplayFile;
	private View view;
	private TrackPointsHandler TPH;
	
	private GraphicalView mChart;
	private String[] mMonth = new String[] {"Jan", "Feb" , "Mar", "Apr", "May", "Jun", "Jul", "Aug" };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_stats, container, false);
		DM = new DataManager();
		TPH = new TrackPointsHandler(DM.readGPSLogFile(DM.readSettingsFile()));
		
		ArrayList<TrackPoint> TPL = TPH.getPointsList();
		
		String tmp = String.valueOf(TPL.size());
		
		for(TrackPoint tp : TPL){
			tmp += " NEW ";
			tmp += String.valueOf(tp.getValid()) + " ";
			tmp += String.valueOf(tp.getLat()) + " ";
			tmp += String.valueOf(tp.getLon()) + " ";
			tmp += String.valueOf(tp.getEle()) + " ";
			tmp += tp.getDate() + " ";
			tmp += tp.getTime() + " ";
			
		}
		
		//DM.rewriteSettingsFile(tmp);
		
		//###################################################### ALT
		//DisplayFile = (TextView)view.findViewById(R.id.fStats_DisplayFileSource);
		
		
		
		/*DM.createNewGPSLogFile();
		DM.createSettingsFile();
		DM.rewriteSettingsFile("test");
		DM.rewriteSettingsFile("läuft");*/
		
		//String tmp = DM.readSettingsFile();

		//DisplayFile.setText(DM.readGPSLogFile(tmp));
		
		
		//####################################################### NEU
		
		OpenChart();
		
	
		return view;
	}

	private void OpenChart() {
		// Define the number of elements you want in the chart.
	    int x[]={0,1,2,3,2,5,6,0};
	    int z[]={0,1,2,3,4,5,6,7};
	   
	    // Create XY Series for X Series.
	    XYSeries xSeries=new XYSeries("X Series");
	   
	    //  Adding data to the X Series.
	    for(int i=0;i<z.length;i++)
	    {
	    xSeries.add(z[i],x[i]);
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
	    mRenderer.setXLabels(0);
	    mRenderer.setPanEnabled(false);
	   
	    mRenderer.setShowGrid(true);
	 
	    mRenderer.setClickEnabled(true);
	   
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
	    mChart.setOnClickListener(this);
	    
	    // Add the graphical view mChart object into the Linear layout .
	    chart_container.addView(mChart);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SeriesSelection series_selection=mChart.getCurrentSeriesAndPoint();

		if(series_selection!=null)
		{
			int series_index=series_selection.getSeriesIndex();
	
			String select_series="X Series";
			if(series_index==0)
			{
				select_series="X Series";
			}else
			{
				select_series="Y Series";
			}
	
			String month=mMonth[(int)series_selection.getXValue()];
	
			int amount=(int)series_selection.getValue();
	
			Toast.makeText(v.getContext(), select_series+"in" + month+":"+amount, Toast.LENGTH_LONG).show();
		
		}
	}
}
