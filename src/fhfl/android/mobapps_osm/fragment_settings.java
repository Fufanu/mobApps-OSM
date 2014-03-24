package fhfl.android.mobapps_osm;

import java.util.ArrayList;
import java.util.Collections;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class fragment_settings extends Fragment implements OnItemSelectedListener, OnClickListener {
	private Button btn_deleteFile;
	private Button btn_saveSettings;
	private Spinner DropDown;
	private DataManager DM;
	private TextView DisplayFile;
	private View view;
	private SettingsContainer settings;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		MainActivity activity = (MainActivity) getActivity();
		settings = activity.settings;
		
		view = inflater.inflate(R.layout.fragment_settings, container, false);
		DisplayFile = (TextView)view.findViewById(R.id.fSettings_DisplayFileSource);
		DropDown = (Spinner)view.findViewById(R.id.fSettings_DropDown_Files);
		DropDown.setOnItemSelectedListener(this);
		btn_deleteFile = (Button) view.findViewById(R.id.fSettings_Button_DeleteFile);
		btn_deleteFile.setOnClickListener(this);
		btn_saveSettings = (Button)view.findViewById(R.id.fSettings_Button_Save);
		btn_saveSettings.setOnClickListener(this);
		
		DM = new DataManager();
		
		DM.createSettingsFile();
		//DM.createNewGPSLogFile();
			
		refreshDropDown();
		
		loadSettings();
		
		DisplayFile.setText(DM.readSettingsFile().toString());
		return view;
	}
	
	private void loadSettings(){
		
		for(int i = 0; i < DropDown.getCount(); i++){
			if(DropDown.getItemAtPosition(i).toString().contains(DM.readSettingsFile().trim())){
				DropDown.setSelection(i);
				break;
			}
		}	
	}

	@Override
	public void onClick(View v) {
		if (v.getId() ==  R.id.fSettings_Button_DeleteFile)  {
        	
        	DM.deleteGPSLogFile(DropDown.getSelectedItem().toString());
        	refreshDropDown();       	
        }
		else if(v.getId() == R.id.fSettings_Button_Save){
			DM.rewriteSettingsFile(DropDown.getSelectedItem().toString());
			DisplayFile.setText(DM.readSettingsFile().toString());
			settings.reloadTrackPointHandler();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void refreshDropDown(){
		ArrayList<String> Files = DM.getAllFiles();
		
		Collections.sort(Files, Collections.reverseOrder());		
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(),   android.R.layout.simple_spinner_item, Files);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		DropDown.setAdapter(spinnerArrayAdapter);
	}
}
