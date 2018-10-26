package com.example.shrisha.onbootreceiver;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sendBroadcast(new Intent("android.intent.action.BOOT_COMPLETED"));
        //finish();
    }
}
