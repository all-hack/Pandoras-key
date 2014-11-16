package fordhamcss.pandorakey4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FinalActivityFragment extends Fragment{
	List<Map<String, String>> returnStrings;
	ListView mListView;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
        returnStrings = generateStrings();
        sortStrings();
        
        setupAdapter();
    }
    
    public void sortStrings()
    {    	
        for (int i = 0; i < returnStrings.size() - 1; i++) {
            int minPos = i;
            for (int j = i + 1; j < returnStrings.size(); j++) {
                if (Integer.parseInt(returnStrings.get(j).get("timestamp")) < Integer.parseInt(returnStrings.get(minPos).get("timestamp"))) {
                    minPos = j;
                }
            }
            Collections.swap(returnStrings, minPos, i);
        }
    }
    
    public List<Map<String, String>> generateStrings()
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
