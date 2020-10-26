package edu.cs4730.broadcastdemo2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * this app will need to be run on emulator to test the varying pieces
 * unless you can charge and discharge your battery very quickly.
 * but on a device plugin it in/unplug it to see no charging messages.
 * <p>
 * In API 25 and below, the broadcast receiver could be static declared in the manifest.xml file
 * but in API 26+ the implicit broadcast are no longer allowed.
 * <p>
 * Most of the battery charging info permissions have been moved to the system level.
 * a normal app can know if it is charging or on battery, but not the level of the battery.
 */
public class MainFragment extends Fragment {

    TextView logger;
    MyReceiver mReceiver = new MyReceiver();
    String TAG = "MainFragment";

    public MainFragment() {
        // Required empty public constructor
    }

    /* register the receiver (declared below) to get the information charging and batter */
    @Override
    public void onResume() {
        super.onResume();
        //the one below registers a global receiver.
        IntentFilter myIF = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        myIF.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        myIF.addAction("android.intent.action.BATTERY_LOW");
        myIF.addAction("android.intent.action.BATTERY_OKAY");
        getContext().registerReceiver(mReceiver, myIF);
        Log.v(TAG, "receiver should be registered");
    }

    @Override
    public void onPause() {  //or onDestory()
        // Unregister since the activity is not visible.

        getContext().unregisterReceiver(mReceiver);
        Log.v(TAG, "receiver should be unregistered");
        super.onPause();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_main, container, false);
        logger = myView.findViewById(R.id.textView1);

        //manual check.
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getContext().registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
            status == BatteryManager.BATTERY_STATUS_FULL;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level * 100 / (float)scale;

        if (isCharging) {
            logger.setText("status is Charging");
            // How are we charging?
            int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            if (usbCharge) logger.append("\n via the usb chargerbattery at "+ batteryPct);
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
            if (acCharge) logger.append("\n via the AC chargerbattery at "+ batteryPct);
        } else {
            logger.setText("status is not Charging.  battery at "+ batteryPct);
        }
        return myView;
    }


    /**
     * To test this on the emulator, remember to telnet to the emulator
     * commands:
     * power ac off      phone is unplugged
     * power ac on       phone is plugged in/charging
     * power capacity  15    low (when unplugged)
     * power capacity 30     okay.. (when plugged in and was low)
     * power capacity 100   full charge.
     */

    public class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.wtf("myReceiver", "received an intent");
            String info = "\n something wrong.";
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float)scale;

            int mStatus = 0;
            if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {  //battery is low!

                info = "\n batter low. should shut down things. battery at " + batteryPct;
                mStatus = 1;
                Log.v("myReceiver", "battery low");
            } else if (intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)) {  //battery is now ok!
                info = "\n battery Okay.  " + batteryPct;
                mStatus = 2;
                Log.v("myReceiver", "battery okay");
            } else if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {  //battery is charging.
                info = "\n Power connected, so phone is charging." + batteryPct;
                mStatus = 3;
                Log.v("myReceiver", "ac on");
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {  //power has been disconnected.
                info = "\n Power disconnected." + batteryPct;
                mStatus = 4;
                Log.v("myReceiver", "ac off");
            }
            logger.setText("status is " + mStatus + info);
        }
    }

}