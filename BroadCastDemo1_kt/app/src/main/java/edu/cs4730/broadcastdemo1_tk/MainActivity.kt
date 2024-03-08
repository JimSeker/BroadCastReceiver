package edu.cs4730.broadcastdemo1_tk

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import edu.cs4730.broadcastdemo1_tk.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //declare the intent names here, except if changes ACTION1, fix it in AndroidManifest.xml as well.
    object Actions {
        const val ACTION1 = "edu.cs4730.bcr.mystaticevent"
        const val ACTION2 = "edu.cs4730.bcr.mydyanicevent"
    }


    var TAG = "MainActivity"

    private lateinit var mReceiver: MyReceiver
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        mReceiver = MyReceiver()

        //setup button to send an intent for static registered receiver.


        //setup button to send an intent for static registered receiver.
        binding.button1.setOnClickListener {
            val i: Intent = Intent(Actions.ACTION1)
            i.setPackage("edu.cs4730.broadcastdemo1_tk") //in API 26, it must be explicit now.
            //since it's registered as a global (in the manifest), use sendBroadCast
            //LocalBroadcastManager.getInstance(getContext()).sendBroadcast(i);
            sendBroadcast(i)
        }

        //setup button to send an intent for dynamic registered receiver, which is registered in MainActivity.

        //setup button to send an intent for dynamic registered receiver, which is registered in MainActivity.
        binding.button2.setOnClickListener {
            val i: Intent = Intent(Actions.ACTION2)
            //since it's registered a local, use the LocalBroadcastManager.
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(i)
            //use this if registered as a global receiver.
            //getActivity().sendBroadcast(i);
            Log.v(TAG, "Should have sent the broadcast.")
        }

    }

    public override fun onResume() {
        super.onResume()
        // Register mReceiver to receive messages.  This is using the local broadcast, instead of a global.
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mReceiver, IntentFilter(Actions.ACTION2))
        //the one below registers a global receiver.
        //registerReceiver(mReceiver, new IntentFilter(ACTION2));
        //Log.v(TAG, "receiver should be registered");
    }

    override fun onPause() {  //or onDestory()
        // Unregister since the activity is not visible.  This is using the local broadcast, instead of a global.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver)
        //the one below unregisters a global receiver.
        //unregisterReceiver(mReceiver);
        Log.v(TAG, "receiver should be unregistered")
        super.onPause()
    }


}