package com.globaledge.geplayer;


import android.support.annotation.NonNull;

import java.util.LinkedList;
public class VideoFilesList<VideFile> extends LinkedList {
    public int getPreviousIndex(int index) {
        int nextIndex = -1;

        if(index <= indexOf(getFirst())) {
            nextIndex = indexOf(getLast());
        } else {
            nextIndex = index - 1;
        }

        return nextIndex;
    }

    public int getNextIndex(int index) {
        int nextIndex = -1;

        if(index >= indexOf(getLast())) {
            nextIndex = indexOf(getFirst());
        } else {
            nextIndex = index + 1;
        }

        return nextIndex;
    }

    public VideoFile getNextOf(@NonNull VideoFile file) {
        return (VideoFile) get(getNextIndex(indexOf(file)));
    }

    public VideoFile getPrevOf(@NonNull VideoFile file) {
        return (VideoFile) get(getPreviousIndex(indexOf(file)));
    }
}
