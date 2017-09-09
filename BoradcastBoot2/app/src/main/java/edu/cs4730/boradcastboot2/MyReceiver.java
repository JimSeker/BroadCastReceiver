package edu.cs4730.boradcastboot2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //boot has completed, now time to start our background service.
            Log.wtf(TAG, "Got the boot one!");
            MyJobIntentService.enqueueWork(context, intent);  //not sure if should create a new intent or not.
        }
    }
}
