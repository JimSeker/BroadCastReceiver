package edu.cs4730.broadcastboot2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class myWorker extends Worker {
   public myWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
      super(context, workerParams);
   }

   @NonNull
   @Override
   public Result doWork() {
      int NotiID = 1;
      boolean done = false;
      while (!done) {
         sendNoti(getApplicationContext(), NotiID);
         Log.wtf("myWorker", "send notification");
         try {
            Thread.sleep(60000);  // 1000 is one second, once a minute would be 60000
         } catch (InterruptedException e) {
            done = true;
            e.printStackTrace();
            return Result.failure();
         }
      }
      return Result.success();
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

      PendingIntent contentIntent = PendingIntent.getActivity(context, notiID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

      //create the notification
      Notification notif = new NotificationCompat.Builder(context, MainActivity.id)
          .setSmallIcon(R.mipmap.ic_launcher)
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
