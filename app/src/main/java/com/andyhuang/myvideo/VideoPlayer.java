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
import android.widget.VideoView;

import java.io.IOException;


public class VideoPlayer extends AppCompatActivity {
    private MyVideoView vidView;
    private android.widget.MediaController vidControl;
    String vidAddress = "https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/protraitVideo.mp4";
   // String vidAddress = "https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/taeyeon.mp4";
    MediaSessionCompat mMediaSession;
    PlaybackStateCompat.Builder mStateBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.video_player);
        vidView = (MyVideoView) findViewById(R.id.myVideo);


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

        int height = vidView.getHeight();
        int width = vidView.getWidth();

        vidView.start();
         height = vidView.getHeight();
         width = vidView.getWidth();

        int x = 0;
        x++;
    }
}
