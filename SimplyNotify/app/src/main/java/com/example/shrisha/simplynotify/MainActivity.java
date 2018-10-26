package com.example.shrisha.simplynotify;

import android.app.Notification;
import android.app.NotificationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private short count = 0;
    private NotificationManager notificationManager;
    private Notification notification;
    private Uri uri;
    private Ringtone ringtone;

    private Button btnStart;
    private Button btnStop;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.start_notification);
        //btnStop = findViewById(R.id.stop_notification);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
        /*final AudioAttributes audioAttributes = new AudioAttributes.Builder()
                //.setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .build();*/
        //ringtone.setAudioAttributes(audioAttributes);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification = new Notification.Builder(MainActivity.this)
                        .setContentTitle("Notification count: " + count)
                        .setContentText("Android Audio Noise maker")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setAutoCancel(true)
                        //.setPriority(Notification.PRIORITY_DEFAULT)
                        .setStyle(new Notification.BigTextStyle())
                        .setVisibility(Notification.VISIBILITY_PRIVATE)
                        .setCategory(Notification.CATEGORY_SYSTEM)
                        //.setContentIntent(pendingIntentSnooze)
                        //.setSound(uri)
                        //.setContentIntent(pendingIntentSnooze)
                        .build();
                //notification.visibility = Notification.VISIBILITY_PUBLIC;
                //notification.defaults = Notification.DEFAULT_VIBRATE;
                notification.defaults |= Notification.DEFAULT_SOUND;
                //notification.defaults |= Notification.DEFAULT_LIGHTS;
                //notification.flags = Notification.FLAG_NO_CLEAR;
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                //notification.flags |= Notification.FLAG_GROUP_SUMMARY;
                notificationManager.notify(0, notification);
                //ringtone.play();
                count++;
            }
        });
    }
}
