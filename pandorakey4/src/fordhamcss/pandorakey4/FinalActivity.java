package fordhamcss.pandorakey4;

import android.support.v4.app.Fragment;

public class FinalActivity extends SingleFragmentActivity {
	
    @Override
    public Fragment createFragment() {
        return new FinalActivityFragment();
    }
    
}
