package com.globaledge.geplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoPlayerActivity extends AppCompatActivity {
    private PlayerView videoView;
    private Intent intent;
    private Uri videoUri;
    private String videoTitle;
    private Player player;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView.findViewById(R.id.view_exoplayer);
        context = getApplicationContext();

        initializePlayer();
    }

    private void initializePlayer() {
        //DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        //LoadControl loadControl = new DefaultLoadControl();
        //RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        player = ExoPlayerFactory.newSimpleInstance(context);
    }
}
