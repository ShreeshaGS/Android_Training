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

    private static final int TP_REQUEST_PERMISSION = new Random().nextInt(1000);
    private final static String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    /*private final static String[] DANGEROUS_PERMISSIONS = new String[] {
            Manifest.permission.SYSTEM_ALERT_WINDOW
    };*/

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
        recyclerView = findViewById(R.id.recycler_video_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("GETraveler", "Permission notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("NotificationChannel empty description");
            notificationChannel.setShowBadge(true);
        }
        //linearLayout = (LinearLayout) findViewById(R.id.linearlayout);

        Log.d(APP +TAG, "onCreate: ");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initPermissions();
        }
        Log.d(APP+TAG, "onCreate: initialised permissions");

        //listAdapter = new VideoFilesListAdapter(this);
        listAdapter = VideoFilesListAdapter.getInstance(getApplicationContext());
        recyclerView.setAdapter(listAdapter);
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

    private void initPermissions() {
        Log.d(APP+TAG, "initPermissions: ");
        Set<String> missingPermissions = getMissingPermissions();

        if(!missingPermissions.isEmpty()) {
            requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]), TP_REQUEST_PERMISSION);
        }
        /*for (String permission: DANGEROUS_PERMISSIONS) {
            switch (permission) {
                case Manifest.permission.SYSTEM_ALERT_WINDOW:
                    final Intent settingsIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).setData(Uri.fromParts("package", VideoBrowserActivity.this.getPackageName(), null));
                    if(!Settings.canDrawOverlays(getApplicationContext())) {
                        alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("One more to go");
                        alertDialogBuilder.setMessage("We need this permission so that you wouldn;t be interrupted by other apps");
                        alertDialogBuilder.setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(settingsIntent);
                                //grantUriPermission(VideoBrowserActivity.this.getPackageName(), Uri.parse(Manifest.permission.SYSTEM_ALERT_WINDOW), 0);
                            }
                        });
                        alertDialogBuilder.setNegativeButton("MAYBE LATER", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(VideoBrowserActivity.this);
                                taskStackBuilder.addNextIntentWithParentStack(settingsIntent);
                                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                //PendingIntent pendingIntent = new PendingIntent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).getCreatorPackage();
                                notification = new Notification.Builder(getApplicationContext())
                                        .setContentTitle("click to grant permission(s)")
                                        .setAutoCancel(true)
                                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                                        .setContentIntent(pendingIntent)
                                        .setContentText("Travel Player app needs these permission to function smoothly")
                                        .setPriority(Notification.PRIORITY_MAX);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    notification.setChannelId("GETraveler");
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    notificationManager.createNotificationChannel(notificationChannel);
                                }
                                notificationManager.notify(APP + TAG, 0, notification.build());
                            }
                        }).setCancelable(false);
                        alertDialogBuilder.show();
                    }
                    break;

                default:
                    Toast.makeText(getApplicationContext(), "Unhandled permission", Toast.LENGTH_SHORT).show();
                    break;
            }
        }*/
    }

    private Set<String> getMissingPermissions() {
        Log.d(APP+TAG, "getMissingPermissions: ");
        Set<String> missingPermissions = new HashSet<String>();
        for(String permission: REQUIRED_PERMISSIONS) {
            if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                missingPermissions.add(permission);
            }
        }

        return missingPermissions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(APP+TAG, "onRequestPermissionsResult: ");
        boolean allPermissionsGranted = true;
        for (int i = 0; i < permissions.length; i++) {
            Log.d(TAG, "onRequestPermissionsResult: " + permissions[i] + ": " + grantResults[i]);
            if(permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                if(null != listAdapter) {
                    listAdapter.notifyDataSetChanged();
                }
            }
        }
        for(int grantResult: grantResults) {
            if(PackageManager.PERMISSION_DENIED == grantResult) {
                allPermissionsGranted = false;
                break;
            }
        }

        if(!allPermissionsGranted) {
            initPermissions();
        }
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

/*    private TextView getTextView(String textMsg) {
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
//        params.setMargins((int) R.dimen.default_margin,
//                (int) R.dimen.default_margin,
//                (int) R.dimen.default_margin,
//                (int) R.dimen.default_margin);
        //params.topMargin = (int) R.dimen.default_margin;
        //params.leftMargin = (int) R.dimen.default_margin;

        TextView textView = new TextView(VideoBrowserActivity.this);
        textView.setText(textMsg);
        textView.setTextSize(20);
        textView.setBackgroundColor(Color.argb(180,
                new Random().nextInt(256),
                new Random().nextInt(256),
                new Random().nextInt(256)));
        //textView.setTextSize();
        textView.setLayoutParams(params);

        return textView;
    }*/
}
