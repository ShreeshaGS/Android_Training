package com.example.shrisha.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WiFiAdapter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.ERROR_AUTHENTICATING);

            switch (state) {
                case WifiManager.WIFI_STATE_ENABLED:
                    Toast.makeText(context,
                            "WiFi turned on", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    Toast.makeText(context,
                            "WiFi turning on", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Toast.makeText(context,
                            "WiFi turned off", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    Toast.makeText(context,
                            "WiFi turning off", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    }
}
