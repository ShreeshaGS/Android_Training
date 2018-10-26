package com.example.shrisha.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class AudioService extends Service {
    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast toast = Toast.makeText(getApplicationContext(), "Service created", Toast.LENGTH_LONG);
        //toast.setText("Service created");
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), intent.getData());
            mediaPlayer.start();
        } else {
            Toast.makeText(getApplicationContext(), "Music already playing. Stop current track to play next", Toast.LENGTH_LONG).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        } else {
            Toast.makeText(getApplicationContext(), "Music already stopped", Toast.LENGTH_LONG).show();
        }
        super.onDestroy();
    }
}
