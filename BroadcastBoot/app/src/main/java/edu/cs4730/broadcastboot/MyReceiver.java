package edu.cs4730.broadcastboot;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;

/**
 * this receiver get the on boot completed message then starts the service.
 * it's registered in the androidmanifest file.
 */
public class MyReceiver extends BroadcastReceiver {

    String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int NotiID = 1;
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //boot has completed, now time to start our background service.
            Log.wtf(TAG, "Got the boot one!");
            setalarm(context, 2, NotiID);  //NotiID is initialize to 1, which is the start.
        } else if (intent.getAction().equals(MainActivity.ACTION)) {  //my custom intent
            Bundle extras = intent.getExtras();
            if (extras != null) {
                NotiID = extras.getInt("notiid");
            }
            sendNoti(context, NotiID);
            setalarm(context, 2, NotiID + 1);  //use the next notification ID number.
        }
    }


    public void setalarm(Context context, int time, int notiID) {

        //---use the AlarmManager to trigger an alarm---
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //---get current date and time---
        Calendar calendar = Calendar.getInstance();
        //---sets the time for the alarm to trigger in 2 minutes from now---
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + time);
        calendar.set(Calendar.SECOND, 0);


        //---PendingIntent to launch receiver when the alarm triggers-
        //Intent notificationIntent = new Intent(getApplicationContext(), MyReceiver.class);
        Intent notificationIntent = new Intent(MainActivity.ACTION);
        notificationIntent.setPackage("edu.cs4730.broadcastboot"); //in API 26, it must be explicit now.
        notificationIntent.putExtra("notiid", notiID);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, notiID, notificationIntent, 0);
        Log.i(TAG, "Set alarm, I hope");
        //---sets the alarm to trigger---
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), contentIntent);

    }

    public void sendNoti(Context context, int notiID) {

        String info = "error"; //changed below.
        Random myRandom = new Random();
        NotificationManager mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //---PendingIntent to launch activity if the user selects
        // the notification---
        Intent notificationIntent = new Intent(context, MainActivity.class);
        //random notification
        switch (myRandom.nextInt(4)) {
            case 0:
                info = "Iphone6 is very bendy!";
                break;
            case 1:
                info = "Pixel phones are cool.";
                break;
            case 2:
                info = "Nexus 6 phone is huge at 5.9 inches!";
                break;
            case 3:
                info = "Hope the Samsung 8 note doesn't catch fire!";
                break;
            default:
                info = "No new headline.";
        }

        notificationIntent.putExtra("mText", info);

        PendingIntent contentIntent = PendingIntent.getActivity(context, notiID, notificationIntent, 0);

        //create the notification
        Notification notif = new NotificationCompat.Builder(context, MainActivity.id)
            .setSmallIcon(R.drawable.ic_launcher)
            .setWhen(System.currentTimeMillis()) //When the event occurred, now, since noti are stored by time.
            .setContentTitle("New headline!") //Title message top row.
            .setContentText(info) //message when looking at the notification, second row
            .setContentIntent(contentIntent) //what activity to open.
            .setChannelId(MainActivity.id)
            .setAutoCancel(true) //allow auto cancel when pressed.
            .build(); //finally build and return a Notification.
        //Show the notification
        mManager.notify(1, notif);  //and if we want different notifications, use notiID here instead of 1.
    }

}
