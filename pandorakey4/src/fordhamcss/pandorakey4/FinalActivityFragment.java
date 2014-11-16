package fordhamcss.pandorakey4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FinalActivityFragment extends Fragment{
	List<String> returnStrings;
	ListView mListView;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		setRetainInstance(true);

        returnStrings = generateStrings();
//        System.out.println(returnStrings.get(0));
//        System.out.println(returnStrings.get(1));
//        System.out.println(returnStrings.get(2));
//        System.out.println(returnStrings.get(3));
        
        setupAdapter();
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
		{
			return;
		}
		
		if (returnStrings != null) {
			System.out.println("Hit");
			mListView.setAdapter(new ItemAdapter(returnStrings));
		} else {
			mListView.setAdapter(null);
		}
	}
	
    public List<String> generateStrings()
    {
    	List<Map<String, String>> serviceAll = new ArrayList<Map<String, String>>();
		
    	Map<String, String> serviceOutput1 = new HashMap<String, String>();
    	serviceOutput1.put("kind" , "contact");
    	serviceOutput1.put("name" , "Stephen Rubio");
    	serviceOutput1.put("timestamp" , "5:30 PM November 15th, 2014");
    	
    	Map<String, String> serviceOutput2 = new HashMap<String, String>();
    	serviceOutput2.put("kind" , "call");
    	serviceOutput2.put("from" , "Sam");
    	serviceOutput2.put("to" , "Sam");
    	serviceOutput2.put("missed" , "true");
    	serviceOutput2.put("timestamp" , "5:30 PM November 15th, 2014");
    	
    	Map<String, String> serviceOutput3 = new HashMap<String, String>();
    	serviceOutput3.put("kind" , "text");
    	serviceOutput3.put("from" , "Stephen");
    	serviceOutput3.put("to" , "Sam");
    	serviceOutput3.put("timestamp" , "5:30 PM November 15th, 2014");
    	
    	Map<String, String> serviceOutput4 = new HashMap<String, String>();
    	serviceOutput4.put("kind" , "location");
    	serviceOutput4.put("geolocation" , "wow such numbers");
    	serviceOutput4.put("timestamp" , "5:30 PM November 15th, 2014");
    	
    	serviceAll.add(serviceOutput1);
    	serviceAll.add(serviceOutput2);
    	serviceAll.add(serviceOutput3);
    	serviceAll.add(serviceOutput4);
    	
    	List<String> returnItems = new ArrayList<String>();
    	returnItems.add(getOutput(serviceAll.get(0)));
    	returnItems.add(getOutput(serviceAll.get(1)));
    	returnItems.add(getOutput(serviceAll.get(2)));
    	returnItems.add(getOutput(serviceAll.get(3)));

    	return returnItems;
    }
	
    public String getOutput(Map<String, String> serviceOutput)
    {
    	String outputString = null;
    	String username = "Stephen";
    	
    	if (serviceOutput.get("kind") == "location") //Not working, wait on others to get API working
    	{
    		//Dump google maps API
    		outputString = serviceOutput.get("geolocation");
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
    		//Future feature: add link to contact
    		outputString = "Added new contact, " + serviceOutput.get("name") + " at " + serviceOutput.get("timestamp"); 
    	}
    	
    	else if (serviceOutput.get("kind") == "photo") //Not Working
    	{
    		
    	}
    			
		return outputString;
    }

	private class ItemAdapter extends ArrayAdapter<String> {

		public ItemAdapter(List<String> returnStrings) {
			super(getActivity(), 0, returnStrings);  //getActivity() is the surrounding Fragment instance's method.
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {		
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.item_layout, parent, false);
			}

			TextView textView = (TextView) convertView.findViewById(R.id.item_layout_textView);
			textView.setText(returnStrings.get(position));

			return convertView;
		}
	}
}
