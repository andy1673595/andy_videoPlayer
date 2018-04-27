package com.andyhuang.myvideo;

import android.content.IntentFilter;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {
    private VideoView vidView;
    private MediaController vidControl;
    String vidAddress = "https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/taeyeon.mp4";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.video_player);
        vidView = (VideoView) findViewById(R.id.myVideo);
        MediaSession session = new MediaSession(this,"myvideo");
        vidControl = new MediaController(this,session.getSessionToken());
        MediaController.TransportControls transportControls = vidControl.getTransportControls();
        MediaControllerCompat
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        vidView.start();

   //     transportControls.play();
    }
}
