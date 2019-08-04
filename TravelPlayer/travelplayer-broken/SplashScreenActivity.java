package com.globaledge.travelplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    private static final int GE_REQUEST_PERMISSION = new Random().nextInt(1000);
    private final static String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private VideoFilesListAdapter videoFilesListAdapter;
    private ServiceConnection serviceConnection;
    private ActionCompletionListener getFilesListCompletionListener;

    ProgressBar progressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Log.d(TAG, "onCreate: ");

        progressBar = (ProgressBar) findViewById(R.id.spinner_loading);
        
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected: ");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected: ");
            }
        };

        getFilesListCompletionListener = new ActionCompletionListener() {
            @Override
            public void onActionCompleted() {
                Log.d(TAG, "getFilesListCompletionListener: onActionCompleted: ");
                progressBar.setVisibility(View.GONE);
                finish();
            }
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initPermissions();
        }

    }

    /*private void intialiseData() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                finish();
            }
        }, 1000);
    }*/

    private Set<String> getMissingPermissions() {
        Log.d(TAG, "getMissingPermissions: ");
        Set<String> missingPermissions = new HashSet<String>();
        for(String permission: REQUIRED_PERMISSIONS) {
            if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                missingPermissions.add(permission);
            }
        }

        return missingPermissions;
    }

    private void initPermissions() {
        Log.d(TAG, "initPermissions: ");
        Set<String> missingPermissions = getMissingPermissions();

        if(!missingPermissions.isEmpty()) {
            requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]), GE_REQUEST_PERMISSION);
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        for (int i = 0; i < permissions.length; i++) {
            Log.d(TAG, "onRequestPermissionsResult: " + permissions[i] + ": " + grantResults[i]);

            if(PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                if(permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    progressBar.setVisibility(View.VISIBLE);
                    videoFilesListAdapter = VideoFilesListAdapter.getInstance(getApplicationContext(), getFilesListCompletionListener);
                }
            } else {
                finishAffinity();
            }
        }
    }
}
