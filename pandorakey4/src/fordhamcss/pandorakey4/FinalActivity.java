package fordhamcss.pandorakey4;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

public class FinalActivity extends SingleFragmentActivity {
	public ArrayList<String> outputStrings;

    @Override
    public Fragment createFragment() {
//        getActionBar().setIcon(R.drawable.open_chest);
//   	    outputStrings = getIntent().getStringArrayListExtra("OutputStrings");
        return new FinalActivityFragment();
    }
}
