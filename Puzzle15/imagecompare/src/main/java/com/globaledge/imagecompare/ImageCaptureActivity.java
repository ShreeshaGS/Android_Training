package com.globaledge.imagecompare;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class ImageCaptureActivity extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "ImageCaptureActivity";

    private OCVCamView cameraView;
    private FloatingActionButton btnClickPicture;
    private File masterDir;
    private String masterDirName = null;


    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    cameraView.enableView();
                    break;

                case LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION:
                    break;

                case LoaderCallbackInterface.INIT_FAILED:
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        cameraView = (OCVCamView) findViewById(R.id.camera_view);
        btnClickPicture = (FloatingActionButton) findViewById(R.id.btn_camera_control);
        Intent data = getIntent();
        masterDirName = data.getStringExtra("masterDir");
        //cameraView.setActivated(false);
        btnClickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String currentDateandTime = sdf.format(new Date());
                String fileName = masterDirName +
                        "/TAF_Img_" + currentDateandTime + ".jpg";
                cameraView.takePicture(fileName);
                Toast.makeText(ImageCaptureActivity.this, fileName + " saved", Toast.LENGTH_SHORT).show();
                //data.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                setResult(RESULT_OK);
                finish();
            }
        });

        cameraView.enableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }
}
