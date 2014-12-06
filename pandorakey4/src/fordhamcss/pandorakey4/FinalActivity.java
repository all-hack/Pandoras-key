package fordhamcss.pandorakey4;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

public class FinalActivity extends SingleFragmentActivity {
	public ArrayList<String> outputStrings;

    @Override
    public Fragment createFragment() {
    	outputStrings = getIntent().getStringArrayListExtra("OutputStrings");
        return new FinalActivityFragment(outputStrings);
    }
}
