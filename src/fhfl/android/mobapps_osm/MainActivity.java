package fhfl.android.mobapps_osm;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_place, new fragment_map());
		transaction.addToBackStack(null);
		transaction.commit();      
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    	
    	Fragment newFragment;
    	
        if(item.getItemId() == R.id.Menue_SwitchButton_Map)
        {            	
        	newFragment = new fragment_map(); 
        }
        else if(item.getItemId() == R.id.Menue_SwitchButton_Settings)
        {
        	newFragment = new fragment_settings();
        }
        else if(item.getItemId() == R.id.Menue_SwitchButton_Stats)
        {
        	newFragment = new fragment_stats();
        }
        else
        {
        	newFragment = new fragment_map();
        }
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_place, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
		
		return true;
    }
    
}
