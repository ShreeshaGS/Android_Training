package com.globaledge.geplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

//import androidx.annotation.NonNull;
//import androidx.car.widget.ListItemAdapter;
//import androidx.car.widget.PagedListView;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;


public class VideoBrowserActivity extends AppCompatActivity {
    private static final String TAG = "VideoBrowserActivity";

    private static final int GE_REQUEST_PERMISSION = new Random().nextInt(1000);
    private final static String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    //private HashMap<Uri, VideoFile> videoFileHashMap;

    private ImageView splashScreen;
    private ContentResolver contentResolver;

    //private PagedListView pagedListView;
    private LinkedList<VideoFile> videoFilesList;
    private VideoFilesListAdapter videoFilesListAdapter;
    //private VideoListItemProvider videoListItemProvider;
    //ListItemAdapter listItemAdapter;
    private RecyclerView recyclerView;
    //private MediaMetadataRetriever retriever;

    /*private Bitmap getThumbnail(Uri uri) {
        Bitmap videoBitmap;
        long videoDuration;
        //MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.getPath());

        videoDuration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        videoBitmap = retriever.getFrameAtTime(videoDuration / 10, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

        return videoBitmap;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        splashScreen = (ImageView) findViewById(R.id.logo);
        //pagedListView  = (PagedListView) findViewById(R.id.PagedListView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        contentResolver = getContentResolver();

        initPermissions();


    }

    private void initPermissions() {
        Log.d(TAG, "initPermissions: ");
        Set<String> missingPermissions = getMissingPermissions();

        if(!missingPermissions.isEmpty()) {
            requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]), GE_REQUEST_PERMISSION);
        } else {
            videoFilesList = new VideoFilesList<VideoFile>();
            populateList();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    videoFilesListAdapter = new VideoFilesListAdapter(getApplicationContext(), (VideoFilesList<VideoFile>) videoFilesList);
                    recyclerView.setAdapter(videoFilesListAdapter);
                    splashScreen.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }, 2000);
            /*videoListItemProvider = new VideoListItemProvider((VideoFilesList<VideoFile>) videoFilesList, getApplicationContext());
            listItemAdapter = new ListItemAdapter(getApplicationContext(), videoListItemProvider);
            pagedListView.setAdapter(listItemAdapter);*/
            //splashScreen.setVisibility(View.INVISIBLE);

        }
    }

    private void populateList() {
        Cursor cursor;
        //videoFileHashMap = new HashMap<>();
        Uri[] sources = new Uri[] {
                MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        };
        String[] projection = new String[] {
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.Thumbnails.DATA,
                //MediaStore.Video.Thumbnails.VIDEO_ID ---- deprecated
        };
        //retriever = new MediaMetadataRetriever();
        for (Uri source: sources) {
            cursor = contentResolver.query(source,
                    projection,
                    null,
                    null,
                    MediaStore.Video.Media.DATE_MODIFIED + " DESC");
            if(null != cursor && cursor.moveToFirst()) {
                do {
                    //byte[] blob = cursor.getBlob(cursor.getColumnIndex(MediaStore.Video.Thumbnails.getThumbnail(contentResolver, )));
                    Bitmap bitmap = null;
                    /*Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(
                            contentResolver,
                            //cursor.getLong(1),
                            //Long.getLong(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.VIDEO_ID))),
                            cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Thumbnails.VIDEO_ID)),
                            MediaStore.Video.Thumbnails.MINI_KIND,
                            null);*/
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_play);

                    /*try {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA))));
                    } catch (IOException e) {
                        e.printStackTrace();
                        bitmap = BitmapFactory.decodeResource(getResources(), R.id.img_thumbnail);
                    }*/
                    //Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(Uri.parse(String.valueOf(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA))).getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                    //Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Uri.parse(String.valueOf(cursor.getColumnIndex(MediaStore.Video.Media.DATA))).getPath()), 80, 80);
                    if(!cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)).startsWith("/storage")) {
                        continue;
                    }
                    VideoFile videoFile = new VideoFile(
                            Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA))),
                            Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA))).getLastPathSegment(),
                            //BitmapFactory.decodeByteArray(blob, 0, blob.length)
                            bitmap
                    );
                    videoFilesList.add(videoFile);
                    //videoFileHashMap.put(videoFile.getUri(), videoFile);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //new ListBitmapLoaderTask((VideoFilesList<VideoFile>) videoFilesList).execute();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        for (int i = 0; i < permissions.length; i++) {
            Log.d(TAG, "onRequestPermissionsResult: " + permissions[i] + ": " + grantResults[i]);

            /*if(PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                if(permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                }
            }*/
        }
        initPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()) {

            case R.id.action_quit:
                finishAndRemoveTask();
                return true;

            case R.id.action_refresh:
                //Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
                videoFilesList.clear();
                populateList();
                videoFilesListAdapter.notifyDataSetChanged();
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

    /*private class ListBitmapLoaderTask extends AsyncTask<Void, Void, Void> {
        VideoFilesList<VideoFile> videoFilesList;
        VideoFile videoFile;
        ListBitmapLoaderTask(VideoFilesList<VideoFile> videoFilesList) {
            this.videoFilesList = videoFilesList;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            for(Object videoFile: videoFilesList) {
                if(null == ((VideoFile) videoFile).getVideoInfo().getThumbnail()) {
                    WeakReference<Bitmap> videoBitmap;
                    long videoDuration;
                    //MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    //retriever.setDataSource(uris[0].getPath());
                    retriever.setDataSource(((VideoFile)videoFile).getUri().getPath());

                    videoDuration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                    videoBitmap = new WeakReference<Bitmap>( retriever.getFrameAtTime(videoDuration / 10, MediaMetadataRetriever.OPTION_CLOSEST_SYNC));
                    ((VideoFile)videoFile).getVideoInfo().setThumbnail(videoBitmap.get());
                }
            }
            return null;
        }
    }*/

    /*public class BitmapLoaderTask extends AsyncTask<Void, Void, Void> {
        //VideoFilesList<VideoFile> videoFilesList;
        VideoFile videoFile;
        BitmapLoaderTask(VideoFile videoFile) {
            this.videoFile = videoFile;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            if(null == videoFile.getVideoInfo().getThumbnail()) {
                WeakReference<Bitmap> videoBitmap;
                long videoDuration;
                //MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                //retriever.setDataSource(uris[0].getPath());
                retriever.setDataSource(((VideoFile)videoFile).getUri().getPath());

                videoDuration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                videoBitmap = new WeakReference<Bitmap>( retriever.getFrameAtTime(videoDuration / 10, MediaMetadataRetriever.OPTION_CLOSEST_SYNC));
                videoFile.getVideoInfo().setThumbnail(videoBitmap.get());
            }
            return null;
        }
    }*/
}
