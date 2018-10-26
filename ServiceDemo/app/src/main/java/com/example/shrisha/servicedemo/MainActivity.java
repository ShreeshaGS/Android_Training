package com.example.shrisha.servicedemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int externalStoragePermission;
    TextView musicFileName;
    ImageView musicIcon;
    Intent intentMusic;
    private static String fileName;

    Uri audioUri = null;
    Cursor cursor;
    MediaMetadataRetriever mediaMetadataRetriever = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        externalStoragePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (externalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            musicFileName = (TextView) findViewById(R.id.music_file_name);
            musicFileName.setMovementMethod(new ScrollingMovementMethod());
            musicIcon = (ImageView) findViewById(R.id.music_icon);
            fileName = musicFileName.getText().toString();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaMetadataRetriever != null) {
            mediaMetadataRetriever.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        fileName = musicFileName.getText().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicFileName.setText(fileName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast
                    .makeText(MainActivity.this, "READ STORAGE ACCESS granted", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast
                    .makeText(MainActivity.this, "Storage permission is necessary. Allow the app to read external storage to search music.", Toast.LENGTH_LONG)
                    .show();
            finish();
        }
    }

    public void chooseMusicFileFromStorage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setType("audio/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case RESULT_CANCELED:
                        Toast.makeText(MainActivity.this, "RESULT_CANCELED", Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_OK:
                        if (data != null) {
                            audioUri = data.getData();
                        }
                        //musicFileName.setText(audioUri.getPath());
                        //audioUri.
                        cursor = getContentResolver().query(audioUri, null, null, null, null);
                        //musicFileName.setText(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        //musicFileName.setText(audioUri.getPath());
                        musicFileName.setText(getPathFromUri(audioUri));
                        fileName = musicFileName.getText().toString();
                        //mediaMetadataRetriever = new MediaMetadataRetriever();
                        //musicFileName.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                        Log.d("--------" + MainActivity.class.getSimpleName() + "--------", "audioUri.getPath(): " + audioUri.getPath());
                        Log.d("--------" + MainActivity.class.getSimpleName() + "--------", "ALBUM NAME: " + musicFileName.getText().toString());
                        //Log.d("--------" + MainActivity.class.getSimpleName() + "--------", "Uri content: " + audioUri.toString());
                        //mediaMetadataRetriever.setDataSource(audioUri.getEncodedPath());
                        //musicIcon.setImageBitmap(BitmapFactory.decodeByteArray(mediaMetadataRetriever.getEmbeddedPicture(), 0, mediaMetadataRetriever.getEmbeddedPicture().length));
                        musicIcon.setVisibility(View.VISIBLE);
                        break;

                    default:
                        Toast.makeText(MainActivity.this, "something else happened", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            default:
                Toast.makeText(MainActivity.this, "result of some other activity", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void startMusicService(View view) {
        if (audioUri != null) {
            intentMusic = new Intent("PLAY_MUSIC", audioUri, MainActivity.this, AudioService.class);
            startService(intentMusic);
        } else {
            Toast.makeText(MainActivity.this, "Select an audio file first", Toast.LENGTH_LONG).show();
        }
    }

    public void stopMusicService(View view) {
        if (audioUri != null) {
            stopService(intentMusic);
        } else {
            Toast.makeText(MainActivity.this, "No audio being played at the moment", Toast.LENGTH_LONG).show();
        }
    }

    public String getPathFromUri(Uri uri) {
        String filePath = "default ";
        String[] data = { MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DISPLAY_NAME };
        Cursor cursor = getApplicationContext().getContentResolver().query(
                uri,
                data,
                //null,
                null,
                null,
                null);
        //Log.d("--------" + MainActivity.class.getSimpleName() + "--------" + "count: ", String.valueOf( cursor.getColumnCount()));
        //String[] colNames = cursor.getColumnNames();
        if (cursor != null) {
            int col_index = cursor.getColumnIndexOrThrow(data[0]);
            cursor.moveToFirst();
            filePath = cursor.getString(col_index);
            if (filePath != null && filePath.length() > 0) {
                Log.d("---- filePath ----", filePath);
            } else {
                col_index = cursor.getColumnIndexOrThrow(data[1]);
                cursor.moveToFirst();
                filePath = cursor.getString(col_index);
            }
            cursor.close();
        }


        //filePath = audioUri.getPath();
        return filePath;
    }
}
