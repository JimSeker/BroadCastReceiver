package edu.cs4730.broadcastboot;

import java.util.Random;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.os.AsyncTask;

public class MyService extends IntentService {
	
	private NotificationManager mManager;
	
    public MyService() {
        super("MyServiceName");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("MyService", "About to execute MyTask");
        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Log.d("MyService - MyTask", "Calling doInBackground within MyTask");
            int notifID = 1;
            String info = "error"; //changed below.
            Random myRandom = new Random();
            mManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            
            //creating an infinite asysnctask here, which is likely a bad idea.
            //it will send out a notification with a "headline" and intent to launch the mainactivity.
            while(true) {

            	//---PendingIntent to launch activity if the user selects
            	// the notification---
            	Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            	//random notification
            	switch (myRandom.nextInt(3)) {
            	case 0:
            		info ="Iphone6 is very bendy!";
            		break;
            	case 1:
            		info = "Blackberry makes a square phone!";
            		break;
            	case 2:
            		info = "Nexus 6 phone is huge at 5.9 inches!";
            		break;
            	default:
            			info = "No new headline.";
            	}
            	
            	notificationIntent.putExtra("mText", info);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
            	PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), notifID, notificationIntent, 0);
            	
                //create the notification
                Notification notif = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis()) //When the event occurred, now, since noti are stored by time.
                .setContentTitle("New headline!") //Title message top row.
                .setContentText(info) //message when looking at the notification, second row
                .setContentIntent(contentIntent) //what activity to open.
                .setAutoCancel(true) //allow auto cancel when pressed.
                .build(); //finally build and return a Notification.
                //Show the notification
                mManager.notify(notifID, notif);
            	try {
					Thread.sleep(6000*5);  //testing, 6000 milliseconds * 60 minutes, ie one hour.  but testing is 60*5.
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

        }
    }
}
