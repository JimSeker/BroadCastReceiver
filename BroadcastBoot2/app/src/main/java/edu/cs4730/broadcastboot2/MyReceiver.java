package edu.cs4730.broadcastboot2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MyReceiver extends BroadcastReceiver {
    String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //boot has completed, now time to start our background service.
            Log.wtf(TAG, "Got the boot one!");
            OneTimeWorkRequest runWork = new OneTimeWorkRequest.Builder(myWorker.class)
                .build();
            WorkManager.getInstance(context).enqueue(runWork);
        }
    }
}
