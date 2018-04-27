package com.andyhuang.myvideo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class VideoPlayer extends AppCompatActivity implements View.OnClickListener  {
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
    TextView timeCurrent;
    TextView timeEnd;
    boolean isplay = true;
    boolean isMute = false;
    SeekBar progressBar;
    MediaPlayer mMediaPlayer;
    int duration = 0;
    boolean isLANDSCAPE = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        setStatusBar();
        setContentView(R.layout.video_player);
        vidView = (MyVideoView) findViewById(R.id.myVideo);

        play = (ImageView) findViewById(R.id.play_button);
        back = (ImageView) findViewById(R.id.back_button);
        forward = (ImageView)findViewById(R.id.forward_button);
        mute = (ImageView)findViewById(R.id.mute_button);
        fullscreen = (ImageView)findViewById(R.id.fullscreen_button);
        timeCurrent = (TextView)findViewById(R.id.timeNow);
        timeEnd = (TextView)findViewById(R.id.timeEnd);
        progressBar = (SeekBar)findViewById(R.id.progressBar);
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
        new MyAsync().execute();
        progressBar.setOnSeekBarChangeListener(seekBarOnSeekBarChange);
    }

    @Override
    public void onClick(View v) {
        int currentPosition;
        switch (v.getId()) {
            case R.id.play_button:
                if(isplay) {
                    play.setImageResource(R.drawable.play);
                    vidView.pause();
                    isplay = false;
                }else {
                    play.setImageResource(R.drawable.pause);
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
                if(isMute) {
                    mMediaPlayer.setVolume(1f,1f);
                    mute.setImageResource(R.drawable.volume_off);
                    isMute = false;

                } else {
                    mMediaPlayer.setVolume(0f, 0f);
                    mute.setImageResource(R.drawable.volume_mute);
                    isMute = true;
                }
                break;
            case R.id.fullscreen_button:
                break;
        }

    }


    private class MyAsync extends AsyncTask<Void, Integer, Void>
    {
        int current = 0;
        @Override
        protected Void doInBackground(Void... params) {

            vidView.start();
            vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer =mp;
                    duration = vidView.getDuration();
                    setTimeEnd(duration);
                }
            });

            do {
                current = vidView.getCurrentPosition();
                System.out.println("duration - " + duration + " current- "
                        + current);
                try {
                    publishProgress((int) (current * 100 / duration));
                    if(progressBar.getProgress() >= 100){
                        break;
                    }
                } catch (Exception e) {
                }
            } while (progressBar.getProgress() <= 100);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            setTime(current);
        }
    }

    void setTime(int currenTime) {
        currenTime /= 1000;
        String minString;
        String secString;
        int min = currenTime/60;
        int sec = currenTime%60;
        minString = min<10? "0"+min:""+min;
        secString = sec<10? "0"+sec:""+sec;
        timeCurrent.setText(minString+":"+secString);
    }

    void setTimeEnd(int EndTime) {
        EndTime /= 1000;
        String minString;
        String secString;
        int min = EndTime/60;
        int sec = EndTime%60;
        minString = min<10? "0"+min:""+min;
        secString = sec<10? "0"+sec:""+sec;
        timeEnd.setText(minString+":"+secString);
    }

    private SeekBar.OnSeekBarChangeListener seekBarOnSeekBarChange
            = new SeekBar.OnSeekBarChangeListener()
    {
        int progressAfterSwipe;
        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
            //停止拖曳時觸發事件
            progressAfterSwipe = seekBar.getProgress();
            mMediaPlayer.seekTo(mMediaPlayer.getDuration()*progressAfterSwipe/100);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {
            //開始拖曳時觸發事件
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            //拖曳途中觸發事件，回傳參數 progress 告知目前拖曳數值
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
           super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 什麼都不用寫
            isLANDSCAPE = true;

        }
        else {
            // 什麼都不用寫
            isLANDSCAPE = false;
        }
    }

    private void setStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//Android4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Android5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }
}
