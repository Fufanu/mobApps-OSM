package fhfl.android.mobapps_osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.R.string;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class fragment_stats extends Fragment {
	private Button btn_deleteFile;
	private Spinner DropDown;
	private DataManager DM;
	private TextView DisplayFile;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{	
		view = inflater.inflate(R.layout.fragment_stats, container, false);
		DisplayFile = (TextView)view.findViewById(R.id.fStats_DisplayFileSource);
		
		
		DM = new DataManager();
		/*DM.createNewGPSLogFile();
		DM.createSettingsFile();
		DM.rewriteSettingsFile("test");
		DM.rewriteSettingsFile("läuft");*/
		
		String tmp = DM.readSettingsFile();

		DisplayFile.setText(DM.readGPSLogFile(tmp));
	
		return view;
	}    
}
