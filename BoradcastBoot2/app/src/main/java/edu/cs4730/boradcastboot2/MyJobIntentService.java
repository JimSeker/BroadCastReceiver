package edu.cs4730.boradcastboot2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

/**
 * Likely I should have created an infinite process here.  but it seems to work.
 *
 */
public class MyJobIntentService extends JobIntentService {
    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.
        int NotiID = 1;
        while(true) {
            sendNoti(getApplicationContext(), NotiID);
            Log.wtf("JobSheduler", "send notification");
            try {
                Thread.sleep(60000);  // 1000 is one second, once a minute would be 60000
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toast("All work complete");
    }

    final Handler mHandler = new Handler();

    // Helper for showing tests
    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
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