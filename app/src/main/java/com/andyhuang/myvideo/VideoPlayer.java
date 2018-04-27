package com.andyhuang.myvideo;

import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.net.Uri;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import java.io.IOException;


public class VideoPlayer extends AppCompatActivity implements View.OnClickListener {
    private MyVideoView vidView;
    private android.widget.MediaController vidControl;
    String vidAddress = "https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/protraitVideo.mp4";
   // String vidAddress = "https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/taeyeon.mp4";
    MediaSessionCompat mMediaSession;
    PlaybackStateCompat.Builder mStateBuilder;
    ImageView play;
    ImageView back;
    ImageView forward;
    ImageView mute;
    ImageView fullscreen;
    boolean isplay = true;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.video_player);
        vidView = (MyVideoView) findViewById(R.id.myVideo);

        play = (ImageView) findViewById(R.id.play_button);
        back = (ImageView) findViewById(R.id.back_button);
        forward = (ImageView)findViewById(R.id.forward_button);
        mute = (ImageView)findViewById(R.id.mute_button);
        fullscreen = (ImageView)findViewById(R.id.fullscreen_button);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        play.setOnClickListener(this);
        back.setOnClickListener(this);
        forward.setOnClickListener(this);
        mute.setOnClickListener(this);
        fullscreen.setOnClickListener(this);

        MediaSession session = new MediaSession(this,"myvideo");

      //  vidControl = new android.widget.MediaController(this);
      //  MediaController.TransportControls transportControls = vidControl.getTransportControls();

     /*   mMediaSession = new MediaSessionCompat(this, "myplayer");

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());*/


        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);

        vidView.start();



        int x = 0;
        x++;
    }

    @Override
    public void onClick(View v) {
        int currentPosition;
        switch (v.getId()) {
            case R.id.play_button:
                if(isplay) {
                    play.setImageResource(R.drawable.pause);
                    vidView.pause();
                    isplay = false;
                }else {
                    play.setImageResource(R.drawable.play);
                    vidView.start();
                    isplay = true;
                }
                break;
            case R.id.back_button:
                currentPosition = vidView.getCurrentPosition();
                currentPosition -= 15000;
                if(currentPosition<0) {
                    currentPosition=0;
                }
                vidView.seekTo(currentPosition);
                break;
            case R.id.forward_button:
                currentPosition = vidView.getCurrentPosition();
                currentPosition += 15000;
                vidView.seekTo(currentPosition);
                break;
            case R.id.mute_button:
                break;
            case R.id.fullscreen_button:
                break;
        }

    }
}
