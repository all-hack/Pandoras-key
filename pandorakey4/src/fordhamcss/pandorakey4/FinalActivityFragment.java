package fordhamcss.pandorakey4;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class FinalActivityFragment extends Fragment{
	ArrayList<String> outputStrings;
	ListView mListView;
	List<Map<String, String>> returnStrings = new ArrayList<Map<String, String>>();
	EventTree loaded = new EventTree();
	String save1 = "save1";
	String open = "open";

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setRetainInstance(true);

		ArrayList<String> moutputStrings = new ArrayList<String>();		
		loaded = Load(getActivity().getApplication(), save1, open );
		if (check(loaded.root) == false)
		{
			getOutput(loaded.root);			
			for (int x=0; x<returnStrings.size(); x++)
				moutputStrings.add(generateOutputString(returnStrings.get(x)));
			

		}
		else
		{			
			moutputStrings.add(loaded.root.theEvent.getFirst().getPlace());
		}
		
		outputStrings = moutputStrings;  

        setupAdapter();
    }

    
    public Boolean check(EventNode t)
    {
    	if (t.theEvent.getFirst().getTime() == null)
    		return true;
    	else
    	return false;
    	
    }
    
    
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.activity_final, container, false);
		mListView = (ListView) v.findViewById(R.id.listView);

        setupAdapter();

		return v;
	}
    
	void setupAdapter() 
	{
		if (getActivity() == null || mListView == null)
            return;
		if (outputStrings != null)
			mListView.setAdapter(new ItemAdapter(outputStrings));
		else
			mListView.setAdapter(new ItemAdapter(new ArrayList<String>()));
	}

    //Sets up text views
    private class ItemAdapter extends ArrayAdapter<String>
    {
        public ItemAdapter(ArrayList<String> outputStrings) {
            super(getActivity(), 0, outputStrings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_layout, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.item_layout_textView);
            
            textView.setText(outputStrings.get(position));
            

            return convertView;
        }

    }
    
   
    
	
    //Formats raw data into workable structure
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

    //Generates the string to be displayed on screen
    public String generateOutputString(Map<String, String> serviceOutput)
    {
    	String outputString = null;
    	String username = "Stephen";
    	
    	if (serviceOutput.get("kind") == "location")
    		outputString = "You were at " + serviceOutput.get("locationName") + " at " + serviceOutput.get("timestamp");

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
    	
    	
		
		
    	
		return outputString;
    }

    //Loads data into tree
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
    		return null;

    	return (EventTree) tree;
    	
    }

    //Creates timestamp
	String make()
	   {
		   Calendar calendar = Calendar.getInstance();
		   int hour = calendar.get(Calendar.HOUR);
		   int minute = calendar.get(Calendar.MINUTE);
		   int pm = calendar.get(Calendar.AM_PM);   
		   
		   String time = hour+":"+minute+" "+am_pm(pm);
		   
		   return time;
	   }
	   
	   String am_pm (int pm)
	   {
		   	if (pm == 1)
		   		return "PM";
		   	else
		   		return "AM";
	   }

}