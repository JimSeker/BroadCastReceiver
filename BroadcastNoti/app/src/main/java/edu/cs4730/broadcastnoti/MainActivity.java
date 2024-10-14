package edu.cs4730.broadcastnoti;

import java.util.Calendar;
import java.util.Map;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;

import edu.cs4730.broadcastnoti.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    public static final String ACTION = "edu.cs4730.bcr.noti";
    public static String id = "test_channel_01";
    static String TAG = "MainActivity";
    ActivityResultLauncher<String[]> rpl;
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.POST_NOTIFICATIONS};
    NotificationManager nm;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // for notifications permission now required in api 33
        //this allows us to check with multiple permissions, but in this case (currently) only need 1.
        rpl = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> isGranted) {
                    boolean granted = true;
                    for (Map.Entry<String, Boolean> x : isGranted.entrySet()) {
                        logthis(x.getKey() + " is " + x.getValue());
                        if (!x.getValue()) granted = false;
                    }
                    if (granted)
                        logthis("Permissions granted for api 33+");
                }
            }
        );


        //check see if there is data in the bundle, ie launched from a notification!
        String info = "Nothing";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            info = extras.getString("mytype");
            if (info == null) {
                info = "still nothing";
            }  //something wrong here.
        }

        logthis(info);

        //setup button to send an intent for static registered receiver.
        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setalarm();
            }
        });

        createchannel();
        //for the new api 33+ notifications permissions.
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!allPermissionsGranted()) {
                rpl.launch(REQUIRED_PERMISSIONS);
            }
        }
    }

    public void setalarm() {
        int NotID = 1;

        //---use the AlarmManager to trigger an alarm---
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //---get current date and time---
        Calendar calendar = Calendar.getInstance();
        //---sets the time for the alarm to trigger in 2 minutes from now---
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 2);
        calendar.set(Calendar.SECOND, 0);


        //---PendingIntent to launch receiver when the alarm triggers-
        //Intent notificationIntent = new Intent(getApplicationContext(), MyReceiver.class);
        Intent notificationIntent = new Intent(MainActivity.ACTION);
        notificationIntent.setPackage("edu.cs4730.broadcastnoti"); //in API 26, it must be explicit now.
        notificationIntent.putExtra("NotifID", NotID);

        //Note there is only one difference in this code from the notificationdemo code.  and it's
        //getBroadcast, instead getActivity..
        PendingIntent contentIntent = PendingIntent.getBroadcast(MainActivity.this, NotID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Log.i(TAG, "Set alarm, I hope");
        //---sets the alarm to trigger---
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), contentIntent);

        //sendBroadcast(notificationIntent);//let's see if it works... without the alarm.
        finish();  //exit the app.
    }

    /**
     * for API 26+ create notification channels
     */
    private void createchannel() {

        NotificationChannel mChannel = new NotificationChannel(id,
            getString(R.string.channel_name),  //name of the channel
            NotificationManager.IMPORTANCE_DEFAULT);   //importance level
        //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
        // Configure the notification channel.
        mChannel.setDescription(getString(R.string.channel_description));
        mChannel.enableLights(true);
        // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setShowBadge(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        nm.createNotificationChannel(mChannel);

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
        binding.textView1.append(msg + "\n");
        Log.d(TAG, msg);
    }
}
