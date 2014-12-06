package fordhamcss.pandorakey4;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

public class FinalActivity extends SingleFragmentActivity {
	public ArrayList<String> outputStrings;
    public String startTime = "1";
    public String endTime = "2";

    @Override
    public Fragment createFragment() {
    	outputStrings = getIntent().getStringArrayListExtra("OutputStrings");
        return new FinalActivityFragment(outputStrings);
    }

    public void showPhotos(View view)
    {
        System.out.println("Photos");
//        Intent dialogIntent = new Intent(FinalActivity.this, FinalActivity.class);
//        ArrayList<String> times = new ArrayList<String>();
//        times.add(startTime);
//        times.add(endTime);
//        dialogIntent.putStringArrayListExtra("OutputStrings", times);
//        startActivityForResult(dialogIntent, 0);
    }
    
}
