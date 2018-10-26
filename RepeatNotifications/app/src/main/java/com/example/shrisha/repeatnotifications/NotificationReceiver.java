package com.example.shrisha.repeatnotifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

class NotificationReceiver extends BroadcastReceiver {
    private final String TAG = "NotificationReceiver";
    private static short count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        //Intent intentRepeating = new Intent(context, RepeatingActivity.class);
        Intent intentRepeating = new Intent();
        intentRepeating.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intentRepeating,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Time: "  + Calendar.getInstance(Locale.getDefault()).getTime().toString())
                //.setContentText("Count: " + count)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{500, 100, 500, 200, 500, 400})
                .setAutoCancel(true);
        count++;
        notificationManager.notify(100, builder.setContentText("Count: " + count).build());
    }
}
