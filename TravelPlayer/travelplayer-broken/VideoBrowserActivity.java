package com.globaledge.travelplayer;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class VideoBrowserActivity extends AppCompatActivity {

    private static final String APP = "GETraveler|";
    private static final String TAG = "VideoBrowser";
    //private LinearLayout linearLayout;
    //List<VideoFile> videoFilesList;


    private RecyclerView recyclerView;
    private VideoFilesListAdapter listAdapter;

    private AlertDialog.Builder alertDialogBuilder;
    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;

    private Notification.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_browser);

        startActivity(new Intent(VideoBrowserActivity.this, SplashScreenActivity.class));
        recyclerView = findViewById(R.id.recycler_video_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("GETraveler", "Permission notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("NotificationChannel empty description");
            notificationChannel.setShowBadge(true);
        }

        Log.d(APP +TAG, "onCreate: ");

        //listAdapter = new VideoFilesListAdapter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(APP+TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(APP+TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(APP+TAG, "onRestart: ");
        listAdapter = VideoFilesListAdapter.getInstance(getApplicationContext());
        recyclerView.setAdapter(listAdapter);
        recyclerView.invalidate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(APP+TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(APP+TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(APP+TAG, "onDestroy: ");
        listAdapter = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(APP+TAG, "onCreateOptionsMenu: ");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(APP+TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()) {

            case R.id.action_quit:
                finishAndRemoveTask();
                return true;

            case R.id.action_refresh:
                //recreate();
                listAdapter.notifyDataSetChanged();
                return true;

            case R.id.action_app_settings:
                startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", VideoBrowserActivity.this.getPackageName(), null)));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

        //return true;
    }

}
