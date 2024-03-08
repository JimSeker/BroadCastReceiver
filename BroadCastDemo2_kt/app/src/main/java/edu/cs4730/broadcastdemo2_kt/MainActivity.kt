package edu.cs4730.broadcastdemo2_kt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import edu.cs4730.broadcastdemo2_kt.databinding.ActivityMainBinding

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


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var mReceiver: MyReceiver = MyReceiver()
    var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        //manual check.
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(null, ifilter)
        val status = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
        val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = level * 100 / scale.toFloat()
        if (isCharging) {
            binding.logger.text = "status is Charging"
            // How are we charging?
            val chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
            if (usbCharge) binding.logger.append("\n via the usb chargerbattery at $batteryPct")
            val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
            if (acCharge) binding.logger.append("\n via the AC chargerbattery at $batteryPct")
        } else {
            binding.logger.text = "status is not Charging.  battery at $batteryPct"
        }
    }

    /* register the receiver (declared below) to get the information charging and batter */
    public override fun onResume() {
        super.onResume()
        //the one below registers a global receiver.
        val myIF = IntentFilter("android.intent.action.ACTION_POWER_CONNECTED")
        myIF.addAction("android.intent.action.ACTION_POWER_DISCONNECTED")
        myIF.addAction("android.intent.action.BATTERY_LOW")
        myIF.addAction("android.intent.action.BATTERY_OKAY")
        registerReceiver(mReceiver, myIF)
        Log.v(TAG, "receiver should be registered")
    }

    public override fun onPause() {  //or onDestory()
        // Unregister since the activity is not visible.
        unregisterReceiver(mReceiver)
        Log.v(TAG, "receiver should be unregistered")
        super.onPause()
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
    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.wtf("myReceiver", "received an intent")
            var info = "\n something wrong."
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level * 100 / scale.toFloat()
            var mStatus = 0
            if (intent.action == Intent.ACTION_BATTERY_LOW) {  //battery is low!
                info = "\n batter low. should shut down things. battery at $batteryPct"
                mStatus = 1
                Log.v("myReceiver", "battery low")
            } else if (intent.action == Intent.ACTION_BATTERY_OKAY) {  //battery is now ok!
                info = "\n battery Okay.  $batteryPct"
                mStatus = 2
                Log.v("myReceiver", "battery okay")
            } else if (intent.action == Intent.ACTION_POWER_CONNECTED) {  //battery is charging.
                info = "\n Power connected, so phone is charging.$batteryPct"
                mStatus = 3
                Log.v("myReceiver", "ac on")
            } else if (intent.action == Intent.ACTION_POWER_DISCONNECTED) {  //power has been disconnected.
                info = "\n Power disconnected.$batteryPct"
                mStatus = 4
                Log.v("myReceiver", "ac off")
            }
            binding.logger.text = "status is $mStatus$info"
        }
    }
}

