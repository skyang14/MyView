package com.ysk.myview.lyric;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**假的播放器，用来计时提供歌词播放时间
 * Created by yang.shikun on 2020/4/1 10:35
 */

public class FakePlayer {
    private static final String TAG = "FakePlayer";
    private int currentPostion;

    public int getCurrentPosition(){
        return currentPostion;
    }

    public FakePlayer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentPostion++;
                Log.e(TAG, "run: position=="+currentPostion );
            }
        };
        new Timer().schedule(timerTask,0,1);
    }

}
