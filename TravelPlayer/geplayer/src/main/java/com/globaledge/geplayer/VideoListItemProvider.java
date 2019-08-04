/*
package com.globaledge.exoplayerdemo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.car.widget.ListItem;
import androidx.car.widget.ListItemProvider;

public class VideoListItemProvider extends ListItemProvider {
    private VideoFilesList<VideoFile> videoFilesList;
    private Context context;

    public VideoListItemProvider(VideoFilesList<VideoFile> videoFilesList, Context context) {
        this.videoFilesList = videoFilesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListItem get(int position) {
        return (ListItem) videoFilesList.get(position);
    }

    @Override
    public int size() {
        return videoFilesList.size();
    }
}
*/
