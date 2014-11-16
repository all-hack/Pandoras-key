//Test change
package fordhamcss.pandorakey4;

import java.util.LinkedList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
	
	
	
	// Array used to store events as they are generated
	LinkedList<Event> eventList = new LinkedList<Event>();
	
	// initial contact list is saved
	ContentResolver cr = getContentResolver();
	Cursor initialContactList = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	
	MyService serviceBinder;
    Intent i;
	
	private Button start_button;
	private Button stop_button;
	private Button report_button;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        start_button 	= (Button)findViewById(R.id.start_button);
        stop_button 	= (Button)findViewById(R.id.stop_button);
        report_button 	= (Button)findViewById(R.id.report_button);
        
        report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	System.out.println("Opening reports");
                Intent i = new Intent(MainActivity.this, FinalActivity.class); 
                startActivityForResult(i, 0);
            }
        });
        
    }

    public void startService(View view) 
    {    	
        startService(  new Intent(getBaseContext(), MyService.class)   ); //works
    }
    
    public void stopService(View view) 
    {
        stopService(new Intent(getBaseContext(), MyService.class));
    }
    
    // Checks contacts
	public void checkContacts(){
		
		LinkedList<String> theList = new LinkedList<String>();
		
		ContentResolver cr2 = getContentResolver();
		Cursor newContactList = cr2.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		int diff = newContactList.getCount() - initialContactList.getCount();
		
		while(diff > 0) {
			initialContactList.moveToNext();
			newContactList.moveToNext();

			if(initialContactList.getString(initialContactList.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
					!= newContactList.getString(newContactList.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))); {
						theList.add(newContactList.getString(newContactList.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
						newContactList.moveToNext();
						diff--;
					}
		}
		// overwrites the initial contact list for next check
		initialContactList = newContactList;
		
		// generates the time
		int time = (int) (System.currentTimeMillis());
		
		// calls function that creates contact objects
		for(int i = 0; i < theList.size(); i++) {
			createContact(theList.get(i), time);
		}
	}
	
	// creates contact objects
	public void createContact(String inName, int inTime) {
		ContactEvent newContact = new ContactEvent(inTime, "Some Location", inName);
		eventList.add(newContact);
	}
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
