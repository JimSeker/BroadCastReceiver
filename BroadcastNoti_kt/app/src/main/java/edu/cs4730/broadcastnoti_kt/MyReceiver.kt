package edu.cs4730.broadcastnoti_kt

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

/**
 * This is implementation of the an earlier example, but new uses a receiver instead of a activity
 * so a activity->alarm manager -> receiver -> notification -> activity
 */
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.v("myReceiver", "received an intent")
        if (intent.action == MainActivity.Actions.ACTION) {  //my custom intent
            //---get the notification ID for the notification;
            // passed in by the MainActivity---
            val notifID = intent.extras!!.getInt("NotifID")
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //---PendingIntent to launch activity if the user selects
            // the notification---
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.putExtra("mytype", "2 minutes later?")
            val contentIntent = PendingIntent.getActivity(
                context,
                notifID,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            //create the notification
            val notif = NotificationCompat.Builder(context, MainActivity.Actions.id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis()) //When the event occurred, now, since noti are stored by time.
                .setContentTitle("Time's up!") //Title message top row.
                .setContentText("This is your alert, courtesy of the AlarmManager") //message when looking at the notification, second row
                .setContentIntent(contentIntent) //what activity to open.
                .setChannelId(MainActivity.Actions.id)
                .setAutoCancel(true) //allow auto cancel when pressed.
                .build() //finally build and return a Notification.
            //Show the notification
            nm.notify(notifID, notif)
        }
    }
}
