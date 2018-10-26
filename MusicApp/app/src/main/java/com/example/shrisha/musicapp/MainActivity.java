package com.example.shrisha.musicapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private int position;
    private MediaPlayer mediaPlayer = null;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "\n\nApp opened\n\n");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = findViewById(R.id.btnPlay);
        Button pause = findViewById(R.id.btnPause);
        Button resume = findViewById(R.id.btnResume);
        Button stop = findViewById(R.id.btnStop);



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ommahapranadeepam);
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                    Log.d(TAG, "\n\nMusic started\n\n");
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    position = mediaPlayer.getCurrentPosition();
                    Log.d(TAG, "\n\nMusic paused\n\n");
                }
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(position);
                    mediaPlayer.start();
                    Log.d(TAG, "\n\nMusic resumed\n\n");
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    Log.d(TAG, "\n\nMusic stopped\n\n");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "\n\nApp closed\n\n");
    }
}
