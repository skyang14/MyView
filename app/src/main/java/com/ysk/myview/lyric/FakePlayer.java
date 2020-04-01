package com.ysk.myview.lyric;

import com.ysk.myview.lrc.LyricView;

import java.util.Timer;
import java.util.TimerTask;

/**假的播放器，用来计时提供歌词播放时间
 * Created by yang.shikun on 2020/4/1 10:35
 */

public class FakePlayer {
    private static final String TAG = "FakePlayer";
    private int currentPostion;
    private LyricView lyricView;
    public int getCurrentPosition(){
        return currentPostion;
    }

    public void setLyricView(LyricView lyricView) {
        this.lyricView = lyricView;
    }

    public FakePlayer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentPostion++;
                testLyricView(currentPostion);
               // Log.e(TAG, "run: position=="+currentPostion );
            }
        };
        new Timer().schedule(timerTask,0,1);
    }

    private void testLyricView(int currentPosition){
        lyricView.updateTime(currentPosition);
    }

}
