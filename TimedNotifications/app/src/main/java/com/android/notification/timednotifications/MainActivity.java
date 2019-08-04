package com.android.notification.timednotifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    Notification.Builder builder;
    Timer timer = null;
    TimerTask timerTask;
    //String notificationGroupKey = "NotificationGroupKey";
    final Handler handler = new Handler();
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new Notification.Builder(getApplicationContext())
                .setContentTitle("My Notification")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                //.setGroup(notificationGroupKey)
                //.setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("myNotifChannel", "Channel for timed notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("NotificationChannel empty description");
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId("myNotifChannel");
        }

    }

    public void startNotifications(View view) {
        if (timer == null) {
            timer = new Timer();
            initialiseTimerTask();
            timer.schedule(timerTask, 0, 10 * 1000);
        }
    }

    private void initialiseTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //builder.setSubText("Count: " + ++count);
                        builder.setContentText("Dummy notification: " + ++count);
                        //builder.addMessage(new NotificationCompat.MessagingStyle.Message(String.valueOf(++count), 0, "shrisha")));
                        //builder.setSmallIcon(R.mipmap.ic_launcher);
                        //builder.setStyle(new Notification.InboxStyle().addLine("Dummy notification: " + ++count));
                        notificationManager.notify(0, builder.build());
                    }
                });
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopNotifications(MainActivity.this.findViewById(R.id.stop_notify));
    }

    public void stopNotifications(View view) {
        if (timer != null) {
            timer.cancel();
            notificationManager.cancel(0);
            timer = null;
        }
    }
}