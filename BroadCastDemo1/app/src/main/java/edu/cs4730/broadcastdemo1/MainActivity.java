package edu.cs4730.broadcastdemo1;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.View;

/**
 * A simple demo of broadcast receiver and custom intent.
 * The fragment has the code to send the broadcast.
 * 
 * this code will register a dynamic intent-filter for Action2
 * action one is static registered in the manifest file.
 *
 * Note, the local broadcast receiver is deprecated.  Instead you should be using a viewmodel with observers.
 * but it still works, so the example will continue.
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
        //need to initialize the variable here.
        mReceiver = new MyReceiver();


        //setup button to send an intent for static registered receiver.
       findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.ACTION1);
                i.setPackage("edu.cs4730.broadcastdemo1"); //in API 26, it must be explicit now.
                //since it's registered as a global (in the manifest), use sendBroadCast
                //LocalBroadcastManager.getInstance(getContext()).sendBroadcast(i);
                sendBroadcast(i);
            }
        });

        //setup button to send an intent for dynamic registered receiver, which is registered in MainActivity.
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.ACTION2);
                //since it's registered a local, use the LocalBroadcastManager.
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                //use this if registered as a global receiver.
                //getActivity().sendBroadcast(i);
                Log.v(TAG, "Should have sent the broadcast.");
            }
        });
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