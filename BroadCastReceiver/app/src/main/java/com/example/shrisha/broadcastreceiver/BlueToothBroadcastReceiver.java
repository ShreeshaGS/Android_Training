package com.example.shrisha.broadcastreceiver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BlueToothBroadcastReceiver extends BroadcastReceiver {
    //@SuppressLint("LongLogTag")
    private String action;
    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        Toast.makeText(context,
                "Bluetooth state changed", Toast.LENGTH_SHORT)
                .show();
        //Log.d("<<<<BlueToothBroadcastReceiver>>>( INTENT)", intent.toString());
        //Bundle bundle = intent.getExtras();
        //Log.d("<<<<BlueToothBroadcastReceiver>>>( EXTRAS)", bundle.toString());

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Toast.makeText(context,
                            "Bluetooth turned off", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case BluetoothAdapter.STATE_ON:
                    Toast.makeText(context,
                            "Bluetooth turned on", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Toast.makeText(context,
                            "Bluetooth turning off", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Toast.makeText(context,
                            "Bluetooth turning on", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }

        //intent.getAction().
    }

}
