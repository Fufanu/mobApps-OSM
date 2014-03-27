package fhfl.android.mobapps_osm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class fragment_settings extends Fragment implements OnItemSelectedListener, OnClickListener {
	private Button btn_deleteFile;
	private ToggleButton TB_GPS;
	private ToggleButton TB_INet;
	private ToggleButton TB_Track;
	private Spinner DropDown;
	private DataManager DM;
	
	private View view;
	private SettingsContainer settings;
	
	//
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		MainActivity activity = (MainActivity) getActivity();
		settings = activity.settings;
		
		view = inflater.inflate(R.layout.fragment_settings, container, false);
		
		DropDown = (Spinner)view.findViewById(R.id.fSettings_DropDown_Files);
		DropDown.setOnItemSelectedListener(this);
		btn_deleteFile = (Button) view.findViewById(R.id.fSettings_Button_DeleteFile);
		btn_deleteFile.setOnClickListener(this);
		TB_GPS = (ToggleButton)view.findViewById(R.id.fSettings_Toggle_GPS);
		TB_GPS.setOnClickListener(this);
		TB_GPS.setChecked(settings.isGpsOnControl());
		TB_INet = (ToggleButton)view.findViewById(R.id.fSettings_Toggle_INet);
		TB_INet.setOnClickListener(this);
		TB_INet.setChecked(settings.isInternetConnection());
		TB_Track = (ToggleButton)view.findViewById(R.id.fSettings_Toggle_Track);
		TB_Track.setOnClickListener(this);
		TB_Track.setChecked(settings.isGpsTrack());
		
		DM = new DataManager();
			
		refreshDropDown();		
		updateSettingsXML();
		
		TB_GPS.setChecked(settings.isGpsOnControl());
		TB_INet.setChecked(settings.isInternetConnection());
		
		return view;
	}
	
	private void loadDropDownChange(){
		
		for(int i = 0; i < DropDown.getCount(); i++){
			if(DropDown.getItemAtPosition(i).toString().contains(settings.getCurrentLogFile())){
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
        	settings.loadOverlayLists();
		}
		else if(v.getId() ==  R.id.fSettings_Toggle_GPS){
			settings.setGpsOnControl(TB_GPS.isChecked());
		}
		else if(v.getId() ==  R.id.fSettings_Toggle_INet){
			settings.setInternetConnection(TB_INet.isChecked());
		}
		else if(v.getId() ==  R.id.fSettings_Toggle_Track){
			if(settings.isGpsOnControl()){
				settings.setGpsTrack(TB_Track.isChecked());
				
				if(TB_Track.isChecked()){
					File f = DM.createNewGPSLogFile();
					settings.setCurrentLogFile(f.getName());
					DropDown.setEnabled(false);
					btn_deleteFile.setEnabled(false);
					TB_GPS.setEnabled(false);
					refreshDropDown();
				}
				else
				{
					DropDown.setEnabled(true);
					btn_deleteFile.setEnabled(true);
					TB_GPS.setEnabled(true);
				}
			}
			else
			{
				TB_Track.setChecked(false);
			}
		}
		updateSettingsXML();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

		settings.setCurrentLogFile(DropDown.getSelectedItem().toString());
		Log.d("DropDown", settings.getCurrentLogFile());
		updateSettingsXML();
		settings.reloadTrackPointHandler();
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
		
		loadDropDownChange();
	}
	
	private boolean updateSettingsXML(){
		DM.createSettingsFile();
		try{
			String xml = "";
			xml += "<CurrentLogFile>" + settings.getCurrentLogFile() +"</CurrentLogFile>\r\n";
			xml += "<GpsOnControl>" + settings.isGpsOnControl() + "</GpsOnControl>\r\n";
			xml += "<InternetConnection>" + settings.isInternetConnection() + "</InternetConnection>";
			
			DM.rewriteSettingsFile(xml);
			
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	
}
