package edu.cs4730.broadcastnoti;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


public class MainActivity extends ActionBarActivity {
	public static final String ACTION = "edu.cs4730.bcr.noti";
	
	MainFragment mFragment = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//check see if there is data in the bundle, ie launched from a notification!
		String info = "Nothing";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			info = extras.getString("mytype");
			if (info == null) {info = "still nothing"; }  //something wrong here.
		}

		Log.v("MainActivity", "info:"+info);
		
		mFragment = new MainFragment(info);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, mFragment).commit();
		}

	}
	
	public void setalarm(){
		int NotID = 1;
		
		//---use the AlarmManager to trigger an alarm---
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		//---get current date and time---
		Calendar calendar = Calendar.getInstance();
		//---sets the time for the alarm to trigger in 2 minutes from now---
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) +2);
		calendar.set(Calendar.SECOND, 0);
		
		
		//---PendingIntent to launch receiver when the alarm triggers-
		//Intent notificationIntent = new Intent(getApplicationContext(), MyReceiver.class);
		Intent notificationIntent = new Intent(MainActivity.ACTION);
		notificationIntent.putExtra("NotifID", NotID);
		
		//Note there is only one difference in this code from the notificationdemo code.  and it's
		//getBroadcast, instead getActivity..
		PendingIntent contentIntent = PendingIntent.getBroadcast(MainActivity.this, NotID, notificationIntent, 0);
		Log.i("MainFragment", "Set alarm, I hope");
		//---sets the alarm to trigger---
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), contentIntent);
		
		//sendBroadcast(notificationIntent);//let's see if it works... without the alarm.
		finish();  //exit the app.
	}
}
