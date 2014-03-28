package fhfl.android.mobapps_osm;

import android.util.Log;

//Implementiert einen VariableChangeListener, der darauf pr�ft ob das entsprechende Objekt ver�ndert wurde und meldet dies dann den Listenern
public class VariableChanged{

	private Object o;
	private VariableChangedListener varibaleChangeListener;
	
	public VariableChanged(Object o){
		this.o = o;
	}
	
	//Hinzuf�gen des Listeners
	public void setVariableChangeListener(VariableChangedListener listener){
		varibaleChangeListener = listener;
	}
	
	//Pr�fung des Objekts
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
	
	//Listener Interface
	public static interface VariableChangedListener {
		void onVariableChanged(Object o);
	}
	
}
