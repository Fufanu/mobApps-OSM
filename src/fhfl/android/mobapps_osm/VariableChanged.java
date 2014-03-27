package fhfl.android.mobapps_osm;

import android.util.Log;

public class VariableChanged{

	private Object o;
	private VariableChangedListener varibaleChangeListener;
	
	public VariableChanged(Object o){
		this.o = o;
	}
	
	public void setVariableChangeListener(VariableChangedListener listener){
		varibaleChangeListener = listener;
	}
	
	public void setVariable(Object newo){

		Log.d("Listener", "geht");
		if(varibaleChangeListener != null){
			if(o != newo){
				Log.d("Listener", "geht");
				varibaleChangeListener.onVariableChanged(o);
			}
		}
	}
	
	public Object getVariable(){
		return o;
	}
	
	public static interface VariableChangedListener {
		void onVariableChanged(Object o);
	}
	
}
