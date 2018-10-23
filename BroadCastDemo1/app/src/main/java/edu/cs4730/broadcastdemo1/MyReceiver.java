package edu.cs4730.broadcastdemo1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * A simple demo of receiving custom intents.
 * action1 is registered statically in the manifest file and action2 is dynamically registered
 * in the mainActivity code.
 *
 * The variables ACTION1 and ACTION2 are declared in the MainActivity as well.
 */

public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Received an intent.", Toast.LENGTH_SHORT).show();

        if (intent.getAction().equals(MainActivity.ACTION1)) { //is it our action1?
            Toast.makeText(context, "We received an intent for Action1.", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(MainActivity.ACTION2)) { //is it our action2?
            Toast.makeText(context, "We received an intent for Action2.", Toast.LENGTH_SHORT).show();
        }
    }
}
