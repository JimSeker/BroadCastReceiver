package edu.cs4730.broadcastdemo2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 */
public class MainFragment extends Fragment {

    int mStatus = 0;
    TextView logger;
    
	public MainFragment() {
		// Required empty public constructor
	}
	public MainFragment(int status) {
		mStatus = status;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View myView = inflater.inflate(R.layout.fragment_main, container, false);
		logger = (TextView) myView.findViewById(R.id.textView1);
		setstat(mStatus);
		return myView;
	}

	public void setstat( int s) {
		mStatus = s;
		String info = "";
		switch (s) {
		case 0:
			info= "\n should not get a zero, unless app just started";
			break;
		case 1:
			info= "\n batter low. should shut down things.";
			break;
		case 2:
			info= "\n battery Okay.  ";
			break;
		case 3:
			info= "\n Power connected, so phone is charging.";
			break;
		case 4:
			info= "\n Power disconnected.";
			break;
		default:
			info= "\n something wrong.";
				
		} 
		logger.setText("status is " + mStatus+ info);
		
		
	}
	
}