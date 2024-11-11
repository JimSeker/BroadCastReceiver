package edu.cs4730.broadcastboot;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import edu.cs4730.broadcastboot.databinding.ActivityMainBinding;

/**
 * This example sets a receiver to a boot message to a receiver.
 * the receiver then sets an alarm for X minutes.  X is set in the receiver.
 * <p>
 * services are hard to keep running in the background in API 26, so alarms were used for this
 * example.   It should be a scheduled job, when I have time to learn them.
 */

public class MainActivity extends AppCompatActivity {

    public static final String ACTION = "edu.cs4730.broadcastboot.myAction";
    final String TAG = "MainActivity";
    public static String id = "test_channel_01";
    ActivityResultLauncher<String[]> rpl;
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.POST_NOTIFICATIONS};
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        String info = "Nothing";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            info = extras.getString("mText");
        }
       binding.textView2.setText(info);

        // for notifications permission now required in api 33
        //this allows us to check with multiple permissions, but in this case (currently) only need 1.
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


        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.ACTION);
                intent.setPackage("edu.cs4730.broadcastboot"); //in API 26, it must be explicit now.
                sendBroadcast(intent);
                finish();
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

    public void logthis(String msg) {

        Log.d(TAG, msg);
    }

    /**
     * for API 26+ create notification channels
     */
    private void createchannel() {
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

    //ask for permissions when we start.
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
