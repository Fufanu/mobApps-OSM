package fhfl.android.mobapps_osm;

import android.util.Log;

//Implementiert einen VariableChangeListener, der darauf prüft ob das entsprechende Objekt verändert wurde und meldet dies dann den Listenern
public class VariableChanged{

	private Object o;
	private VariableChangedListener varibaleChangeListener;
	
	public VariableChanged(Object o){
		this.o = o;
	}
	
	//Hinzufügen des Listeners
	public void setVariableChangeListener(VariableChangedListener listener){
		varibaleChangeListener = listener;
	}
	
	//Prüfung des Objekts
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
