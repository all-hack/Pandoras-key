//Needs revision/commenting

package fordhamcss.pandorakey4;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	MyService serviceBinder;
    Intent i;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openReport(View view)
    {
  //  	ArrayList<String> outputStrings = new ArrayList<String>(); //Replace with code to pull saved report
    	
		Intent dialogIntent = new Intent(MainActivity.this, FinalActivity.class);
//		dialogIntent.putStringArrayListExtra("OutputStrings", outputStrings);  		
        startActivity(dialogIntent);
    }
    
    public void startService(View view) 
    {    	
        startService(new Intent(getBaseContext(), MyService.class));
    }
    
    public void stopService(View view) 
    {
        stopService(new Intent(getBaseContext(), MyService.class));
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }
}
