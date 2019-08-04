package com.globaledge.travelplayer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final String APP = "GETraveler|";
    private static final String TAG = "VideoPlayer";

    private int videoIndex;
    private VideoView videoView;
    private Intent intent;
    private Uri videoUri;
    private String videoTitle;
    private long seekTime;
    private boolean videoWasPlaying = false;
    private View view;

    private float defVolume;
    private float newVolume;

    private VideoFile videoFile;
    private MediaController mediaController;

    private TelephonyManager telephonyManager;
    private AudioManager audioManager;
    private WindowManager windowManager;

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private MediaPlayer mediaPlayer;
    private VideoFilesListAdapter videoFilesListAdapter;
    //private ViewTreeObserver.OnWindowFocusChangeListener onWindowFocusChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoFilesListAdapter = VideoFilesListAdapter.getInstance(getApplicationContext());

        intent = getIntent();
        Log.d(TAG, "onCreate: intent.toString: " + intent.toString());
        videoIndex = intent.getIntExtra("VideoFileIndex", -1);
        Log.d(TAG, "onCreate: videoIndex: " + videoIndex);
        videoView = findViewById(R.id.view_video);
        updateVideoInfo(videoIndex);
        /*videoUri = intent.getData();
        videoTitle = intent.getStringExtra("VideoTitle");*/
        //setTitle(videoTitle);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager = VideoPlayerActivity.this.getWindowManager();

        //view = (View) findViewById(R.layout.layout_entry_video_file);
        /*final WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                        PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;
        params.height = 0;
        params.width = 0;
        */
        if(null != telephonyManager) {
            telephonyManager.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE:
                            Log.d(APP+TAG, "onCallStateChanged: CALL_STATE_IDLE");
                            //windowManager.removeView(view);
                            updatePlay();
                            break;

                        case TelephonyManager.CALL_STATE_RINGING:
                            Log.d(APP+TAG, "onCallStateChanged: CALL_STATE_RINGING");
                            //windowManager.addView(view, params);
                            audioFocusChangeListener.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK);
                            break;

                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            Log.d(APP+TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
                            updatePause();
                            break;
                            
                        default:
                            Log.d(TAG, "onCallStateChanged: default state is NOTA..!!");
                            break;
                    }
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        }

        mediaController = new MediaController(this) {
        //mediaController = new MediaController(this, false) {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                switch (event.getKeyCode()) {

                    case KeyEvent.KEYCODE_BACK:
                        endPlaybackAndFinish();
                        break;
                        //return true;

                    default:
                        break;

                }
                return true;
            }
        };

        mediaController.setAnchorView(findViewById(R.id.view_media_controller));
        //mediaController.show(MediaController.OVER_SCROLL_ALWAYS);
        /*mediaController.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Log.d(APP+TAG, "onSystemUiVisibilityChange: " + visibility);

            }
        });*/
        /*mediaController.setPrevNextListeners(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(APP+TAG, "onClick: next clicked");
                        videoView.stopPlayback();
                        int nextIndex = videoFilesListAdapter.getNextVideoIndex(videoIndex);
                        updateVideoInfo(nextIndex);
                        videoView.start();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(APP+TAG, "onClick: prev clicked");
                        videoView.stopPlayback();
                        int prevIndex = videoFilesListAdapter.getPrevVideoIndex(videoIndex);
                        updateVideoInfo(prevIndex);
                        videoView.start();
                    }
                }
        );*/

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
                            newVolume = (float) (defVolume * 0.8);
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

        mediaPlayer = new MediaPlayer();

        videoView.setMediaController(mediaController);

        //returns true to 'consume' the touch events to videoView, and not pass on to super class
        /*videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mediaController.show(0);
                //mediaController.show();
                //return true;
                return false;
            }
        });*/
        videoView.setOnErrorListener(
                new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return endPlaybackAndFinish();
                    }
                }
        );
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(APP+TAG, "onCompletion: end of playback");
                audioManager.abandonAudioFocus(audioFocusChangeListener);
                endPlaybackAndFinish();
            }
        });
        videoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if((v == videoView)) {
                    //let videoView have focus
                    Log.d(APP + TAG, "onFocusChange: videoView focus changed");
                    if(v.hasFocus()) {
                        updatePause();
                        videoView.requestFocus();
                    } else {
                        updatePlay();
                    }
                } else {
                    //handle other conditions
                    Log.d(TAG, "onFocusChange: some other view's focus changed");
                }
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                        audioManager.requestAudioFocus(
                                audioFocusChangeListener,
                                AudioManager.STREAM_MUSIC | AudioManager.STREAM_NOTIFICATION,
                                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)) {
                    Log.d(TAG, "onPrepared: AUDIOFOCUS_REQUEST_GRANTED");
                    defVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    newVolume = defVolume;
                    //mediaPlayer = mp;
                    videoView.start();
                    //mediaController.show(videoView.getDuration());
                    //mediaController.show();
                } else {
                    Log.d(TAG, "onPrepared: Cannot play video at the time");
                    Toast.makeText(VideoPlayerActivity.this, "Cannot play video at the time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

    }

    private void updateVideoInfo(int videoIndex) {
        if(-1 != videoIndex) {
            videoFile = videoFilesListAdapter.getVideoFileFromID(videoIndex);
            videoUri = videoFile.getUri();
            videoTitle = videoFile.getTitle();
            videoView.setVideoURI(videoUri);
            //setting title of player activity to video title.
            setTitle(videoTitle);
        }
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
        endPlayback();
        finish();
        return true;
    }

    private void endPlayback() {
        videoView.stopPlayback();
        mediaController = null;
        telephonyManager = null;
        audioFocusChangeListener = null;
    }

    @Override
    public void onBackPressed() {
        endPlaybackAndFinish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(APP+TAG, "onResume: ");
        if(videoWasPlaying) {
            updatePlay();
            //mediaController.show(videoView.getDuration() - videoView.getCurrentPosition());
            mediaController.show();
            videoWasPlaying = false;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(APP+TAG, "onPause: ");
        if(videoView.isPlaying()) {
            videoWasPlaying = true;
        }
        updatePause();
    }
}
