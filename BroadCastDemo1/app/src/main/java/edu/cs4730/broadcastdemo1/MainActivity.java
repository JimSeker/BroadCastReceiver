package edu.cs4730.broadcastdemo1;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Map;

import edu.cs4730.broadcastdemo1.databinding.ActivityMainBinding;

/**
 * A simple demo of broadcast receiver and custom intent.
 * The fragment has the code to send the broadcast.
 * <p>
 * this code will register a dynamic intent-filter for Action2
 * action one is static registered in the manifest file.
 * <p>
 * Note, the local broadcast receiver is deprecated.  Instead you should be using a viewmodel with observers.
 * but it still works, so the example will continue.
 */

public class MainActivity extends AppCompatActivity {

    //declare the intent names here, except if change ACTION1, fix it in AndroidManifest.xml as well.
    public static final String ACTION1 = "edu.cs4730.bcr.mystaticevent";
    public static final String ACTION2 = "edu.cs4730.bcr.mydyanicevent";
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.POST_NOTIFICATIONS};
    ActivityResultLauncher<String[]> rpl;
    String TAG = "MainActivity";

    MyReceiver mReceiver;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rpl = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> isGranted) {
                boolean granted = true;
                for (Map.Entry<String, Boolean> x : isGranted.entrySet()) {
                    logthis(x.getKey() + " is " + x.getValue());
                    if (!x.getValue()) granted = false;
                }
                if (granted) logthis("Permissions granted for api 33+");
            }
        });


        //need to initialize the variable here.
        mReceiver = new MyReceiver();


        //setup button to send an intent for static registered receiver.
        binding.button1.setOnClickListener(new View.OnClickListener() {
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
        binding.button2.setOnClickListener(new View.OnClickListener() {
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!allPermissionsGranted()) {
                rpl.launch(REQUIRED_PERMISSIONS);
            }
        }
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
    //ask for permissions when we start.
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public void logthis(String msg) {

        Log.d(TAG, msg);
    }
}