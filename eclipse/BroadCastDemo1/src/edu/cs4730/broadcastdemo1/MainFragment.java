package edu.cs4730.broadcastdemo1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/**
 * This is code demo to send two different custom intents.
 * 
 * 
 */
public class MainFragment extends Fragment {

	String TAG= "MainFragment";
	
	public MainFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View myView = inflater.inflate(R.layout.fragment_main, container, false);
		
		//setup button to send an intent for static registered receiver.
		myView.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainActivity.ACTION1);
				getActivity().sendBroadcast(i);
			}
		});
		
		//setup button to send an intent for dynamic registered receiver, which is registered in MainActivity.
		myView.findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainActivity.ACTION2);
				getActivity().sendBroadcast(i);
				Log.v(TAG, "Should have sent the broadcast.");
			}
		});
		return myView;
	}

}
