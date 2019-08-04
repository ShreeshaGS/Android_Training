package com.globaledge.geplayer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class VideoFilesListAdapter extends RecyclerView.Adapter<VideoFilesListAdapter.VideoFileViewHolder> {
    private static final String TAG = "VideoFilesListAdapter";

    private Context context;
    private static VideoFilesList<VideoFile> videoFilesList;
    MediaMetadataRetriever retriever;
    private BitmapLoaderTask loaderTask;
    private RecyclerView.AdapterDataObserver adapterDataObserver;

    public VideoFilesListAdapter(Context context, VideoFilesList<VideoFile> videoFilesList) {
        this.context = context;
        //this.videoFilesList = videoFilesList;
        VideoFilesListAdapter.videoFilesList = videoFilesList;
        retriever = new MediaMetadataRetriever();
        //new ListBitmapLoaderTask(videoFilesList).execute();

    }

    public static VideoFilesList<VideoFile> getVideoFilesList() {
        return videoFilesList;
    }

    @NonNull
    @Override
    public VideoFileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View videoItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_entry_video_file, viewGroup, false);
        return new VideoFileViewHolder(videoItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoFileViewHolder videoFileViewHolder, int position) {
        final VideoFile videoFile = (VideoFile) videoFilesList.get(position);

        videoFileViewHolder.title.setText(videoFile.getVideoInfo().getTitle());
        videoFileViewHolder.videoUri = videoFile.getUri();
        videoFileViewHolder.index = videoFilesList.indexOf(videoFile);
        if(null == videoFile.getVideoInfo().getThumbnail()) {
            videoFileViewHolder.setDefaultBitmap();
            loaderTask = new BitmapLoaderTask(videoFileViewHolder.imageView, videoFile);
            loaderTask.execute(videoFile.getUri());
        } else {
            videoFileViewHolder.imageView.setImageBitmap(videoFile.getVideoInfo().getThumbnail());
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoFileViewHolder holder) {
        if(loaderTask.getStatus().equals(AsyncTask.Status.RUNNING) | loaderTask.getStatus().equals(AsyncTask.Status.PENDING)) {
            loaderTask.cancel(true);
        }
    }

    @Override
    public int getItemCount() {
        return videoFilesList.size();
    }

    private class BitmapLoaderTask extends AsyncTask<Uri, Void, Bitmap> {
        WeakReference<ImageView> imageViewWeakReference;
        WeakReference<VideoFile> videoFileWeakReference;

        BitmapLoaderTask(ImageView imageView, VideoFile videoFile) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
            videoFileWeakReference = new WeakReference<VideoFile>(videoFile);
        }

        @Override
        protected Bitmap doInBackground(Uri... uris) {

            WeakReference<Bitmap> videoBitmap;
            long videoDuration;
            //MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            retriever.setDataSource(uris[0].getPath());

            videoDuration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            videoBitmap = new WeakReference<Bitmap>( retriever.getFrameAtTime(videoDuration / 10, MediaMetadataRetriever.OPTION_CLOSEST_SYNC));

            return videoBitmap.get();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = imageViewWeakReference.get();
            VideoFile videoFile = videoFileWeakReference.get();
            imageView.setImageBitmap(bitmap);
            videoFile.getVideoInfo().setThumbnail(bitmap);
        }
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

    public class VideoFileViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView imageView;
        private Uri videoUri;
        private int index;
        private VideoFile videoFile;

        public VideoFileViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textview_video_title);
            imageView = itemView.findViewById(R.id.img_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DefaultVideoPlayerActivity.class);
                    Log.d(TAG, "onClick: Index = " + index);
                    intent.putExtra("Index", index);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);

                }
            });

            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("File name", videoUri.getLastPathSegment());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Filename copied", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        public void setDefaultBitmap() {
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_media_play));
        }
    }
}
