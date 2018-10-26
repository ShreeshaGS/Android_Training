package com.example.shrisha.broadcastreceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    BlueToothBroadcastReceiver BTReceiver;
    WiFiAdapter WReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BTReceiver = new BlueToothBroadcastReceiver();
        WReceiver = new WiFiAdapter();

        IntentFilter BTIntentFilter = new IntentFilter();
        BTIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        BTIntentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);

        IntentFilter WIntentFilter = new IntentFilter();
        WIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        registerReceiver(BTReceiver, BTIntentFilter);
        registerReceiver(WReceiver, WIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(BTReceiver);
        unregisterReceiver(WReceiver);
    }
}
