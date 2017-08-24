package edu.cs4730.broadcastnoti;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/*
 * This is implementation of the an earlier example, but new uses a receiver instead of a activity
 * so a activity->alarm manager -> receiver -> notification -> activity
 * 
 */

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.v("myReceiver", "received an intent");
		if (intent.getAction().equals(MainActivity.ACTION)){  //my custom intent
			//---get the notification ID for the notification;
			// passed in by the MainActivity---
			int notifID = intent.getExtras().getInt("NotifID");
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			//---PendingIntent to launch activity if the user selects
			// the notification---
			Intent notificationIntent = new Intent(context, MainActivity.class);
			notificationIntent.putExtra("mytype", "2 minutes later?");
			PendingIntent contentIntent = PendingIntent.getActivity(context, notifID, notificationIntent, 0);
			//create the notification
			Notification notif = new NotificationCompat.Builder(context, MainActivity.id)
			.setSmallIcon(R.drawable.ic_launcher)
			.setWhen(System.currentTimeMillis()) //When the event occurred, now, since noti are stored by time.
			.setContentTitle("Time's up!") //Title message top row.
			.setContentText("This is your alert, courtesy of the AlarmManager") //message when looking at the notification, second row
			.setContentIntent(contentIntent) //what activity to open.
			.setChannelId(MainActivity.id)
			.setAutoCancel(true) //allow auto cancel when pressed.
			.build(); //finally build and return a Notification.
			//Show the notification
			nm.notify(notifID, notif);
		}
	}
}
