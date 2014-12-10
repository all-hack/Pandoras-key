package fordhamcss.pandorakey4;

import java.util.ArrayList;
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
//    int counter = 0;
//    URL[] urls=new URL[5];
        
//    static final int UPDATE_INTERVAL = 1000;
//    private Timer timer = new Timer();
	
	
	// initial contact list is saved
	Cursor initialContactList;
	
	String CurrentLocation = "Null";
	LinkedList<String> CurrLocation = new LinkedList<String>();
	LinkedList<String> CurrContact = new LinkedList<String>();
	
	EventTree theTree = new EventTree();
	List<Map<String, String>> returnStrings = new ArrayList<Map<String, String>>();

	Thread myThread;
	
	
	/*
	daTree = new EventTree();

	daTree.insertLocation(new PlaceEvent(5, "location", "Pugsley's Pizza"));
	daTree.insertEvent(new ContactEvent(6, "location", "First Contact"));
	daTree.insertEvent(new ContactEvent(7, "location", "Second Contact"));

	daTree.insertLocation(new PlaceEvent(8, "Another Location", "Full Moon Pizza"));
	daTree.insertEvent(new ContactEvent(9, "Another Location", "Third Contact"));
	daTree.insertEvent(new ContactEvent(10, "Another Location", "Fourth Contact"));
	*/
	//System.out.println(daTree.root.theEvent);
	//System.out.println(daTree.root.theEvent);
	
	
	/* An abstract method we must implement. 
	 The onBind() method enables you to bind an activity to a service. 
	 This in turn enables an activity to directly access members and methods inside a service. 
	 For now, you simply return a null for this method. 
	 */
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

	/*
	The onStartCommand() method is called when you start the service explicitly using the startService() method. 
	This method signifies the start of the service, and you code it to do the things you need to do for your service. 
	In this method, you returned the constant START_STICKY so that the service will continue to run until it is explicitly stopped.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly  stopped, so return sticky.
		
    	ContentResolver cr = getContentResolver();
    	String[] array = new String[0];
    	initialContactList = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, array, null);
		
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

		/* Use the LocationManager class to obtain GPS locations */
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		// below updates on time interval (mill seconds) AND location (meters)
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
				5 * 1000, 0, mlocListener);
		
/*
		for (int i=0; i<urls.length; i++) {
			try {
				urls[i] = new URL("http://www.amazon.com/") ;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		 */
	//	new DoBackgroundTask().execute(urls);		// Run a job in a separate thread specified by an AsyncTask. 
	//	doSomethingRepeatedly();		
		
		return START_STICKY;
	}
    
	/*
    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate( new TimerTask() {
            public void run() {
                Log.d("MyService", String.valueOf(++counter));
            }
        }, 0, UPDATE_INTERVAL );
        //http://docs.oracle.com/javase/7/docs/api/java/util/Timer.html#scheduleAtFixedRate(java.util.TimerTask,%20long,%20long)
        //In fixed-rate execution, each execution is scheduled relative to the scheduled execution time of the initial execution. 
        //If an execution is delayed for any reason (such as garbage collection or other background activity), 
        //two or more executions will occur in rapid succession to "catch up."
    }*/
	
	/*
    private int DownloadFile(URL url) {
        try {
            //---simulate taking some time to download a file---
            Thread.sleep(5000);
        } catch (InterruptedException e) {
             e.printStackTrace();
        }
        //---return an arbitrary number representing 
        // the size of the file downloaded---
        return 100;
    }*/

    /*
    private class DoBackgroundTask extends AsyncTask<URL, Integer, Long> {
    	
        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalBytesDownloaded = 0;
            for (int i = 0; i < count; i++) {
                totalBytesDownloaded += DownloadFile(urls[i]);
                //---calculate percentage downloaded and
                // report its progress---
                publishProgress((int) (((i+1) / (float) count) * 100));
            }
            return totalBytesDownloaded;
        }*/

        /*
        protected void onProgressUpdate(Integer... progress) {
            Log.d("MyService",  String.valueOf(progress[0]) + "% downloaded");
            
            //Toast.makeText(getBaseContext(),   String.valueOf(progress[0]) + "% downloaded", Toast.LENGTH_LONG).show();
            Toast.makeText(MyService.this,   String.valueOf(progress[0]) + "% downloaded", Toast.LENGTH_LONG).show();
           
        }*/
