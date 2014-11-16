package fordhamcss.pandorakey4;

import java.util.List;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
//    int counter = 0;
//    URL[] urls=new URL[5];
        
//    static final int UPDATE_INTERVAL = 1000;
//    private Timer timer = new Timer();

	
	String CurrentLocation = "No Value";
	
	
	
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
		
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

		/* Use the LocationManager class to obtain GPS locations */
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		// below updates on time interval (mill seconds) AND location (meters)
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
				 1000, 10, mlocListener);
		
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
    
    public class MyLocationListener implements LocationListener {
		
		@Override
		public void onLocationChanged(Location loc) {
		double lat = loc.getLatitude();
		double lon = loc.getLongitude();
		
		//String Text = "My current location is: " + "Latitud = "
		//+ loc.getLatitude() + "Longitud = " + loc.getLongitude();
		
		String Text = getCompleteAddressString(lat, lon);
		
		
		Toast.makeText( getApplicationContext(),Text, Toast.LENGTH_SHORT).show();
		
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
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		
        //if (timer != null){
        //    timer.cancel();
        //}
        
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}
}