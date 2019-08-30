package com.globaledge.imagecompare;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageCompareActivity extends AppCompatActivity {
    private static final String TAG = "ImageCompareActivity";

    private Uri capturedImage;
    private Uri referenceImage;

    private TextView textView_captured;
    private TextView textView_reference;
    private TextView textView_result;

    private ImageView imageView_captured;
    private ImageView imageView_reference;
    private ImageView imageView_result;


    private Intent intent;
    private Bitmap bitmap_captured;
    private Bitmap bitmap_reference;
    private Bitmap bitmap_result;

    private Mat mat_captured;
    private Mat mat_reference;
    private Mat mat_result;
    private int mat_result_rows;
    private int mat_result_cols;
    private Point matchLoc;     //matching location in the image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_compare);

        intent = getIntent();

        capturedImage = intent.getParcelableExtra("capturedImage");
        referenceImage = intent.getParcelableExtra("referenceImage");

        textView_captured = findViewById(R.id.text_title_captured);
        textView_reference = findViewById(R.id.text_title_reference);
        textView_result = findViewById(R.id.text_title_result);

        imageView_captured = findViewById(R.id.img_captured);
        imageView_reference = findViewById(R.id.img_reference);
        imageView_result = findViewById(R.id.img_result);
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView_captured.setText(getPathFromUri(this, capturedImage));
        textView_reference.setText(getPathFromUri(this, referenceImage));

        //bitmap_captured = BitmapFactory.decodeFile(capturedImage.getPath());
        //setImageFromBitmap(imageView_captured, bitmap_captured, capturedImage);
        /*Toast.makeText(this, "capturedImage PATH: " + getPathFromUri(this, capturedImage), Toast.LENGTH_LONG).show();
        bitmap_captured = BitmapFactory.decodeFile(getPathFromUri(this, capturedImage));
        //imageView_captured.setImageBitmap(bitmap_captured);

        bitmap_reference = BitmapFactory.decodeFile(getPathFromUri(this, referenceImage));
        imageView_reference.setImageBitmap(bitmap_reference);*/

/*        Mat mat_captured = new Mat(bitmap_captured.getHeight(), bitmap_captured.getWidth(), CvType.CV_8UC4);

        Utils.bitmapToMat(bitmap_captured, mat_captured);
        byte[] blob = new byte[(int) (mat_captured.total() * mat_captured.channels())];
        mat_captured.put(0, 0, blob);
        bitmap_captured = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        imageView_captured.setImageBitmap(bitmap_captured);*/
        /*mat_captured = new Mat(bitmap_captured.getHeight(), bitmap_captured.getWidth(), CvType.CV_8UC4, new Scalar(4));
        mat_reference = new Mat(bitmap_reference.getHeight(), bitmap_reference.getWidth(), CvType.CV_8UC4, new Scalar(4));*/



        mat_captured = Imgcodecs.imread(textView_captured.getText().toString()/*, Imgcodecs.IMREAD_COLOR -- default*/);
        setImage(imageView_captured, mat_captured);

        mat_reference = Imgcodecs.imread(textView_reference.getText().toString());
        setImage(imageView_reference, mat_reference);

        mat_result_rows = mat_captured.rows() - mat_reference.cols() + 1;
        mat_result_cols = mat_captured.cols() - mat_reference.cols() + 1;
        mat_result = new Mat(mat_result_rows, mat_result_cols, CvType.CV_32FC1, new Scalar(4));

        Imgproc.matchTemplate(mat_captured, mat_reference, mat_result, Imgproc.TM_CCORR);
        Core.normalize(mat_result, mat_result, 0, 1, Core.NORM_MINMAX, -1, new Mat());  //"normalize" the result matrix
        //For match methods TM_SQDIFF and TM_SQDIFF_NORMED, best match is given by lowest values, higher is better for all other match methods.
        matchLoc = Core.minMaxLoc(mat_result).maxLoc;

        //draw rectangle around matching area
        Imgproc.rectangle(mat_result, matchLoc, new Point(matchLoc.x + mat_reference.cols(), matchLoc.y + mat_reference.rows()), new Scalar(0, 0, 0), 2, 8, 0);
        mat_result.convertTo(mat_result, CvType.CV_8UC4, 255.0);
        setImage(imageView_result, mat_result);
        /*byte[] blob = new byte[(int) (mat_result.total() * mat_result.channels())];
        bitmap_result = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        if(null != bitmap_result) {
            Utils.matToBitmap(mat_result, bitmap_result);

            textView_result.setText("Result image");
            imageView_result.setImageBitmap(bitmap_result);
        }*/

    }

    private void setImage(ImageView imageView, Mat mat) {
        //Utils.matToBitmap();
        Mat newMat = null;
        //byte[] blob = new byte[(int) (mat.total() * mat.channels())];

        //mat.put(0, 0, blob);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);
        newMat = mat.clone();

        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
        Bitmap bitmap = Bitmap.createBitmap(newMat.cols(), newMat.rows(), Bitmap.Config.ARGB_8888);

        if(null != bitmap) {
            Utils.matToBitmap(newMat, bitmap);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(ImageCompareActivity.this, "matToBitmap failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPathFromUri(Context context, Uri imageUri) {
        //String resultPath = null;

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if(DocumentsContract.isDocumentUri(context, imageUri)) {
                final String docId = DocumentsContract.getDocumentId(imageUri);
                final String[] splitID = docId.split(":");
                String type = splitID[0];

                if(isExternalStorageDocument(imageUri)) {

                    if("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + splitID[1];
                    }
                } else if (isMediaDocument(imageUri)) {
                    return getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_id=?", new String[]{splitID[1]});
                }
            }
        } else if ( "content".equalsIgnoreCase(imageUri.getScheme())){
            return getDataColumn(context, imageUri, null, null);
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*private void setImageFromBitmap(ImageView imageView, Bitmap bitmap, Uri imageUri) {
        if(null != imageUri.getPath()) {
            if(null == bitmap) {
                Log.d(TAG, "setImageFromBitmap: bitmap is null. trying to set to uri.");
                bitmap = BitmapFactory.decodeFile(imageUri.getPath());
            } else {
                int height = bitmap.getHeight(), width = bitmap.getWidth();
                if (height > 1280 && width > 960) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap newBitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);
                    imageView.setImageBitmap(newBitmap);
                    Log.d(TAG, "setImageFromBitmap: imageView set to bitmap " + imageUri.getPath());
                }
            }
        } else {
            Log.d(TAG, "setImageFromBitmap: imageUri is null.");
        }
    }*/
}
