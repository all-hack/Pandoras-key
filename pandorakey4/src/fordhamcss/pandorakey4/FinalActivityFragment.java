package fordhamcss.pandorakey4;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

public class FinalActivityFragment extends Fragment{
	ArrayList<String> outputStrings;
	ListView mListView;
	
	public FinalActivityFragment (ArrayList<String> importedReturnStrings)
	{
		super();
		outputStrings = importedReturnStrings;
	}

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setRetainInstance(true);

        setupAdapter();
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.activity_final, container, false);
		mListView = (ListView) v.findViewById(R.id.listView);
        mListView.addHeaderView(inflater.inflate(R.layout.button_layout, null));

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
}
