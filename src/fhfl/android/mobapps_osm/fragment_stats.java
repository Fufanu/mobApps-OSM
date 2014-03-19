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

public class fragment_stats extends Fragment implements OnClickListener, OnItemSelectedListener{
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
		DropDown = (Spinner)view.findViewById(R.id.fStats_DropDown_Files);
		DropDown.setOnItemSelectedListener(this);
		//userInput = (EditText)view.findViewById(R.id.editText1);
		btn_deleteFile = (Button) view.findViewById(R.id.fStats_Button_DeleteFile);
		btn_deleteFile.setOnClickListener(this);
		
		DM = new DataManager();
		//DM.createNewGPSLogFile();
		
		refreshDropDown();
		DisplayFile.setText("Startup");
	
		return view;
	}

	@Override
    public void onClick(View v) {
        if (v.getId() ==  R.id.fStats_Button_DeleteFile)  {
        	
        	DM.deleteGPSLogFile(DropDown.getSelectedItem().toString());
        	refreshDropDown();
        	
        }
	}
	
	public void refreshDropDown(){
		ArrayList<String> Files = DM.getAllFiles();
		
		Collections.sort(Files, Collections.reverseOrder());		
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(),   android.R.layout.simple_spinner_item, Files);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		DropDown.setAdapter(spinnerArrayAdapter);
	}

	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		DisplayFile.setText(DM.readGPSLogFile(DropDown.getSelectedItem().toString()));
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
