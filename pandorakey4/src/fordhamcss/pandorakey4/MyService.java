package fordhamcss.pandorakey4;

import android.app.Service; 
import android.content.Intent; 
import android.os.IBinder; 
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    int counter = 0;
    URL[] urls=new URL[5];
        
    static final int UPDATE_INTERVAL = 1000;
    private Timer timer = new Timer();

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

		for (int i=0; i<urls.length; i++) {
			try {
				urls[i] = new URL("http://www.amazon.com/") ;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		 
		new DoBackgroundTask().execute(urls);		// Run a job in a separate thread specified by an AsyncTask. 
	//	doSomethingRepeatedly();		
		
		return START_STICKY;
	}
    
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
    }
	
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
    }

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
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.d("MyService",  String.valueOf(progress[0]) + "% downloaded");
            
            //Toast.makeText(getBaseContext(),   String.valueOf(progress[0]) + "% downloaded", Toast.LENGTH_LONG).show();
            Toast.makeText(MyService.this,   String.valueOf(progress[0]) + "% downloaded", Toast.LENGTH_LONG).show();
           
        }

        protected void onPostExecute(Long result) {
        	
            //Toast.makeText(getBaseContext(),  "Downloaded " + result + " bytes", Toast.LENGTH_LONG).show(); //works
        		Toast.makeText(MyService.this,  "Downloaded " + result + " bytes", Toast.LENGTH_LONG).show(); //works
        	
            Log.d("MyService", getBaseContext().toString());
            Log.d("MyService", MyService.this.toString());
            
            stopSelf();  // a method of the surrounding service instance. 
        }
    }    
  
	/*
		The onDestroy() method is called when the service is stopped using the stopService() method. 
		This is where you clean up the resources used by your service.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		
        if (timer != null){
            timer.cancel();
        }
        
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}
}