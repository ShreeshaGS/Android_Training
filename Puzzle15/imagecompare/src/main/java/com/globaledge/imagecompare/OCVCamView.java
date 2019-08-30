package com.globaledge.imagecompare;

import org.opencv.android.JavaCameraView;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

public class OCVCamView extends JavaCameraView implements Camera.PictureCallback {
    private static final String TAG = "OCVCamView";

    private String mPictureFilename;

    public OCVCamView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void takePicture(final String fileName) {
        mPictureFilename = fileName;
        mCamera.setPreviewCallback(null);
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);

        try {
            FileOutputStream fos = new FileOutputStream(mPictureFilename);

            fos.write(bytes);
            fos.close();

        } catch (IOException e) {
            Log.e(TAG, "onPictureTaken: Exception in callback" );
        }
    }
}
