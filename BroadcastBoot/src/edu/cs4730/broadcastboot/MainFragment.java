package edu.cs4730.broadcastboot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Use the {@link MainFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class MainFragment extends Fragment {

	String info = "";
	TextView logger;
	
	public MainFragment() {
		// Required empty public constructor
	}

	public MainFragment(String i) {
		info = i;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
	    View myView =  inflater.inflate(R.layout.fragment_main, container, false);
	    logger = (TextView) myView.findViewById(R.id.textView2);
	    settext(info);
	    return myView;
	}
	
	public void settext(String i) {
		info = i;
		logger.setText(info);

	}

}
