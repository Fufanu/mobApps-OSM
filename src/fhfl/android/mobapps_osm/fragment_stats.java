package fhfl.android.mobapps_osm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class fragment_stats extends Fragment implements OnClickListener{
	public TextView TV_displayText;
	public Button b;
	public EditText userInput;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{	
		View view = inflater.inflate(R.layout.fragment_stats, container, false);
		TV_displayText = (TextView)view.findViewById(R.id.textView2);
		userInput = (EditText)view.findViewById(R.id.editText1);
		b = (Button) view.findViewById(R.id.button1);
        b.setOnClickListener(this);
		return view;
	}
	
	
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button1:
        	TV_displayText.setText(userInput.getText());
            break;
        }
	}
}
