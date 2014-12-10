package fordhamcss.pandorakey4;

import java.util.ArrayList;
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
import android.widget.Toast;

import com.google.gson.Gson;
//import android.widget.Toast;

public class FinalActivityFragment extends Fragment{
	ArrayList<String> outputStrings;
	ListView mListView;
	List<Map<String, String>> returnStrings = new ArrayList<Map<String, String>>();
	EventTree loaded = new EventTree();
	String save1 = "save1";
	String open = "open";

	/*
	public FinalActivityFragment (ArrayList<String> importedReturnStrings)
	{
		super();
		
	//	loaded = Load(getActivity().getApplication(), save1, open );
/*		getOutput(loaded.root);
		ArrayList<String> moutputStrings = new ArrayList<String>();		
		for (int x=0; x<returnStrings.size(); x++)
			moutputStrings.add(generateOutputString(returnStrings.get(x)));
		outputStrings = moutputStrings; 
		
		
		outputStrings = importedReturnStrings;
	}*/

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setRetainInstance(true);

//		Toast.makeText(getActivity().getApplication(),getActivity().getApplication().toString(), Toast.LENGTH_SHORT).show();		
		loaded = Load(getActivity().getApplication(), save1, open );
		getOutput(loaded.root);
		ArrayList<String> moutputStrings = new ArrayList<String>();		
		for (int x=0; x<returnStrings.size(); x++)
			moutputStrings.add(generateOutputString(returnStrings.get(x)));
		outputStrings = moutputStrings;  
		
		
		
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
		//Toast.makeText(getActivity().getApplication(), "please", Toast.LENGTH_SHORT).show();		

		/*
		loaded = Load(getActivity().getApplication(), save1, open );
		getOutput(loaded.root);
		ArrayList<String> moutputStrings = new ArrayList<String>();		
		for (int x=0; x<returnStrings.size(); x++)
			moutputStrings.add(generateOutputString(returnStrings.get(x)));
		outputStrings = moutputStrings;*/
		
		if (getActivity() == null || mListView == null)
            return;

		
		if (outputStrings != null)
			mListView.setAdapter(new ItemAdapter(outputStrings));
		else
		{
			mListView.setAdapter(new ItemAdapter(new ArrayList<String>()));
		}
	}

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
	
    public void getOutput(EventNode t)
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
	
}
