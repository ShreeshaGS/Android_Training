package com.globaledge.geplayer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

public class DefaultVideoPlayerActivity extends AppCompatActivity {
    private static final String TAG = "DefaultPlayer";
    private MediaController mediaController;
    private VideoView videoView;
    private MediaPlayer mediaPlayer;

    private TelephonyManager telephonyManager;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private AudioAttributes audioAttributes;
    private AudioFocusRequest audioFocusRequest;

    private boolean videoWasPlaying = false;
    private long seekTime;
    private float defVolume;
    private float newVolume;
    private Intent intent;
    private VideoFile videoFile;
    private VideoFile newVideoFile;

    private VideoFilesList<VideoFile> videoFilesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player2);
        Log.d(TAG, "onCreate: ");

        videoView = (VideoView) findViewById(R.id.videoview_default);
        mediaController = new MediaController(DefaultVideoPlayerActivity.this);
        mediaController.setAnchorView(videoView.getRootView());
        videoView.setMediaController(mediaController);
        //SmediaPlayer = new MediaPlayer();

        videoFilesList = VideoFilesListAdapter.getVideoFilesList();

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        initialize();

        intent = getIntent();
        updateUI(null);
    }

    private void initialize() {
        Log.d(TAG, "initialize: ");

        audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        updatePause();
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS:
                        updatePause();
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        if(null != mediaPlayer) {
                            newVolume = (float) (defVolume * 0.7);
                            mediaPlayer.setVolume(newVolume, newVolume);
                        }
                        break;

                    case AudioManager.AUDIOFOCUS_GAIN:
                        if(null != mediaPlayer && newVolume != defVolume) {
                            mediaPlayer.setVolume(defVolume, defVolume);
                            newVolume = defVolume;
                        }
                        break;

                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                        if(null != mediaPlayer && newVolume != defVolume) {
                            mediaPlayer.setVolume(defVolume, defVolume);
                            newVolume = defVolume;
                        }
                        break;
                }
            }
        };

        if(null != telephonyManager) {
            telephonyManager.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE:
                            Log.d(TAG, "onCallStateChanged: CALL_STATE_IDLE");
                            //windowManager.removeView(view);
                            updatePlay();
                            break;

                        case TelephonyManager.CALL_STATE_RINGING:
                            Log.d(TAG, "onCallStateChanged: CALL_STATE_RINGING");
                            //windowManager.addView(view, params);
                            audioFocusChangeListener.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK);
                            break;

                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            Log.d(TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
                            updatePause();
                            break;

                        default:
                            Log.d(TAG, "onCallStateChanged: default state is NOTA..!!");
                            break;
                    }
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        }

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                .build();

        audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAcceptsDelayedFocusGain(false)
                .setAudioAttributes(audioAttributes)
                .setForceDucking(true)
                .setWillPauseWhenDucked(false)
                .setOnAudioFocusChangeListener(audioFocusChangeListener)
                .build();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mMediaPlayer) {
                if(AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.requestAudioFocus(audioFocusRequest)) {
                /*if(AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                        audioManager.requestAudioFocus(
                                audioFocusChangeListener,
                                AudioManager.STREAM_MUSIC | AudioManager.STREAM_NOTIFICATION,
                                //AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)) {
                                AudioManager.AUDIOFOCUS_GAIN)) {*/
                    Log.d(TAG, "onPrepared: AUDIOFOCUS_REQUEST_GRANTED");
                    defVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    newVolume = defVolume;
                    mediaPlayer = mMediaPlayer;
                    //Log.d(TAG, "onPrepared: DrmInfo: " + mediaPlayer.getDrmInfo().toString());
                    //mediaPlayer.start();
                    videoView.start();
                    //mediaController.show(videoView.getDuration());
                    //mediaController.show();
                } else {
                    Log.d(TAG, "onPrepared: Cannot play video at the time");
                    Toast.makeText(DefaultVideoPlayerActivity.this, "Could not play video " + videoFile.getVideoInfo().getTitle()+ "at the time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d(TAG, "onCompletion: end of playback");
                audioManager.abandonAudioFocus(audioFocusChangeListener);
                endPlaybackAndFinish();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(getApplicationContext(), "Cannot play this video: " + videoFile.getVideoInfo().getTitle(), Toast.LENGTH_SHORT).show();
                //skip this (unsupported) video and playing next video
                newVideoFile = videoFilesList.getNextOf(videoFile);
                updateUI(newVideoFile);
                return true;
            }
        });

        mediaController.setPrevNextListeners(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onPrevNextClick: Next");
                        newVideoFile = videoFilesList.getNextOf(videoFile);
                        updateUI(newVideoFile);
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onPrevNextClick: Previous");
                        newVideoFile = videoFilesList.getPrevOf(videoFile);
                        //newVideoFile = (VideoFile) videoFilesList.descendingIterator().next();
                        updateUI(newVideoFile);
                    }
                }
        );
    }

    private void updateUI(VideoFile newVideoFile) {
        Log.d(TAG, "updateUI: ");
        if(null == newVideoFile) {
            //mediaPlayer.setDataSource(getApplicationContext(), );
            newVideoFile = (VideoFile) videoFilesList.get(intent.getIntExtra("Index", -1));
        }
        setTitle(newVideoFile.getVideoInfo().getTitle());
        /*try {
            mediaPlayer.setDataSource(getApplicationContext(), newVideoFile.getUri());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        videoView.setVideoURI(newVideoFile.getUri());
        //Log.d(TAG, "updateUI: contentDescription " + videoView.getContentDescription());
        videoFile = newVideoFile;
        newVideoFile = null;
        //mediaPlayer.start();
    }

    private void updatePlay() {
        if(null != videoView && !videoView.isPlaying()) {
            videoView.resume();
            videoView.seekTo((int) seekTime);
        }
    }

    private void updatePause() {
        if(null != videoView && videoView.isPlaying()) {
            seekTime = videoView.getCurrentPosition();
            videoView.pause();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        endPlaybackAndFinish();
        return true;
    }

    private boolean endPlaybackAndFinish() {
        prepareToFinish();
        finish();
        return true;
    }

    private void prepareToFinish() {
        videoView.stopPlayback();
        mediaController = null;
        telephonyManager = null;
        audioManager.abandonAudioFocus(audioFocusChangeListener);
        audioFocusChangeListener = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(videoView.isPlaying()) {
            videoWasPlaying = true;
        }
        updatePause();
        setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(videoWasPlaying) {
            updatePlay();
            videoWasPlaying = false;
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        updatePause();
        outState.putLong("CurrentPosition", seekTime);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        seekTime = savedInstanceState.getLong("CurrentPosition");
        updatePlay();
    }
}
