package fordhamcss.pandorakey4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FinalActivityFragment extends Fragment{
	List<Map<String, String>> returnStrings;
	ListView mListView;
		
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		returnStrings = new ArrayList<Map<String, String>>();
		
		EventTree tree = genDummyTree();
		getOutput(tree.root);
        
        setupAdapter();
    }

    public List<Map<String, String>> genDummyStrings()
    {
    	List<Map<String, String>> serviceAll = new ArrayList<Map<String, String>>();
		
    	Map<String, String> serviceOutput1 = new HashMap<String, String>();
    	serviceOutput1.put("kind" , "contact");
    	serviceOutput1.put("name" , "Stephen Rubio");
    	serviceOutput1.put("timestamp" , "2");
    	
    	Map<String, String> serviceOutput2 = new HashMap<String, String>();
    	serviceOutput2.put("kind" , "call");
    	serviceOutput2.put("from" , "Sam");
    	serviceOutput2.put("to" , "Sam");
    	serviceOutput2.put("missed" , "true");
    	serviceOutput2.put("timestamp" , "5");
    	
    	Map<String, String> serviceOutput3 = new HashMap<String, String>();
    	serviceOutput3.put("kind" , "text");
    	serviceOutput3.put("from" , "Stephen");
    	serviceOutput3.put("to" , "Sam");
    	serviceOutput3.put("timestamp" , "4");
    	
    	Map<String, String> serviceOutput4 = new HashMap<String, String>();
    	serviceOutput4.put("kind" , "location");
    	serviceOutput4.put("geolocation" , "wow such numbers");
    	serviceOutput4.put("locationName" , "Pugsley's Pizza");
    	serviceOutput4.put("timestamp" , "1");
    	
    	serviceAll.add(serviceOutput1);
    	serviceAll.add(serviceOutput2);
    	serviceAll.add(serviceOutput3);
    	serviceAll.add(serviceOutput4);

    	return serviceAll;
    }

    public void getOutput(EventNode t)
    {
    	Map<String, String> locOutput = new HashMap<String, String>();
    	locOutput.put("kind" , "location");
    	locOutput.put("locationName" ,  t.theEvent.get(0).place);
    	locOutput.put("timestamp" , "1");
    	returnStrings.add(locOutput);
    	
	    if(t.left != null) {
			for(int i=0; i < t.left.theEvent.size(); i++) {
		    	Map<String, String> tempServiceOutput = new HashMap<String, String>();
		    	tempServiceOutput.put("kind" , "contact");
		    	tempServiceOutput.put("name" , t.left.theEvent.get(i).contactName);
		    	tempServiceOutput.put("timestamp" , "2");
		    	returnStrings.add(tempServiceOutput);
			}
		}
		if(t.right != null) {
			getOutput(t.right);
		}
    }
    
    public EventTree genDummyTree()
    {
    	EventTree daTree = new EventTree();
    	
    	daTree.insertLocation(new PlaceEvent(5, "Pugsley's Pizza"));
    	daTree.insertEvent(new ContactEvent(6, "location", "First Contact"));
    	daTree.insertEvent(new ContactEvent(7, "location", "Second Contact"));

    	daTree.insertLocation(new PlaceEvent(8, "Full Moon Pizza"));
    	daTree.insertEvent(new ContactEvent(9, "Another Location", "Third Contact"));
    	daTree.insertEvent(new ContactEvent(10, "Another Location", "Fourth Contact"));
    	
    	return daTree;
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
    	
    	else if (serviceOutput.get("kind") == "photo") //Not Working
    	{
    		
    	}
    			
		return outputString;
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
		
		if (returnStrings != null)
			mListView.setAdapter(new ItemAdapter(returnStrings));
		else
			mListView.setAdapter(null);
	}

	private class ItemAdapter extends ArrayAdapter<Map<String, String>> 
	{
		public ItemAdapter(List<Map<String, String>> returnStrings) {
			super(getActivity(), 0, returnStrings);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {		
			if (convertView == null) 
				convertView = getActivity().getLayoutInflater().inflate(R.layout.item_layout, parent, false);

			if (returnStrings.get(position).get("kind") == "photo")
			{
				ImageView imageView = (ImageView) convertView.findViewById(R.id.item_layout_imageView);
				imageView.setImageResource(R.drawable.ic_launcher);
				
				TextView textView = (TextView) convertView.findViewById(R.id.item_layout_textView);
				textView.setText(returnStrings.get(position).get("timestamp"));
			}
			
			else //Text
			{
				TextView textView = (TextView) convertView.findViewById(R.id.item_layout_textView);
				textView.setText(generateOutputString(returnStrings.get(position)));
			}
			
			return convertView;
		}
		
	}
}
