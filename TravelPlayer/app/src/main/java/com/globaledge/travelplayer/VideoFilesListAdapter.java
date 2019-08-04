package com.globaledge.travelplayer;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class VideoFilesListAdapter extends RecyclerView.Adapter<VideoFilesListAdapter.VideoFileViewHolder>{
    private static final String APP = "GETraveler|";
    private static final String TAG = "ListAdapter";
    private static final String[] supportedVideoFormats = new String[]{
            ".3gp",
            ".flv",
            ".mkv",
            ".mp4"
    };

    private Window window;

    private Context context;
    private List<VideoFile> videoFiles;
    private ContentResolver contentResolver;
    private Cursor cursor;
    private static VideoFilesListAdapter videoFilesListAdapterInstance;
    //private MediaMetadataRetriever mediaMetadataRetriever;

    public static VideoFilesListAdapter getInstance(Context context) {
        if(null == videoFilesListAdapterInstance) {
            videoFilesListAdapterInstance = new VideoFilesListAdapter(context);
        }
        return videoFilesListAdapterInstance;
    }

    private VideoFilesListAdapter(Context context) {
        this.context = context;
        //this.videoFiles = new ArrayList<VideoFile>();
        this.videoFiles = Collections.synchronizedList(new LinkedList<VideoFile>());
        //this.videoFiles = VideoFile.videoFileList;
        contentResolver = context.getContentResolver();
        //mediaMetadataRetriever = new MediaMetadataRetriever();



        //listStoragePaths(null);
        resolveVideoContents();
/*
        RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                videoFiles.clear();
                //listStoragePaths(null);
            }
        };

        registerAdapterDataObserver(adapterDataObserver);*/
    }

    private void resolveVideoContents() {
        if(PackageManager.PERMISSION_DENIED != context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            contentResolver = context.getContentResolver();
            cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Thumbnails.DATA},
                    null,
                    null,
                    MediaStore.Video.VideoColumns.DATE_MODIFIED + " DESC");
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                    VideoFile videoFile = new VideoFile(Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA))).getLastPathSegment(),
                            Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA))),
                            BitmapFactory.decodeByteArray(blob, 0, blob.length));
                    videoFiles.add(videoFile);
                    Log.d(TAG, "resolveVideoContents: " + videoFile.toString());
                } while (cursor.moveToNext());
            }
            cursor = contentResolver.query(MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Thumbnails.DATA},
                    null,
                    null,
                    MediaStore.Video.VideoColumns.DATE_MODIFIED);
            if (null != cursor && cursor.moveToFirst()) {
                do {
                        byte[] blob = cursor.getBlob(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                    VideoFile videoFile =new VideoFile(Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA))).getLastPathSegment(),
                            Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA))),
                            BitmapFactory.decodeByteArray(blob, 0, blob.length));
                    videoFiles.add(videoFile);
                    Log.d(TAG, "resolveVideoContents: " + videoFile.toString());
                } while (cursor.moveToNext());
            }
        }
    }


    private void listStoragePaths(File path) {
        if(PackageManager.PERMISSION_DENIED != context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (null == path) {

                String[] rootDirs = new String[]{

                        String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())
                        //String.valueOf(Environment.getRootDirectory().getAbsolutePath())
                };
                Log.d(APP + TAG, "listStoragePaths: getExtStorageDir: " + Environment.getExternalStorageDirectory().getAbsolutePath());

                for (String dir : rootDirs) {
                    parseDir(dir);

                }
            } else {
                parseDir(path.getAbsolutePath());

            }
        }
    }

    private void parseDir(String dir) {
        File storage = new File(dir);
        File[] files = storage.listFiles();
        //Toast.makeText(context, "Currently at " + dir, Toast.LENGTH_SHORT).show();
        for (File file : files) {
            if(file.isDirectory()) {
                listStoragePaths(file);
            } else if(file.isFile()) {
                for (String format: supportedVideoFormats) {
                    if (file.getName().endsWith(format)) {
                        Log.d(APP + TAG, "file: " + file.getAbsolutePath());
                        if (null != videoFiles) {
                            videoFiles.add(new VideoFile(file.getName(), Uri.fromFile(file)));
                        }
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    public VideoFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View videoInfo = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_entry_video_file, parent, false);
        return new VideoFileViewHolder(videoInfo);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFileViewHolder holder, int position) {
        VideoFile videoFile = videoFiles.get(position);

        holder.videoFileName.setText(videoFile.getTitle());
        holder.videoFileUri = videoFile.getUri();
        holder.videoFileIndex = position;
        //holder.thumbnail.setImageBitmap(getThumbnail((videoFile.getUri())));
        holder.thumbnail.setImageBitmap(videoFile.getThumbnail());
        //(new BitMapLoader(holder.thumbnail)).execute(videoFile.getUri());
        //loadBitmap(videoFile.getUri(), holder.thumbnail);

    }

    /*private void loadBitmap(Uri uri, ImageView thumbnail) {
        final Bitmap bitmap = getBitmapFromCache(uri);
        if(bitmap != null) {
            thumbnail.setImageBitmap(bitmap);
        } else {
            BitmapLoaderTask bitMapLoaderTask = new BitmapLoaderTask(bitmap);
            bitMapLoaderTask.execute(uri);
            thumbnail.setImageBitmap(bitmap);
        }
    }*/

    /*class BitmapLoaderTask extends AsyncTask<Uri, Void, Bitmap> {
        private Bitmap bitmap;
        public BitmapLoaderTask(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected Bitmap doInBackground(Uri... uris) {
            bitmap = getThumbnail(uris[0]);
            addBitmapToCache(uris[0], bitmap);
            return bitmap;
        }
    }*/

    private Bitmap getThumbnail(Uri uri) {
        Bitmap videoBitmap;
        long videoDuration;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        synchronized (retriever) {
            //MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri.getPath());

            videoDuration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            videoBitmap = retriever.getFrameAtTime(videoDuration / 10, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        }
        return videoBitmap;
    }

    /*class BitMapLoader extends AsyncTask<Uri, Void, Bitmap> {
        private WeakReference<ImageView> imageViewWeakReference;
        private WeakReference<Long> videoDuration;
        private WeakReference<MediaMetadataRetriever> retrieverWeakReference;

        public BitMapLoader(ImageView imageView) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
            retrieverWeakReference = new WeakReference<MediaMetadataRetriever>(mediaMetadataRetriever);
        }

        @Override
        protected Bitmap doInBackground(Uri... uris) {
            retrieverWeakReference.get().setDataSource(String.valueOf(uris[0]));
            videoDuration = new WeakReference<Long>(Long.parseLong(retrieverWeakReference.get().extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
            return retrieverWeakReference.get().getFrameAtTime(videoDuration.get() / 10, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(imageViewWeakReference != null &&  bitmap != null) {
                imageViewWeakReference.get().setImageBitmap(bitmap);
            }
        }
    }*/

    @Override
    public int getItemCount() {
        return videoFiles.size();
    }

    public VideoFile getVideoFileFromID(int videoIndex) {
        return videoFiles.get(videoIndex);
    }

    public int getNextVideoIndex(int videoIndex) {
        int nextVideoIndex = -1;
        if(videoFiles.size() == videoIndex) {
            nextVideoIndex = videoFiles.indexOf(videoFiles.get(0));
        }
        return nextVideoIndex;

        //return (getVideoFileFromID(videoIndex))
        //videoFiles.
    }

    public int getPrevVideoIndex(int videoIndex) {
        int nextVideoIndex = -1;
        if(videoFiles.indexOf(videoFiles.get(0)) == videoIndex) {
            nextVideoIndex = videoFiles.size();
        }
        return nextVideoIndex;
    }

    public class VideoFileViewHolder extends RecyclerView.ViewHolder{
        private int videoFileIndex;
        private TextView videoFileName;
        private ImageView thumbnail;

        private Uri videoFileUri;

        public VideoFileViewHolder(final View itemView) {
            super(itemView);
            videoFileName = itemView.findViewById(R.id.textview_video_file);
            thumbnail = itemView.findViewById(R.id.img_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(APP+TAG, "onClick: ");
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.setData(videoFileUri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra("VideoFileIndex", videoFileIndex);
                    intent.putExtra("VideoTitle", videoFileName.getText());
                    context.startActivity(intent);
                }
            });

            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("File name", videoFileUri.getPath());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Filename copied", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

    }
}
