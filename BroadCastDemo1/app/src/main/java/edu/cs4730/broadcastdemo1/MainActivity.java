package edu.cs4730.broadcastdemo1;

import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

/**
 * A simple demo of broadcast receiver and custom intent.
 * The fragment has the code to send the broadcast.
 * 
 * this code will register a dynamic intent-filter for Action2
 * action one is static registered in the manifest file.
 */

public class MainActivity extends AppCompatActivity {

    //declare the intent names here, except if change ACTION1, fix it in AndroidManifest.xml as well.
    public static final String ACTION1 = "edu.cs4730.bcr.mystaticevent";
    public static final String ACTION2 = "edu.cs4730.bcr.mydyanicevent";

    String TAG = "MainActivity";

    MyReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment()).commit();
        }

        //need to initialize the variable here.
        mReceiver = new MyReceiver();

    }

    @Override
    public void onResume() {
        super.onResume();
        // Register mReceiver to receive messages.  This is using the local broadcast, instead of a global.
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(ACTION2));
        //the one below registers a global receiver.
        //registerReceiver(mReceiver, new IntentFilter(ACTION2));
        //Log.v(TAG, "receiver should be registered");
    }

    @Override
    protected void onPause() {  //or onDestory()
        // Unregister since the activity is not visible.  This is using the local broadcast, instead of a global.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        //the one below unregisters a global receiver.
        //unregisterReceiver(mReceiver);
        Log.v(TAG, "receiver should be unregistered");
        super.onPause();

    }

}