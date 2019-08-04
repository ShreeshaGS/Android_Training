package com.globaledge.orientationdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button btnPortrait;
    Button btnLandscape;
    Button btnUpsideDown;
    Button btnSeascape;

    Button btnToggleOrientation;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    private static String[] orientations = {"Portrait", "Landscape"};
    int currentRotation;

    WindowManager windowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        windowManager = MainActivity.this.getWindowManager();
        //windowManager.getDefaultDisplay().
        //initialise();



        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select orientation:");

        Log.d(TAG, "onCreate: currentOrientation: " + currentRotation);

        btnToggleOrientation = findViewById(R.id.btn_orientation);
        btnToggleOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentRotation();
                builder.setSingleChoiceItems(orientations, currentRotation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(i != currentRotation) {
                            if(0 == i) {
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            } else if (1 == i) {
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            }
                        }
                    }
                });

                /*builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int selectedItemPosition = ((AlertDialog)dialogInterface).getListView().getSelectedItemPosition();
                    }
                });*/

                builder.show();
            }
        });

    }

    private void setCurrentRotation() {
        switch (windowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                currentRotation = 0; //orientation.PORTRAIT
                break;

            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                currentRotation = 1; //orientation.LANDSCAPE
                break;
        }
    }

    /*private void initialise() {


        btnPortrait = findViewById(R.id.btn_portrait);
        btnLandscape = findViewById(R.id.btn_landscape);
        btnUpsideDown = findViewById(R.id.btn_upside_down);
        btnSeascape = findViewById(R.id.btn_seascape);

        btnPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upDateOrientation(SurfaceView.ROTATION_0);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        btnLandscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        btnUpsideDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            }
        });

        btnSeascape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
        });
    }*/
}