/*
        protected void onPostExecute(Long result) {
        	
            //Toast.makeText(getBaseContext(),  "Downloaded " + result + " bytes", Toast.LENGTH_LONG).show(); //works
        		Toast.makeText(MyService.this,  "Downloaded " + result + " bytes", Toast.LENGTH_LONG).show(); //works
        	
            Log.d("MyService", getBaseContext().toString());
            Log.d("MyService", MyService.this.toString());
            
            stopSelf();  // a method of the surrounding service instance. 
        }
    }    /*
  
	/*
		The onDestroy() method is called when the service is stopped using the stopService() method. 
		This is where you clean up the resources used by your service.
	 */
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
    		int time = (int) (System.currentTimeMillis());

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
		double lat = loc.getLatitude();
		double lon = loc.getLongitude();
		
		//String Text = "My current location is: " + "Latitud = "
		//+ loc.getLatitude() + "Longitud = " + loc.getLongitude();
		
		
		String Text = getCompleteAddressString(lat, lon);
		
		if (CurrentLocation == "Null") {
			CurrentLocation = Text;
			Toast.makeText( getApplicationContext(), CurrentLocation, Toast.LENGTH_SHORT).show();
			CurrLocation.add(CurrentLocation);

			int time = (int)(System.currentTimeMillis());
			Event newPlace = new Event(time, CurrentLocation, "Place");
			theTree.insertLocation(newPlace);
		}

		checkContacts();

		if(CurrentLocation != Text )
		{
			CurrentLocation = Text;		
			//	Toast.makeText( getApplicationContext(), CurrentLocation, Toast.LENGTH_SHORT).show();
			CurrLocation.add(CurrentLocation);

			int time = (int)(System.currentTimeMillis());
			Event newPlace = new Event(time, CurrentLocation, "Place");
			theTree.insertLocation(newPlace);
		}
	
		/*else if(CurrentLocation == Text)
		{
			Toast.makeText(getApplicationContext(), "you are in the same place", Toast.LENGTH_SHORT).show();
		}*/
		
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
    
   /* public void getOutput(EventNode t)
    {
    	Map<String, String> locOutput = new HashMap<String, String>();
    	locOutput.put("kind" , "location");
    	locOutput.put("locationName" ,  t.theEvent.get(0).place);
    	locOutput.put("timestamp" , convertToString(t.theEvent.get(0).getTime()));
    	returnStrings.add(locOutput);
    	
	    if(t.left != null) {
			for(int i=0; i < t.left.theEvent.size(); i++) {
		    	Map<String, String> tempServiceOutput = new HashMap<String, String>();
		    	tempServiceOutput.put("kind" , "contact");
		    	tempServiceOutput.put("name" , t.left.theEvent.get(i).getContactName());
		    	tempServiceOutput.put("timestamp" , convertToString(t.left.theEvent.get(i).getTime()));
		    	returnStrings.add(tempServiceOutput);
			}
		}
		if(t.right != null) {
			getOutput(t.right);
		}
    }
    
    public String convertToString(long t)
    {
    	if (t == 1)
    		return "1";
    	else
    	if (t == 2)
    		return "2";
    	else
		if (t == 3)
    		return "3";
    	else
    	if (t == 4)
    		return "4";
    	else    		
    	if (t == 5)
    		return "5";
    	else
    	if (t == 6)
    		return "6";
    	else
		if (t == 7)
     		return "7";
    	else
    	if (t == 8)
    		return "8";
    	else    		
    	if (t == 9)
    		return "9";
    	else
    	if (t == 10)
    		return "10";
    	else
		if (t == 11)
    		return "11";
    	else
    	if (t == 12)
    		return "12";
    	else 
    		return null;
    	
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
    	
		Toast.makeText(this, outputString, Toast.LENGTH_LONG).show();		

    	
		return outputString;
    }*/
    
    @Override
	public void onDestroy() {
		super.onDestroy();
		
		myThread = new Thread(new MyThread());
		myThread.start();
		
		//For development, uses dummy tree
/*		EventTree dummyTree = new EventTree();
		dummyTree.insertLocation(new Event(5, "Pugsley's Pizza", "Place"));
		dummyTree.insertEvent(new Event(6, "Pugsley's Pizza ","Contact", "Person McPersonface"));
		dummyTree.insertEvent(new Event(7,"Pugsley's Pizza ","Contact", "Fatso McPersonface"));
		dummyTree.insertLocation(new Event(8, "Full Moon Pizza", "Place"));
		dummyTree.insertEvent(new Event(9,"Full Moon Pizza","Place", "Person McNotPersonFace"));
		dummyTree.insertEvent(new Event(10,"Full Moon Pizza","Place", "Fatso McNotPersonFace"));
    	
//		getOutput(dummyTree.root);
//		EventTree loaded = new EventTree();
		String save1 = "save1";
		String open = "open";
		
		Store(getApplication().getApplicationContext(), dummyTree, save1, open); */
//		loaded = Load(this, save1, open );
		
	/*	loaded.insertLocation(new Event(5, "Da Club", "Place"));
		loaded.insertEvent(new Event(6, "Da Club","Contact", "Yo MAma"));
		loaded.insertEvent(new Event(7,"Da Club","Contact", "HolyShit ItWerks"));*/
		
		
		//For production, uses actual recorded data
//		getOutput(theTree.root);
//		getOutput(loaded.root);
	//	ArrayList<String> outputStrings = new ArrayList<String>();		
		//for (int x=0; x<returnStrings.size(); x++)
			//outputStrings.add(generateOutputString(returnStrings.get(x)));
		
		
		
		
		/*
		Intent dialogIntent = new Intent(getBaseContext(), FinalActivity.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		dialogIntent.putStringArrayListExtra("OutputStrings", outputStrings);  
		getApplication().startActivity(dialogIntent);		*/
		
//		Toast.makeText(this, "Stopped Recording", Toast.LENGTH_SHORT).show();		
		
		
	/*	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(buffer);
			oos.writeObject(theTree);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] rawData = buffer.toByteArray();
		FileOutputStream outputStream;
		try {
			outputStream = openFileOutput("File", Context.MODE_PRIVATE);
			try {
				outputStream.write(rawData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/	
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
    		EventTree dummyTree = new EventTree();
    		dummyTree.insertLocation(new Event(5, "Pugsley's Pizza", "Place"));
    		dummyTree.insertEvent(new Event(6, "Pugsley's Pizza ","Contact", "Person McPersonface"));
    		dummyTree.insertEvent(new Event(7,"Pugsley's Pizza ","Contact", "Fatso McPersonface"));
    		dummyTree.insertLocation(new Event(8, "Full Moon Pizza", "Place"));
    		dummyTree.insertEvent(new Event(9,"Full Moon Pizza","Place", "Person McNotPersonFace"));
    		dummyTree.insertEvent(new Event(10,"Full Moon Pizza","Place", "Fatso McNotPersonFace"));
    		dummyTree.insertLocation(new Event(5, "Pugsley's Pizza", "Place"));
    		dummyTree.insertEvent(new Event(6, "Pugsley's Pizza ","Contact", "Person McPersonface"));
    		dummyTree.insertEvent(new Event(7,"Pugsley's Pizza ","Contact", "Fatso McPersonface"));
    		dummyTree.insertLocation(new Event(8, "Full Moon Pizza", "Place"));
    		dummyTree.insertEvent(new Event(9,"Full Moon Pizza","Place", "Person McNotPersonFace"));
    		dummyTree.insertEvent(new Event(10,"Full Moon Pizza","Place", "Fatso McNotPersonFace"));
    		dummyTree.insertLocation(new Event(5, "Pugsley's Pizza", "Place"));
    		dummyTree.insertEvent(new Event(6, "Pugsley's Pizza ","Contact", "Person McPersonface"));
    		dummyTree.insertEvent(new Event(7,"Pugsley's Pizza ","Contact", "Fatso McPersonface"));
    		dummyTree.insertLocation(new Event(8, "Full Moon Pizza", "Place"));
    		dummyTree.insertEvent(new Event(9,"Full Moon Pizza","Place", "Person McNotPersonFace"));
    		dummyTree.insertEvent(new Event(10,"Full Moon Pizza","Place", "Fatso McNotPersonFace"));
        	
//    		getOutput(dummyTree.root);
//    		EventTree loaded = new EventTree();
    		String save1 = "save1";
    		String open = "open";
    		
    		Store(getApplication().getApplicationContext(), dummyTree, save1, open);
    		
    		Intent dialogIntent = new Intent(getBaseContext(), FinalActivity.class);
    		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    		dialogIntent.putStringArrayListExtra("OutputStrings", outputStrings);  
    		getApplication().startActivity(dialogIntent);		
    	}
    }
    
    
   /* public EventTree Load (Context context, String prefName, String key)
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
    	
    }*/
    
    
    
    
    
    
    
    
    
    
    
}