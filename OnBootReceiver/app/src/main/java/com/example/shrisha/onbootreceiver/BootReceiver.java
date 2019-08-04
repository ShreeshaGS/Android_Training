package com.example.shrisha.onbootreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()) ||
                "android.intent.action.ACTION_BOOT_COMPLETED".equals(intent.getAction())) {
            Toast toast = Toast.makeText(context, "Application received boot broadcast", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            //Intent intent1 = new Intent(context, MainActivity.class);
            //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intent1);
        }
    }
}
