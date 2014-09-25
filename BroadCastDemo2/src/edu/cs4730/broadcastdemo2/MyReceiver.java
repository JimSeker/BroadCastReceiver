package edu.cs4730.broadcastdemo2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
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

		Log.v("myReceiver", "received an intent");
		Intent i = new Intent(context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //required when start an activity from a non activity.
		
		if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)){  //battery is low!
			Log.v("myReceiver", "battery low");
			i.putExtra("mStatus", 1);  //battery low
			context.startActivity(i);
		} else if (intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)){  //battery is now ok!
			Log.v("myReceiver", "battery okay");
			i.putExtra("mStatus", 2);  //battery okay
			context.startActivity(i);
		}else if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){  //battery is charging.
			Log.v("myReceiver", "ac on");
			i.putExtra("mStatus", 3);  //charging/on power
			context.startActivity(i);
		} else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)){  //power has been disconnected.
			Log.v("myReceiver", "ac off");
			i.putExtra("mStatus", 4);  //disconnected from power.
			context.startActivity(i);
		}
		
		
	}
}
