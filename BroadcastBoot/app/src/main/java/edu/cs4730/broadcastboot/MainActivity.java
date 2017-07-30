package edu.cs4730.broadcastboot;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/*
 * This example sets a receiver to a boot message and then makes sure that it's service
 * is running on start up.
 */

public class MainActivity extends AppCompatActivity {

    MainFragment mFragment;
    final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String info = "Nothing";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            info = extras.getString("mText");
        }
        mFragment = MainFragment.newInstance(info);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mFragment).commit();
        }
        //if my service is not running, start it.  for testing, before rebooting emulators... so much fun...
        if (!isMyServiceRunning(MyService.class)) {
            //not running, start it.
            Intent i = new Intent(getBaseContext(), MyService.class);
            startService(i);
            Log.v(TAG, "Started service");
        }

        Log.v(TAG, "started up: " + info);
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
            Log.d(TAG, "new value");
        }
        Log.v(TAG, "New intent " + info);

        mFragment.settext(info);
    }
}
