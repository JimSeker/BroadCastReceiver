package edu.cs4730.broadcastboot;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


public class MainActivity extends ActionBarActivity {

	MainFragment mFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String info = "Nothing";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			info = extras.getString("mText"); 
		}
		mFragment = new MainFragment(info);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, mFragment).commit();
		}
		//if my service is not running, start it.  for testing, before rebooting emulators... so much fun...
		if (isMyServiceRunning(MyService.class)) {
			//good it's running.
		} else {
			//not running, start it.
			Intent i = new Intent(getBaseContext(), MyService.class);
			startService(i);
		}
		
		Log.v("Main", "started up: " + info);
	}

	
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		String info = "Nothing";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			info = extras.getString("mText"); 
		}
		Log.v("Main", "New intent" + info);
		
		mFragment.settext(info);
	}
}
