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
import android.widget.Toast;


public class MainActivity extends Activity {
	
	// creates a tree root to keep track
	EventTree daTree = new EventTree();
	
	// initial contact list is saved
	Cursor initialContactList;
	
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
        
    	ContentResolver cr = getContentResolver();
    	String[] array = new String[0];
    	initialContactList = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, array, null);
    	
//    	// PRINTS THE FIRST CONTACT
//    	initialContactList.moveToFirst();
//    	String Text = initialContactList.getString(initialContactList.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//    	Toast.makeText( getApplicationContext(),Text,Toast.LENGTH_SHORT).show();
    	
        report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	System.out.println("Opening reports");
                // Intent i = new Intent(MainActivity.this, FinalActivity.class); 
                // startActivityForResult(i, 0);
            	
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
    public void checkContacts() {

    	Cursor newContactList;
    	ContentResolver cr = getContentResolver();
    	String[] array = new String[0];
    	newContactList = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, array, null);

    	int diff = newContactList.getCount() - initialContactList.getCount();

		Toast.makeText( getApplicationContext(),"CHECK",Toast.LENGTH_SHORT).show();
    	
    	if(diff > 0) {
        	// generates the time
        	int time = (int) (System.currentTimeMillis());
        	// find last contact added
    		newContactList.moveToLast();
    		String newContact = newContactList.getString(newContactList.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
    		// create a contact object
    		createContact(newContact, time);
    	}
    		
//    		if(initialContactList.getString(initialContactList.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).toString()
//    				!= newContactList.getString(newContactList.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).toString()) {
//    			
//    			String newString = newContactList.getString(newContactList.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).toString();
//    			Toast.makeText( getApplicationContext(),newString,Toast.LENGTH_SHORT).show();
//    			
//    			newContactList.moveToNext();
//    			diff--;
//    			String newText = theList.get(0);
//    			Toast.makeText( getApplicationContext(),"WENT THRU",Toast.LENGTH_SHORT).show();
//    		}
//    		initialContactList.moveToNext();
//    		newContactList.moveToNext();
//    	}
    	
    	// overwrites the initial contact list for next check
    	initialContactList = newContactList;

//    	// calls function that creates contact objects
//    	for(int i = 0; i < theList.size(); i++) {
//    		createContact(theList.get(i), time);
//    	}
    }

    // creates contact objects
    public void createContact(String inName, int inTime) {
    	ContactEvent newContact = new ContactEvent(inTime, "Some Location", inName);
    	daTree.insertEvent(newContact);
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
