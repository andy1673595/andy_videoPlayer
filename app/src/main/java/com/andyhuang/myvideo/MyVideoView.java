package com.andyhuang.myvideo;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

public class MyVideoView extends VideoView{
    int video_height = 0;
    int video_width = 0;
    int layout_height =0;
    int layout_width = 0;
    int heightAfter=0;
    int widthAfter=0;
    int fullwidth =0;
    boolean getVideo = false;
    boolean fullscreen = false;
    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //first
        if(layout_height == 0) {
            layout_height = getHeight();
            layout_width = getWidth();
            fullwidth = layout_height;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        if( ( layout_height != getHeight()|| layout_width != getWidth() ) && !getVideo) {
            video_height = getHeight();
            video_width = getWidth();
            getVideo = true;
            if(video_width >= video_height) {
                if(fullscreen) {
                    widthAfter = fullwidth;
                    heightAfter = (video_height * widthAfter) / video_width;

                }else {
                    widthAfter = layout_width;
                    heightAfter = (video_height * widthAfter) / video_width;
                }

            } else {
                heightAfter = layout_width;
                widthAfter = (video_width*heightAfter)/video_height;
            }
        }

        if(getVideo) {
            setMeasuredDimension( widthAfter, heightAfter);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
      //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      /*  int w = getWidth();
        int h = getHeight();
        int width = getDefaultSize(getWidth(), widthMeasureSpec);
        int height = getDefaultSize(getHeight(), heightMeasureSpec);*/
        Log.d("myvideo","v w"+video_width+", v h" +video_height+",l w "+layout_width+", la h "+layout_height);
       // setMeasuredDimension(width, height);
    }

}