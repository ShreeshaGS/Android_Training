package com.globaledge.geplayer;

import android.graphics.Bitmap;
import android.net.Uri;

public class VideoFile {
    private Uri uri;
    private VideoInfo videoInfo;

    public class VideoInfo {
        private String title;
        private Bitmap thumbnail;

        public VideoInfo(String title, Bitmap thumbnail) {
            this.title = title;
            this.thumbnail = thumbnail;
        }

        protected void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        protected void setThumbnail(Bitmap thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Bitmap getThumbnail() {
            return thumbnail;
        }
    }

    public VideoFile(Uri uri, String title, Bitmap thumbnail) {
        this.uri = uri;
        this.videoInfo = new VideoInfo(title, thumbnail);
    }

    public Uri getUri() {
        return uri;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }
}
