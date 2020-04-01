package com.ysk.myview.lyric;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang.shikun on 2020/4/1 10:21
 */

public class LrcUtil {
    private static final String TAG = "LrcUtil";
    /**
     * 传入的参数为标准歌词字符串
     * @param lrcStr
     * @return
     */
    public static List<LrcBean> parseStr2List(String lrcStr) {
        List<LrcBean> list = new ArrayList<>();
        String lrcText = lrcStr.replaceAll("&#58;", ":")
                .replaceAll("&#10;", "\n")
                .replaceAll("&#46;", ".")
                .replaceAll("&#32;", " ")
                .replaceAll("&#45;", "-")
                .replaceAll("&#13;", "\r")
                .replaceAll("&#39;", "'")
                .replace(" ","");
        String[] split = lrcText.split("\n");
        for (int i = 0; i < split.length; i++) {
            String lrc = split[i];
            if (lrc.contains(".")) {
                String min = lrc.substring(0,2);
                Log.e(TAG, "parseStr2List:min== "+min );
                String seconds = lrc.substring(lrc.indexOf(":") + 1, lrc.indexOf(":") + 3);
                Log.e(TAG, "parseStr2List:seconds== "+seconds );
                String mills = lrc.substring(lrc.indexOf(".") + 1, lrc.indexOf(".") + 3);
                Log.e(TAG, "parseStr2List:mills== "+mills );
                long startTime = Long.valueOf(min) * 60 * 1000 + Long.valueOf(seconds) * 1000 + Long.valueOf(mills) * 10;
                String text = lrc.substring(8);
                if (text == null || "".equals(text)) {
                    text = "music";
                }
                LrcBean lrcBean = new LrcBean();
                lrcBean.setStart(startTime);
                lrcBean.setLrc(text);
                list.add(lrcBean);
                if (list.size() > 1) {
                    list.get(list.size() - 2).setEnd(startTime);
                }
                if (i == split.length - 1) {
                    list.get(list.size() - 1).setEnd(startTime + 100000);
                }
            }
        }
        return list;
    }
}
