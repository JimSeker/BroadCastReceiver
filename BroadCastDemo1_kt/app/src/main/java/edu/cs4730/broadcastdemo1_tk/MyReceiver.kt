package edu.cs4730.broadcastdemo1_tk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * A simple demo of receiving custom intents.
 * action1 is registered statically in the manifest file and action2 is dynamically registered
 * in the mainActivity code.
 *
 * The variables ACTION1 and ACTION2 are declared in the MainActivity as well.
 */
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Received an intent.", Toast.LENGTH_SHORT).show()
        if (intent.action == MainActivity.Actions.ACTION1) { //is it our action1?
            Toast.makeText(context, "We received an intent for Action1.", Toast.LENGTH_SHORT).show()
        } else if (intent.action == MainActivity.Actions.ACTION2) { //is it our action2?
            Toast.makeText(context, "We received an intent for Action2.", Toast.LENGTH_SHORT).show()
        }
    }
}
