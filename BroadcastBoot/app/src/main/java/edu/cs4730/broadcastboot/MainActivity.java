package edu.cs4730.broadcastboot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

/**
 * This example sets a receiver to a boot message to a receiver.
 * the receiver then sets an alarm for X minutes.  X is set in the receiver.
 * <p>
 * services are hard to keep running in the background in API 26, so alarms were used for this
 * example.   It should be a scheduled job, when I have time to learn them.
 */

public class MainActivity extends AppCompatActivity {

    public static final String ACTION = "edu.cs4730.broadcastboot.myAction";
    MainFragment mFragment;
    final String TAG = "MainActivity";
    public static String id = "test_channel_01";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String info = "Nothing";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            info = extras.getString("mText");
        }
        Log.wtf(TAG, "info is " + info);
        mFragment = MainFragment.newInstance(info);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment).commit();
        }

        createchannel();
    }

    /*
     * for API 26+ create notification channels
     */
    private void createchannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
    }
}
