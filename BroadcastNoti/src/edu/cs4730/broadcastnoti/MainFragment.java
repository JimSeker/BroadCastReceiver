package edu.cs4730.broadcastnoti;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * This sets an alarm and then exits.   If this was launched from an alarm/receiver, then
 * hopefully the text fill is set with information.
 * 
 */
public class MainFragment extends Fragment {

	TextView logger = null;
	String myText = "";
	
	public MainFragment(String item) {
		// Required empty public constructor
		myText = item;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View myView= inflater.inflate(R.layout.fragment_main, container, false);

		logger = (TextView) myView.findViewById(R.id.textView1);
		logger.setText(myText);
		//setup button to send an intent for static registered receiver.
		myView.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).setalarm();
				//getActivity().finish();
			}
		});

		return myView;
	}
}
