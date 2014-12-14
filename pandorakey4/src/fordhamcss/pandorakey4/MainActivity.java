//Needs revision/commenting

package fordhamcss.pandorakey4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	static final String SAVE_BUNDLE = "service present";
	
	MyService serviceBinder;
    Intent i;
    Boolean present; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            
            present = savedInstanceState.getBoolean(SAVE_BUNDLE);
        } 
        else 
        {
            present = false;
        }
        setContentView(R.layout.activity_main);
    }

    public void openReport(View view)
    {
    	
		Intent dialogIntent = new Intent(MainActivity.this, FinalActivity.class);
        startActivity(dialogIntent);
        
    }
    
    public void startService(View view) 
    {    	
    	if (present == false)
    	{

    		startService(new Intent(getBaseContext(), MyService.class));
    		present = true;
    	}
        
    }
    
    public void stopService(View view) 
    {    	
    	present = false;
        stopService(new Intent(getBaseContext(), MyService.class));
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_main_activity_menu, menu);
        
        return true;
    }
    
  

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_old_reports:
            	Intent dialogIntent = new Intent(MainActivity.this, FinalActivity.class);
            	startActivity(dialogIntent);
            	return true;
            case R.id.menu_item_start:
            	if (present == false)
            	{

            		startService(new Intent(getBaseContext(), MyService.class));
            		present = true;
            	}                
            	return true;
            case R.id.menu_item_stop:
            	present = false;
                stopService(new Intent(getBaseContext(), MyService.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putBoolean(SAVE_BUNDLE, present);
        super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override 
    public void onRestoreInstanceState(Bundle savedInstanceState) 
    {
    	super.onRestoreInstanceState(savedInstanceState);
    	   
        // Restore state members from saved instance
        present = savedInstanceState.getBoolean(SAVE_BUNDLE, present);
    }

    
}
