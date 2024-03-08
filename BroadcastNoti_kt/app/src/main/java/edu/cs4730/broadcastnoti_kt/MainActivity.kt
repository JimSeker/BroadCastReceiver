package edu.cs4730.broadcastnoti_kt

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import edu.cs4730.broadcastnoti_kt.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    object Actions {
        const val ACTION = "edu.cs4730.bcr.noti"
        const val id = "test_channel_01"
        var TAG = "MainActivity"
    }

    private lateinit var rpl: ActivityResultLauncher<Array<String>>
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    private lateinit var nm: NotificationManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // for notifications permission now required in api 33
        //this allows us to check with multiple permissions, but in this case (currently) only need 1.
        rpl = registerForActivityResult<Array<String>, Map<String, Boolean>>(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGrant ->
            var granted = true
            for ((key, value) in isGrant!!) {
                logthis("$key is $value")
                if (!value) granted = false
            }
            if (granted) logthis("Permissions granted for api 33+")
        }


        //check see if there is data in the bundle, ie launched from a notification!


        //check see if there is data in the bundle, ie launched from a notification!
        var info = "Nothing"
        val extras = intent.extras
        if (extras != null) {
            info = extras.getString("mytype").toString()
            if (info == null) {
                info = "still nothing"
            } //something wrong here.
        }

        logthis(info)

        //setup button to send an intent for static registered receiver.
        binding.button1.setOnClickListener { setalarm() }

        createchannel()
        //for the new api 33+ notifications permissions.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!allPermissionsGranted()) {
                rpl.launch(REQUIRED_PERMISSIONS)
            }
        }
    }

    fun setalarm() {
        val NotID = 1

        //---use the AlarmManager to trigger an alarm---
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        //---get current date and time---
        val calendar = Calendar.getInstance()
        //---sets the time for the alarm to trigger in 2 minutes from now---
        calendar[Calendar.MINUTE] = calendar[Calendar.MINUTE] + 2
        calendar[Calendar.SECOND] = 0


        //---PendingIntent to launch receiver when the alarm triggers-
        //Intent notificationIntent = new Intent(getApplicationContext(), MyReceiver.class);
        val notificationIntent: Intent = Intent(Actions.ACTION)
        notificationIntent.setPackage("edu.cs4730.broadcastnoti_kt") //in API 26, it must be explicit now.
        notificationIntent.putExtra("NotifID", NotID)

        //Note there is only one difference in this code from the notificationdemo code.  and it's
        //getBroadcast, instead getActivity..
        val contentIntent = PendingIntent.getBroadcast(
            this@MainActivity,
            NotID,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        Log.i(Actions.TAG, "Set alarm, I hope")
        //---sets the alarm to trigger---
        alarmManager[AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()] = contentIntent

        //sendBroadcast(notificationIntent);//let's see if it works... without the alarm.
        finish() //exit the app.
    }

    /**
     * for API 26+ create notification channels
     */
    private fun createchannel() {
        val mChannel = NotificationChannel(
            Actions.id,
            getString(R.string.channel_name),  //name of the channel
            NotificationManager.IMPORTANCE_DEFAULT
        ) //importance level
        //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
        // Configure the notification channel.
        mChannel.description = getString(R.string.channel_description)
        mChannel.enableLights(true)
        // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.setShowBadge(true)
        mChannel.setVibrationPattern(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
        nm.createNotificationChannel(mChannel)
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
        binding.textView1.append(msg + "\n")
        Log.d(Actions.TAG, msg)
    }
}