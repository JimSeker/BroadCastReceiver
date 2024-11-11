package edu.cs4730.broadcastdemo1_tk

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import edu.cs4730.broadcastdemo1_tk.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //declare the intent names here, except if changes ACTION1, fix it in AndroidManifest.xml as well.
    object Actions {
        const val ACTION1 = "edu.cs4730.bcr.mystaticevent"
        const val ACTION2 = "edu.cs4730.bcr.mydyanicevent"
    }

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    lateinit var rpl: ActivityResultLauncher<Array<String>>
    var TAG = "MainActivity"

    private lateinit var mReceiver: MyReceiver
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
        rpl = registerForActivityResult<Array<String>, Map<String, Boolean>>(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted ->
            var granted = true
            for ((key, value) in isGranted) {
                logthis("$key is $value")
                if (!value) granted = false
            }
            if (granted) logthis("Permissions granted for api 33+")
        }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!allPermissionsGranted()) {
                rpl.launch(REQUIRED_PERMISSIONS)
            }
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

    //ask for permissions when we start.
    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun logthis(msg: String) {
        Log.d(TAG, msg)
    }
}