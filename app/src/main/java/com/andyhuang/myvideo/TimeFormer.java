package com.andyhuang.myvideo;

public class TimeFormer {

    public String getTime(int currenTime) {
        currenTime /= 1000;
        String minString;
        String secString;
        int min = currenTime/60;
        int sec = currenTime%60;
        minString = min<10? "0"+min:""+min;
        secString = sec<10? "0"+sec:""+sec;
        return minString+":"+secString;
    }

    public String getTimeEnd(int EndTime) {
        EndTime /= 1000;
        String minString;
        String secString;
        int min = EndTime/60;
        int sec = EndTime%60;
        minString = min<10? "0"+min:""+min;
        secString = sec<10? "0"+sec:""+sec;
        return minString+":"+secString;
    }
}
