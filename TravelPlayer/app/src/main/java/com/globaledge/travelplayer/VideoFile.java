package com.globaledge.travelplayer;

import android.graphics.Bitmap;
import android.net.Uri;

class VideoFile {
    //public static List<VideoFile> videoFileList = Collections.synchronizedList(new LinkedList<VideoFile>());
    private String title;
    private Uri path;
    private Bitmap thumbnail;

    public VideoFile(String title, Uri path) {
        this.title = title;
        this.path = path;;
    }

    public VideoFile(String title, Uri path, Bitmap thumbnail) {
        this.title = title;
        this.path = path;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getUri() {
        return path;
    }

    public void setUri(Uri path) {
        this.path = path;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "VideoFile [ " + "Title: " + title + ", Path: " + path.toString() + " ]";
        //(new File(path.getPath()))
    }
}
