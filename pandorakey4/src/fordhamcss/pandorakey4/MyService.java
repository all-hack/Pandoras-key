//Needs revision/commenting

package fordhamcss.pandorakey4;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class MyService extends Service {

	// initial contact list is saved
	Cursor initialContactList;
	
	int tTimer = 12;
	int tHour;
	int tMinute;
	Boolean test = false;
	LocationManager mlocManager;
	LocationListener mlocListener;

	Location Where = null;
	String CurrentLocation = null;
	String Text;
	LinkedList<String> CurrLocation = new LinkedList<String>();
	LinkedList<String> CurrContact = new LinkedList<String>();
	
	EventTree theTree = new EventTree();
	List<Map<String, String>> returnStrings = new ArrayList<Map<String, String>>();

	Thread myThread;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly  stopped, so return sticky.
		
    	ContentResolver cr = getContentResolver();
    	String[] array = new String[0];
    	initialContactList = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, array, null);
    	    	
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

		 Calendar calendar = Calendar.getInstance();
		 tHour = calendar.get(Calendar.HOUR_OF_DAY)+tTimer;
		 tMinute = calendar.get(Calendar.MINUTE);
		
		 
		 
		/* Use the LocationManager class to obtain GPS locations */
		 mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		 mlocListener = new MyLocationListener();
		// below updates on time interval (mill seconds) AND location (meters)		
		
