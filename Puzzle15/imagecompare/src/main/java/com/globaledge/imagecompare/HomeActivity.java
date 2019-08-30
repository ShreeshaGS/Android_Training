package com.globaledge.imagecompare;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private Button btnCaptureImage;
    private Button btnSelectCapturedImage;
    private Button btnSelectReferenceImage;
    private Button btnCompareImage;

    private File masterDir = null;
    private String masterDirName = null;

    private static final int CODE_REQUEST_CAPTURE = 1;
    private static final int CODE_REQUEST_SELECT_CAPTURED = 2;
    private static final int CODE_REQUEST_SELECT_REFERENCE = 3;
    private static final int CODE_REQUEST_COMPARE_IMAGE = 4;

    private static final int CODE_REQUEST_PERMISSION = new Random().nextInt(1000);
    private final static String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Uri capturedImage;
    private Uri referenceImage;

    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            //super.onManagerConnected(status);
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:

                    if(OpenCVLoader.initDebug()) {
                        initSession();
                    }
                    initUI();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_compare_home);
        Log.d(TAG, "onCreate: ");
        btnCaptureImage = findViewById(R.id.btn_capture);
        btnSelectCapturedImage = findViewById(R.id.btn_select_captured_img);
        btnSelectReferenceImage = findViewById(R.id.btn_select_reference_img);
        btnCompareImage = findViewById(R.id.btn_compare);
        btnCompareImage.setEnabled(false);

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, baseLoaderCallback);

        initPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(canCompareBtnBeEnabled()) {
            btnCompareImage.setEnabled(true);
        }
        Log.d(TAG, "onResume: ");
    }

    private boolean canCompareBtnBeEnabled() {

        return ((null != capturedImage) && (null != referenceImage));
    }

    private void initUI() {

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ImageCaptureActivity.class);
                intent.putExtra("masterDir", masterDirName);
                startActivityForResult(intent, CODE_REQUEST_CAPTURE);
            }
        });

        btnSelectCapturedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setDataAndType(Uri.fromFile(masterDir), "image/*");
                Uri fileUri = FileProvider.getUriForFile(HomeActivity.this, getPackageName()+".provider", masterDir);
                intent.setDataAndType(fileUri, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, CODE_REQUEST_SELECT_CAPTURED);
            }
        });

        btnSelectReferenceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setDataAndType(Uri.fromFile(masterDir), "image/*");
                Uri fileUri = FileProvider.getUriForFile(HomeActivity.this, getPackageName()+".provider", masterDir);
                intent.setDataAndType(fileUri, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, CODE_REQUEST_SELECT_REFERENCE);
            }
        });

        btnCompareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ImageCompareActivity.class);
                intent.putExtra("capturedImage", capturedImage);
                intent.putExtra("referenceImage", referenceImage);
                startActivityForResult(intent, CODE_REQUEST_COMPARE_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_REQUEST_CAPTURE:
                switch (resultCode) {
                    case RESULT_OK:
                        Toast.makeText(HomeActivity.this, "Image captured successfully", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case CODE_REQUEST_SELECT_CAPTURED:
                switch (resultCode) {
                    case RESULT_OK:
                        //File selectedFile = new File(String.valueOf(data.getData()));
                        //Toast.makeText(HomeActivity.this, "Selected file: " + selectedFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        //capturedImage = Imgcodecs.imread(selectedFile.getName())
                        capturedImage = data.getData();
                        //showImageInAlertDialog(capturedImage);

                        break;

                    case RESULT_CANCELED:
                        capturedImage = null;
                        break;
                }
                break;

            case CODE_REQUEST_SELECT_REFERENCE:
                switch (resultCode) {
                    case RESULT_OK:
                        //Toast.makeText(HomeActivity.this, "Selected file: " + data.getData().getPath(), Toast.LENGTH_LONG).show();
                        referenceImage = data.getData();
                        //showImageInAlertDialog(referenceImage);
                        break;

                    case RESULT_CANCELED:
                        referenceImage = null;
                        break;
                }
                break;

            case CODE_REQUEST_COMPARE_IMAGE:
                switch (resultCode) {
                    case RESULT_OK:
                        //TODO: implement if needed
                        break;

                    case RESULT_CANCELED:
                        Log.d(TAG, "onActivityResult: RESULT_CANCELED");
                        break;
                }
                break;

        }
    }

    private void showImageInAlertDialog(Uri data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(HomeActivity.this).inflate(R.layout.image_alert_dialog, null);
        ImageView imageView = linearLayout.findViewById(R.id.img_opencv);
        TextView textView = linearLayout.findViewById(R.id.text_dummy);
        textView.setText("Showing selected image in AlertDialog. This AlertDialog can be discarded later.");
        //Bitmap bitmap = null;
        //Mat tmp = new Mat(capturedImage.rows(), capturedImage.cols(), CvType.CV_8UC4, new Scalar(4));
        /*try {
            Imgproc.cvtColor(capturedImage, tmp,
                    Imgproc.COLOR_RGB2RGBA, //Imgproc.COLOR_RGBA2mRGBA, //Imgproc.COLOR_RGB2RGBA,
                    4);
            bitmap = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bitmap);
        } catch (CvException e) {
            Log.d(TAG, "onActivityResult: " + e.toString());
        }*/
        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //bitmap = BitmapFactory.decodeFile(selectedFile.getPath(), options);

        //imageView.setImageBitmap(bitmap);
        imageView.setImageURI(data);
        builder.setTitle("Confirm Selected Image");

        builder.setView((View) linearLayout);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        for (int i = 0; i < permissions.length; i++) {
            Log.d(TAG, "onRequestPermissionsResult: " + permissions[i] + ": " + grantResults[i]);

            /*if(PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                if(permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    initSession();
                }
            }*/
        }
        initPermissions();
    }

    private void initPermissions() {
        Log.d(TAG, "initPermissions: ");
        Set<String> missingPermissions = getMissingPermissions();

        if(!missingPermissions.isEmpty()) {
            requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]), CODE_REQUEST_PERMISSION);
        } else {

            /*videoListItemProvider = new VideoListItemProvider((VideoFilesList<VideoFile>) videoFilesList, getApplicationContext());
            listItemAdapter = new ListItemAdapter(getApplicationContext(), videoListItemProvider);
            pagedListView.setAdapter(listItemAdapter);*/
            //splashScreen.setVisibility(View.INVISIBLE);

        }
    }

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

    private void initSession() {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());

        //set directoryname to proper one, like /storage/sdcard/0/TAFImages
        masterDirName = Environment.getExternalStorageDirectory() + "/" + "TAFImages";
        Toast.makeText(HomeActivity.this, "masterDirName: " + masterDirName, Toast.LENGTH_SHORT).show();
        masterDir = new File(masterDirName);

        if(!masterDir.exists()) {
            //Toast.makeText(ImageCaptureActivity.this, "This should not be toasted", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "initSession: masterDir does not exist. Creating..");
            if (masterDir.mkdir()) {
                Log.d(TAG, "initSession: masterDir created successfully");
                Toast.makeText(HomeActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d(TAG, "initSession: masterDir creation failed");
                Toast.makeText(HomeActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }
}
