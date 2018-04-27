package com.andyhuang.myvideo;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class VideoPlayer extends BaseActivity implements View.OnClickListener  {
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
    ConstraintLayout bottomLayout;
    ConstraintLayout video_backLayout;
    boolean isplay = true;
    boolean isMute = false;
    boolean check = true; //use for check Rotating screen
    SeekBar progressBar;
    MediaPlayer mMediaPlayer;
    int duration = 0;
    boolean isLANDSCAPE = false;
    Handler myHandler = new Handler();
    private OrientationEventListener mOrientationListener;


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
        timeCurrent = (TextView)findViewById(R.id.timeNow);
        timeEnd = (TextView)findViewById(R.id.timeEnd);
        bottomLayout = (ConstraintLayout)findViewById(R.id.bottom_bar);
        video_backLayout = (ConstraintLayout)findViewById(R.id.video_back_layout);
        progressBar = (SeekBar)findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        play.setOnClickListener(this);
        back.setOnClickListener(this);
        forward.setOnClickListener(this);
        mute.setOnClickListener(this);
        fullscreen.setOnClickListener(this);
        video_backLayout.setOnClickListener(this);


      //  MediaSession session = new MediaSession(this,"myvideo");

        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        new MyAsync().execute();
        progressBar.setOnSeekBarChangeListener(seekBarOnSeekBarChange);


        mOrientationListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                if(check) {
                    if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315)||((rotation >= 135)&&(rotation <= 225))) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    } else if (((rotation > 45) && (rotation < 135))||((rotation > 225) && (rotation < 315))) {
                        //landscape
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                }
            }
        };
        mOrientationListener.enable();


    }

    @Override
    public void onClick(View v) {
        int currentPosition;
        //移除現有倒數的handler
        myHandler.removeCallbacks(runTimerStop);
        if(isLANDSCAPE) {
            Log.d("trans","click");
            bottomLayout.setVisibility(View.VISIBLE);
            myHandler.postDelayed(runTimerStop,3*1000);
        }

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
                if(!isLANDSCAPE) {
                    check =false;
                    fullscreen.setImageResource(R.drawable.fullscreen_exit);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


                }else {
                    check =false;
                    fullscreen.setImageResource(R.drawable.fullscreen);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
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


                    mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                        public void onBufferingUpdate(MediaPlayer mp, int percent)
                        {
                            double ratio = percent / 100.0;
                            int bufferingLevel = (int)(mp.getDuration() * ratio);

                            progressBar.setSecondaryProgress(bufferingLevel);
                        }

                    });
                }
            });

            do {
                current = vidView.getCurrentPosition();
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
            //resize video screen
            vidView.fullscreen =true;
            vidView.getVideo = false;
            /**/
            isLANDSCAPE = true;
            myHandler.removeCallbacks(runTimerStop);
            myHandler.postDelayed(runTimerStop,3*1000);
            fullscreen.setImageResource(R.drawable.fullscreen_exit);
            check =true;

        }
        else {
            //resize video screen
            vidView.fullscreen =false;
            vidView.getVideo = false;
            /**/
            isLANDSCAPE = false;
            myHandler.removeCallbacks(runTimerStop);
            bottomLayout.setVisibility(View.VISIBLE);
            fullscreen.setImageResource(R.drawable.fullscreen);
            check =true;
        }
    }
    
    //主體
    private Runnable runTimerStop = new Runnable()
    {
        @Override
        public void run()
        {
            bottomLayout.setVisibility(View.INVISIBLE);
        }
    };
}