//		Where = mlocManager.getLastKnownLocation(mlocManager.GPS_PROVIDER);

		if (test == false)
		{
			mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
				5 * 60 * 60* 1000, 5, mlocListener);
		}
		
		else 
		{	mlocListener.onLocationChanged(Where);}

		return START_STICKY;
	}

	private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
	
	// Checks contacts
    public void checkContacts(){
    	
    	Cursor newContactList;
    	ContentResolver cr = getContentResolver();
    	String[] array = new String[0];
    	newContactList = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, array, null);

    	int diff = newContactList.getCount() - initialContactList.getCount();

    	if(diff > 0) {
    		

        	String time = make();

    		newContactList.moveToLast();

    		String newContact = newContactList.getString(newContactList.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

    		Event newContactEntry = new Event(time, CurrentLocation, "Contact", newContact);
    		
    		theTree.insertEvent(newContactEntry);
    		
    		CurrContact.add(newContact);
    		
    		initialContactList = newContactList;
    	}
    }

    
    public class MyLocationListener implements LocationListener {
		
    	
    	
    	@Override
		public void onLocationChanged(Location loc) {
					
    	Calendar calendar = Calendar.getInstance();
   		int hour = calendar.get(Calendar.HOUR_OF_DAY);
   		int minute = calendar.get(Calendar.MINUTE);	
    		
    	if (timer( hour, minute ) == true) 	
    	{
			double lat = loc.getLatitude();
			double lon = loc.getLongitude();
				
	  	   
			
			//String Text = "My current location is: " + "Latitud = "
			//+ loc.getLatitude() + "Longitud = " + loc.getLongitude();
			
//			Toast.makeText( getApplicationContext(), "01:"+CurrentLocation, Toast.LENGTH_SHORT).show();
//			Toast.makeText( getApplicationContext(), "02:"+Text, Toast.LENGTH_SHORT).show();

			
			Text = getCompleteAddressString(lat, lon);
			
//			Toast.makeText( getApplicationContext(), "03:"+CurrentLocation, Toast.LENGTH_SHORT).show();
//			Toast.makeText( getApplicationContext(), "04:"+Text, Toast.LENGTH_SHORT).show();
	
			if (CurrentLocation == null) {
				
//				Toast.makeText( getApplicationContext(), "05:"+CurrentLocation, Toast.LENGTH_SHORT).show();
				CurrentLocation = Text;
				Toast.makeText( getApplicationContext(), CurrentLocation, Toast.LENGTH_SHORT).show();
				CurrLocation.add(CurrentLocation);
	
				String time = make();
				Event newPlace = new Event(time, CurrentLocation, "Place");
				theTree.insertLocation(newPlace);
			}
			else
				
			
			if(CurrentLocation != Text )
			{
				CurrentLocation = Text;		
					Toast.makeText( getApplicationContext(), CurrentLocation, Toast.LENGTH_SHORT).show();
				CurrLocation.add(CurrentLocation);
	
				String time = make();
				Event newPlace = new Event(time, CurrentLocation, "Place");
				theTree.insertLocation(newPlace);
			}
		
			else if(CurrentLocation == Text)
			{
				Toast.makeText(getApplicationContext(), "you are in the same place", Toast.LENGTH_SHORT).show();
			}
    	
    	
			checkContacts();

    	
    	}
    	else 
    		onDestroy();
    	
		}
		@Override
		public void onProviderDisabled(String provider) {
		Toast.makeText( getApplicationContext(),
		"Gps Disabled",
		Toast.LENGTH_SHORT ).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
		Toast.makeText( getApplicationContext(),
		"Gps Enabled",
		Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		}/* End of Class MyLocationListener */    
 
    public void getOutput(EventNode t)
    {
    	Map<String, String> locOutput = new HashMap<String, String>();
    	locOutput.put("kind" , "location");
    	locOutput.put("locationName" ,  t.theEvent.get(0).place);
    	locOutput.put("timestamp" , t.theEvent.get(0).getTime());
    	returnStrings.add(locOutput);
    	
	    if(t.left != null) {
			for(int i=0; i < t.left.theEvent.size(); i++) {
		    	Map<String, String> tempServiceOutput = new HashMap<String, String>();
		    	tempServiceOutput.put("kind" , "contact");
		    	tempServiceOutput.put("name" , t.left.theEvent.get(i).getContactName());
		    	tempServiceOutput.put("timestamp" , t.left.theEvent.get(i).getTime());
		    	returnStrings.add(tempServiceOutput);
			}
		}
		if(t.right != null) {
			getOutput(t.right);
		}
    }
	
    public String generateOutputString(Map<String, String> serviceOutput)
    {
    	String outputString = null;
    	String username = "Stephen";
    	
    	if (serviceOutput.get("kind") == "location")
    	{
    		outputString = "You were at " + serviceOutput.get("locationName") + " at " + serviceOutput.get("timestamp");
    		
    	}
    	
    	else if (serviceOutput.get("kind") == "text")
    	{
    		outputString = "Text message ";
    		if (username.equals(serviceOutput.get("from")))
    			outputString += "sent to ";
    		else
    			outputString += "from ";
    		outputString += serviceOutput.get("to") + " at " + serviceOutput.get("timestamp");
    	}
    	
    	else if (serviceOutput.get("kind") == "call")
    	{
    		if (serviceOutput.get("missed").equals("true"))
    			outputString = "Missed call ";
    		else
    			outputString = "Call ";
    		
    		if (username.equals(serviceOutput.get("from")))
    			outputString += "to ";
    		else
    			outputString += "from ";
    		outputString += serviceOutput.get("to") + " at " + serviceOutput.get("timestamp");
    	}
    	
    	else if (serviceOutput.get("kind") == "contact")
    	{
    		outputString = "Added new contact, " + serviceOutput.get("name") + " at " + serviceOutput.get("timestamp"); 
    	}
    	
		//Toast.makeText(this, outputString, Toast.LENGTH_SHORT).show();

    	
		return outputString;
    }
	
    
    
    
    @Override
	public void onDestroy() {
		super.onDestroy();

//		Toast.makeText( getApplicationContext(), "before store", Toast.LENGTH_SHORT).show();
		mlocManager.removeUpdates(mlocListener);
		
		System.out.println("The tree is: "+theTree.toString());
//		System.out.println("The tree is: "+theTree.root.toString());
//		System.out.println("The tree is: "+theTree.root.theEvent.toString());
//		System.out.println("The tree is: "+theTree.root.theEvent.size());

		if (theTree.root == null )
		{
			String time = make();
			Event newPlace = new Event(null, "We are sorry for the inconvinience, but you gotta give us enough time to triangilate your coordinates! Go back and try again", "Place");
			theTree.insertLocation(newPlace);
			
			
		}

		
		myThread = new Thread(new MyThread());
		myThread.start();

//		String save1 = "save1";
	//	String open = "open";
		
//		Toast.makeText( getApplicationContext(), "before store", Toast.LENGTH_SHORT).show();

		//Store( getApplication().getApplicationContext(), theTree, save1, open);
		/*loaded = Load(getApplication().getApplicationContext(), save1, open ); */

		/*
		Intent dialogIntent = new Intent(getBaseContext(), FinalActivity.class);
	
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		dialogIntent.putStringArrayListExtra("OutputStrings", outputStrings);  
		getApplication().startActivity(dialogIntent);
		*/
		
		Toast.makeText(this, "Stopped Recording", Toast.LENGTH_SHORT).show();		
	}

    public void Store(Context context, EventTree tree, String prefName, String key)
    {
    	
    	SharedPreferences settings;
    	Editor editor;
    	
    	//get context for edit 
    	settings = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    	editor = settings.edit();
    	
    	//serialize object 
    	Gson gson = new Gson();
    	String jsonTree = gson.toJson(tree);
    	
    	//set into editor and push key 
    	editor.putString(key, jsonTree);
    	editor.apply();    	    	    	    	    	    	
    }
    
    class MyThread implements Runnable 
    {
    	
    	@Override 
    	public void run()
    	{
    		//For development, uses dummy tree
    /*		EventTree dummyTree = new EventTree();
    		dummyTree.insertLocation(new Event(make(), "Pugsley's Pizza", "Place"));
    		dummyTree.insertEvent(new Event(make(), "Pugsley's Pizza ","Contact", "Person McPersonface"));
    		dummyTree.insertEvent(new Event(make(),"Pugsley's Pizza ","Contact", "Fatso McPersonface"));
    		dummyTree.insertLocation(new Event(make(), "Full Moon Pizza", "Place"));
    		dummyTree.insertEvent(new Event(make(),"Full Moon Pizza","Place", "Person McNotPersonFace"));
    		dummyTree.insertEvent(new Event(make(),"Full Moon Pizza","Place", "Fatso McNotPersonFace"));
    		dummyTree.insertLocation(new Event(make(), "Pugsley's Pizza", "Place"));
    		dummyTree.insertEvent(new Event(make(), "Pugsley's Pizza ","Contact", "Person McPersonface"));
    		dummyTree.insertEvent(new Event(make(),"Pugsley's Pizza ","Contact", "Fatso McPersonface"));
    		dummyTree.insertLocation(new Event(make(), "Full Moon Pizza", "Place"));
    		dummyTree.insertEvent(new Event(make(),"Full Moon Pizza","Place", "Person McNotPersonFace"));
    		dummyTree.insertEvent(new Event(make(),"Full Moon Pizza","Place", "Fatso McNotPersonFace"));
    		dummyTree.insertLocation(new Event(make(), "Pugsley's Pizza", "Place"));
    		dummyTree.insertEvent(new Event(make(), "Pugsley's Pizza ","Contact", "Person McPersonface"));
    		dummyTree.insertEvent(new Event(make(),"Pugsley's Pizza ","Contact", "Fatso McPersonface"));
    		dummyTree.insertLocation(new Event(make(), "Full Moon Pizza", "Place"));
    		dummyTree.insertEvent(new Event(make(),"Full Moon Pizza","Place", "Person McNotPersonFace"));
    		dummyTree.insertEvent(new Event(make(),"Full Moon Pizza","Place", "Fatso McNotPersonFace")); 
        */
//    		formatData(dummyTree.root);*/
//    		EventTree loaded = new EventTree();
    		String save1 = "save1";

    		String open = "open";
    		/*loaded = Load(getApplication().getApplicationContext(), save1, open ); */

    		Store(getApplication().getApplicationContext(), theTree, save1, open);

//    		Store(getApplication().getApplicationContext(), theTree, save1, open);
    		
    		Intent dialogIntent = new Intent(getBaseContext(), FinalActivity.class);
    		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    		dialogIntent.putStringArrayListExtra("OutputStrings", outputStrings);  
    		getApplication().startActivity(dialogIntent); 		
    		
    	}
    }
    
   
    
    
   public EventTree Load (Context context, String prefName, String key)
    {
    	SharedPreferences settings;
    	
    	EventTree tree = new EventTree();
    	
    	//get context 
    	settings = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    	
    	//key exist 
    	if (settings.contains(key))
    	{
    		//deserialize 
    		String jsonTree = settings.getString(key, null);
    		Gson gson = new Gson();
    		
    		tree = gson.fromJson(jsonTree, EventTree.class);    		
    	}
    	else 
    	{
    		return null;
    	}
    	
    	return (EventTree) tree;
    	
    }
    
   
   String make()
   {
	   Calendar calendar = Calendar.getInstance();
	   int hour = calendar.get(Calendar.HOUR);
	   int minute = calendar.get(Calendar.MINUTE);
	   int pm = calendar.get(Calendar.AM_PM);   
	   
	   SimpleDateFormat df = new SimpleDateFormat("hh:mm");
		
	   Date mTime = new GregorianCalendar( 0, 0, 0, hour, minute).getTime();
	   
	   
	   
	   String time = df.format(mTime).toString()+am_pm(pm);
	   
	   return time;
   }
   
   String am_pm (int pm)
   {
	   	if (pm == 1)
	   	{
	   		return " PM";
	   	}
	   	else
	   	{
	   		return " AM";
	   	}
   }
   
   Boolean timer(int currentH, int currentM )
   {
	  
	   if ( currentH >= tHour)
	   {
		   if (currentH == tHour)
		   {
			   if(currentM >= tMinute)
				   return false;
			   else 
				   return true;
		   }
		   else 
			   return false;
	   }	   
	   else 
		   return true;
   }
}